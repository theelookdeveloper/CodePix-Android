package com.codepix.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindFriendsActivity extends Activity {
	
	private Button btnInviteViaFacebook;
	private Button btnInviteViaTwitter;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences mPrefs;
	private Button btnBack;

	/**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(DashboardActivity.PREF_KEY_TWITTER_LOGIN, false);
    }
	
	 @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	
    	startActivity(new Intent(FindFriendsActivity.this,DashboardActivity.class));
    }
    
    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.fragment_findfriends);
	        
	        btnInviteViaFacebook=(Button)findViewById(R.id.btnInviteViaFacebook);
	        btnInviteViaTwitter=(Button)findViewById(R.id.btnInviteViaTwitter);
	        
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
	                "MyPref", 0);
	        mPrefs =  getApplicationContext().getSharedPreferences(
	                "MyPrefFB", 0);
	        
	        if (mPrefs.getString("fbToken",null)==null)
	        {
	        	btnInviteViaFacebook.setEnabled(false);
	        	btnInviteViaFacebook.setTextColor(Color.BLACK);
	        	
	        }
	        
	        if ( !isTwitterLoggedInAlready())
	        {
	        	btnInviteViaTwitter.setEnabled(false);
	        	btnInviteViaTwitter.setTextColor(Color.BLACK);
	        	
	        }
	        
	        
	        btnInviteViaFacebook.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(FindFriendsActivity.this,InviteFacebook.class));
				}
			});
	        btnInviteViaTwitter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(FindFriendsActivity.this,InviteTwitter.class));
				}
			});
	        
	        btnBack=(Button)findViewById(R.id.btnBack);
	        
	        btnBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
	       
	}

}