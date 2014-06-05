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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepix.utilz.Global;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.HttpClient;
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

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {
	
	private class LongLoginOperation extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            
        	String email=GlobalMethods.decodeNumericEntities(txtEmail.getText().toString());
			String password=GlobalMethods.decodeNumericEntities(txtPassword.getText().toString());
			
                   System.out.println("started asyn task");
                    JSONParser jParser = new JSONParser();
                    
                    Map<String, String> mapCodePix = new HashMap<String, String>();
                 	
                    mapCodePix.put("login_type", "codepix");
                    mapCodePix.put("login", "1");
                    mapCodePix.put("email", email);
                    mapCodePix.put("password", password);
                    
                	 
                	
                	        // Getting JSON from URL
                    JSONObject json = null;
					try {
						json = jParser.getJSONFromUrl(mapCodePix);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						try {
							json = new JSONObject();
							json.put("status", "error");
							json.put("message", getString(R.string.internet_error));
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
                    
                    
                    return json;
               
        }

        @Override
        protected void onPostExecute(JSONObject result) {
        	
                 // Getting JSON Array
        		 pd.dismiss();
        		 try {
        		     
        			JSONObject jsonFromCodepix =result;
        			//JSONObject jSonArray=jsonFromCodepix.getString("status");
        		
                     String message="success";
                     String status="done";
                     	 
        					
        						 
                               status=jsonFromCodepix.getString("status");
                               
                               
                              
                               
                               
                               if(status.equals("Failure")==true)
           					{
           						
                            	   message=jsonFromCodepix.getString("message");
                            	
           						       GlobalMethods.showMessage(getApplicationContext(), message);
           						     
           						   
           					}
                               else  if(status.equals("success")==true)
                               {
                            	 
                            	   
                            	   
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
                            	    Intent intent=  new Intent(LoginActivity.this,DashboardActivity.class);
        		    	     		 startActivity(intent);  
                               }
                              
        					       					
                  	  
               
         } catch (JSONException e) {
             e.printStackTrace();
         }
        
        }
        	 

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	private class SendHttpRequestForgotPasswordTask extends AsyncTask<String, Void, String[]> {

	    @Override
	    protected String[] doInBackground(String... params) {
	    	
			String email=GlobalMethods.decodeNumericEntities(txtEmail.getText().toString());
			//String password=GlobalMethods.decodeNumericEntities(txtPassword.getText().toString());
			
	       
	           
	       

	        try {
	            HttpClient client = new HttpClient(Global.url,getApplicationContext(),3);
	            client.connectForMultipart();
	            
	            client.addFormPart("email", email);
	            //client.addFormPart("password", password);
	         
	           
	            client.addFormPart("login_type", "codepix");
	            client.addFormPart("forgotpassword","1");
	            
	           
	         
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
           
          
     	   
     	   if(result[0].equals("success"))
     	   {
     		  //DashBoardActivity
     		   
     		  GlobalMethods.showMessage(getApplicationContext(),"Your password has changed successfully.");
     	   }
     	   else
     	   {
     		  GlobalMethods.showMessage(getApplicationContext(),"Invalid email id or email id does not exist.");
     	   }
	    }

	}
	private class SendHttpRequestTask extends AsyncTask<String, Void, String[]> {

	    @Override
	    protected String[] doInBackground(String... params) {
	    	
			String email=GlobalMethods.decodeNumericEntities(txtEmail.getText().toString());
			String password=GlobalMethods.decodeNumericEntities(txtPassword.getText().toString());
			
	       
	           
	       

	        try {
	            HttpClient client = new HttpClient(Global.url,getApplicationContext(),1);
	            client.connectForMultipart();
	            
	            client.addFormPart("email", email);
	            client.addFormPart("password", password);
	         
	           
	            client.addFormPart("login_type", "codepix");
	            client.addFormPart("login","1");
	            
	           
	         
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
     		   
     		 Intent intent=  new Intent(LoginActivity.this,DashboardActivity.class);
     		 startActivity(intent);
     	   }
	    }

	}
	private EditText txtEmail;
	private EditText txtPassword;
	 private Button btnSubmit;
	 private ProgressDialog pd;
	protected boolean confirmLogout;

	 Map<String, String> mapFacebook = new HashMap<String, String>();
	 
	 Map<String, String> mapTwitter = new HashMap<String, String>();
	 // Your Facebook APP ID
	 private static String APP_ID ; // Replace with your App ID
	// Instance of Facebook Class
	 private Facebook facebook ;
	
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
	    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
		// Twitter oauth urls
	    static final String URL_TWITTER_AUTH = "auth_url";
	    
	    
	    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	    private static final int TWITTER_AUTH = 101;
		private static Twitter twitter;
		private static RequestToken requestToken;
		private AccessToken accessToken;
		private Button btnLoginWith;
		private ImageView imgArrow;
		private RelativeLayout mainLoginLayout;
		private ImageView imageFacebook;
		private ImageView imageTwitter;
		 private ImageView imageLogo;
		private static boolean flag=true;
		private Session session;
	String requestId =null;
	private TextView textForgotPassword;
	
	
	 private SharedPreferences codePixPref;
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
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("unchecked")
	public void doHandleLogin()
	 {
	     final LongLoginOperation task =new LongLoginOperation();
	     
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
			    	  
			    	  GlobalMethods.showMessage(LoginActivity.this, getString(R.string.internet_error));
			      }	  
			  }
			}, 10*1000);
	 } 	
	
	
	private boolean doValidate() {
		// TODO Auto-generated method stub
	
	  
	     
		   if(txtEmail.length()==0)
		   {
			   GlobalMethods.showMessage(LoginActivity.this,"Please enter email ID.");
			   txtEmail.requestFocus();
			   return false;
		   }
		   
		   if(txtPassword.length()==0)
		   {
			   GlobalMethods.showMessage(LoginActivity.this,"Please enter password.");
			   txtPassword.requestFocus();
			   return false;
		   }
		   
		   
		 
		   boolean net=false;
		     try {
		    	 //checking Internet connection exit or not
				net = GlobalMethods.checkInternetConnection(LoginActivity.this);
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
	    // mapFacebook.put("image_url", "http://graph.facebook.com/"+profile.getString("id")+"/picture?type=small");
	     
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
	            	    Intent intent=  new Intent(LoginActivity.this,DashboardActivity.class);
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
	        //if (!isTwitterLoggedInAlready()) {
	        	 //Uri uri =intent.getData();
	        	// System.out.println("test2"+uri.toString());

	        
	           // if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
	                // oAuth verifier
	                final String verifier = intent.getExtras().getString("oauth_verifier");

	                try {

	                    Thread thread = new Thread(new Runnable(){
	                        @Override
	                        public void run() {
	                            try {
	                            	 System.out.println("test3");
	                                // Get the access token
	                                LoginActivity.this.accessToken = twitter.getOAuthAccessToken(
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
	    		       		     
	    		       		     
	    		       		     
	    		       		     JSONParser jParser =new JSONParser();
	    		       			JSONObject jsonFromCodepix = jParser .getJSONFromUrl(mapTwitter);
	    		       		     
	    		       		
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
	    		                           	    Intent intent=  new Intent(LoginActivity.this,DashboardActivity.class);
	    		       		    	     		 startActivity(intent);  
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
	
		 /**
		 * Check user already logged in your application using twitter Login flag is
		 * fetched from Shared Preferences
		 * */
		private boolean isTwitterLoggedInAlready() {
		    // return twitter login status from Shared Preferences
		    return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		}
		 
		 private void login() {
			System.out.println("step3");
			 try {
				 System.out.println("step4");
				if (Session.getActiveSession() == null
				            || Session.getActiveSession().isClosed()) {
					
					System.out.println("step5");
				        Session.openActiveSession(this, true, new StatusCallback() {

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
				                    
				                    pd.dismiss();
									
									GlobalMethods.showMessage(getApplicationContext(), "Facebook Login failed.");
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
		/**
		     * Function to login twitter
		     * */
		    private void loginToTwitter() {
		        // Check if already logged in
		        try {
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
					                    
					                   /* Intent intent=new Intent(Intent.ACTION_VIEW, Uri
					                            .parse(requestToken.getAuthenticationURL()));
					                    LoginActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					                            .parse(requestToken.getAuthenticationURL())));*/
					                    
					                    Intent intent=new Intent(LoginActivity.this,TwitterActivity.class);
					                       
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
			if(GlobalMethods.checkInternetConnection(getApplicationContext()))
			{
			 Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
			}
			else
			{
				GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
			}	
		 }
		 
		 }
 
		 @SuppressWarnings("deprecation")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			 
			 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			    .detectAll()
			    .penaltyLog()
			    .penaltyDeath()
			    .build()); 
			super.onCreate(savedInstanceState);
			
			
			setContentView(R.layout.activity_login);
			
			
			
			
			
			
			txtEmail=(EditText)findViewById(R.id.editTextEmail);
			txtPassword=(EditText)findViewById(R.id.editTextPassword);
			btnSubmit=(Button)findViewById(R.id.buttonSubmit);
			
			btnLoginWith=(Button)findViewById(R.id.buttonLoginWith);
			imgArrow=(ImageView)findViewById(R.id.imageArrow);
			imageFacebook=(ImageView)findViewById(R.id.imageFacebook);
			imageTwitter=(ImageView)findViewById(R.id.imageTwitter);
			imageLogo=(ImageView)findViewById(R.id.imageLogo);
			textForgotPassword=(TextView)findViewById(R.id.textForgotPassword);
			
			mainLoginLayout=(RelativeLayout)findViewById(R.id.mainLoginLayout);
			
			APP_ID = getString(R.string.app_id);
			mSharedPreferences = getApplicationContext().getSharedPreferences(
		            "MyPref", 0);
			 mPrefs =  getApplicationContext().getSharedPreferences(
		                "MyPrefFB", 0);
			 
			 codePixPref = getSharedPreferences("CodePixPref", 0);
			facebook = new Facebook(APP_ID);
			
			 requestId = getString(R.string.app_id);
			 
			
			 
			btnSubmit.setOnClickListener(new Button.OnClickListener() {
				
			
			@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
		       
				if(doValidate())
				{
					/*SendHttpRequestTask task=new SendHttpRequestTask();
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
					else
						task.execute("start");*/
					pd.show();
					
					doHandleLogin();
				}
		       		//loginToTwitter();
				
					
			
				}
			});
			
			btnLoginWith.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("step1");
					if(flag==true)
					{
						System.out.println("step2");
						
						 //logoutFromFacebook();
						try {
							if(GlobalMethods.checkInternetConnection(getApplicationContext()))
							{	
							   pd.show();
							   login();
							}
							else
							{
								GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
							}	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							pd.dismiss();
							e.printStackTrace();
							GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
						}
							//loginToFacebook();
					}
					else
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
							finish();
		               	    Intent intent=  new Intent(LoginActivity.this,DashboardActivity.class);
			    	     		 startActivity(intent);
						}
							
					}
					
				}
			});
			
			
			imageFacebook.setOnClickListener(new ImageView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgArrow.getWidth(),imgArrow.getHeight());
		            //lp.addRule(0);
		            //this will make it center according to its parent
					lp.addRule(RelativeLayout.BELOW,btnLoginWith.getId());
		            lp.addRule(RelativeLayout.LEFT_OF,imageLogo.getId());
		            //lp.leftMargin=110; 
		            
		            imgArrow.setLayoutParams(lp);
		            imgArrow.setImageResource(R.drawable.codepix_loginscreen_arrow);
		            
		            flag=true;
				}
			});
			
		   imageTwitter.setOnClickListener(new ImageView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(imgArrow.getWidth(),imgArrow.getHeight());
		          //  lp.addRule(0);
		            //this will make it center according to its parent
					 lp.addRule(RelativeLayout.BELOW,btnLoginWith.getId());
					lp.addRule(RelativeLayout.RIGHT_OF,imageLogo.getId());
		           //lp.leftMargin=330; 
		           
		            imgArrow.setLayoutParams(lp);
		            
		            imgArrow.setImageResource(R.drawable.codepix_loginscreen_arrow);
		            
		            flag=false;
				}
			});
			
			
		   textForgotPassword.setOnClickListener(new TextView.OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if(txtEmail.length()==0)
				   {
					   GlobalMethods.showMessage(LoginActivity.this,"Please enter email ID.");
					   txtEmail.requestFocus();
					   
				   }
				 else
				 {
					 pd.show();
					 SendHttpRequestForgotPasswordTask task=new SendHttpRequestForgotPasswordTask();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
						else
							task.execute("start"); 
				 }
			}
		});
			
			 pd = new ProgressDialog(this);
				
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				
				//getTwitterProfileInformation(getIntent());	
				
				
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

		    
		    
		    /**
			  * Function to show Access Tokens
			  * */
			 public void showAccessTokens() {
			  String access_token = facebook.getAccessToken();

			Toast.makeText(getApplicationContext(),
			    "Access Token: " + access_token, Toast.LENGTH_LONG).show();
			 }
}
