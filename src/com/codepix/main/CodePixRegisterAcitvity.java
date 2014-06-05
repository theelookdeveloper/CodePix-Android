package com.codepix.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import com.codepix.model.CameraIntentHelperActivity;
import com.codepix.utilz.BitmapHelper;
import com.codepix.utilz.Global;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.HttpClient;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CodePixRegisterAcitvity extends CameraIntentHelperActivity {
	
	private class SendHttpRequestTask extends AsyncTask<String, Void, String[]> {

	    @Override
	    protected String[] doInBackground(String... params) {
	    	String fname=GlobalMethods.decodeNumericEntities(txtFirstName.getText().toString());
			String lname=GlobalMethods.decodeNumericEntities(txtLastName.getText().toString());
			String email=GlobalMethods.decodeNumericEntities(txtEmail.getText().toString());
			String password=GlobalMethods.decodeNumericEntities(txtPassword.getText().toString());
			String birthdate=GlobalMethods.decodeNumericEntities(txtBirthday.getText().toString());
	        String gender="Male";
	        
	        if(checkBoxFeMale.isChecked())
	        {
	        	gender="Female";
	        }
	           
	           
	       

	        try {
	            HttpClient client = new HttpClient(Global.url,getApplicationContext(),1);
	            client.connectForMultipart();
	            client.addFormPart("first_name", fname);
	            client.addFormPart("last_name", lname);
	            client.addFormPart("email", email);
	            client.addFormPart("password", password);
	            client.addFormPart("gender", gender);
	            client.addFormPart("Birth_date", birthdate);
	            client.addFormPart("login_type", "codepix");
	            client.addFormPart("cloginreg","1");
	            
	           
	            if(filePath!=null&&filePath.length()>0)
	            {	
	            	 Bitmap b = GlobalMethods.ShrinkBitmap(filePath, 403, 504);

	     	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     	        b.compress(CompressFormat.PNG, 0, baos);
	     	       System.out.println("filePath "+filePath);
	            client.addFilePart("fileurl", filePath, baos.toByteArray());
	            
	            }
	            client.finishMultipart();
	            String data[] = client.getResponse();
	            return data;
	            //Toast.makeText(getActivity(), "response"+data, Toast.LENGTH_LONG).show();
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	            
	            return new String[]{getString(R.string.internet_error)};
	        }
	    }

	    @Override
	    protected void onPostExecute(String result[]) {            
	       // item.setActionView(null);
	       pd.dismiss();
	       
	       GlobalMethods.showMessage(getApplicationContext(),result[0]);
	 	   
	 	   if(result[0].equals("success"))
	 	   {
	 		  //DashBoardActivity
	 		   
	 		 codePixPref.edit().clear();
	 		   
	 		 Intent intent=  new Intent(CodePixRegisterAcitvity.this,LoginActivity.class);
	 		 startActivity(intent);
	 	   }
	 	   /*else
	 	   {
	 		 GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
			
	 	   }  */
	    }

	}
	private EditText txtFirstName;
	private EditText txtLastName;
	private EditText txtEmail;
	private EditText txtPassword;
	private EditText txtBirthday;
	private Button buttonSubmit;
	private CheckBox checkBoxMale;
	private CheckBox checkBoxFeMale;
	private int mMonth;
	
	private int mDay;
   
	private int mYear;
	private final int DATE_DIALOG_ID=0;
	 static final int SELECT_PHOTO = 100;
	private static final int SELECT_PHOTO_FROM_CAMERA=200;
	private String filePath;	
	private ImageView imagView;
	
	private ProgressDialog pd;
	private SharedPreferences codePixPref;
	

	private Button btnBack;
	
	// the callback received when the user "sets" the date in the dialog
		private DatePickerDialog.OnDateSetListener mDateSetListener =
		        new DatePickerDialog.OnDateSetListener() {
			         @Override
		            public void onDateSet(DatePicker view, int year,
		                                  int monthOfYear, int dayOfMonth) {
		                mYear = year;
		                mMonth = monthOfYear;
		                mDay = dayOfMonth;
		                updateDisplay();
		            }

		        };
	protected void alertDialog(String title, String msg)
	  {
		//CLOSE APPLICATION
			// Create the alert box 
	      AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	      alertbox.setTitle(title);
	      // Set the message to display
	      alertbox.setMessage(msg);
	      
	      // Add a neutral button to the alert box and assign a click listener
	      alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	           
	          // Click listener on the neutral button of alert box
	          @Override
			public void onClick(DialogInterface arg0, int arg1) {
	        	  arg0.dismiss();
	            
	        	  
	        	 // startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
	        	  
	        	// onDestroy();
		          //finish();
	          }
	      });    
	      alertbox.show();
	      // show the alert box
	  }
		
		
	private void doHandleImage()
	{
		CharSequence[] items={"Take Picture","Select From Gallery"};
		new AlertDialog.Builder(this)
        .setSingleChoiceItems(items, 0, null)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
           

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                // Do something useful withe the position of the selected radio button
                
                if(selectedPosition==0)
                {
                	/*Intent photoPickerIntent = new Intent(CodePixRegisterAcitvity.this,Custom_CameraActivity.class);
    				
    				startActivityForResult(photoPickerIntent, SELECT_PHOTO_FROM_CAMERA);*/
                	
                	try {
	     				startCameraIntent();
	     			} catch (Exception e) {
	     				// TODO Auto-generated catch block
	     				e.printStackTrace();
	     			}
                }
                else  if(selectedPosition==1)
                {
                	Intent photoPickerIntent1 = new Intent(Intent.ACTION_PICK);
    				photoPickerIntent1.setType("image/*");
    				startActivityForResult(photoPickerIntent1, SELECT_PHOTO);
                }
            }
        })
        .show();
	}
	 private boolean doValidate() {
			// TODO Auto-generated method stub
		
		  
		     
			   if(txtEmail.length()==0)
			   {
				   GlobalMethods.showMessage(CodePixRegisterAcitvity.this,"Please enter email ID.");
				   txtEmail.requestFocus();
				   return false;
			   }
			   
			   if(txtPassword.length()==0)
			   {
				   GlobalMethods.showMessage(CodePixRegisterAcitvity.this,"Please enter password.");
				   txtPassword.requestFocus();
				   return false;
			   }
			   
			   
			 
			   boolean net=false;
			     try {
			    	 //checking Internet connection exit or not
					net = GlobalMethods.checkInternetConnection(CodePixRegisterAcitvity.this);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     if(net==false)
				  {
				   alertDialog("ERROR","Unable to connect with server."+"\n"+"Please check your internet connection.");
				    return false;
				  }  
			
			   return true;
			  
				  
		}	
		
	 public String getPath(Uri uri) {
	    String[] projection = { MediaColumns.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor
	            .getColumnIndexOrThrow(MediaColumns.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	 
	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case SELECT_PHOTO:
	        if(resultCode ==RESULT_OK){  
	            Uri selectedImage = imageReturnedIntent.getData();
	           filePath= getPath(selectedImage);  
	           
	          // Toast.makeText(getActivity(), "File://"+filePath, Toast.LENGTH_LONG).show();
	            InputStream imageStream = null;
				try {
					imageStream =getContentResolver().openInputStream(selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
	            imagView.setImageBitmap(yourSelectedImage);
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
	            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
	            imagView.setLayoutParams(params);
	        }
	        break;
	        
	    case SELECT_PHOTO_FROM_CAMERA:
	    	//Toast.makeText(this, "file"+filePath, Toast.LENGTH_LONG).show();
	        if(resultCode ==RESULT_OK){ 
	        	filePath=imageReturnedIntent.getStringExtra("filepath");
	        	     
		            imagView.setImageURI(Uri.fromFile(new File(filePath)));
		            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
		            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
		            imagView.setLayoutParams(params);
		           // imagView.setRotation(90);
		            
	        }
	        break;
	    }  
	   
	}
	 
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_codepixregister);
			
			txtFirstName=(EditText)findViewById(R.id.editTextFirstName);
			txtLastName=(EditText)findViewById(R.id.editTextLastName);
			txtEmail=(EditText)findViewById(R.id.editTextEmail);
			txtPassword=(EditText)findViewById(R.id.editTextPassword);
			txtBirthday=(EditText)findViewById(R.id.edittextBirthDate);
			
			buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
			
			checkBoxMale=(CheckBox)findViewById(R.id.checkBoxMale);
			checkBoxFeMale=(CheckBox)findViewById(R.id.checkBoxFeMale);
			
			 codePixPref = getSharedPreferences("CodePixPref", 0);
			
			  imagView=(ImageView)findViewById(R.id.imageViewProfilePicture);
			
			imagView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doHandleImage();
					
				}
			});
			txtBirthday.setOnClickListener(new OnClickListener() {
				
				

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(DATE_DIALOG_ID);
				}
			});
		    
		    
		     final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				
				//updateDisplay();
			
			checkBoxMale.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
					{
						checkBoxFeMale.setChecked(false);	
					}
				}
			});
			
   checkBoxFeMale.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
					{
						checkBoxMale.setChecked(false);	
					}
				}
			});
			
			
			buttonSubmit.setOnClickListener(new Button.OnClickListener() {
				
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					if(doValidate())
					{
						pd.show();
					final SendHttpRequestTask task=new SendHttpRequestTask();
					
					
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
					    	  task.cancel(true);
					    	  
					    	  pd.dismiss();
					    	  
					    	  Toast.makeText(getApplicationContext(),getString(R.string.process_long),15000).show();
					      }	  
					  }
					}, 120*1000);
					
					}
				}
			});
			
			btnBack=(Button)findViewById(R.id.btnBack);
			
			btnBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(CodePixRegisterAcitvity.this,RegisterActivity.class);
					
					startActivity(intent);
				}
			});
			
			 pd = new ProgressDialog(this);
				
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
			
		}
		
	
		        

		        @Override
					protected Dialog onCreateDialog(int id) {
						 switch (id) {
						    case DATE_DIALOG_ID: 
						    	DatePickerDialog dt=new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
						       dt.setTitle("Select Birthdate");
						       
						       return dt;
						    }
						    return null;
					}	
		    	    
		    	    @Override
					protected void onResume() {
						// TODO Auto-generated method stub
						super.onResume();
						  final Calendar c = Calendar.getInstance();
							mYear = c.get(Calendar.YEAR);
							mMonth = c.get(Calendar.MONTH);
							mDay = c.get(Calendar.DAY_OF_MONTH);
							
							//updateDisplay();
					}
		    	    
		    	    // updates the date in the TextView
					private void updateDisplay() {
					    txtBirthday.setText(
					        new StringBuilder()
					                // Month is 0 based so add 1
					                .append(mDay).append("-")
					                .append(mMonth + 1).append("-")
					                .append(mYear).append(" "));
					} 	
					
					 @SuppressLint("NewApi")
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
					            Bitmap yourSelectedImage = null;
					            //String path=getRealPathFromURI(ImageEffectsActivity.this,photoUri);
					    		yourSelectedImage=photo;
					    		
					    		filePath=new File(photoUri.getPath()).getAbsolutePath();
					           // GlobalMethods.showMessage(getApplicationContext(), filePath);
					           // imgEffect.setLayoutParams(params);
					          //  ImageView imageView = (ImageView) findViewById(R.id.acitvity_take_photo_image_view);
					            
					    		Bitmap b=GlobalMethods.ShrinkBitmap(filePath, 200, 200);
					    		
					    		imagView.setImageBitmap(b); 
					    		imagView.setRotation(90);
					            
					          //  imageViewProfile=imgEffect.getHeight();
					    		//imageHeight=imgEffect.getWidth();
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
					
    
}
