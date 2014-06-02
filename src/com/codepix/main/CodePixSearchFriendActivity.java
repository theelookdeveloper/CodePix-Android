
package com.codepix.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class CodePixSearchFriendActivity extends AbsListViewBaseActivity {

	

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public Button button;
			public ImageView image;
			public TextView title;
		}
		 private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private ArrayList<HashMap<String, String>> data;

		public ItemAdapter(ArrayList<HashMap<String, String>> friendList) {
			// TODO Auto-generated constructor stub
			
			this.data=friendList;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.list_row, parent, false);
				holder = new ViewHolder();
				holder.button = (Button) view.findViewById(R.id.btnInvite);
				holder.image = (ImageView) view.findViewById(R.id.list_image);
				holder.title = (TextView)view.findViewById(R.id.title);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			 HashMap<String, String> friend = new HashMap<String, String>();
		     friend = data.get(position);
		     final int pos=position;
		    holder.title.setText(friend.get("name"));
		    
		    if(friend.get("status").equalsIgnoreCase("N")==true)
	        {
		    	holder.button.setText("Add");
	        }
	    	
	    	else if(friend.get("status").equalsIgnoreCase("F")==true)
	        {
	    		holder.button.setText("Remove");	
	        }	
		    
		    holder.button.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					pd.show();
					doHandleButtonClick(v,pos,holder.button.getText().toString());
				}
			});
			imageLoader.displayImage(friend.get("photourl"), holder.image, options, animateFirstListener);

			return view;
		}
	}

	private class LongLoginOperation extends AsyncTask<String, Void, JSONObject> {

	        @Override
	        protected JSONObject doInBackground(String... params) {
	            
	        	
	                   System.out.println("started asyn task");
	                    JSONParser jParser = new JSONParser();
	                    
	                    Map<String, String> mapCodePix = new HashMap<String, String>();
	                 	
	                    mapCodePix.put("searchCodePixfriend", "1");
	                    mapCodePix.put("viewerid", codePixPref.getString("userid",""));
	                   	                    
	                    String username=textSearch.getText().toString();
						if(username.indexOf(" ")==-1)
	  	              {
							mapCodePix.put("first_name", username);
							mapCodePix.put("last_name","");
	  	              }
	  	              else
	  	              {	  
	  	            	mapCodePix.put("first_name", username.substring(0, username.indexOf(" ")));
	  	            	mapCodePix.put("last_name",username.substring(username.lastIndexOf(" "), username.length()));
	  	              }
	                          	 
	                	
	                	        // Getting JSON from URL
	                    JSONObject json = jParser.getJSONFromUrl(mapCodePix);
	                    
	                    
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
	                               
	                               
	                              
	                               
	                               
	                               if(status.equals("error")==true)
	           					{
	           						
	                            	   message=jsonFromCodepix.getString("message");
	                            	
	           						       GlobalMethods.showMessage(getApplicationContext(), message);
	           						     
	           						   
	           					}
	                               else  if(status.equals("success")==true)
	                               {
	                            	 
	                            	   
	                            	   
	                            	 
	                            		   JSONArray friends=jsonFromCodepix.getJSONArray("friends");
	        	                        	
	        	                        	for(int i=0;i<friends.length();i++)
	        								{
	        									JSONObject friend=friends.getJSONObject(i);
	        									
	        									 HashMap<String, String> map = new HashMap<String, String>();
	        									 
	        									 map.put("name",friend.getString("firstname")+" "+friend.getString("lastname"));
	        									 map.put("social_id", friend.getString("social_id"));
	        									 map.put("photourl", friend.getString("photourl"));
	        									 map.put("status",friend.getString("status"));
	        									 map.put("friendid",friend.getString("userid"));
	        									 map.put("social_type",friend.getString("social_type"));
	        									
	        									 
	        									 friendList.add(map);
	        								}
	        	                        	
	        	                                                 	   
	                            	   
	                            	   
	                            	   
	                            	   displayCodepixFriendList();
	                            	    
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

	DisplayImageOptions options;
	String[] imageUrls=new String[]{"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
			"http://simpozia.com/pages/images/stories/windows-icon.png",
			"http://radiotray.sourceforge.net/radio.png"};

	private EditText textSearch;

	private ProgressDialog pd;

	private ArrayList<HashMap<String, String>> friendList;
	
	 private SharedPreferences codePixPref;

	 private View btnBack;
		
	 
	private void addFriend(final int position) {
		// TODO Auto-generated method stub
           new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences codePixPref = getSharedPreferences("CodePixPref", 0);
				 HashMap<String, String> friend = new HashMap<String, String>();
			        friend = friendList.get(position);
				
				JSONParser jParser = new JSONParser();
                
                Map<String, String> mapCodePix = new HashMap<String, String>();
             	
                mapCodePix.put("addfriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("social_type"));
                
                final JSONObject json = jParser.getJSONFromUrl(mapCodePix);
                runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						pd.dismiss();
						try {	// TODO Auto-generated method stub
						 GlobalMethods.showMessage(getApplicationContext(), json.getString("message"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				});
			}
		}).start();
	}
	
	public void displayCodepixFriendList()
	{
		//pd.dismiss();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_profile)
		.showImageForEmptyUri(R.drawable.default_profile)
		.showImageOnFail(R.drawable.default_profile)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();

	
	((ListView) listView).setAdapter(new ItemAdapter(friendList));
	}

	

	protected void doHandleButtonClick(View v, int position, String text) {
		// TODO Auto-generated method stub
		
		
		if(text.equalsIgnoreCase("add")==true)
		{
			addFriend(position);
		 Button btn=(Button)v.findViewById(R.id.btnInvite);
		 btn.setText("Remove");
		}
		
		if(text.equalsIgnoreCase("remove")==true)
		{
			removeFriend(position);
		 Button btn=(Button)v.findViewById(R.id.btnInvite);
		 btn.setText("Add");
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressWarnings("unchecked")
		public void getCodPixUsersList()
		 {
          final LongLoginOperation task =new LongLoginOperation();
	         
	         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	 			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "start");
	 		else
	 			task.execute("start");
		 }
	
	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		
		super.onBackPressed();
		
		startActivity(new Intent(CodePixSearchFriendActivity.this,DashboardActivity.class));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codepixsearch);
		
		codePixPref = getSharedPreferences("CodePixPref", 0);
		textSearch=(EditText)findViewById(R.id.textSearch);
		listView = (ListView) findViewById(R.id.listCodePixSearch);
		friendList=new ArrayList<HashMap<String,String>>();
		pd = new ProgressDialog(this);
		
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		
		textSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
			public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    	pd.show();
                    	
                    	friendList.clear();
                    	getCodPixUsersList();
                    	//displayCodepixFriendList();
                       // GlobalMethods.showMessage(getApplicationContext(), "Go Clicked");
                    	return true;
                    }
                    return false;
                }

            });
		
		btnBack=findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
		
	}

	private void removeFriend(final int position) {
		// TODO Auto-generated method stub
               new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences codePixPref = getSharedPreferences("CodePixPref", 0);
				 HashMap<String, String> friend = new HashMap<String, String>();
			        friend = friendList.get(position);
				
				JSONParser jParser = new JSONParser();
                
                Map<String, String> mapCodePix = new HashMap<String, String>();
             	
                mapCodePix.put("removefriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("social_type"));
                
             // Getting JSON from URL
                
                final JSONObject json = jParser.getJSONFromUrl(mapCodePix);
                runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						pd.dismiss();
						try {	// TODO Auto-generated method stub
						 GlobalMethods.showMessage(getApplicationContext(), json.getString("message"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				});
               
                               					
				
			}
		}).start();
	}
}