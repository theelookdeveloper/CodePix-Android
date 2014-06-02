package com.codepix.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class Custom_CameraActivity extends Activity {
    public class CameraPreview extends SurfaceView implements
    SurfaceHolder.Callback {
private SurfaceHolder mSurfaceHolder;
private Camera mCamera;

// Constructor that obtains context and camera
@SuppressWarnings("deprecation")
public CameraPreview(Context context, Camera camera) {
    super(context);
    this.mCamera = camera;
    this.mSurfaceHolder = this.getHolder();
    this.mSurfaceHolder.addCallback(this);
    this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
}

@Override
public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
        int width, int height) {
    // start preview with new settings
    try {
        mCamera.setPreviewDisplay(surfaceHolder);
        mCamera.startPreview();
    } catch (Exception e) {
        // intentionally left blank for a test
    }
}

@Override
public void surfaceCreated(SurfaceHolder surfaceHolder) {
    try {
        mCamera.setPreviewDisplay(surfaceHolder);
        mCamera.startPreview();
    } catch (IOException e) {
        // left blank for now
    }
}

@Override
public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    mCamera.stopPreview();
    mCamera.release();
}
}
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private Camera mCamera;

    private CameraPreview mCameraPreview;

    PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Intent i=new Intent();
                i.putExtra("filepath", pictureFile.getAbsolutePath());
                setResult(Activity.RESULT_OK, i);
                
                finish();
                //Toast.makeText(Custom_CameraActivity.this, "file"+pictureFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     * 
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_camera);
        mCamera = getCameraInstance();
       // mCamera.setDisplayOrientation(90);
        
        mCamera.autoFocus(null);
        mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
                
               
            }
        });
    }
}