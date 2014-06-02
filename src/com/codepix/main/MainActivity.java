package com.codepix.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button btnLogin,btnRegister;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences codePixPref;
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		
		btnLogin=(Button)findViewById(R.id.btnLogin);
		btnRegister=(Button)findViewById(R.id.btnRegister);
		
		 SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
		
		 mSharedPreferences = getSharedPreferences("MyPref", 0);
		 
		 codePixPref = getSharedPreferences("CodePixPref", 0);
		  String access_token = mPrefs.getString("access_token", null);
		  boolean twitter=mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		  if(access_token!=null || twitter==true||codePixPref.getString("userid", null)!=null)
		  {
			  Intent intent=  new Intent(MainActivity.this,DashboardActivity.class);
	     		 startActivity(intent);
	     		 finish();
		  }
		
		btnLogin.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(i);
			}
		});
		
   btnRegister.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
