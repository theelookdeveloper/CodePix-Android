package com.codepix.main;



import com.codepix.model.CameraIntentHelperActivity;
import com.codepix.utilz.BitmapHelper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Example Activity of how to use the CameraIntentHelperActivity
 * 
 * @author Ralf Gehrer <ralf@ecotastic.de>
 */
public class TakePhotoActivity extends CameraIntentHelperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
	}
	
	@Override
	protected void onPhotoUriFound() {
		TextView uirView = (TextView) findViewById(R.id.acitvity_take_photo_image_uri);
		uirView.setText("photo uri: " + photoUri.toString());
		
		Bitmap photo = BitmapHelper.readBitmap(this, photoUri);
        if (photo != null) {
            photo = BitmapHelper.shrinkBitmap(photo, 300, rotateXDegrees);
            ImageView imageView = (ImageView) findViewById(R.id.acitvity_take_photo_image_view);
 			imageView.setImageBitmap(photo); 
        }
		
        //Delete photo in second location (if applicable)
        if (preDefinedCameraUri != null && !preDefinedCameraUri.equals(photoUri)) {
        	BitmapHelper.deleteImageWithUriIfExists(preDefinedCameraUri, this);
        }
	}
	
	@Override
	protected void onPhotoUriNotFound() {
		super.onPhotoUriNotFound();
		TextView uirView = (TextView) findViewById(R.id.acitvity_take_photo_image_uri);
		uirView.setText("photo uri: not found");
	}
	
	public void onStartCamera(View view) {
		startCameraIntent();
	}
}