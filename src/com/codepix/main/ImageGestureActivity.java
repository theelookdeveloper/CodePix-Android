package com.codepix.main;

import java.io.InputStream;

import com.codepix.utilz.GlobalMethods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageGestureActivity extends Activity {
	
	private LinearLayout imgEffect;
	private Intent mIntent;
	private Bitmap yourSelectedImage;
	private String filePath;
	InputStream imageStream = null;
	private String filePathDistorted;
	int imageWidth = 0;
	int imageHeight = 0;
	int gestureType = 0;
	public static String gestureCordinates=null;
	boolean effectsAdded = false;
	private Button btnCancel;
	private Button btnGesture;
	public static boolean gesture=false;
	protected static boolean gestureEnabled;
	private GestureImageViewLock view;

	private void addGesture()
	 {
	 	CharSequence[] items={"A single tap at specific point","Draw Pattern"};
	 	new AlertDialog.Builder(this)
	     .setSingleChoiceItems(items, -1, null)
	     .setTitle("Add Gesture")
	     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        

	 		@Override
	 		public void onClick(DialogInterface dialog, int whichButton) {
	             dialog.dismiss();
	             int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
	             // Do something useful withe the position of the selected radio button
	             
	             if(selectedPosition==0)
	             {
	             	gestureType=1;
	             	
	             	gesture=true;
	             }
	             else  if(selectedPosition==1)
	             {
	            	 gestureType=2;
	             }
	         }
	     })
	     .setNegativeButton("CANCEL",  new DialogInterface.OnClickListener() {
		        

		 		@Override
		 		public void onClick(DialogInterface dialog, int whichButton) {
		             dialog.dismiss();
		             
		         }
		     })
	     .show();
	 }
	
	
    protected void addGesture(float x, float y) {
		// TODO Auto-generated method stub
    	BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;
        
       /* InputStream is=null;
        try {
			Uri selectedImage=Uri.fromFile(new File(filePath));
			is =getContentResolver().openInputStream(selectedImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
        // yourSelectedImage = BitmapFactory.decodeStream(imageStream);

        Bitmap bitmap = GlobalMethods.ShrinkBitmap(filePath, 403, 503);
        
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);


        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x-10, y-5, 20, paint);

       // ImageView imageView = (ImageView)findViewById(R.id.schoolboard_image_view);
        //imgEffect.setAdjustViewBounds(true);
        //imgEffect.setImageBitmap(mutableBitmap);
        
        gestureEnabled=true;
	}
    public void onBackPress() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	Intent intent=new Intent(ImageGestureActivity.this,ImageEffectsActivity.class); 
    	intent.putExtra("filePath", filePath);
        
    	startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	/*Intent intent=new Intent(ImageGestureActivity.this,ImageEffectsActivity.class); 
    	intent.putExtra("filePath", filePath);
        
    	startActivity(intent);*/
    }
	
	 public void onCancelPressed() {
	    	startActivity(new Intent(ImageGestureActivity.this,DashboardActivity.class));
	    }
	 
	 
	 @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.activity_imagegesture);
	        
	        
	        mIntent=getIntent();
	 	   
	 	   Bundle str=mIntent.getExtras();
	 	   
	 	   filePath = str.getString("filePath");
	 	  filePathDistorted=str.getString("filePathDistorted");
	 	  imageWidth=str.getInt("imageWidth");
	 	 imageHeight=str.getInt("imageHeight");
	 	 effectsAdded=str.getBoolean("effectsAdded");
	 	   
	 	   boolean pictureFromCamera=str.getBoolean("pictureFromCamera");
	        
	        imgEffect=(LinearLayout)findViewById(R.id.imageViewEffect);
	        
	        yourSelectedImage = GlobalMethods.ShrinkBitmap(filePath, 403, 503);
              view=new GestureImageViewLock(ImageGestureActivity.this);
			
			LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(403, 503);
		        
		      
			imgEffect.addView(view,params);
			
			view.setImageBitmap(yourSelectedImage);
	        
			/*try {
				Uri selectedImage=Uri.fromFile(new File(filePath));
				imageStream =getContentResolver().openInputStream(selectedImage);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
             
           // imgEffect.setImageBitmap(yourSelectedImage);
           // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
            if(pictureFromCamera)
            	imgEffect.setRotation(90);
            //imgEffect.setLayoutParams(params);
            
           /* imgEffect.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				if(gesture==true)
				{
				 addGesture(event.getX(),event.getY());
				 gestureCordinates=event.getX()+","+event.getY();
				 
				}
				
				
				//gestureType=1;
					return false;
				}
			});*/
            
            
          Button btnNext=(Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if(gestureType!=0)
					{	
						gesture=true;
						if(gestureEnabled==true)
						{
						Intent intent=new Intent(ImageGestureActivity.this,ImageDistortedUploadActivity.class);
				       
					         intent.putExtra("filePath", filePath);
					         //intent.putExtra("pictureFromCamera", false);
					         intent.putExtra("filePathDistorted", filePathDistorted);
					         intent.putExtra("imageWidth", imageWidth);
					         intent.putExtra("imageHeight", imageHeight);
					         intent.putExtra("effectsAdded", effectsAdded);
					         intent.putExtra("gestureCordinates",gestureCordinates);
					         intent.putExtra("gestureType", gestureType);
			         
			               startActivity(intent);
			               finish();
						}
						else
						{
							GlobalMethods.showMessage(getApplicationContext(), "Please add gesture first.");
						}	
					}
					else
					{
						GlobalMethods.showMessage(getApplicationContext(), "Please add gesture first.");
					}	
				}
			});
		
		/* btnBack=(Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});*/
		/*btnCancel=(Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCancelPressed();
			}
		});*/
		
		btnGesture=(Button)findViewById(R.id.btnGesture);
		btnGesture.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addGesture();
			}
		});
}
	 @Override
	    protected void onDestroy() {
	    super.onDestroy();

	    unbindDrawables(findViewById(R.id.rootView));
	    System.gc();
	    }

	    private void unbindDrawables(View view) {
	        if (view.getBackground() != null) {
	        view.getBackground().setCallback(null);
	        }
	        if (view instanceof ViewGroup) {
	            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            unbindDrawables(((ViewGroup) view).getChildAt(i));
	            }
	        ((ViewGroup) view).removeAllViews();
	        }
	    }
}