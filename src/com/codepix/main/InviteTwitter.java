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

import twitter4j.IDs;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class InviteTwitter extends AbsListViewBaseActivity{

	
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
		    
		    if(friend.get("is_user").equalsIgnoreCase("Y")==true)
	        {
		    	holder.button.setText("Add");
	        }
	        else  if(friend.get("is_user").equalsIgnoreCase("N")==true)
		        {
	        	holder.button.setText("Invite");	
		        }	
	        	
	        
	        if(friend.get("status").equalsIgnoreCase("P")==true)
	        {
	        	holder.button.setText("Invited");
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
	private SharedPreferences mSharedPreferences;
	private DisplayImageOptions options;
	private SharedPreferences codePixPref;
	private ArrayList<HashMap<String, String>> friendList;
	private ListView listInvite;
	private ProgressDialog pd;
	
	
	private Button btnBack;
	
	 
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
                //  final JSONObject json = jParser.getJSONFromUrl(mapCodePix);
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

    public void displayTwitterFriendList()
	{
		//pd.dismiss();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_profile)
		.showImageForEmptyUri(R.drawable.default_profile)
		.showImageOnFail(R.drawable.default_profile)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();

	
	((ListView) listView).setAdapter(new ItemAdapter(friendList));
	}
    
    protected void doHandleButtonClick(View v, int position, String text) {
		// TODO Auto-generated method stub
		if(text.equalsIgnoreCase("invite")==true)
		{
			inviteFriend(position);
		 Button btn=(Button)v.findViewById(R.id.btnInvite);
		 btn.setText("Invited");
		}
		
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
	
	private void getTwitterFriendList()
    {
    	
        pd.show();
        
        if (isTwitterLoggedInAlready()) {
    

               try {

                   Thread thread = new Thread(new Runnable(){
                       @Override
                       public void run() {
                           try {
                           	 System.out.println("test3");
                              
   		                    Log.e("Twitter OAuth Token", "> " + DashboardActivity.accessToken.getToken());
   		                    
   		              
   		                 long userID = DashboardActivity.accessToken.getUserId();
   		                 long lCursor = -1;
   		              IDs friendsIDs =DashboardActivity.twitter.getFollowersIDs(userID, lCursor);
   		              System.out.println(DashboardActivity.twitter.showUser(userID).getName());
   		              System.out.println("==========================");
   		              
   		           JSONArray codepix =new JSONArray();
   		              do
   		              {
   		                for (long i : friendsIDs.getIDs())
   		                 {
   		                   String username=DashboardActivity.twitter.showUser(i).getName();
   		                  System.out.println("friend "+username);
   		           JSONObject value=new JSONObject();
	              value.put("socialid",i);
	              
	              if(username.indexOf(" ")==-1)
	              {
	            	  value.put("firstname", username);
	            	  value.put("lastname","");
	              }
	              else
	              {	  
	              value.put("firstname", username.substring(0, username.indexOf(" ")));
	              value.put("lastname",username.substring(username.lastIndexOf(" "), username.length()));
	              }
	              value.put("gender","");
	              value.put("email","");
	              value.put("social_type","Twitter");
	              value.put("photourl",DashboardActivity.twitter.showUser(i).getOriginalProfileImageURL() );
				   codepix.put(value);
   		            
   		            
   		            
   		                 }
   		              }while(friendsIDs.hasNext());
   		              
   		          twitterFriendListSyncServer(codepix);
   		                
   		                    
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

	

	private void inviteFriend(final int position) {
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
             	
                mapCodePix.put("invitefriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("socialid"));
                
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
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
		startActivity(new Intent(InviteTwitter.this,DashboardActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		
	
		mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
		 codePixPref = getSharedPreferences("CodePixPref", 0);
		 friendList=new ArrayList<HashMap<String,String>>();
		 
			 
		 
		 listView=(ListView)findViewById(R.id.listInvite);
			
			// Getting adapter by passing xml data ArrayList
	      //  adapter=new LazyAdapter(this, friendList);        
	       // listInvite.setAdapter(adapter);
	    
              pd = new ProgressDialog(this);
			
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			
			if(isTwitterLoggedInAlready())
			{
				pd.show();
			
			getTwitterFriendList();
			 
			}
			
			
            btnBack=(Button)findViewById(R.id.btnBack);
	        
	        btnBack.setOnClickListener(new View.OnClickListener() {
				
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
                mapCodePix.put("type", friend.get("socialid"));
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

	public void twitterFriendListSyncServer(final JSONArray codepix)
	{
		
		
			//mapFacebook.put("uploadfriend","1");
		    // mapFacebook.put("json", codepix.toString());
		    // mapFacebook.put("userid", codePixPref.getString("userid",""));
		final JSONObject data=new JSONObject();
		try {
			data.put("json", codepix.toString());
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 
		     new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
			
		     
					 
				     JSONObject jsonFromCodepix = null;
					try {
						JSONParser jParser =new JSONParser();
						jsonFromCodepix = jParser.sendJSONFromUrl(codePixPref.getString("userid",""),codepix.toString());
						
						
						if(jsonFromCodepix.getString("status").equals("success")==true)
						{
							
							JSONArray friends=jsonFromCodepix.getJSONArray("friends");
							
							for(int i=0;i<friends.length();i++)
							{
								JSONObject friend=friends.getJSONObject(i);
								
								 HashMap<String, String> map = new HashMap<String, String>();
								 
								 map.put("name",friend.getString("firstname")+" "+friend.getString("lastname"));
								 map.put("socialid", friend.getString("socialid"));
								 map.put("is_user", friend.getString("is_user"));
								 map.put("status",friend.getString("status"));
								 map.put("friendid",friend.getString("userid"));
								 map.put("social_type",friend.getString("social_type"));
								 map.put("photourl",friend.getString("photourl"));
								
								 
								 friendList.add(map);
							}
							
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									 pd.dismiss();
									 
									 displayTwitterFriendList();
								}
							});
					     
					      
							
						}
						
						
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
		     
					
				}
				}).start();		
					
		  	}
}
