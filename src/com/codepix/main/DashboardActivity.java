package com.codepix.main;



import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.facebook.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;

public class DashboardActivity extends FragmentActivity {
    private static View createTabView(Context context, int id) {
	    View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null, false);
	    ImageButton tv = (ImageButton) view.findViewById(R.id.tabTitleText);
	    tv.setFocusable(true);
	    view.setFocusable(true);
	    tv.setBackgroundDrawable(context.getResources().getDrawable(id));
	    //tv.setText(tabText);
	    return view;
	}
    // Fragment TabHost as mTabHost
    private TabHost tHost;
    private HomeFragment homeFragment;
    private CameraFragment cameraFragment;
    private ExploreFragment exploreFragment;
    private SettingsFragment settingsFragment;
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
	private static final int TWITTER_AUTH = 103;
	protected static final int SELECT_PHOTO_FROM_CAMERA = 1010;


    protected static final int SELECT_PHOTO = 1000;
    static Twitter twitter;
    static RequestToken requestToken;
	static AccessToken accessToken;
	private SharedPreferences mSharedPreferences;
	private String filePath;

static String verifier;

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
            	Intent photoPickerIntent = new Intent(DashboardActivity.this,ImageEffectsActivity.class);
            	startActivity(photoPickerIntent);
				//startActivityForResult(photoPickerIntent, SELECT_PHOTO_FROM_CAMERA);
            }
            else  if(selectedPosition==1)
            {
            	Intent photoPickerIntent1 = new Intent(Intent.ACTION_PICK);
				photoPickerIntent1.setType("image/*");
				startActivityForResult(photoPickerIntent1, SELECT_PHOTO);
            }
        }
    })
    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
       

		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
            dialog.dismiss();
            
        }
    })
    .show();
}


public String getPath(Uri uri) {
    String[] projection = { MediaColumns.DATA };
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    int column_index = cursor
            .getColumnIndexOrThrow(MediaColumns.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
}

private void getTwitterAccessToken(Intent intent) {
	// TODO Auto-generated method stub
	 System.out.println("test1");
     if (!isTwitterLoggedInAlready()) {
     	 //Uri uri =intent.getData();
     	 //System.out.println("test2"+uri.toString());

     
       //  if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
             // oAuth verifier
             verifier = intent.getExtras().getString("oauth_verifier");
             try {

                 Thread thread = new Thread(new Runnable(){
                     @Override
                     public void run() {
                         try {
                         	 System.out.println("test3");
                             // Get the access token
                             DashboardActivity.accessToken = twitter.getOAuthAccessToken(
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

private boolean isTwitterLoggedInAlready() {
    // return twitter login status from Shared Preferences
    return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
 


	 

switch(requestCode) { 
case TWITTER_AUTH: 

    if (resultCode == Activity.RESULT_OK)
    {
    	
    	String oauthVerifier = (String) data.getExtras().get("oauth_verifier");
    	
    	System.out.println("oauthVerifier:-"+oauthVerifier);
    	getTwitterAccessToken(data);
        
    }
 break;
case SELECT_PHOTO:
    if(resultCode ==RESULT_OK){  
        Uri selectedImage = data.getData();
       filePath= getPath(selectedImage);
       
       
       Intent intent=new Intent(DashboardActivity.this,ImageEffectsActivity.class);
       
         intent.putExtra("filePath", filePath);
         intent.putExtra("pictureFromCamera", false);
         
         startActivity(intent);
         finish();
       
      // Toast.makeText(getActivity(), "File://"+filePath, Toast.LENGTH_LONG).show();
        /*InputStream imageStream = null;
		try {
			imageStream =getContentResolver().openInputStream(selectedImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);*/
        /*imagView.setImageBitmap(yourSelectedImage);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
        imagView.setLayoutParams(params);*/
    }
    break;
    
   case SELECT_PHOTO_FROM_CAMERA:
	//Toast.makeText(this, "file"+filePath, Toast.LENGTH_LONG).show();
    if(resultCode ==RESULT_OK){ 
    	filePath=data.getStringExtra("filepath");
    	
    	 Intent intent=new Intent(DashboardActivity.this,ImageEffectsActivity.class);
         
         intent.putExtra("filePath", filePath);
         intent.putExtra("pictureFromCamera", true);
         startActivity(intent);
         finish();
           /* imagView.setImageURI(Uri.fromFile(new File(filePath)));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
            imagView.setLayoutParams(params);
            imagView.setRotation(90);*/
            
    }
    break;
    
   default:
	   
	   Session.getActiveSession().onActivityResult(DashboardActivity.this, requestCode, resultCode, data);
	   break;
}

}

@Override
protected void onCreate(Bundle savedInstanceState) {
/*	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
    .detectAll()
    .penaltyLog()
    .penaltyDeath()
    .build()); */
	       super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_dashboard);
           

			tHost = (TabHost) findViewById(android.R.id.tabhost);
			tHost.setup();
			mSharedPreferences = getApplicationContext().getSharedPreferences(
	                "MyPref", 0);
			 
			 TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
				 
      			

				@Override
				public void onTabChanged(String tabId) {
         	//System.out.println("tabId"+tabId);
				android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
				homeFragment = (HomeFragment) fm.findFragmentByTag("home");
         
				//cameraFragment=(CameraFragment) fm.findFragmentByTag("camera");
				exploreFragment=(ExploreFragment) fm.findFragmentByTag("explore");
				settingsFragment=(SettingsFragment) fm.findFragmentByTag("settings");
             
          
             
             
             android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
             //ft.addToBackStack(null);

             /** Detaches the homeFragment if exists */
             if(homeFragment!=null)
                 ft.detach(homeFragment);
             
            // if(cameraFragment!=null)
              //   ft.detach(homeFragment);
             if(exploreFragment!=null)
                 ft.detach(exploreFragment);
             if(settingsFragment!=null)
                 ft.detach(settingsFragment);
             
          //   GlobalMethods.showMessage(getApplicationContext(), "current tab"+tabId);
             
             if(tabId.equalsIgnoreCase("home")){

                 if(homeFragment==null){
                    // *//** Create AndroidFragment and adding to fragmenttransaction *//*
                     ft.add(R.id.realtabcontent,new HomeFragment(), "home");
                 }else{
                    // *//** Bring to the front, if already exists in the fragmenttransaction *//*
                   ft.attach(homeFragment);
                 	
                 //ft.replace(R.id.realtabcontent, Fragment.instantiate(DashboardFragmentActivity.this, homeFragment.getClass().getName()));
                //ft.replace(R.id.realtabcontent,homeFragment);
                 }

             }
               if(tabId.equalsIgnoreCase("camera")){

               
            	 doHandleImage();
            	 /*if(cameraFragment==null){
                    // *//** Create AndroidFragment and adding to fragmenttransaction *//*
                     ft.add(R.id.realtabcontent,new CameraFragment(), "camera");
                 }else{
                    // *//** Bring to the front, if already exists in the fragmenttransaction *//*
                 //  ft.attach(cameraFragment);
                   ft.replace(R.id.realtabcontent,cameraFragment);
                 	//ft.replace(R.id.realtabcontent, Fragment.instantiate(DashboardFragmentActivity.this, homeFragment.getClass().getName()));
                 }*/

             }
              if(tabId.equalsIgnoreCase("explore")==true){
            	System.out.println("Explore true");
                 if(exploreFragment==null){
                    // /** Create AndroidFragment and adding to fragmenttransaction */
                     ft.add(R.id.realtabcontent,new ExploreFragment(), "explore");
                 }else{
                    // /** Bring to the front, if already exists in the fragmenttransaction */
                   ft.attach(exploreFragment);
                   //ft.replace(R.id.realtabcontent,exploreFragment);
                 	//ft.replace(R.id.realtabcontent, Fragment.instantiate(DashboardFragmentActivity.this, homeFragment.getClass().getName()));
                 }

             }
             if(tabId.equalsIgnoreCase("settings")){
            	 System.out.println("Explore false");
                 if(settingsFragment==null){
                    // *//** Create AndroidFragment and adding to fragmenttransaction *//*
                     ft.add(R.id.realtabcontent,new SettingsFragment(), "settings");
                 }else{
                    // *//** Bring to the front, if already exists in the fragmenttransaction *//*
                   ft.attach(settingsFragment);
                  // ft.replace(R.id.realtabcontent,settingsFragment);
                 	
                 	//ft.replace(R.id.realtabcontent, Fragment.instantiate(DashboardFragmentActivity.this, homeFragment.getClass().getName()));
                 }

             }
             
            	 
             

           
           
            // ft.commitAllowingStateLoss();
             ft.commit();
         }
     };
     
     

     /** Setting tabchangelistener for the tab */
     tHost.setOnTabChangedListener(tabChangeListener);
     
     
     
     View tabView = createTabView(this, R.drawable.icon_logo_config);
	 /** Defining tab builder for home tab */
	 TabHost.TabSpec tSpechome = tHost.newTabSpec("logo");
	 tSpechome.setIndicator(tabView);
	tSpechome.setContent(new DummyTabContent(getBaseContext()));
	
	 tHost.addTab(tSpechome);
	
	 /** Defining tab builder for intro tab */
	 TabHost.TabSpec tSpecintro = tHost.newTabSpec("home");
	 tabView = createTabView(this, R.drawable.icon_home_config);
	 tSpecintro.setIndicator(tabView);
	 tSpecintro.setContent(new DummyTabContent(getBaseContext()));
	 tHost.addTab(tSpecintro);
	 
	 /** Defining tab builder for updates tab */
	 TabHost.TabSpec tSpecupdates = tHost.newTabSpec("camera");
	 tabView = createTabView(this, R.drawable.icon_snap_config);
	 tSpecupdates.setIndicator(tabView);
	 tSpecupdates.setContent(new DummyTabContent(getBaseContext()));
	 tHost.addTab(tSpecupdates);
	 
	 
	 /** Defining tab builder for history tab */
	 TabHost.TabSpec tSpechistory = tHost.newTabSpec("explore");
	 tabView = createTabView(this, R.drawable.icon_explore_config);
	 tSpechistory.setIndicator(tabView);
	 tSpechistory.setContent(new DummyTabContent(getBaseContext()));
	 tHost.addTab(tSpechistory);
	 
	 /** Defining tab builder for settings tab */
	 TabHost.TabSpec tSpecsettings = tHost.newTabSpec("settings");
	 tabView = createTabView(this, R.drawable.icon_settings_config);
	 tSpecsettings.setIndicator(tabView);
	 tSpecsettings.setContent(new DummyTabContent(getBaseContext()));
	 tHost.addTab(tSpecsettings);
	
	/* *//** Set tab when select  from Menubar *//*
	 Intent intent=getIntent();
	 String tab=intent.getStringExtra("tab");
	 
	 if(tab!=null)
	 tHost.setCurrentTabByTag(tab);
	 */
	 
	 tHost.setCurrentTabByTag("home");
	 
	 
	 
	// Intent i = new Intent("com.codepix.main.CodepixService");
	 
	// startService(i);
    }



@Override
protected void onDestroy() {
super.onDestroy();
 finish();
unbindDrawables(findViewById(R.id.rootView));
System.gc();
}

@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
      System.out.println("called onNewIntent");
      Uri uri =intent.getData();
      if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL))
      getTwitterAccessToken(intent);
}


    
    @Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		String tHostName=savedInstanceState.getString("tHost");
		
		tHost.setCurrentTabByTag(tHostName);
	}
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("tHost", tHost.getCurrentTabTag());
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