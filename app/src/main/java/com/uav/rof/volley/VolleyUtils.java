package com.uav.rof.volley;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.uav.rof.R;
import com.uav.rof.activity.ConnectionVO;
import com.uav.rof.constants.ApplicationConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyUtils {
    ProgressBar progressBar;
    static VolleyResponseListener responseListener;
    static String errorMessage;
    public static void makeJsonObjectRequest(final Context context, ConnectionVO connectionVO, final VolleyResponseListener listener) {
        Map<String, Object> params = connectionVO.getParams();
        responseListener  = listener;
        JSONObject jsonParams = null;
        if(params!=null){
            jsonParams = new JSONObject(params);
        }

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Connecting ...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (connectionVO.getRequestType(), ApplicationConstant.getHttpURL(context) + connectionVO.getMethodName() +"?authkey=" + ApplicationConstant.AUTHKEY, jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            listener.onResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        errorMessage= error.toString();
                        showError("Connection Error", error.getMessage(), context );
                    //    listener.onError(error.toString());
                    }
                }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                pDialog.dismiss();
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        // Access the RequestQueue through singleton class.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(ApplicationConstant.SOCKET_TIMEOUT_MILLISEC,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addTorequestque(jsonObjectRequest);
    }



    public static   void showError(String title, String error, final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(error)
                .setTitle(title+"!")
                .setIcon(android.R.drawable.ic_dialog_alert)


                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent =new Intent();
                        responseListener.onError(errorMessage);
                    }
                });
        AlertDialog alert= builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();


    }


    public static  void furnishErrorMsg( String errorTitle, JSONObject jsonObject,Context context) throws JSONException {
        JSONArray error = jsonObject.getJSONArray("error");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<error.length(); i++){
            sb.append(error.get(i)).append("\n");
        }
        VolleyUtils.showError(errorTitle, sb.toString(), context );


    }



    /// manoj shakya
    public static File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }


    public static Bitmap grabImage(Uri mImageUri, Context context)
    {
        //context.getContentResolver().notifyChange(mImageUri, null);



        ContentResolver cr = context.getContentResolver();
        Bitmap  bitmap = null;
        try
        {
            bitmap  = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }


}

