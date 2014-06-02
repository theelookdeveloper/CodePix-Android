package com.codepix.main;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import com.codepix.utilz.Global;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.HttpClient;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EditProfileActivity extends Activity  {

 

public class DownloadProfileImageTask extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {
		
		
		String path=GlobalMethods.downloadFile(codePixPref.getString("image_url", ""));
		// TODO Auto-generated method stub
		return path;
	}
	
	 @Override
	    protected void onPostExecute(String result) {            
	       // item.setActionView(null);
           progressImage.setVisibility(View.GONE);
           
           
           if(new File(result).isFile()==true)
           {
           Bitmap src=BitmapFactory.decodeFile(result);
           
           codePixPref.edit().putString("downloaded_Profile_image", result).commit();
           
           imageViewProfile.setImageBitmap(src);
           
           RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
            
            imageViewProfile.setLayoutParams(params);
             codePixPref.edit().putBoolean("image_url_flag", true).commit();
           }
	    }

}
private class SendHttpRequestTask extends AsyncTask<String, Void, String[]> {

    @Override
    protected String[] doInBackground(String... params) {
    	String fname=GlobalMethods.decodeNumericEntities(textEditFirstName.getText().toString());
		String lname=GlobalMethods.decodeNumericEntities(textEditLastName.getText().toString());
		String mobile_number=GlobalMethods.decodeNumericEntities(textEditMobileNumber.getText().toString());
		//String password=GlobalMethods.decodeNumericEntities(txtPassword.getText().toString());
		String birthdate=GlobalMethods.decodeNumericEntities(textEditBirthDate.getText().toString());
        String gender="Male";
        
        if(checkBoxFemale.isChecked())
        {
        	gender="Female";
        }
           
           
       

        try {
        	
        	String userid=codePixPref.getString("userid", "");
            HttpClient client = new HttpClient(Global.url,getApplicationContext());
            client.connectForMultipart();
            client.addFormPart("first_name", fname);
            client.addFormPart("last_name", lname);
            client.addFormPart("phone", mobile_number);
            
            client.addFormPart("gender", gender);
            client.addFormPart("Birth_date", birthdate);
            client.addFormPart("userid", userid);
            client.addFormPart("updateProfile","1");
            
           
            if(filePath!=null&&filePath.length()>0)
            {	
            	 Bitmap b = BitmapFactory.decodeFile(filePath);

     	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
     	        b.compress(CompressFormat.PNG, 0, baos);
     	       System.out.println("filePath "+filePath);
            client.addFilePart("fileurl", filePath, baos.toByteArray());
            
            codePixPref.edit().putBoolean("image_url_flag", false).commit();
            
            }
            client.finishMultipart();
            String data[] = client.getResponse();
            return data;
            //Toast.makeText(getActivity(), "response"+data, Toast.LENGTH_LONG).show();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result[]) {            
       // item.setActionView(null);
       pd.dismiss();
       
       GlobalMethods.showMessage(getApplicationContext(),result[0]);
 	   
 	   if(result[0].equals("success"))
 	   {
 		  //DashBoardActivity
 		   
 		
 	   }
    }

}
private ImageView imageViewProfile;
private EditText textEditFirstName;
private EditText textEditLastName;
private EditText textEditMobileNumber;
private EditText textEditBirthDate;
private CheckBox checkBoxMale;
private CheckBox checkBoxFemale;
private Button btnSave;
private ProgressDialog pd;

private int mMonth;

private int mDay;
private int mYear;
private final int DATE_DIALOG_ID=0;
static final int SELECT_PHOTO = 100;
private static final int SELECT_PHOTO_FROM_CAMERA=200;
private String filePath;
private LinearLayout linearLayout;
private SharedPreferences codePixPref;

private ProgressBar progressImage;

private FileInputStream is;

//the callback received when the user "sets" the date in the dialog
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
		            	Intent photoPickerIntent = new Intent(EditProfileActivity.this,Custom_CameraActivity.class);
						
						startActivityForResult(photoPickerIntent, SELECT_PHOTO_FROM_CAMERA);
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
		        
		     protected boolean doValid() {
					// TODO Auto-generated method stub
					
					
					if(textEditFirstName.length()==0)
					{
						GlobalMethods.showMessage(getApplicationContext(), "Please enter first name.");
						textEditFirstName.requestFocus();
						return false;
						
					}
					
					 boolean net=false;
				     try {
				    	 //checking Internet connection exit or not
						net = GlobalMethods.checkInternetConnection(EditProfileActivity.this);
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
			    	            imageViewProfile.setImageBitmap(yourSelectedImage);
			    	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
			    	            
			    	            imageViewProfile.setLayoutParams(params);
			    	            
			    	        }
			    	        break;
			    	        
			    	    case SELECT_PHOTO_FROM_CAMERA:
			    	    	//Toast.makeText(this, "file"+filePath, Toast.LENGTH_LONG).show();
			    	        if(resultCode ==RESULT_OK){ 
			    	        	
			    	        	try
			    	        	{
			    	        	filePath=imageReturnedIntent.getStringExtra("filepath");
			    	        	     
			    	        	imageViewProfile.setImageURI(Uri.fromFile(new File(filePath)));
			    	        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
				    	           // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
				    	            imageViewProfile.setLayoutParams(params);
				    	            imageViewProfile.setRotation(90);
			    	        	}
			    	        	catch(Exception e)
			    	        	{
			    	        		GlobalMethods.showMessage(getApplicationContext(), "Error");
			    	        	}
				    	            
			    	        }
			    	        break;
			    	    }  
			    	   
			    	}	
			    	    
			    	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
						@Override
						    public void onCreate(Bundle savedInstanceState) {
						        // Inflate the layout for this fragment
						       super.onCreate(savedInstanceState);
						       
						       
						       setContentView(R.layout.fragment_profile);
						       codePixPref = getSharedPreferences("CodePixPref", 0);
						        
						        imageViewProfile=(ImageView)findViewById(R.id.imageViewProfile);
						        textEditFirstName=(EditText)findViewById(R.id.editTextFirstName);
						        textEditLastName=(EditText)findViewById(R.id.editTextLastName);
						        textEditMobileNumber=(EditText)findViewById(R.id.editTextMobileNumber);
						        textEditBirthDate=(EditText)findViewById(R.id.editTextBirthDate);
						        checkBoxMale=(CheckBox)findViewById(R.id.checkBoxMale);
						        checkBoxFemale=(CheckBox)findViewById(R.id.checkBoxFemale);
						        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
						        
						       textEditFirstName.setText(codePixPref.getString("first_name", ""));
						       textEditLastName.setText(codePixPref.getString("last_name", ""));
						       textEditMobileNumber.setText(codePixPref.getString("phone", ""));
						       textEditBirthDate.setText(codePixPref.getString("Birth_date", ""));
						       progressImage=(ProgressBar)findViewById(R.id.progressImage);
						       
						       
						       
						       
						       String gender=codePixPref.getString("gender", "Male");
						       
						       if(gender.equalsIgnoreCase("Male")==true)
						       {
						    	   checkBoxMale.setChecked(true);
						    	   checkBoxFemale.setChecked(false);
						    	  
						       }   
						       else
						       {
						    	   checkBoxFemale.setChecked(true);
						    	   checkBoxMale.setChecked(false);
						    	 
						       }
						       
						       if(codePixPref.getBoolean("image_url_flag", false)==false && codePixPref.getString("downloaded_Profile_image",null)==null)
						       {
						    	   progressImage.setVisibility(View.VISIBLE);
						    	   
						    	   final DownloadProfileImageTask task=new DownloadProfileImageTask();
									
									
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
									    	  
									    	  progressImage.setVisibility(View.GONE);
									    	  
									    	  Toast.makeText(getApplicationContext(),getString(R.string.process_long),15000).show();
									      }	  
									  }
									}, 120*1000);
									
								}
						       
						       if(codePixPref.getString("downloaded_Profile_image",null)!=null)
						       {
						    	   
						    	  Bitmap bp=BitmapFactory.decodeFile(codePixPref.getString("downloaded_Profile_image",null));
								// Bitmap src=BitmapFactory.decodeStream(is);
								   
								   imageViewProfile.setImageBitmap(bp);
								   
								   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
						           
						           imageViewProfile.setLayoutParams(params);
						    	   
						    	  
						       }
						       
						
						       
						       
						       
						       btnSave=(Button)findViewById(R.id.btnSave);
						        
						        btnSave.setOnClickListener(new Button.OnClickListener() {
									
									@TargetApi(Build.VERSION_CODES.HONEYCOMB)
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										boolean f=doValid();
										
										
										
										if(f==true)
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
						    pd = new ProgressDialog(this);
								
								pd.setMessage("Please wait.");
								pd.setCancelable(false);
								pd.setIndeterminate(true);
								
								
								textEditBirthDate.setOnClickListener(new OnClickListener() {
									
									
						
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
									
									imageViewProfile=(ImageView)findViewById(R.id.imageViewProfile);
									
									imageViewProfile.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											doHandleImage();
											
										}
									});
									
									checkBoxMale.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
										
										@Override
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											// TODO Auto-generated method stub
											if(isChecked)
											{
												checkBoxFemale.setChecked(false);
												
											}
										}
									});
									
							   checkBoxFemale.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
										
										@Override
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											// TODO Auto-generated method stub
											if(isChecked)
											{
												checkBoxMale.setChecked(false);	
											}
										}
									});
										
									
						        
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
						public void onResume() {
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
						    textEditBirthDate.setText(
						        new StringBuilder()
						                // Month is 0 based so add 1
						                .append(mDay).append("-")
						                .append(mMonth + 1).append("-")
						                .append(mYear).append(" "));
						}	    	    
		
}