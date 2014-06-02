package com.codepix.main;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.JSONParser;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class RegisterActivity extends Activity {

	private Button buttonRegisterWith;
	private static int flag=0;
	
	
	 String FILENAME = "AndroidSSO_data";
	 private SharedPreferences mPrefs;
	private SharedPreferences mSharedPreferences;
	
	 static String TWITTER_CONSUMER_KEY = "BeN61RJOYUbuQIdFqdZYA"; // place your cosumer key here
	    static String TWITTER_CONSUMER_SECRET = "CpIPrD5l7t7tB54WSmNHfxE0tGEMkM1HABimt7IyE"; // place your consumer secret here

	    // Preference Constants
	    static String PREFERENCE_NAME = "twitter_oauth";
	    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	    static final String TWITTER_CALLBACK_URL = "codepix://twitter_register";

	    // Twitter oauth urls
	    static final String URL_TWITTER_AUTH = "auth_url";
	    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
		private static final int TWITTER_AUTH = 102;
	    
	    
	    private static Twitter twitter;
	    private static RequestToken requestToken;
	    private AccessToken accessToken;
	    private static String APP_ID ; // Replace with your App ID
	    private Session session;
		 String requestId =null;
		 Map<String, String> mapFacebook = new HashMap<String, String>();
		 Map<String, String> mapTwitter = new HashMap<String, String>();
		private ProgressDialog pd;
		private ImageView imgArrow;
		private ImageView imageFacebook;
		private ImageView imageTwitter;
		private ImageView imageLogo;
		private ImageView imageCodepix;
		protected SharedPreferences codePixPref;
		private Facebook facebook;

	/**
	  * Get Profile information by making request to Facebook Graph API
	  * */
	 
	 public void getProfileInformation(Response response) {
	  
	   
			
	    try {
	    	
	    	GraphObject go  = response.getGraphObject();
	        JSONObject  profile = go.getInnerJSONObject();
	      	
	    	
	    	System.out.println("response from Facebook"+profile.toString());
	    	
	     // Facebook Profile JSON data
	    // JSONObject profile = Util.parseJson(response);
	     
	     // getting name of the user
	     final String name = profile.getString("name");
	     
	     // getting email of the user
	     final String email = profile.getString("email");
	     final String socialid = profile.getString("id");
	     
	    
	     mapFacebook.put("scloginreg","1");
	     mapFacebook.put("first_name", profile.getString("first_name"));
	     mapFacebook.put("last_name", profile.getString("last_name"));
	     mapFacebook.put("email", profile.getString("email"));
	     mapFacebook.put("social_id", profile.getString("id"));
	     mapFacebook.put("gender", profile.getString("gender"));
	     mapFacebook.put("Birth_date", profile.getString("birthday"));
	     mapFacebook.put("login_type", "Facebook");
	    // mapFacebook.put("profile_page_url", "http://graph.facebook.com/"+profile.getString("id")+"/picture?type=large");
	     
	     new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
		
	     
	     JSONObject jsonFromCodepix = null;
		try {
			JSONParser jParser =new JSONParser();
			jsonFromCodepix = jParser .getJSONFromUrl(mapFacebook);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			jsonFromCodepix=new JSONObject();
			try {
				jsonFromCodepix.put("status", "none");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	     //JSONObject jSonArray=jsonFromCodepix.getJSONObject("status");
	
        String message="success";
        String status="done";
        	 
				try {
					//user = jObj.getJSONArray("success");
					 //JSONObject c = user.getJSONObject(0);
					 
                  status=jsonFromCodepix.getString("status");
                  
                  
                 
                  
                  
                  if(status.equals("error")==true)
					{
						
               	   message=jsonFromCodepix.getString("message");
               	   final String msg=message;
               	   runOnUiThread(new Runnable() {

						      @Override
						      public void run() {
						    	pd.dismiss(); 
						       GlobalMethods.showMessage(getApplicationContext(), msg);
						      }

						     });
					}
                  else  if(status.equals("success")==true)
                  {
               	  pd.dismiss();  
               	   
               	   
               	   if(jsonFromCodepix.getJSONObject("userinfo") != null)
                       {
                       	JSONObject userInfo=jsonFromCodepix.getJSONObject("userinfo");
                       	
                       	//for(int i=0;i<userInfo.length();i++)
                       	//{
                       	codePixPref.edit().putString("userid",userInfo.getString("userid")).commit();
                       	codePixPref.edit().putString("first_name",userInfo.getString("first_name")).commit();
                       	codePixPref.edit().putString("last_name",userInfo.getString("last_name")).commit();
                       	codePixPref.edit().putString("email",userInfo.getString("email")).commit();
                       	codePixPref.edit().putString("social_id",userInfo.getString("social_id")).commit();
                       	codePixPref.edit().putString("image_url",userInfo.getString("image_url")).commit();
                       	codePixPref.edit().putString("gender",userInfo.getString("gender")).commit();
                       	codePixPref.edit().putString("Birth_date",userInfo.getString("Birth_date")).commit();
                       	codePixPref.edit().putString("login_type",userInfo.getString("login_type")).commit();
                       	codePixPref.edit().putString("profile_page_url",userInfo.getString("profile_page_url")).commit();
                       	
                       	
                       }
               	   
               	   
               	   
               	   
               	   
               	   
               	   finish();
               	    Intent intent=  new Intent(RegisterActivity.this,DashboardActivity.class);
	    	     		 startActivity(intent);  
                  }
                  else if(status.equals("none")==true)
                  {
                	  runOnUiThread(new Runnable() {

					      @Override
					      public void run() {
					    	pd.dismiss(); 
					       GlobalMethods.showMessage(getApplicationContext(), "Server Internal Error!");
					      }

					     });  
                  }
                	  
                 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
				
			}
			}).start();		
				
	   
	    

	     
	    } catch (JSONException e) {
	     e.printStackTrace();
	    }
			

	  
	 }

	
	

		 private void getTwitterProfileInformation(Intent intent) {
				// TODO Auto-generated method stub
			 /** This if conditions is tested once is
		         * redirected from twitter page. Parse the uri to get oAuth
		         * Verifier
		         * */
			 
			 System.out.println("test1");
		        if (!isTwitterLoggedInAlready()) {
		        	// Uri uri =intent.getData();
		        	 //System.out.println("test2"+uri.toString());

	                /*ConfigurationBuilder builder = new ConfigurationBuilder();
			            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			            Configuration configuration = builder.build();

			            TwitterFactory factory = new TwitterFactory(configuration);
			            twitter = factory.getInstance();*/
		            //if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
		                // oAuth verifier
		                final String verifier =intent.getExtras().getString("oauth_verifier");

		                try {

		                    Thread thread = new Thread(new Runnable(){
		                        @Override
		                        public void run() {
		                            try {
		                            	 System.out.println("test3");
		                                // Get the access token
		                                RegisterActivity.this.accessToken = twitter.getOAuthAccessToken(
		                                        requestToken, verifier);
		                                
		                                // Shared Preferences
		    		                    Editor e = mSharedPreferences.edit();
		                                  
		    		                    // After getting access token, access token secret
		    		                    // store them in application preferences
		    		                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		    		                    e.putString(PREF_KEY_OAUTH_SECRET,
		    		                            accessToken.getTokenSecret());
		    		                    // Store login status - true
		    		                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
		    		                    e.commit(); // save changes
		    		                    System.out.println("test4");
		    		                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

		    		                    // Getting user details from twitter
		    		                    // For now i am getting his name only
		    		                    
		    		                    
		    		                    long userID = accessToken.getUserId();
		    		                    User user = twitter.showUser(userID);
		    		                    
		    		                    
		    		                    String username = user.getName();
		    		                    String profileimage=user.getProfileImageURL();
		    		                    String name=user.getScreenName();
		    		                    
		    		                    System.out.println( "username: " + username + "\n profileimage: " + profileimage+"\nname:-"+name);
                                        
		    		                    
		    		                    mapTwitter.put("scloginreg","1");
		    		                    mapTwitter.put("first_name", username.substring(0, username.indexOf(" ")));
		    		                    mapTwitter.put("last_name", username.substring(username.indexOf(" "), username.length()-1));
		    		                    mapTwitter.put("email", name);
		    		                    mapTwitter.put("social_id", String.valueOf(userID));
		    		                    mapTwitter.put("gender", "");
		    		                    mapTwitter.put("Birth_date","");
		    		                    mapTwitter.put("login_type", "Twitter");
		    		       		    // mapFacebook.put("profile_page_url", "http://graph.facebook.com/"+profile.getString("id")+"/picture?type=large");
		    		       		     
		    		       		     
		    		       		     
		    		       		     JSONObject jsonFromCodepix = null;
									try {
										JSONParser jParser = new JSONParser();
										jsonFromCodepix = jParser
												.getJSONFromUrl(mapTwitter);
									} catch (Exception e2) {
										// TODO: handle exception
										
										System.out.println("JSON Parser:-"+e2.getMessage());
									}
		    		       		     
		    		       		
		    		                    String message="success";
		    		                    String status="done";
		    		                    	 
		    		       					try {
		    		       						//user = jObj.getJSONArray("success");
		    		       						 //JSONObject c = user.getJSONObject(0);
		    		       						 
		    		                              status=jsonFromCodepix.getString("status");
		    		                              
		    		                              
		    		                             
		    		                              
		    		                              
		    		                              if(status.equals("error")==true)
		    		          					{
		    		          						
		    		                           	   message=jsonFromCodepix.getString("message");
		    		                           	   final String msg=message;
		    		                           	   runOnUiThread(new Runnable() {

		    		          						      @Override
		    		          						      public void run() {
		    		          						    	pd.dismiss(); 
		    		          						       GlobalMethods.showMessage(getApplicationContext(), msg);
		    		          						      }

		    		          						     });
		    		          					}
		    		                              else  if(status.equals("success")==true)
		    		                              {
		    		                           	   pd.dismiss(); 
		    		                           	   
		    		                           	if(jsonFromCodepix.getJSONObject("userinfo") != null)
		            	                        {
		            	                        	JSONObject userInfo=jsonFromCodepix.getJSONObject("userinfo");
		            	                        	
		            	                        	//for(int i=0;i<userInfo.length();i++)
		            	                        	//{
		            	                        	codePixPref.edit().putString("userid",userInfo.getString("userid")).commit();
		            	                        	codePixPref.edit().putString("first_name",userInfo.getString("first_name")).commit();
		            	                        	codePixPref.edit().putString("last_name",userInfo.getString("last_name")).commit();
		            	                        	codePixPref.edit().putString("email",userInfo.getString("email")).commit();
		            	                        	codePixPref.edit().putString("social_id",userInfo.getString("social_id")).commit();
		            	                        	codePixPref.edit().putString("image_url",userInfo.getString("image_url")).commit();
		            	                        	codePixPref.edit().putString("gender",userInfo.getString("gender")).commit();
		            	                        	codePixPref.edit().putString("Birth_date",userInfo.getString("Birth_date")).commit();
		            	                        	codePixPref.edit().putString("login_type",userInfo.getString("login_type")).commit();
		            	                        	codePixPref.edit().putString("profile_page_url",userInfo.getString("profile_page_url")).commit();
		            	                        	
		            	                        	
		            	                        }
		    		                           	  
		    		                           	    Intent intent=  new Intent(RegisterActivity.this,DashboardActivity.class);
		    		       		    	     		 startActivity(intent);  
		    		       		    	     	 finish();
		    		                              }
		    		                             
		    		       					} catch (JSONException e1) {
		    		       						// TODO Auto-generated catch block
		    		       						
		    		       						e1.printStackTrace();
		    		       					}
		    		                    
		    		                    
		    		                    
		    		                    
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
		 
		 /**
		 * Function to login twitter
		 * */
		private void loginToTwitter() {
		    // Check if already logged in
		    if (!isTwitterLoggedInAlready()) {
		        ConfigurationBuilder builder = new ConfigurationBuilder();
		        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		        Configuration configuration = builder.build();

		        TwitterFactory factory = new TwitterFactory(configuration);
		        twitter = factory.getInstance();


		            Thread thread = new Thread(new Runnable(){
		                @Override
		                public void run() {
		                    try {

		                    	 try {
				                    	
										requestToken = twitter
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
		                        
		                      /*  Intent intent=new Intent(Intent.ACTION_VIEW, Uri
		                                .parse(requestToken.getAuthenticationURL()));
		                        RegisterActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
		                                .parse(requestToken.getAuthenticationURL())));*/
		                        Intent intent=new Intent(RegisterActivity.this,TwitterActivity.class);
		                           
		                           intent.putExtra("url", requestToken.getAuthenticationURL());
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
		        Toast.makeText(getApplicationContext(),
		                "Already Logged into twitter", Toast.LENGTH_LONG).show();
		    }
		}
	@Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if (requestCode == TWITTER_AUTH)
	    {

	        if (resultCode == Activity.RESULT_OK)
	        {
	        	
	        	String oauthVerifier = (String) data.getExtras().get("oauth_verifier");
	        	
	        	System.out.println("oauthVerifier:-"+oauthVerifier);
	        	getTwitterProfileInformation(data);
	            
	        }
	    } 
	 
	 else
	 {
		 Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);	 
	 }
	 }
	
	 @SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_register);
		
		
		APP_ID = getString(R.string.app_id);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
		 mPrefs =  getApplicationContext().getSharedPreferences(
	                "MyPrefFB", 0);
		 codePixPref = getSharedPreferences("CodePixPref", 0);
		
		 facebook = new Facebook(APP_ID);
			
		 requestId = getString(R.string.app_id);
		 
		 imgArrow=(ImageView)findViewById(R.id.imageArrow);
			imageFacebook=(ImageView)findViewById(R.id.imageFacebook);
			imageTwitter=(ImageView)findViewById(R.id.imageTwitter);
			imageCodepix=(ImageView)findViewById(R.id.imageCodepix);
			imageLogo=(ImageView)findViewById(R.id.imageLogo);
		
	    buttonRegisterWith=(Button)findViewById(R.id.buttonRegisterWith);
	    buttonRegisterWith.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(flag==0)
				{
				  Intent i=new Intent(RegisterActivity.this,CodePixRegisterAcitvity.class);
				  startActivity(i);
				  finish();
				}
				else if(flag==1)
				{
					pd.show();
					//registerViaFacebook();
					
					try {
						registerViaFacebook();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						pd.dismiss();
						e.printStackTrace();
						GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
					}
				}
				else if(flag==2)
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
						
                   	    Intent intent=  new Intent(RegisterActivity.this,DashboardActivity.class);
		    	     		 startActivity(intent);
		    	     		finish();
					}
				}
				
			}
		});
	    
	    pd = new ProgressDialog(this);
		
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		 imageCodepix.setOnClickListener(new ImageView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgArrow.getWidth(),imgArrow.getHeight());
	                //lp.addRule(0);
	                //this will make it center according to its parent
					lp.addRule(RelativeLayout.BELOW,buttonRegisterWith.getId());
	                lp.addRule(RelativeLayout.LEFT_OF,imageLogo.getId());
	                //lp.leftMargin=110; 
	                
	                imgArrow.setLayoutParams(lp);
	                imgArrow.setImageResource(R.drawable.codepix_loginscreen_arrow);
	                
	                flag=0;
				}
			});
		
      imageFacebook.setOnClickListener(new ImageView.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgArrow.getWidth(),imgArrow.getHeight());
                //lp.addRule(0);
                //this will make it center according to its parent
				lp.addRule(RelativeLayout.BELOW,buttonRegisterWith.getId());
                lp.addRule(RelativeLayout.CENTER_IN_PARENT,imageLogo.getId());
                //lp.leftMargin=110; 
                
                imgArrow.setLayoutParams(lp);
                imgArrow.setImageResource(R.drawable.codepix_loginscreen_arrow);
                
                flag=1;
			}
		});
		
       imageTwitter.setOnClickListener(new ImageView.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgArrow.getWidth(),imgArrow.getHeight());
              //  lp.addRule(0);
                //this will make it center according to its parent
				 lp.addRule(RelativeLayout.BELOW,buttonRegisterWith.getId());
				lp.addRule(RelativeLayout.RIGHT_OF,imageLogo.getId());
               //lp.leftMargin=330; 
               
                imgArrow.setLayoutParams(lp);
                
                imgArrow.setImageResource(R.drawable.codepix_loginscreen_arrow);
                
                flag=2;
			}
		});
		
		
		
		
	}	 
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTwitterProfileInformation(intent);
    }
    
    protected void registerViaFacebook() {
	// TODO Auto-generated method stub
		 
		 try {
			if (Session.getActiveSession() == null
			            || Session.getActiveSession().isClosed()) {
			        Session.openActiveSession(this, true, new StatusCallback() {

			            @SuppressWarnings("deprecation")
						@Override
			            public void call(Session session, SessionState state,
			                    Exception exception) {
			                System.out.println("State= " + state);

			                if (session.isOpened()) {
			                    System.out.println("Token=" + session.getAccessToken());
			                    Request.executeMeRequestAsync(session,
			                            new GraphUserCallback() {
			                                @Override
			                                public void onCompleted(GraphUser user,
			                                        Response response) {
			                                    if (user != null) {
			                                        System.out.println("User=" + user);

			                                    }
			                                    if (response != null) {
			                                    	try {
														getProfileInformation(response);
													} catch (Exception e) {
														// TODO Auto-generated catch block
														pd.dismiss();
														e.printStackTrace();
														GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
													}
			                                    }
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
				pd.dismiss();
				e.printStackTrace();
				GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
			
		}
	
}



}
