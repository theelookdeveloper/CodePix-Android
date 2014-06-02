package com.codepix.main;



import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codepix.utilz.GlobalMethods;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class SettingsFragment extends Fragment  {

 private Button btnProfile;
private Button btnLogout;
private SharedPreferences mSharedPreferences;
private SharedPreferences mPrefs;
private SharedPreferences codePixPref;
private Button btnInviteFreinds;
private Button btnSearchCodePixFreinds;
private String APP_ID;
private Button btnFacebookConnect;
private ProgressDialog pd;
static String TWITTER_CONSUMER_KEY = "BeN61RJOYUbuQIdFqdZYA"; // place your cosumer key here
static String TWITTER_CONSUMER_SECRET = "CpIPrD5l7t7tB54WSmNHfxE0tGEMkM1HABimt7IyE"; // place your consumer secret here

// Preference Constants
static String PREFERENCE_NAME = "twitter_oauth";
static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

static final String TWITTER_CALLBACK_URL = "codepixconnect://twitter_connect";

// Twitter oauth urls
static final String URL_TWITTER_AUTH = "auth_url";
static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
protected static final int TWITTER_AUTH = 103;


private static Twitter twitter;
private static RequestToken requestToken;
private AccessToken accessToken;
private Button btnTwitterConnect;


private boolean isTwitterLoggedInAlready() {
    // return twitter login status from Shared Preferences
    return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
}

private void login() {
	 if (Session.getActiveSession() == null
	            || Session.getActiveSession().isClosed()) {
	        Session.openActiveSession(getActivity(), true, new StatusCallback() {

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
	        if (!isTwitterLoggedInAlready()) {
	            ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	            Configuration configuration = builder.build();

	            TwitterFactory factory = new TwitterFactory(configuration);
	            DashboardActivity.twitter = factory.getInstance();


	                Thread thread = new Thread(new Runnable(){
	                    @Override
	                    public void run() {
	                        try {
                               pd.dismiss();
                               DashboardActivity.requestToken = DashboardActivity.twitter
	                                    .getOAuthRequestToken(TWITTER_CALLBACK_URL);
	                            
	                         /*  Intent intent=new Intent(getActivity(),TwitterActivity.class);
	                           
	                           intent.putExtra("url", requestToken.getAuthenticationURL());
	                           intent.putExtra("callback", TWITTER_CALLBACK_URL);
	                           
								
	                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri
	                                    .parse(DashboardActivity.requestToken.getAuthenticationURL()));
	                            getActivity().startActivity(intent);*/
	                            
	                            
	                            Intent intent=new Intent(getActivity(),TwitterActivity.class);
		                           
		                           intent.putExtra("url", DashboardActivity.requestToken.getAuthenticationURL());
		                           intent.putExtra("callback", TWITTER_CALLBACK_URL);
		                           
		                           getActivity().startActivityForResult(intent, TWITTER_AUTH);

	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });
	                thread.start();         
	        } else {
	            // user already logged into twitter
	            Toast.makeText(getActivity(),
	                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
	            //getTwitterFriendList();
	        }
	    }	
	    
	    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		        View V = inflater.inflate(R.layout.fragment_settings, container, false);
		        
		        mSharedPreferences = getActivity().getSharedPreferences(
		                "MyPref", 0);
				 mPrefs =   getActivity().getSharedPreferences(
			                "MyPrefFB", 0);
				 
				 APP_ID = getString(R.string.app_id);
					
				 
				 codePixPref =  getActivity().getSharedPreferences("CodePixPref", 0);
		        
		        btnProfile=(Button)V.findViewById(R.id.btnProfile);
		        btnLogout=(Button)V.findViewById(R.id.btnLogout);
		        
		        btnInviteFreinds=(Button)V.findViewById(R.id.btnInviteFreinds);
		        btnSearchCodePixFreinds=(Button)V.findViewById(R.id.btnSearchCodePixFreinds);
		        btnFacebookConnect=(Button)V.findViewById(R.id.btnFacebookConnect);
		        btnTwitterConnect=(Button)V.findViewById(R.id.btnTwitterConnect);
		        
		        if (mPrefs.getString("fbToken",null)==null)
		        {
		        	Drawable img =getActivity().getResources().getDrawable( R.drawable.connect );
		        	int sdk = android.os.Build.VERSION.SDK_INT;
		        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		        		btnFacebookConnect.setBackgroundDrawable(img);
		        	} else {
		        		btnFacebookConnect.setBackground(img);
		        	}
		        }
		        else
		        {
		        	Drawable img =getActivity().getResources().getDrawable( R.drawable.disconnect );
		        	int sdk = android.os.Build.VERSION.SDK_INT;
		        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		        		btnFacebookConnect.setBackgroundDrawable(img);
		        	} else {
		        		btnFacebookConnect.setBackground(img);
		        	}	
		        }
		        
		        
		        pd = new ProgressDialog(getActivity());
				
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				
		        
		       // System.out.println("State= " + Session.getActiveSession().isClosed());
		        
		        btnFacebookConnect.setOnClickListener(new Button.OnClickListener() {
					
					@SuppressWarnings({ "deprecation", "deprecation" })
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						
						if (mPrefs.getString("fbToken",null)==null)
				        {
							
							
							if(GlobalMethods.checkInternetConnection(getActivity()))
							{	
								pd.show();
								login();
								Drawable img =getActivity().getResources().getDrawable( R.drawable.disconnect );
					        	int sdk = android.os.Build.VERSION.SDK_INT;
					        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					        		btnFacebookConnect.setBackgroundDrawable(img);
					        	} else {
					        		btnFacebookConnect.setBackground(img);
					        	}
							}
							
							else
							{
								GlobalMethods.showMessage(getActivity(), getActivity().getString(R.string.internet_error));
							}
							
				        }
						else
						{
							pd.show();
						
						 
				        	
				        	mPrefs.edit().clear().commit();
							  
							  Drawable img =getActivity().getResources().getDrawable( R.drawable.connect );
								int sdk = android.os.Build.VERSION.SDK_INT;
								if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
									btnFacebookConnect.setBackgroundDrawable(img);
								} else {
									btnFacebookConnect.setBackground(img);
								}
								
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
		        
		        
		        if (!isTwitterLoggedInAlready())
		        {
		        	Drawable img =getActivity().getResources().getDrawable( R.drawable.connect );
		        	int sdk = android.os.Build.VERSION.SDK_INT;
		        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		        		btnTwitterConnect.setBackgroundDrawable(img);
		        	} else {
		        		btnTwitterConnect.setBackground(img);
		        	}
		        }
		        else
		        {
		        	Drawable img =getActivity().getResources().getDrawable( R.drawable.disconnect );
		        	int sdk = android.os.Build.VERSION.SDK_INT;
		        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		        		btnTwitterConnect.setBackgroundDrawable(img);
		        	} else {
		        		btnTwitterConnect.setBackground(img);
		        	}	
		        }
		        
		        
		        
		        
		        btnTwitterConnect.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
										
						if (!isTwitterLoggedInAlready()) {
						  
						    
						    if(GlobalMethods.checkInternetConnection(getActivity()))
							{	
								pd.show();
								
								Drawable img =getActivity().getResources().getDrawable( R.drawable.disconnect );
					        	int sdk = android.os.Build.VERSION.SDK_INT;
					        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					        		btnTwitterConnect.setBackgroundDrawable(img);
					        	} else {
					        		btnTwitterConnect.setBackground(img);
					        	}
					        	loginToTwitter();
							}
							
							else
							{
								GlobalMethods.showMessage(getActivity(), getActivity().getString(R.string.internet_error));
							}
						  }
						else
						{
							pd.show();		  
							Editor e = mSharedPreferences.edit();
					        e.remove(PREF_KEY_OAUTH_TOKEN);
					        e.remove(PREF_KEY_OAUTH_SECRET);
					        e.remove(PREF_KEY_TWITTER_LOGIN);
					        e.commit();
							  Drawable img =getActivity().getResources().getDrawable( R.drawable.connect );
					        	int sdk = android.os.Build.VERSION.SDK_INT;
					        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					        		btnTwitterConnect.setBackgroundDrawable(img);
					        	} else {
					        		btnTwitterConnect.setBackground(img);
					        	}
					        	
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
		        
		        
		        btnSearchCodePixFreinds.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),CodePixSearchFriendActivity.class);
						getActivity().startActivity(intent);
					}
				});
		        
		        
		        
		        
		        btnProfile.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),EditProfileActivity.class);
						getActivity().startActivity(intent);
					}
				});
		        
		        
		        btnLogout.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						mSharedPreferences.edit().clear().commit();
						mPrefs.edit().clear().commit();
						codePixPref.edit().clear().commit();
						getActivity().finish();
						System.exit(0);
						
					}
				});
		        
		        btnInviteFreinds.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if (mPrefs.getString("fbToken",null)==null && !isTwitterLoggedInAlready())
				        {
				        	GlobalMethods.alertDialog(getActivity(),getString(R.string.invite_friends),getString(R.string.invite_friend_social_error));
				        }
						else
						{
							Intent intent=new Intent(getActivity(),FindFriendsActivity.class);
							getActivity().startActivity(intent);	
						}	
						
						
						
					}
				});
		        
		        return V;
		    }


}