package com.codepix.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;

import com.codepix.utilz.Global;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.HttpClientUploadPost;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;
import com.facebook.SessionState;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDistortedUploadActivity extends Activity {
	
	private class SendHttpRequestTask extends AsyncTask<String, Void, String[]> {

	    @Override
	    protected String[] doInBackground(String... params) {
	    	//String fname=GlobalMethods.decodeNumericEntities(txtFirstName.getText().toString());
			//String lname=GlobalMethods.decodeNumericEntities(txtLastName.getText().toString());
			String data[]=null;
	    	caption=editTextCaption.getText().toString();
	    	secret_message=editTextSecretMessage.getText().toString();
	    	
	    	String userid=codePixPref.getString("userid", "");

	        try {
	            HttpClientUploadPost client = new HttpClientUploadPost(Global.url,getApplicationContext());
	            client.connectForMultipart();
	            
				client.addFormPart("caption", caption);
	            client.addFormPart("uploadedby", userid);
	            
				client.addFormPart("secrete_message", secret_message);
	            client.addFormPart("public", "Y");	
	            
				client.addFormPart("width", ""+imageWidth);
	            
				client.addFormPart("height", ""+imageHeight);
	            
				client.addFormPart("gesture_type",""+ gestureType);
	            
				client.addFormPart("gesture_cordinates",gestureCordinates);
	            if(effectsAdded==true)
				client.addFormPart("effects_added","added");
	            else
	            	client.addFormPart("effects_added","");
	            client.addFormPart("uploadPost","1");
	            
	           
	            if(filePath!=null&&filePath.length()>0)
	            {	
	            	 Bitmap b = GlobalMethods.ShrinkBitmap(filePath, 403, 504);
	            	 
	            	 

	     	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     	        b.compress(CompressFormat.PNG, 0, baos);
	     	       System.out.println("filePath "+filePath);
	            client.addFilePart("fileurl", filePath, baos.toByteArray());
	            
	            }
	            
	            
	            if(filePathDistorted!=null&&filePathDistorted.length()>0)
	            {	
	            	 Bitmap b = GlobalMethods.ShrinkBitmap(filePath, 403, 504);

	     	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     	        b.compress(CompressFormat.PNG, 0, baos);
	     	       System.out.println("fileurldisorted "+filePathDistorted);
	            client.addFilePart("fileurldisorted", filePathDistorted, baos.toByteArray());
	            
	            }
	            
	            
	            
	            
	            
	            client.finishMultipart();
	            data = client.getResponse();
	            //return data;
	            //Toast.makeText(getActivity(), "response"+data, Toast.LENGTH_LONG).show();
	        }
	        catch(Throwable t) {
	        	
	        	data= new String[]{"error",getString(R.string.internet_error)};
	            t.printStackTrace();
	        }

	        return data;
	    }

	    @Override
	    protected void onPostExecute(String result[]) {            
	       // item.setActionView(null);
           pd.dismiss();
           
           GlobalMethods.showMessage(getApplicationContext(),result[1]);
     	   
     	   if(result[0].equals("success"))
     	   {
     		  //DashBoardActivity
     		      		     		   
     		 Intent intent=  new Intent(ImageDistortedUploadActivity.this,DashboardActivity.class);
     		 startActivity(intent);
     	   }
	    }

	}
	private ImageView imageViewActual;
	private Intent mIntent;
	private Bitmap yourSelectedImage;
	private String filePath;
	InputStream imageStream = null;
	private String filePathDistorted;
	private InputStream imageStreamDistorted;
	private Bitmap yourSelectedDistortedImage;
	private ImageView imageViewDistorted;
	private Button btnDone;
	private ProgressDialog            pd;
	String caption = null;   
	String secret_message = null;
	int imageWidth = 0;
	int imageHeight = 0;
	int gestureType = 0;
	String gestureCordinates=null;
	boolean effectsAdded = false;
	private EditText editTextCaption;
	private EditText editTextSecretMessage;
	private Button btnBack;
	private Button btnCancel;
	private SharedPreferences codePixPref;
	private boolean pendingPublishReauthorization;
	private CheckBox checkBoxFacebookShare;
	private SharedPreferences mSharedPreferences;
	private String requestId;
	private CheckBox checkBoxTwitterShare;
	
	
	 private SharedPreferences mPrefs;
	    static String TWITTER_CONSUMER_KEY = "BeN61RJOYUbuQIdFqdZYA"; // place your cosumer key here

	    static String TWITTER_CONSUMER_SECRET = "CpIPrD5l7t7tB54WSmNHfxE0tGEMkM1HABimt7IyE"; // place your consumer secret here
	    // Preference Constants
	    static String PREFERENCE_NAME = "twitter_oauth";
	    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

	    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	    static final String TWITTER_CALLBACK_URL = "codepixshare://twitter_share";
	    // Twitter oauth urls
	    static final String URL_TWITTER_AUTH = "auth_url";
	    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
		static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	    
	    
	    private static final int TWITTER_AUTH = 101;
	    private static Twitter twitter;

	private static RequestToken requestToken;
	
	

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


	 protected boolean doValidate() {
		// TODO Auto-generated method stub
		 if(editTextCaption.length()==0)
		   {
			   GlobalMethods.showMessage(ImageDistortedUploadActivity.this,"Please add caption.");
			   editTextCaption.requestFocus();
			   return false;
		   }
		   
		   if(editTextSecretMessage.length()==0)
		   {
			   GlobalMethods.showMessage(ImageDistortedUploadActivity.this,"Please add secret message .");
			   editTextSecretMessage.requestFocus();
			   return false;
		   }
		   
		   
		 
		   boolean net=false;
		     try {
		    	 //checking Internet connection exit or not
				net = GlobalMethods.checkInternetConnection(ImageDistortedUploadActivity.this);
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
	private void getTwitterAccessToken(Intent intent) {
    	// TODO Auto-generated method stub
    	 System.out.println("test1");
         if (!isTwitterLoggedInAlready()) {
         	 //Uri uri =intent.getData();
         	 //System.out.println("test2"+uri.toString());

         
           //  if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                 // oAuth verifier
                 DashboardActivity.verifier = intent.getExtras().getString("oauth_verifier");
                 try {

                     Thread thread = new Thread(new Runnable(){
                         @Override
                         public void run() {
                             try {
                             	 System.out.println("test3");
                                 // Get the access token
                                 DashboardActivity.accessToken = DashboardActivity.twitter.getOAuthAccessToken(
                                		 DashboardActivity.requestToken, DashboardActivity.verifier);
                                 
                                 // Shared Preferences
     		                    Editor e = mSharedPreferences.edit();
                                   
     		                    // After getting access token, access token secret
     		                    // store them in application preferences
     		                    e.putString(PREF_KEY_OAUTH_TOKEN, DashboardActivity.accessToken.getToken());
     		                    e.putString(PREF_KEY_OAUTH_SECRET,
     		                    		DashboardActivity.accessToken.getTokenSecret());
     		                    // Store login status - true
     		                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
     		                    e.commit(); // save changes
     		                    System.out.println("test4");
     		                    Log.e("Twitter OAuth Token", "> " + DashboardActivity.accessToken.getToken());

     		                    // Getting user details from twitter
     		                    // For now i am getting his name only
     		                    
     		                    
     		                    long userID = DashboardActivity.accessToken.getUserId();
     		                    User user = DashboardActivity.twitter.showUser(userID);
     		                    
     		                    
     		                    String username = user.getName();
     		                    String profileimage=user.getProfileImageURL();
     		                    String name=user.getScreenName();
     		                    
     		                    System.out.println( "username: " + username + "\n profileimage: " + profileimage+"\nname:-"+name);
                                 
     		                        
     		                         pd.dismiss();     		                    
     		                    
     		                    
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     });
                     thread.start();

                    
                     
                     //Toast.makeText(getApplicationContext(), "username: " + username + "\n profileimage: " + profileimage+"\nname:-"+name, Toast.LENGTH_LONG).show();
                    
                 } catch (Exception e) {
                     // Check log for login errors
                     Log.e("Twitter Login Error", "> " + e.getMessage());
                     e.printStackTrace();
                 }
             }
         
    }
	/**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    } 	    


	
    private void login() {
   	 if (Session.getActiveSession() == null
   	            || Session.getActiveSession().isClosed()) {
   	        Session.openActiveSession(ImageDistortedUploadActivity.this, true, new StatusCallback() {

   	            @SuppressWarnings("deprecation")
   				@Override
   	            public void call(Session session, SessionState state,
   	                    Exception exception) {
   	                System.out.println("State= " + state);

   	                if (session.isOpened()) {
   	                    System.out.println("Token=" + session.getAccessToken());
   	                    
   	                    mPrefs.edit().putString("fbToken", session.getAccessToken()).commit();
   	                    Request.executeMeRequestAsync(session,
   	                            new GraphUserCallback() {
   	                                @Override
   	                                public void onCompleted(GraphUser user,
   	                                        Response response) {
   	                                    if (user != null) {
   	                                        System.out.println("User=" + user);

   	                                    }
   	                                    pd.dismiss();
   	                                }
   	                            });
   	                }
   	                if (exception != null) {
   	                    System.out.println("Some thing bad happened!");
   	                    exception.printStackTrace();
   	                }
   	            }
   	        });
   	    }
   }
    
    /**
     * Function to login twitter
     * */
    private void loginToTwitter() {
        // Check if already logged in
        try {
			if (!isTwitterLoggedInAlready()) {
				
			    ConfigurationBuilder builder = new ConfigurationBuilder();
			    builder.setMediaProviderAPIKey(TWITTER_CONSUMER_KEY);
			    builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			   
			    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			   
			    Configuration configuration = builder.build();
			 

			    TwitterFactory factory = new TwitterFactory(configuration);
			  
                   DashboardActivity.twitter = factory.getInstance();


			        Thread thread = new Thread(new Runnable(){
			            @Override
			            public void run() {
			                try {
			                	

			                    try {
			                    	
			                    	DashboardActivity.requestToken = DashboardActivity.twitter
									        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									
									pd.dismiss();
									
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));		
										}
									});
									
									
								}
			                    
			                   /* Intent intent=new Intent(Intent.ACTION_VIEW, Uri
			                            .parse(requestToken.getAuthenticationURL()));
			                    LoginActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
			                            .parse(requestToken.getAuthenticationURL())));*/
			                    
			                    Intent intent=new Intent(ImageDistortedUploadActivity.this,TwitterActivity.class);
			                       
			                       intent.putExtra("url", DashboardActivity.requestToken.getAuthenticationURL());
			                       intent.putExtra("callback", TWITTER_CALLBACK_URL);
			                       
									startActivityForResult(intent, TWITTER_AUTH);

			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }
			        });
			        thread.start();         
			} else {
			    // user already logged into twitter
				System.out.println("step10");
			    Toast.makeText(getApplicationContext(),
			            "Already Logged into twitter", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
			GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
		}
        System.out.println("step9");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     


    	 

    if(requestCode==TWITTER_AUTH) 
    { 
            if (resultCode == Activity.RESULT_OK)
        {
        	
        	String oauthVerifier = (String) data.getExtras().get("oauth_verifier");
        	
        	System.out.println("oauthVerifier:-"+oauthVerifier);
        	getTwitterAccessToken(data);
            
        }
     }

    else
	 {
		 Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);	 
	 }
    
    }
    
    public void onBackPress() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	
    	
    	Intent intent=new Intent(ImageDistortedUploadActivity.this,ImageGestureActivity.class);
	       
        intent.putExtra("filePath", filePath);
        intent.putExtra("pictureFromCamera", false);
        intent.putExtra("filePathDistorted", filePathDistorted);
        intent.putExtra("imageWidth", imageWidth);
        intent.putExtra("imageHeight", imageHeight);
        intent.putExtra("effectsAdded", effectsAdded);
    	
    	startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	
    	
    	/*Intent intent=new Intent(ImageDistortedUploadActivity.this,ImageGestureActivity.class);
	       
        intent.putExtra("filePath", filePath);
        intent.putExtra("pictureFromCamera", false);
        intent.putExtra("filePathDistorted", filePathDistorted);
        intent.putExtra("imageWidth", imageWidth);
        intent.putExtra("imageHeight", imageHeight);
        intent.putExtra("effectsAdded", effectsAdded);
    	
    	startActivity(intent);*/
    }
    
    public void onCancelPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	
    	startActivity(new Intent(ImageDistortedUploadActivity.this,DashboardActivity.class));
    }

    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	
    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .penaltyDeath()
        .build()); 
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.activity_imagedistortedupload);
	        codePixPref = getSharedPreferences("CodePixPref", 0);
	        
	    	mSharedPreferences = getApplicationContext().getSharedPreferences(
	                "MyPref", 0);
			
			 
			 codePixPref = getSharedPreferences("CodePixPref", 0);
			
			 mPrefs = getSharedPreferences("MyPrefFB", 0);
			
			 requestId = getString(R.string.app_id);
	        
	        
	        mIntent=getIntent();
	 	   
	 	   Bundle str=mIntent.getExtras();
	 	   
	 	   filePath = str.getString("filePath");
	 	  filePathDistorted=str.getString("filePathDistorted");
	 	 imageWidth=str.getInt("imageWidth");
	 	 imageHeight=str.getInt("imageHeight");
	 	 effectsAdded=str.getBoolean("effectsAdded");
	 	gestureType=str.getInt("gestureType");
	 	gestureCordinates=str.getString("gestureCordinates");
	 	   
	 	   boolean pictureFromCamera=str.getBoolean("pictureFromCamera");
	        
	        imageViewActual=(ImageView)findViewById(R.id.imageViewActual);
	        imageViewDistorted=(ImageView)findViewById(R.id.imageViewDistorted);
	        editTextCaption=(EditText)findViewById(R.id.editTextCaption);
	        editTextSecretMessage=(EditText)findViewById(R.id.editTextSecretMessage);
	        btnBack=(Button)findViewById(R.id.btnBack);
	        btnCancel=(Button)findViewById(R.id.btnCancel);
	        
	        checkBoxFacebookShare=(CheckBox)findViewById(R.id.checkBoxFacebookShare);
	        checkBoxTwitterShare=(CheckBox)findViewById(R.id.checkBoxTwitterShare);
	        
	        if(isTwitterLoggedInAlready())
	        	checkBoxTwitterShare.setChecked(true);
	        
	        if (mPrefs.getString("fbToken",null)!=null) 
	        {
	        	checkBoxFacebookShare.setChecked(true);
	        }
	        
			try {
				Uri selectedImage=Uri.fromFile(new File(filePath));
				imageStream =getContentResolver().openInputStream(selectedImage);
				
				yourSelectedImage = BitmapFactory.decodeStream(imageStream);
	            imageViewActual.setImageBitmap(yourSelectedImage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
             
            
            
            try {
				Uri selectedImage=Uri.fromFile(new File(filePathDistorted));
				imageStreamDistorted =getContentResolver().openInputStream(selectedImage);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
             yourSelectedDistortedImage = BitmapFactory.decodeStream(imageStreamDistorted);
            imageViewDistorted.setImageBitmap(yourSelectedDistortedImage);
            
            
            
           // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,200);
            if(pictureFromCamera)
            	imageViewActual.setRotation(90);
            //imageViewActualt.setLayoutParams(params);
            
           
            btnDone=(Button)findViewById(R.id.btnDone);
            
            btnDone.setOnClickListener(new Button.OnClickListener() {
				
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
					
					if(checkBoxFacebookShare.isChecked()) 
					publishStory();
					
					}
					/*new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								uploadPic(new File(filePath),editTextCaption.getText().toString(),twitter);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();*/
					
				}
			});
            
            
            btnBack.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
            
           btnCancel.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onCancelPressed();
				}
			});
           
           
           checkBoxTwitterShare.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				 if(isChecked)
					{
						if(!isTwitterLoggedInAlready())
						{
						  pd.show();
						  try {
								 loginToTwitter();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									pd.dismiss();
									e.printStackTrace();
									GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
								}
						}
						else
						{
							
	                   	    Intent intent=  new Intent(ImageDistortedUploadActivity.this,DashboardActivity.class);
			    	     		 startActivity(intent);
			    	     		finish();
						}
					}
			}
		});
           
           checkBoxFacebookShare.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
						if (mPrefs.getString("fbToken",null)==null)
				        {
							
							
							if(GlobalMethods.checkInternetConnection(getApplicationContext()))
							{	
								pd.show();
								login();
								
							}
							
							else
							{
								GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
							}
							
				        }
				}	
				else
				{
					pd.show();
				
				 
		        	
		        	mPrefs.edit().clear().commit();
					  
					 
						
						AsyncTask<Void, Void, Boolean> waitForCompletion = new AsyncTask<Void, Void, Boolean>(){
					        @Override
					        protected Boolean doInBackground(Void... params) {
					            long timeStarted = System.currentTimeMillis();
					            while(System.currentTimeMillis() - timeStarted < 1500){
					                // wait for 1.5 ms
					                 try {
					                        Thread.sleep(100);
					                    } catch (InterruptedException e) {
					                       // Log.e(TAG, "thread interrupted", e);
					                    }
					            }
								return null;
					          
					        };
					        @Override
					        protected void onPostExecute(Boolean result) {
					        pd.dismiss();
					        }
					    };
					    waitForCompletion.execute(null, null, null);	
						
				}	
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // getTwitterProfileInformation(intent);
        pd.dismiss();
        getTwitterAccessToken(intent);
        
    }
    
    
    private void publishStory() {
    	
    	Session.openActiveSession(this, true, new Session.StatusCallback() {

  	      // callback when session changes state
  	    	 @Override
  	 	      public void call(Session session, SessionState state, Exception exception) {
  	 	    	  if (session.isOpened()) {
  	                  
  	                 	
  	                 	
  	                 	if (session != null){

  	              		
  	          			if(session.isOpened() && state == SessionState.OPENED && !session.getPermissions().contains("publish_stream")){
  	                          final String[] PERMISSION_ARRAY_PUBLISH = {"publish_stream"};
  	                          final List<String> permissionList = Arrays.asList(PERMISSION_ARRAY_PUBLISH);
  	                          session.requestNewPublishPermissions(new NewPermissionsRequest(ImageDistortedUploadActivity.this,permissionList ));
  	                          return;
  	                      }

  	              	   // Bundle postParams = new Bundle();
  	              	   // postParams.putString("name", "Facebook SDK for Android");
  	              	   // postParams.putString("caption", "Build great social apps and get more installs.");
  	              	   // postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
  	              	   // postParams.putString("link", "https://developers.facebook.com/android");
  	              	   // postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
  	              	    byte[] data = null;

  	              	    Bitmap bi = BitmapFactory.decodeFile(new File(filePathDistorted).getAbsolutePath());
  	              	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
  	              	    bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
  	              	    data = baos.toByteArray();

  	              	   
  	              	    
  	              	    
  	              	    Request.Callback callback= new Request.Callback() {
  	              	        @Override
							public void onCompleted(Response response) {
  	              	            System.out.println("response"+response);
  	              	        }
  	              	    };
                       
  	              	    Request request = Request.newUploadPhotoRequest(session,bi, callback);
  	              	 Bundle params = request.getParameters();
 	              	  params.putString("message", editTextCaption.getText().toString()+": To view image, click here to download CodePix");
 	              	   // params.putString("method", "photos.upload");
 	              	    //params.putByteArray("picture", data);
 	              	    //params.putString("link", "https://developers.facebook.com/android");
  	              	request.setParameters(params);
  	              	    RequestAsyncTask task = new RequestAsyncTask(request);
  	              	    task.execute();
  	              	}
  	                 	//Request.executeBatchAsync(request); 
  	 	    	  }    
  	 	      }
  		
  	    });
    	
    	 
    	
    	
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
    
    
    protected void uploadDistortedImage() {
		// TODO Auto-generated method stub
		
	}
    
    public void uploadPic(File file, String message,Twitter twitter) throws Exception  {
        try{
            StatusUpdate status = new StatusUpdate(message);
            
            Uri selectedImage=Uri.fromFile(file);
           // InputStream is =getContentResolver().openInputStream(selectedImage);
            ConfigurationBuilder builder = new ConfigurationBuilder();
		    builder.setMediaProviderAPIKey("24c64525aaf50919a1b243cf9babc04d");
		    builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		   
		    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		   
		    Configuration configuration = builder.build();
		 

		   TwitterFactory factory = new TwitterFactory(configuration);
		  
                  Twitter twitter1 = factory.getInstance();
            System.out.println("twitter "+message+" -"+file.getAbsolutePath());
          // status.setMedia(file);
            ImageUpload upload = new ImageUploadFactory(configuration).getInstance(MediaProvider.PLIXI); //Use ImageUploadFactory
            String url = upload.upload(file);
            
            twitter1.updateStatus(url);}
        catch(TwitterException e){
            Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
            throw e;
        }
    }
    	

}