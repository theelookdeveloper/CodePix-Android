package com.codepix.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraDashboardActivity extends Activity {
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

private Camera.Size getBestPreviewSize(int width, int height,
        Camera.Parameters parameters) {
Camera.Size result=null;

for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
if (size.width <= width && size.height <= height) {
if (result == null) {
result=size;
}
else {
int resultArea=result.width * result.height;
int newArea=size.width * size.height;

if (newArea > resultArea) {
result=size;
}
}
}
}

return(result);
}

@Override
public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
        int width, int height) {
    // start preview with new settings
    try {
        mCamera.setPreviewDisplay(surfaceHolder);
        mCamera.startPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        setCameraDisplayOrientation(CameraDashboardActivity.this,0,mCamera);
    } catch (Exception e) {
        // intentionally left blank for a test
    }
}

@Override
public void surfaceCreated(SurfaceHolder surfaceHolder) {
	 try {
        // mCamera = Camera.open();

        // mCamera.setDisplayOrientation(90);
         mCamera.setPreviewDisplay(surfaceHolder);
         Camera.Parameters parameters = mCamera.getParameters();
         List<Size> sizes = parameters.getSupportedPictureSizes();
         parameters.setPictureSize(sizes.get(0).width, sizes.get(0).height); // mac dinh solution 0
         parameters.set("orientation","portrait");
         //parameters.setPreviewSize(viewWidth, viewHeight);
         List<Size> size = parameters.getSupportedPreviewSizes();
         parameters.setPreviewSize(size.get(0).width, size.get(0).height);
         mCamera.setParameters(parameters);
         mCamera.startPreview();
     } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
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
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setCameraDisplayOrientation(Activity activity,
    	     int cameraId, android.hardware.Camera camera) {

    	   android.hardware.Camera.CameraInfo info = 
    	       new android.hardware.Camera.CameraInfo();

    	   android.hardware.Camera.getCameraInfo(cameraId, info);

    	   int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    	   int degrees = 0;

    	   switch (rotation) {
    	       case Surface.ROTATION_0: degrees = 0; break;
    	       case Surface.ROTATION_90: degrees = 90; break;
    	       case Surface.ROTATION_180: degrees = 180; break;
    	       case Surface.ROTATION_270: degrees = 270; break;
    	   }

    	   int result;
    	   if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
    	       result = (info.orientation + degrees) % 360;
    	       result = (360 - result) % 360;  // compensate the mirror
    	   } else {  // back-facing
    	       result = (info.orientation - degrees + 360) % 360;
    	   }
    	   camera.setDisplayOrientation(result);
    	}

    private Camera mCamera;

    private CameraPreview mCameraPreview;

    private Button btnCancel;

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
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	
    	startActivity(new Intent(CameraDashboardActivity.this,DashboardActivity.class));
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_cameradashboard);
        
        btnCancel=(Button)findViewById(R.id.btnCancel);
        mCamera = getCameraInstance();
        
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
 	   int degrees = 0;

 	   switch (rotation) {
 	       case Surface.ROTATION_0: degrees = 0; break;
 	       case Surface.ROTATION_90: degrees = 90; break;
 	       case Surface.ROTATION_180: degrees = 180; break;
 	       case Surface.ROTATION_270: degrees = 270; break;
 	   }
        
        
        mCamera.setDisplayOrientation(degrees);
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
        
        btnCancel.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
    }
}