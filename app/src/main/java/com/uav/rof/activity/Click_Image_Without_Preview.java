package com.uav.rof.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uav.exceptions.ExceptionsNotification;
import com.uav.rof.R;
import com.uav.rof.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.Parameters.ANTIBANDING_AUTO;

public class Click_Image_Without_Preview extends AppCompatActivity implements SurfaceHolder.Callback{

    Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button capture_image;
    ProgressDialog  progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click__image__without__preview);

        getSupportActionBar().hide();



        capture_image = (Button) findViewById(R.id.capture_image);

        ImageView back_activity_button=(ImageView)findViewById(R.id.back_activity_button);
        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        capture_image.setVisibility(View.VISIBLE);



        capture_image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                capture_image.setVisibility(View.GONE);
                progress= new ProgressDialog(Click_Image_Without_Preview.this);
                progress.setMessage("Reading Pincode ....");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                mCamera.takePicture(null, null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        String partFilename= Utility.currentDateFormat();
                        File filename= Utility.storeCameraPhotoInSDCard(bitmap,partFilename);

                        Intent intent = new Intent();
                        intent.putExtra("imagepath",filename.toString());
                        setResult(RESULT_OK, intent);
                        camera.stopPreview();
                        if (camera != null) {
                            camera.release();
                            mCamera = null;
                        }
                        progress.dismiss();
                        finish();
                    }
                });
            }
        });


        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(Click_Image_Without_Preview.this);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            ExceptionsNotification.ExceptionHandling(Click_Image_Without_Preview.this , Utility.getStackTrace(e));
        }
    }

    /*private void capture() {

    }
*/
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.e("Surface Changed", "format   ==   " + format + ",   width  ===  "
                + width + ", height   ===    " + height);
        try {

            Camera.Parameters parameters=mCamera.getParameters();
           /* List<Camera.Size> sizes=parameters.getSupportedPictureSizes();
            Camera.Size size=sizes.get(0);
            for(int i=0 ; i<sizes.size() ;i++){
                if(sizes.get(i).width >size.width){
                    size=sizes.get(i);
                }
            }
*/
            parameters.setPictureSize(1280, 720);

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setExposureCompensation(0);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);





            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.setParameters(parameters);
         //   mCamera.startPreview();


/*
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });*/



        } catch (IOException e) {
            ExceptionsNotification.ExceptionHandling(Click_Image_Without_Preview.this , Utility.getStackTrace(e));
        }
    }





























    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("Surface Created", "");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("Surface Destroyed", "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }


    }




}
