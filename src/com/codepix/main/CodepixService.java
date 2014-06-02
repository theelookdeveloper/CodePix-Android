package com.codepix.main;

import java.util.Timer;
import java.util.TimerTask;

import com.codepix.utilz.GlobalMethods;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class CodepixService extends Service {

	
	
	class IncomingHandler extends Handler 
	    {
	        @Override
	        public void handleMessage(Message msg) 
	        {
	        	
	        }
	    }
	private class LongOperation extends AsyncTask<String, Void, String> {

	        @Override
	        protected String doInBackground(String... params) {
	                
	        	
	        GlobalMethods.doSearchPost(getApplicationContext(),1);
	        	
				return null;
	        }        

	       
			@Override
	        protected void onPostExecute(String result) {             
	        }

	        @Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {
	        }
	    }
	  SharedPreferences sharedPrefs;
	  int settings;
	  private Messenger outMessenger;     
	
	  final Messenger inMessenger = new Messenger(new IncomingHandler());
	public void callAsynchronousTask() {
		
		    final Handler handler = new Handler();
		    Timer timer = new Timer();
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                @Override
						@TargetApi(Build.VERSION_CODES.HONEYCOMB)
						public void run() {       
		             
		             			
		                	if(GlobalMethods.checkInternetConnection(CodepixService.this))
    	                	{	
    	                	
    	                	      
    			                    	final LongOperation task=new LongOperation();
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
    			        				    	  
    			        				    	 
    			        				      }	  
    			        				  }
    			        				}, 2*60*1000);
    	                	}	
    	                	 
		                }
		            });
		        }
		    };
		    timer.schedule(doAsynchronousTask, 0, settings*60*1000); //execute in every 50000 ms
		}
	
	  
	@Override
	    public IBinder onBind(Intent intent) 
	    {
	        Bundle extras = intent.getExtras();
	       

	    /* if (extras != null) 
	        {
	            outMessenger = (Messenger) extras.get("messenger");
	        }
*/
	       // return inMessenger.getBinder();
	        return null;
	    }
   
	
	 @Override
	public void onCreate()
	{
		super.onCreate();
		
		
	}
	 
	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		
		//callAsynchronousTask();
		/*// Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
		 LongOperation task=new LongOperation();
			
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
			else
				task.execute("start");*/
		
		final LongOperation task=new LongOperation();
       	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
			else
				task.execute("start");
		return START_STICKY;
		
	}
	 
	 
}
