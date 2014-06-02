package com.codepix.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


 
public class TwitterActivity extends Activity {

	/**
	 * Declaring Variables
	 */
	protected FrameLayout webViewPlaceholder;
	protected WebView wvMain;
	    
	
	String url;
	
	Bundle outState;
	private String callback;
	private Intent mIntent;
	

	

    
	 

	
	/**
     * Function where webview is implemented
     */
    @SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	public void init()
    {
    	webViewPlaceholder = ((FrameLayout)findViewById(R.id.webViewPlaceholder));

    	if(wvMain==null)
    	{
    		wvMain=new WebView(this);   
    		WebSettings webSettings8 = wvMain.getSettings();
    		wvMain.getSettings().setSupportZoom(true);
    		wvMain.getSettings().setBuiltInZoomControls(true);
    		wvMain.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    		wvMain.setScrollbarFadingEnabled(true);
    		wvMain.getSettings().setLoadsImagesAutomatically(true);
    		wvMain.getSettings().setJavaScriptEnabled(true);
    		wvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    		//wvMain.getSettings().setPluginState(true);
    		wvMain.getSettings().setSupportZoom(false);     

    		wvMain.getSettings().setCacheMode(wvMain.getSettings().LOAD_NO_CACHE);
    		webSettings8.setPluginState(WebSettings.PluginState.ON);
    		wvMain.setWebViewClient(new WebViewClient());
    		wvMain.addJavascriptInterface(this, "Android");
    		wvMain.getSettings().setSupportMultipleWindows(true);
    		//wvMain.getSettings().setPluginState(true);
    		wvMain.getSettings().setUseWideViewPort(true);
    		wvMain.getSettings().setLoadWithOverviewMode(true);

    		//Progress bar
    		final Activity MyActivity = this;
    		final ProgressDialog progressDialog = new ProgressDialog(MyActivity);

    		progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
    		progressDialog.setMessage("Loading...");
    		progressDialog.setCancelable(true);
    		wvMain.setWebChromeClient(new WebChromeClient(){

    			@Override
				public void onProgressChanged(WebView view, int progress) {
    				progressDialog.show();
    				progressDialog.setProgress(0);
    				MyActivity.setProgress(progress * 1000);

    				progressDialog.incrementProgressBy(progress);

    				if (progress == 100 && progressDialog.isShowing())
    					progressDialog.dismiss();
    			}
    		});

    		wvMain.getSettings().setAllowFileAccess(true);
    		wvMain.loadUrl(url); // webview will load URL from here..

    		wvMain.setWebViewClient(new WebViewClient() {
    	  
    			@Override
    			public void onReceivedError(WebView view, int errorCode,
    					String description, String failingUrl) {
    				// Handle the error
    			}

    			@Override
    			public boolean shouldOverrideUrlLoading(WebView view, String url) {
    				//Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
    				if( url.contains(callback))
                    {
                        Uri uri = Uri.parse( url );
                        String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
                        //Intent mIntent=new Intent();
						mIntent.putExtra( "oauth_verifier", oauthVerifier );
                        setResult( RESULT_OK, mIntent );
                        finish();
                        return true;
                    }
                    return false;
                
    			}
    		});

    	
    			wvMain.loadUrl(url);
    		
    	}
    	webViewPlaceholder.addView(wvMain); //here adding webview in webviewplaceholder
    }    

	
  
    /**
	   * configuration changed listener 
	   */
	    @Override
	    public void onConfigurationChanged(Configuration newConfig)
	    {
	    	if (wvMain != null)
	    	{
	    		/**
	    		 *  Remove the WebView from the old placeholder
	    		 */
	    		webViewPlaceholder.removeView(wvMain);
	    	}
	    	super.onConfigurationChanged(newConfig);
	
	    	// Load the layout resource for the new configuration
	    	setContentView(R.layout.activity_twitter);
	
	    	/**
	    	 *  Reinitialize the UI
	    	 */
	    	init();
	    }
  @Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	

        
	//File apk=new File(t.toString());
	
	//apk.delete();
	
   mIntent=getIntent();
   
   Bundle str=mIntent.getExtras();
   
   url=str.getString("url");
   callback=str.getString("callback");
    //Toast.makeText(this, str.getString("url"), Toast.LENGTH_LONG).show();
	requestWindowFeature(Window.FEATURE_NO_TITLE);   
	
	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	setContentView(R.layout.activity_twitter);   
	
			init();   //Initiating the WebView	
    	
	
	
}

    public void onDestory()
	   {
		   super.onDestroy();
		   
		   finish();  
	   }
   
	 
   @Override
/**
 * Saving instance state 
 */

protected void onSaveInstanceState(Bundle outState)
{
	super.onSaveInstanceState(outState);
 
	// Save the state of the WebView
	wvMain.saveState(outState);
}
  
   
	  
} 
