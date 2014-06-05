package com.codepix.main;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepix.db.DBHelper;
import com.codepix.loader.FileCache;
import com.codepix.loader.ImageLoader;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.JSONParser;

public class ImageUnlockGestureActivity extends Activity {
	
	private ImageView imgEffect;
	private boolean gesture=true;
	private Intent mIntent;
	private String filePath;
	InputStream imageStream = null;
	int imageWidth = 0;
	int imageHeight = 0;
	int gestureType = 0;
	String gestureCordinates=null;
	boolean effectsAdded = false;
	protected boolean gestureEnabled;
	private String postid;
	private ImageLoader mLoader;
	private LinearLayout imageViewUnlock;
	private String image;
	private SharedPreferences codePixPref;
	private Button btnUnlock;
	private double[] cordinates;
	public static int xPos;
	public static int yPos;
	private TextView textViewCaption;
	private ProgressDialog pd;
	private String likestatus;
	private Button btnBack;
	private GestureImageViewUnlock view;
	public static Bitmap SelectedImage;

	
	
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
		
        FileCache fileCache=new FileCache(getApplicationContext());
	// yourSelectedImage = BitmapFactory.decodeStream(imageStream);
       File f=fileCache.getFile(image);
        
        //from SD cache
       // Bitmap b = mLoader.decodeFile(f);
       // Bitmap bitmap =mLoader.decodeFile(f);
        Bitmap bitmap =GlobalMethods.ShrinkBitmap(f.getAbsolutePath(), 403, 503);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
       

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_4444, true);
        
        
        Canvas canvas = new Canvas(mutableBitmap);
        //canvas.drawBitmap(mutableBitmap, 0, 0, paint);
        canvas.drawCircle(x, y, 20, paint);
        imageViewUnlock.invalidate();
       // ImageView imageView = (ImageView)findViewById(R.id.schoolboard_image_view);
        //imageViewUnlock.setAdjustViewBounds(true);
       // imageViewUnlock.setImageBitmap(mutableBitmap);
       
        
        gestureEnabled=true;
	}
    public void onBackPress() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	Intent intent=new Intent(ImageUnlockGestureActivity.this,ImageEffectsActivity.class); 
    	intent.putExtra("filePath", filePath);
        
    	startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	/*Intent intent=new Intent(ImageGestureActivity.this,ImageEffectsActivity.class); 
    	intent.putExtra("filePath", filePath);
        
    	startActivity(intent);*/
    }
	
	 public void onCancelPressed() {
	    	startActivity(new Intent(ImageUnlockGestureActivity.this,DashboardActivity.class));
	    }
	 
	 private void showUnlockDialog()
	 {
	 	//CharSequence[] items={"A single tap at specific point","Draw Pattern"};
	 	new AlertDialog.Builder(this)
	 	  .setMessage(getString(R.string.message_to_unlock))
	      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        

	 		@Override
	 		public void onClick(DialogInterface dialog, int whichButton) {
	             dialog.dismiss();
	 		}    
	     })
	      .show();
	 }
	 
	 private void showUnlockSuccessDialog()
	 {
	 	//CharSequence[] items={"A single tap at specific point","Draw Pattern"};
	 	new AlertDialog.Builder(this)
	 	  .setTitle("Success!")
	 	  .setMessage(getString(R.string.message_to_unlock_success))
	      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        

	 		@Override
	 		public void onClick(DialogInterface dialog, int whichButton) {
	             dialog.dismiss();
	             
	             unLockPostToServer();
	 		}    
	     })
	      .show();
	 }
	 
	 private void showUnlockErrorDialog(int attempt)
	 {
	 	//CharSequence[] items={"A single tap at specific point","Draw Pattern"};
	 	new AlertDialog.Builder(this)
	 	  .setTitle("Attempt "+attempt)
	 	  .setMessage(getString(R.string.message_to_unlock_error))
	      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        

	 		@Override
	 		public void onClick(DialogInterface dialog, int whichButton) {
	             dialog.dismiss();
	 		}    
	     })
	      .show();
	 }
	 @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.activity_unlockpicture);
	        
	        codePixPref = getSharedPreferences("CodePixPref", 0);
	        Intent intent=getIntent();
			Bundle b=intent.getExtras();
			
			
			imageViewUnlock=(LinearLayout)findViewById(R.id.imageViewUnlock);
			textViewCaption=(TextView)findViewById(R.id.textViewCaption);
			btnUnlock=(Button)findViewById(R.id.btnUnlock);
			btnBack=(Button)findViewById(R.id.btnBack);
	 	   
	 	  DBHelper db = new DBHelper(this);
	        
			db.open();
	        postid=b.getString("postid", "0");
	        //Toast.makeText(this, "postid"+postid, Toast.LENGTH_LONG).show();
			Cursor c = db.getPostDetails(Integer.parseInt(postid));
	 
			startManagingCursor(c);
			
			c.moveToNext();
			
		 image=c.getString(c.getColumnIndex(DBHelper.KEY_DISTORTED_IMAGE_URL));
			
			if(image.length()==0)
				image=c.getString(c.getColumnIndex(DBHelper.KEY_ORIGINAL_IMAGE_URL));
			
			String points=c.getString(c.getColumnIndex(DBHelper.KEY_GESTURE_CORDINATES));
			
			
			
			cordinates=new double[2];
			
			try {
				cordinates[0]=Double.parseDouble(points.substring(0, points.indexOf(",")));
				cordinates[1]=Double.parseDouble(points.substring(points.lastIndexOf(",")+1, points.length()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				cordinates[0]=0;
				cordinates[1]=0;
				e.printStackTrace();
			}
			
			CharSequence caption=c.getString(c.getColumnIndex(DBHelper.KEY_CAPTION));
			
			textViewCaption.setText(caption);
			
		
			SelectedImage = GlobalMethods.ShrinkBitmap(image, 403, 503);
			view=new GestureImageViewUnlock(ImageUnlockGestureActivity.this,btnUnlock);
			
			LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(403, 503);
		        
		      
		        imageViewUnlock.addView(view,params);
			    
		    	mLoader = new ImageLoader(this);
				
				mLoader.DisplayImage(image, view);	
				
				
				
				
				
			/*view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btnUnlock.setEnabled(true);
					btnUnlock.setBackgroundColor(Color.parseColor("#FF0000"));
				}
			});*/
				
				

				

			        	
						//addGesture(event.getX(),event.getY());
					    //xPos=(int) event.getX();
					   //yPos=(int) event.getY();
			
			
			btnUnlock.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doUnlock(xPos,yPos);
				}
			});
        btnBack.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
			
			showUnlockDialog();
			
			
           pd = new ProgressDialog(this);
			
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			
	 	   db.close();
	 	 
}
	 protected void callPostDetails() {
			// TODO Auto-generated method stub
			//Toast.makeText(context, "test"+postid, Toast.LENGTH_LONG).show();
			Intent intent=new Intent(ImageUnlockGestureActivity.this,PostDetailsActivity.class);
			intent.putExtra("postid", postid);
			startActivity(intent);
		}
	 public void doUnlock(int x, int y)
	 {
		 int diff=15; 
			
			boolean f=cordinates[0]<=(x+diff) && cordinates[0]>=(x-diff) && cordinates[1]<=(y+diff) && cordinates[1]>=(y-diff);
			
			System.out.println("unlock x"+x+"y"+y+"diff"+diff+"cordinates[0]"+cordinates[0]+"cordinates[1]"+cordinates[1]+"flag"+f);
			if(cordinates[0]<=(x+diff) && cordinates[0]>=(x-diff) && cordinates[1]<=(y+diff) && cordinates[1]>=(y-diff))
			{
				//GlobalMethods.showMessage(ImageUnlockGestureActivity.this, "success");
				showUnlockSuccessDialog();
			}
			else
			{
				int value = codePixPref.getInt("attempts"+postid, 0)+1;
				codePixPref.edit().putInt("attempts"+postid, value).commit();
				showUnlockErrorDialog(value);
			}
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
	    
	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void unLockPostToServer()
		{

			
			if(GlobalMethods.checkInternetConnection(getApplicationContext())==true)
			{
				pd.show();
				final LongUnlockPostOperation task=new LongUnlockPostOperation();
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
				else
					task.execute("start");
					Handler handler = new Handler();
					handler.postDelayed(new Runnable()
					{
					  @Override
					  public void run() {
					      if ( task.getStatus() == AsyncTask.Status.RUNNING )
					      {
					    	 
					    	  runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									pd.dismiss();
							    	  GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));	
								}
							});
					    	  
					    	  task.cancel(true);
					    				    	  
					      }	  
					  }
					}, 10*1000);
			}
			else
			{
				GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
			}	
		}
	    
	    
	    private class LongUnlockPostOperation extends AsyncTask<String, Void, String[]> {

	    	 @Override
		      protected String[] doInBackground(String... params) {
		    	  
		            //  while(true)
		             // {
				 //String []str=null;
		              	if(GlobalMethods.checkInternetConnection(getApplicationContext()))
		              	{	
		              	   try {
		              		 JSONParser jParser = new JSONParser();
		              		 SharedPreferences codePixPref=getSharedPreferences("CodePixPref", 0);
		              		 Map<String, String> map = new HashMap<String, String>();
		         	        String userid=codePixPref.getString("userid", "");
		         	    	 map.put("unlockedPost", "1");
		         	    	 map.put("userid", userid);
		         	    	 map.put("postid", postid);	 
		         	    	 
		         	    	
		         	 
		         	        // Getting JSON from URL
		         	        final JSONObject json = jParser.getJSONFromUrl(map);
		         	        
		         	        if(json.getString("status").equals("success"))
		         	        {
		         	        	String[] str=new String[2];
		         	        	
		         	        	str[0]="true";
		         	        	str[1]=json.getString("message");
		         	        	System.out.println("response in addcomment"+json.toString());
		         	        	return str;
		         	        }
		         	        else
		         	        {
		         	        	String[] str=new String[2];
		         	        	
		         	        	str[0]="false";
		         	        	str[1]=json.getString("message");
								return str;
		         	        }	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							String[] str=new String[2];
	         	        	
	         	        	str[0]="false";
	         	        	str[1]=getString(R.string.internet_error);
							return str;
						}
		              	
							}
		             // }
		              	String[] str=new String[2];
	     	        	
	     	        	str[0]="false";
	     	        	str[1]=getString(R.string.internet_error);
						return str;
		      }        

		     
				@Override
		      protected void onPostExecute(String[] result) { 
					pd.dismiss();
					if(result[0].equals("true")==true)
					{
						GlobalMethods.showMessage(getApplicationContext(), result[1]);
						DBHelper db=new DBHelper(getApplicationContext());
						db.open();
						int status=db.updateUnlockedPost(postid);
						db.close();
						if(status!=0)
						callPostDetails();
						
					}
					else
					{
						GlobalMethods.showMessage(getApplicationContext(), result[1]);
					}
					
		      }

		      @Override
		      protected void onPreExecute() {
		      }

		      @Override
		      protected void onProgressUpdate(Void... values) {
		      }
		  }
	    
	   
}