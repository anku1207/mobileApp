package com.uav.rof.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uav.rof.R;

import java.util.HashMap;

public class Login extends AppCompatActivity {

   /* GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE = 1001;
    AlertDialog.Builder builder;
    String usermail;
    int vercode;
    HashMap<String, String> params;


    protected void onStart() {
        super.onStart();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                    }
                });
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);

            vercode = pInfo.versionCode;
            Toast.makeText(this, ""+vercode, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

  /*      params=new HashMap<>();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();


        //added by vijay@uav
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Intent intent = mGoogleSignInClient.getSignInIntent(); //Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            usermail=account.getEmail();

            Emailverify();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //   Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            // Set a title for alert dialog
            builder.setTitle("Say Hello!");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            // Show a message on alert dialog
            builder.setMessage("Are you want to do this?");
            // Set the positive button
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            // Set the negative button
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loginscreen();
                }
            });
            // Set the neutral button
            // builder.setNeutralButton("Cancel", null);
            // Create the alert dialog
            AlertDialog dialog = builder.create();
            // Finally, display the alert dialog
            dialog.show();

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            // Get the alert dialog buttons reference
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            // Change the alert dialog buttons text and background color
            positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
            positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

            negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
            negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
*//*
            neutralButton.setTextColor(Color.parseColor("#FF1B5AAC"));
            neutralButton.setBackgroundColor(Color.parseColor("#FFFFFF"));*//*




        }
    }


    public  void loginscreen(){

        Intent intent = mGoogleSignInClient.getSignInIntent(); //Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    public void Emailverify(){


  */  }
}
