package com.uav.rof.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.uav.rof.R;

import com.uav.rof.volley.VolleyUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utility {
    static int errorColor;

    public static SpannableStringBuilder getErrorSpannableString(Context context, String msg){
        int version = Build.VERSION.SDK_INT;
        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(context, R.color.errorColor);
        } else {
            errorColor = context.getResources().getColor(R.color.errorColor);
        }

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, msg.length(), 0);

        return spannableStringBuilder;
    }

    public  static   String imagetostring (Uri mImageUri,Context context){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();


        Bitmap bmp= VolleyUtils.grabImage(mImageUri,context);
        bmp= Utility.scaleDown(bmp, 1100, true);

        if(bmp.getWidth()>bmp.getHeight()){
            Matrix matrix =new Matrix();
            matrix.postRotate(90);
            bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);

        }

        /*Bitmap  bmp1= Bitmap.createScaledBitmap(
                bitmap, 320, 500, false);*/
        bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();

        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    //15-1-2019


    public static Bitmap ByteArrayToBitmap(byte[] byteArray){
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }




    public static String currentDateFormat(){

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS");
        String currentTimeStamp=dateFormat.format(new Date());
        return  currentTimeStamp;

    }

    public static File storeCameraPhotoInSDCard(Bitmap bitmap ,String currentdate){
        //  File outputFile = new File(Environment.getExternalStorageDirectory(),"photo_"+currentdate);
        File direct = new File(Environment.getExternalStorageDirectory() + "/ROF");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+"/"+"ROF/");
            wallpaperDirectory.mkdirs();
        }
        File file = new File(new File(Environment.getExternalStorageDirectory()+"/"+"ROF/"), "photo_"+currentdate+".JPEG");
        /*if (file.exists()) {
            file.delete();
        }*/
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static   Bitmap getImageFileFromSDCared(File filename){

        Bitmap bitmap=null;
        try {
            FileInputStream fis=new FileInputStream(filename);
            bitmap= BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }



    public  static String getImageByPincode(Context context,Bitmap bitmap){





        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < textBlocks.size(); index++) {
            //extract scanned text blocks here
            TextBlock item = textBlocks.valueAt(index);
            stringBuilder.append(item.getValue());
            stringBuilder.append("\n");
        }
        String value=stringBuilder.toString().replaceAll(" ", "");
        String replaceString=value.toString().replaceAll("\n", "");

        int i=0;
        String nos = "";
        List<String> digitsArray = new ArrayList<String>();
        while(replaceString.length()>i){
            Character ch = replaceString.charAt(i);
            if(Character.isDigit(ch)){
                nos += ch;
            }else if(nos!=""){
                digitsArray.add(nos);
                nos ="";
            }
            i++;
        }

        String pincode=null;
        for(String p : digitsArray){
            if(p.length()==6){
                pincode=p;
                break;
            }
        }
        return pincode;


    }



    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }





    public static void  alertDialog(Context context,String title ,String msg , String buttonname){

        AlertDialog alertDialog;

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });
        alertDialog.show();

    }

}
