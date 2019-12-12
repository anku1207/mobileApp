package com.uav.rof.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.uav.rof.R;
import com.uav.rof.bo.MemberBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GmailLogin extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private static   int REQ_CODE = 9001;
    private static   int REQ_CODE_USERAUTHENTICATION = 101;
    private static   int REQ_CODE_DIALOGE = 201;
    AlertDialog.Builder builder;
    String usermail;
    HashMap<String, Object> params;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                    }
                });

        //chooseEmailAccount();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamillogin);


        getSupportActionBar().hide();

        LinearLayout layout = new LinearLayout(this);

        params=new HashMap<>();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        //added by vijay@uav
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Intent intent = mGoogleSignInClient.getSignInIntent(); //Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);









        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //reesult code not handle in gmail response so keep outside from resuleok condition
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }


        if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQ_CODE_USERAUTHENTICATION) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                chooseEmailAccount();
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            usermail=account.getEmail();




            userAuthentication();
            progressBar.setVisibility(View.GONE);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //   Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            reAttemptDialog();
        }
    }


    public  void chooseEmailAccount(){
        onStart();   //on start is used to reset mGoogleSiginClient
        Intent intent = mGoogleSignInClient.getSignInIntent(); //Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    public void userAuthentication(){
        VolleyUtils.makeJsonObjectRequest(this, MemberBO.getMember(usermail), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                chooseEmailAccount();
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){

                    unAuthorisedUser(response.getString("error"));
                    return;
                }
                successlogin(response.toString());
                progressBar.setVisibility(View.GONE);




            }
        });

    }
    public  void successlogin(String resp) throws JSONException {



        JSONObject jsonObject = new JSONObject(resp);
        Log.w("responce",""+jsonObject);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString( ApplicationConstant.CACHE_MAMBERID,jsonObject.getString("memberId"));
        edit.putString( ApplicationConstant.CACHE_ACTIVITYES,jsonObject.getJSONArray("activity").toString());
        edit.apply();
        edit.commit();



        Intent intent = new Intent(GmailLogin.this,MainActivity.class);
        finish();

        startActivity(intent);
        overridePendingTransition(0, 0);


    }



    private void reAttemptDialog(){

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(GmailLogin.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Want to Exit!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                chooseEmailAccount();
            }
        });
        alertDialog.show();
    }


    private void  unAuthorisedUser(String error){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error)
                .setTitle("Login Error!")
                .setIcon(android.R.drawable.ic_dialog_alert)

                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finishAffinity();

                    }
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();



    }


}