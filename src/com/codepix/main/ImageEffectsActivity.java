package com.codepix.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import com.codepix.model.CameraIntentHelperActivity;
import com.codepix.utilz.BitmapHelper;
import com.codepix.utilz.GlobalMethods;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ImageEffectsActivity extends CameraIntentHelperActivity {
	
	public static Bitmap applyGaussianBlur(Bitmap src) {
		double[][] GaussianBlurConfig = new double[][] {
			{ 2, 4, 2 },
			{ 4, 8, 4 },
			{ 2, 4, 2 }
		};
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(GaussianBlurConfig);
		convMatrix.Factor = 16;
		convMatrix.Offset = 0;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	private ImageView imgEffect;
	private Intent mIntent;
	private Bitmap yourSelectedImage;
	private Button btnNext;
	String filePath =null; 
	protected String filePathDistorted=null;
	int imageWidth = 0;
	int imageHeight = 0;
	boolean effectsAdded = false;
	private Button btnCancel;
	private Button btnNone;
	private Button btnBlur;
	
	private Button btnPixcelate;
	int effect=0; 
	Bitmap yourImage=null;
	private ProgressDialog pd;
	

	private boolean pictureFromCamera;
	
	@SuppressLint("NewApi")
	protected void activateButton(int i)
	{
		if(i==1)
		{
			btnNone.setBackgroundResource(R.drawable.segment_button_left);
			btnBlur.setBackgroundResource(R.drawable.segment_button_center_deactive);
			btnPixcelate.setBackgroundResource(R.drawable.segment_button_right_deactive);
			
			btnNone.setTextColor(Color.parseColor("#FFFFFF"));
			btnBlur.setTextColor(Color.parseColor("#0033CC"));
			btnPixcelate.setTextColor(Color.parseColor("#0033CC"));
		}
		else
			if(i==2)
			{
				btnNone.setBackgroundResource(R.drawable.segment_button_left_deactivate);
				btnBlur.setBackgroundResource(R.drawable.segment_button_center);
				btnPixcelate.setBackgroundResource(R.drawable.segment_button_right_deactive);
				
				btnNone.setTextColor(Color.parseColor("#0033CC"));
				btnBlur.setTextColor(Color.parseColor("#FFFFFF"));
				btnPixcelate.setTextColor(Color.parseColor("#0033CC"));
			}
			/*else
				if(i==3)
				{
					btnNone.setBackgroundResource(R.drawable.segment_button_left_deactivate);
					btnBlur.setBackgroundResource(R.drawable.segment_button_center_deactive);
					btnPixcelate.setBackgroundResource(R.drawable.segment_button_right);
					
					btnNone.setTextColor(Color.parseColor("#0033CC"));
					btnBlur.setTextColor(Color.parseColor("#0033CC"));
					btnPixcelate.setTextColor(Color.parseColor("#FFFFFF"));
				}*/
	}
	
	
	
	protected void applyBlurEffect(final int i)
	{
	 
		pd.show();
	  new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
				
			
			if(i==1)
			{
				yourImage = GlobalMethods.ShrinkBitmap(filePath, 403, 503);
				
			//	imgEffect.setImageBitmap(yourImage);
				
				imageWidth=yourImage.getWidth();
				imageHeight=yourImage.getHeight();
				
				effectsAdded=false;
				filePathDistorted=saveFile(yourImage);
			}
			else
			if(i==2)
			{
			//imgEffect.setImageBitmap(null);
			yourImage=fastblur(yourSelectedImage,10);
			//imgEffect.setImageBitmap(t);
			
			imageWidth=yourImage.getWidth();
			imageHeight=yourImage.getHeight();
			
			effectsAdded=true;
			filePathDistorted=saveFile(yourImage);
			}
		/*	else
				if(i==3)
				{
				//imgEffect.setImageBitmap(null);
				//yourImage = GlobalMethods.ShrinkBitmap(filePath, 403, 503);
					yourImage=applyPixelateEffect(yourSelectedImage);
				//Bitmap t = BitmapFilter.changeStyle(yourImage, BitmapFilter.PIXELATE_STYLE, 5);
				
				
					
					
				imageWidth=yourImage.getWidth();
				imageHeight=yourImage.getHeight();
				
				effectsAdded=true;
				filePathDistorted=saveFile(yourImage);
				}*/
			runOnUiThread(new Runnable() {
				public void run() {
					
					imgEffect.setImageBitmap(yourImage);
					pd.dismiss();
				}
			}
			);
			
			
		}
	}).start();
		
		
	} 
	
	protected Bitmap applyPixelateEffect(Bitmap yourSelectedImage2)
	{
		int avR,avB,avG; // store average of rgb 
		int pixel;
		int width=imageWidth;
		int height=imageHeight;
		Bitmap bmOut = Bitmap.createBitmap(imageWidth, imageHeight, yourSelectedImage2.getConfig());

		int pixelationAmount=0;
		for(int x = 0; x < width; x+= pixelationAmount) { // do the whole image
		
		for(int y = 0; y < height; y++) {
		   avR = 0; avG = 0; avB =0;

		   for(int xx =x; xx <pixelationAmount;xx++){// YOU WILL WANT TO PUYT SOME OUT OF                                      BOUNDS CHECKING HERE
		   for(int yy= y; yy <pixelationAmount;yy++){ // this is scanning the colors
		      pixel = yourSelectedImage.getPixel(x, y);
		      avR += (Color.red(pixel));
		      avG+= (Color.green(pixel));
		      avB += (Color.blue(pixel));
		    }
		    }
		  int avrR = pixelationAmount^2; //divide all by the amount of samples taken to get an average
		  int avrG=0;
		  avrG/= pixelationAmount^2;
		  int avrB = pixelationAmount^2;

		 for(int xx =x; xx <pixelationAmount;xx++){// YOU WILL WANT TO PUYT SOME OUT OF BOUNDS CHECKING HERE
		 for(int yy= y; yy <pixelationAmount;yy++){ // this is going back over the block
		  bmOut.setPixel(xx, yy, Color.argb(255, avR, avG,avB)); //sets the block to the average color
		 }
		 }

		}

		}
		return bmOut;
	}
	public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaColumns.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}

	@SuppressWarnings("deprecation")
	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	
	public void onBackPress() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	
    	startActivity(new Intent(ImageEffectsActivity.this,DashboardActivity.class));
    }
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	System.out.println("on back pressed");
    	//startActivity(new Intent(ImageEffectsActivity.this,DashboardActivity.class));
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	/*	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	    .detectAll()
	    .penaltyLog()
	    .penaltyDeath()
	    .build());*/ 
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.activity_imageeffect);
	        
	        imgEffect=(ImageView)findViewById(R.id.imageViewEffect);
	        mIntent=getIntent();
	        Bundle str=mIntent.getExtras();
	 	   if(str!=null)
	 	   {
	 	   
	 	     filePath = str.getString("filePath");
	 	   
	 	    pictureFromCamera=str.getBoolean("pictureFromCamera");
	        
	 	   /*Bitmap yourBitmap;
	 	  Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
	*/
	       // if(getScreenOrientation()==1)
            yourSelectedImage = GlobalMethods.ShrinkBitmap(filePath, 403, 504);
	        //else
	        //	yourSelectedImage = GlobalMethods.ShrinkBitmap(filePath, 800, 480);	
           imgEffect.setImageBitmap(yourSelectedImage);
           
           imageWidth=yourSelectedImage.getWidth();
			imageHeight=yourSelectedImage.getHeight();
            
	 	   }
	 	   else
	 	   {
	 		  try {
				startCameraIntent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	   } 
	 	   
	 	  
            btnNext=(Button)findViewById(R.id.btnNext);
            btnNone=(Button)findViewById(R.id.btnNone);
            btnBlur=(Button)findViewById(R.id.btnBlur);
            btnPixcelate=(Button)findViewById(R.id.btnPixcelate);
           // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
            
            
            
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
      	   int degrees = 0;

      	 /*  switch (rotation) {
      	       case Surface.ROTATION_0: degrees = 0; break;
      	       case Surface.ROTATION_90: degrees = 90; break;
      	       case Surface.ROTATION_180: degrees = 180; break;
      	       case Surface.ROTATION_270: degrees = 270; break;
      	   }*/
      	//imgEffect.setRotation(0);
      	 
      	//Toast.makeText(getApplicationContext(), getScreenOrientation()+"-"+imageHeight+">"+imageWidth, Toast.LENGTH_LONG).show();
      	/* if(getScreenOrientation()==1)
           imgEffect.setRotation(90);
      	 else
      		imgEffect.setRotation(0);*/
      	 
      	   
      	// if(pictureFromCamera)
      		//imgEffect.setRotation(0);
            //imgEffect.setLayoutParams(params);
            
          /*  imgEffect.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					imgEffect.setImageBitmap(null);
					Bitmap t=fastblur(yourSelectedImage,10);
					imgEffect.setImageBitmap(t);
					
					imageWidth=t.getWidth();
					imageHeight=t.getHeight();
					
					effectsAdded=true;
					filePathDistorted=saveFile(t);
					
					//Toast.makeText(getApplicationContext(), filePathDistorted+"-"+filePath, Toast.LENGTH_LONG).show();
					return false;
				}
			});*/
            
            
            
            btnNext.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if(filePathDistorted==null)
						filePathDistorted=filePath;
					Intent intent=new Intent(ImageEffectsActivity.this,ImageGestureActivity.class);
				       
			         intent.putExtra("filePath", filePath);
			         intent.putExtra("pictureFromCamera", pictureFromCamera);
			         intent.putExtra("filePathDistorted", filePathDistorted);
			         intent.putExtra("imageWidth", imageWidth);
			         intent.putExtra("imageHeight", imageHeight);
			         intent.putExtra("effectsAdded", effectsAdded);
			         
			         startActivity(intent);
				}
			});
            
            
            btnCancel=(Button)findViewById(R.id.btnCancel);
            
            btnCancel.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPress();
					
				}
			});
            
            
			btnNone.setOnClickListener(new Button.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								activateButton(1);				
								applyBlurEffect(1);
								
							}
						});
			
			btnBlur.setOnClickListener(new Button.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								activateButton(2);
								applyBlurEffect(2);
								
							}
						});
			btnPixcelate.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					activateButton(3);
					applyBlurEffect(3);
					
					       
					       
					       
				}
			});

           pd = new ProgressDialog(this);
			
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);    
            
}
    
    @Override
    protected void onDestroy() {
    super.onDestroy();

    unbindDrawables(findViewById(R.id.rootView));
    System.gc();
    }

    @Override
	protected void onPhotoUriFound() {
		//TextView uirView = (TextView) findViewById(R.id.acitvity_take_photo_image_uri);
		//uirView.setText("photo uri: " + photoUri.toString());
		
		//GlobalMethods.showMessage(getApplicationContext(), photoUri.toString());
		Bitmap photo = BitmapHelper.readBitmap(this, photoUri);
		
		//yourSelectedImage=GlobalMethods.ShrinkBitmap(photoUri., 480, 800);
        if (photo != null) {
        	
        	/*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        	LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        	params.addRule(RelativeLayout.BELOW,linearLayout.getId());
        	params.addRule(RelativeLayout.CENTER_IN_PARENT);
        	params.topMargin=100;
        	params.bottomMargin=10;*/
        	
        	
           photo = BitmapHelper.shrinkBitmap(photo, 370, rotateXDegrees);
            yourSelectedImage=null;
            //String path=getRealPathFromURI(ImageEffectsActivity.this,photoUri);
    		yourSelectedImage=photo;
    		
    		filePath=new File(photoUri.getPath()).getAbsolutePath();
            GlobalMethods.showMessage(getApplicationContext(), filePath);
           // imgEffect.setLayoutParams(params);
          //  ImageView imageView = (ImageView) findViewById(R.id.acitvity_take_photo_image_view);
            imgEffect.setImageBitmap(photo); 
            
            imageHeight=imgEffect.getHeight();
    		imageHeight=imgEffect.getWidth();
            //imgEffect.setRotation(rotateXDegrees);
        }
		
        //Delete photo in second location (if applicable)
        if (preDefinedCameraUri != null && !preDefinedCameraUri.equals(photoUri)) {
        	BitmapHelper.deleteImageWithUriIfExists(preDefinedCameraUri, this);
        }
	}
    

	@Override
	protected void onPhotoUriNotFound() {
		super.onPhotoUriNotFound();
		GlobalMethods.showMessage(getApplicationContext(),"photo uri: not found");
	}
	
	protected String saveFile(Bitmap t) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		t.compress(Bitmap.CompressFormat.PNG, 40, bytes);

		//you can create a new file name "test.jpg" in sdcard folder.
		File f = new File(Environment.getExternalStorageDirectory()
		                        + File.separator + "test.png");
		FileOutputStream fo=null;
		try {
			f.createNewFile();
			 fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//write the bytes in file
		

		// remember close de FileOutput
		
		return f.getAbsolutePath();
		
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