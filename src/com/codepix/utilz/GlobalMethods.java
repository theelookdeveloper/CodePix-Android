package com.codepix.utilz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepix.db.DBHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;




public final class GlobalMethods {
	
	
	public static void alertDialog(Context c,String title,String msg)
	  {
		//CLOSE APPLICATION
			// Create the alert box 
	      AlertDialog.Builder alertbox = new AlertDialog.Builder(c);

	      // Set the message to display
	      alertbox.setMessage(msg);
	      alertbox.setTitle(title);
	      
	      // Add a neutral button to the alert box and assign a click listener
	      alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	           
	          // Click listener on the neutral button of alert box
	          @Override
			public void onClick(DialogInterface arg0, int arg1) {
	        	  arg0.dismiss();
	        	 
	        	
	          }
	      });    
	      alertbox.show();
	      // show the alert box
	  }
	
	public static boolean checkInternetConnection(Context context)
    {
     boolean flag=false;
     
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
    	 
    	 flag=networkConnectivityForHigher(context);
     else
    	 flag=networkConnectivity(context);
    
       return flag;
    }
	
	public static String decodeNumericEntities(String s) {
		    StringBuffer sb = new StringBuffer();
		    Matcher m = Pattern.compile("\\&#(\\d+);").matcher(s);
		    while (m.find()) {
		        int uc = Integer.parseInt(m.group(1));
		        m.appendReplacement(sb, "");
		        sb.appendCodePoint(uc);
		    }
		    m.appendTail(sb);
		    return sb.toString();
  }


	public static String decodeString(String str)
	{
		  byte[] data1 = Base64.decode(str, Base64.DEFAULT);
          
          try {
        	  str = new String(data1, "UTF-8");
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          
          return str;
		
		
	
	}
	
	public static synchronized  void doSearchPost(Context context,int type) {
			// TODO Auto-generated method stub
		 JSONParser jParser = new JSONParser();
		 DBHelper db = new DBHelper(context);
		 SharedPreferences codePixPref=context.getSharedPreferences("CodePixPref", 0);
			db.open();
			
	        Map<String, String> map = new HashMap<String, String>();
	        String userid=codePixPref.getString("userid", "");
	       String excludesids=db.getExploreListIDs(userid);
	        
	        map.put("getMyPost", "1");
	    	 map.put("viewerid", userid);
	    	 if(type==1)
	    	  map.put("excludeids", "");
	    	 else 
	    		 map.put("excludeids", excludesids);	 
	    	 map.put("type", "facebook");
	    	
	 
	        // Getting JSON from URL
	        JSONObject json = jParser.getJSONFromUrl(map);
	       
	        
	        
	        
	        
	        if(json!=null)
	        {

	        	 
     
       
       //db.close();
	        
	        
	        try {
	            // Getting Array of Contacts
	            JSONArray posts = json.getJSONArray("post");
	         
	            if(posts!=null&&posts.getJSONObject(0).getInt("post_id")!=0)
	            {
	            // looping through All Contacts
	            	
	            	
	            for(int i = 0; i < posts.length(); i++){
	                JSONObject c = posts.getJSONObject(i);
	                
	                // Storing each json item in variable
	                int postid = c.getInt("post_id");
	               
	               
	              System.out.println("postid"+postid+"---"+db.checkPostid(postid));
	               if(postid>0)
	               {	   
	               
	            	   String caption = c.getString("caption");
		                String secrete_message=c.getString("secrete_message");
					
		                String post_public = c.getString("public");
		                String original_iamge_url = c.getString("original_image_url");
		                String distorted_image_url = c.getString("distorted_image_url");
		                String display_image_url = c.getString("display_image_url");
		                String width=c.getString("width"); 
		                
		                 String height = c.getString("height");
		                String gesture_type = c.getString("gesture_type");
		                String gesture_cordinates = c.getString("gesture_cordinates");
		                String uploadedby = c.getString("uploadedby");
		                String status=c.getString("status");
		                
		                 String like_status = c.getString("like_status");
		                String first_name = c.getString("first_name");
		                String last_name = c.getString("last_name");
		                String image_url = c.getString("image_url");
		                String login_type=c.getString("login_type");
		                
		                 String like_count = c.getString("like_count");
		                String comments_count=c.getString("comments_count");
		                
		                String post_date=c.getString("post_date");
		               //String post=String.valueOf(postid);
		             
	            	   
				long t=db.insertPost(caption,comments_count,display_image_url,distorted_image_url,first_name, gesture_cordinates,gesture_type,height, image_url, last_name,like_status,like_count,login_type,original_iamge_url,postid,post_date,post_public, secrete_message, status,uploadedby,width);
				
				 
				if(t!=-1)
				{	
				 Intent broadcast2 = new Intent();
				 
				 if(type==1)
				 broadcast2.setAction("NEWS_ACTION");
				 else
					 broadcast2.setAction("EXPLORE_ACTION");	 
				 broadcast2.putExtra("action", "refresh");
				 context.sendBroadcast(broadcast2);
				}
				/*if(news.length()>100)
		  	        {
		  	    	 news=news.substring(0,100);
		  	    	news=news.substring(0,news.lastIndexOf(" "));
		  	        }
				 if(news.equals("empty"))
					 news="";
				 
				 
				
	               
	               NotificationCompat.Builder builder =
	  	                 new NotificationCompat.Builder(context)
	  	                         .setSmallIcon(R.drawable.logo)
	  	                         .setContentTitle(comment)
	  	                         .setContentText(news);
	  	         
	  	         builder.setAutoCancel(true);
	  	         builder.setLights(Color.BLUE, 500, 500);
	  	         long[] pattern = {500,500,500,500,500,500,500,500,500};
	  	         builder.setVibrate(pattern);
	  	         builder.setStyle(new NotificationCompat.InboxStyle());
	  	         
	  	         Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	  	         builder.setSound(alarmSound);
	  	         
	  	         
	  	         int NOTIFICATION_ID = 111;
	  	      
	  	         Intent targetIntent = new Intent(context, NotificationReceiver.class);
	  	         targetIntent.putExtra("comment", comment);
	  	         targetIntent.putExtra("news", news); 
	  	         targetIntent.putExtra("newsid",newsid);
	  	       
	  	         PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	  	         builder.setContentIntent(contentIntent);
	  	         
	  	       targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
	  	         NotificationManager nManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
	  	         nManager.notify(NOTIFICATION_ID, builder.build());*/
	               
	               }//end if    
	               
	                 
	            }//end for
	            
	            
	          }//end outer if 
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	             
	           
	       
	        
	         
	        } 
	        
	       db.close();
	      
		}
   
	/**
		 * download file from server
		 * @return
		 */
		public static synchronized  String downloadFile(String uri) {
			
			
			BufferedInputStream bistream = null;
			BufferedOutputStream bostream = null;
			boolean bExit = false;
			int nBuffSize = 16384;
			byte[] buffer = null;
			boolean bDownloadCompleted = false;
			int bytesRead = 0;
			File apkFile = null;
			String apkFilePath = null;
		
			try {
				if (android.os.Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED)) {
				
					 
	                if( !TextUtils.isEmpty( uri ) ) {
	                	
					File path = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
					  			+ "CodePix");
					  			
					  //	File path=new File("data/data/com.codepix.main/profile/");		

						if (!path.isDirectory()) {
							path.mkdirs();
						}
						apkFilePath = path.getAbsolutePath() + File.separator
								+ uri.substring(uri.lastIndexOf("/") + 1);
						path = null;
						
						HttpGet httpGet = new HttpGet(uri);
						HttpParams httpParameters = new BasicHttpParams();
						// Set the timeout in milliseconds until a connection is
						// established.
						int timeoutConnection = 20000;
						HttpConnectionParams.setConnectionTimeout(httpParameters,
								timeoutConnection);
						// Set the default socket timeout (SO_TIMEOUT)
						// in milliseconds which is the timeout for waiting for
						// data.
						int timeoutSocket = 30000;
						HttpConnectionParams.setSoTimeout(httpParameters,
								timeoutSocket);

						DefaultHttpClient httpClient = new DefaultHttpClient(
								httpParameters);
						HttpResponse response = httpClient.execute(httpGet);

						HttpEntity entity = response.getEntity();
						buffer = new byte[nBuffSize];

						if (entity != null) {
							
							bistream = new BufferedInputStream(entity.getContent(),
									nBuffSize);
							apkFile = new File(apkFilePath);
							bostream = new BufferedOutputStream(
									new FileOutputStream(apkFile));
							do {
								if (bExit) {
									break; 
								}
							
								bytesRead = bistream.read(buffer);
								if (bytesRead > 0) {
									bostream.write(buffer, 0, bytesRead);
								}
							} while (bytesRead > 0);

							httpClient.getConnectionManager().shutdown();
						}
						httpClient = null;
						response = null;
						entity = null;
						bDownloadCompleted = true;
						
					
						return apkFilePath;
					} else {
						apkFilePath = null;
					}
				} else {
					apkFilePath = "media_unmount";
				}

			} catch (SocketTimeoutException e) {
				Log.i("R", "SocketTimeoutException while downloading Media : "
						+ e.toString());
				e.printStackTrace();
				bDownloadCompleted = false;
			} catch (Exception e) {
				Log.i("R", "Exception while downloading Media : " + e.toString());
				e.printStackTrace();
				bDownloadCompleted = false;
			} finally {
				try {
					if (bostream != null) {
						bostream.flush();
						bostream.close();
						bostream = null;
					}

					if (bistream != null) {
						bistream.close();
						bistream = null;
					}

					buffer = null;

					if (!bDownloadCompleted) {

						if (apkFile != null && apkFile.exists()) {
							apkFile.delete();
							apkFile = null;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			return apkFilePath;
		}
		
	
	 public static String encodeString(String str)
	{
		// String comment=txtTitle.getText().toString();
 		
	        // Sending side
	        byte[] data = null;
	        try {
	        	data = str.getBytes("UTF-8");
	        } catch (UnsupportedEncodingException e1) {
	        e1.printStackTrace();
	        }
	      return    str = Base64.encodeToString(data, Base64.DEFAULT);
	}

	 private static boolean networkConnectivity(Context context) {
		ConnectivityManager connec = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING  ) {
		 return true;
		} else if(connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED   ) {
			return false;           
		}
		return false;
	}
	 
	 
	 
	    public static boolean networkConnectivityForHigher(Context context)
		{

			boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;

		    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		    for (NetworkInfo ni : netInfo) {
		    	
		    	//showMessage(ni.getTypeName());
		        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		            if (ni.isConnected())
		                haveConnectedWifi = true;
		        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		            if (ni.isConnected())
		                haveConnectedMobile = true;
		    }
		    return haveConnectedWifi || haveConnectedMobile;
		}
	    
	   public static void showMessage(Context c,String msg)
	{
	
		Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();	
	}
	   
	   public static Bitmap ShrinkBitmap(String file, int width, int height) {
	    // TODO Auto-generated method stub
	    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	    bmpFactoryOptions.inJustDecodeBounds = true;
	    Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

	    int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	    int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

	    if (heightRatio > 1 || widthRatio > 1)
	    {
	     if (heightRatio > widthRatio)
	     {
	      bmpFactoryOptions.inSampleSize = heightRatio;
	     } else {
	      bmpFactoryOptions.inSampleSize = widthRatio;
	     }
	    }

	    bmpFactoryOptions.inJustDecodeBounds = false;
	    bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	 return bitmap;
	}
}
