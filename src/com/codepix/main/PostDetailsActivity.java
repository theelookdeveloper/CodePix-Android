package com.codepix.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codepix.db.DBHelper;
import com.codepix.loader.ImageLoader;
import com.codepix.utilz.GlobalMethods;
import com.codepix.utilz.JSONParser;

public class PostDetailsActivity extends Activity{
	
	public class LongAddCommentOperation extends AsyncTask<String, Void, String[]> {

		 @Override
	      protected String[] doInBackground(String... params) {
	    	  
	            //  while(true)
	             // {
			 //String []str=null;
	              	if(GlobalMethods.checkInternetConnection(getApplicationContext()))
	              	{	
	              	   try {
	              		 JSONParser jParser = new JSONParser();
	              		 SharedPreferences codePixPref=getSharedPreferences("CodePixPref", 0);
	              		 Map<String, String> map = new HashMap<String, String>();
	         	        String userid=codePixPref.getString("userid", "");
	         	    	 map.put("addcomment", "1");
	         	    	 map.put("userid", userid);
	         	    	 map.put("postid", postid);	 
	         	    	 map.put("comment", params[0]);
	         	    	
	         	 
	         	        // Getting JSON from URL
	         	        final JSONObject json = jParser.getJSONFromUrl(map);
	         	        
	         	        if(json.getString("status").equals("success"))
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="true";
	         	        	str[1]=json.getString("message");
	         	        	System.out.println("response in addcomment"+json.toString());
	         	        	return str;
	         	        }
	         	        else
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="false";
	         	        	str[1]=json.getString("message");
							return str;
	         	        }	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						String[] str=new String[2];
         	        	
         	        	str[0]="false";
         	        	str[1]=getString(R.string.internet_error);
						return str;
					}
	              	
						}
	             // }
	              	String[] str=new String[2];
     	        	
     	        	str[0]="false";
     	        	str[1]=getString(R.string.internet_error);
					return str;
	      }        

	     
			@Override
	      protected void onPostExecute(String[] result) { 
				pd.dismiss();
				if(result[0].equals("true")==true)
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
					//adapter.notifyDataSetChanged();
					getCommentsFromServer();
				}
				else
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
				}
				
	      }

	      @Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	      
	      

	}

	public class LongDeletePostOperation extends AsyncTask<String, Void, String[]> {

		 @Override
	      protected String[] doInBackground(String... params) {
	    	  
	            //  while(true)
	             // {
			 //String []str=null;
	              	if(GlobalMethods.checkInternetConnection(getApplicationContext()))
	              	{	
	              	   try {
	              		 JSONParser jParser = new JSONParser();
	              		 SharedPreferences codePixPref=getSharedPreferences("CodePixPref", 0);
	              		 Map<String, String> map = new HashMap<String, String>();
	         	        String userid=codePixPref.getString("userid", "");
	         	    	 map.put("deletePost", "1");
	         	    	 map.put("userid", userid);
	         	    	 map.put("postid", postid);	 
	         	    	// map.put("comment", params[0]);
	         	    	
	         	 
	         	        // Getting JSON from URL
	         	        final JSONObject json = jParser.getJSONFromUrl(map);
	         	        
	         	        if(json.getString("status").equals("success"))
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="true";
	         	        	str[1]=json.getString("message");
	         	        	System.out.println("response in addcomment"+json.toString());
	         	        	return str;
	         	        }
	         	        else
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="false";
	         	        	str[1]=json.getString("message");
							return str;
	         	        }	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						String[] str=new String[2];
        	        	
        	        	str[0]="false";
        	        	str[1]=getString(R.string.internet_error);
						return str;
					}
	              	
						}
	             // }
	              	String[] str=new String[2];
    	        	
    	        	str[0]="false";
    	        	str[1]=getString(R.string.internet_error);
					return str;
	      }        

	     
			@Override
	      protected void onPostExecute(String[] result) { 
				pd.dismiss();
				if(result[0].equals("true")==true)
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
					//adapter.notifyDataSetChanged();
					//getCommentsFromServer();
					
					
					if(deletePostFromLocal(postid)!=0);
					startActivity(new Intent(PostDetailsActivity.this,DashboardActivity.class));
				}
				else
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
				}
				
	      }

	     


		@Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	      
	      

	}
	
	 private void updateLikeStatus(String postid, String likestatus, int likecnt) {
			// TODO Auto-generated method stub
		 DBHelper db=new DBHelper(getApplicationContext());
	      
	      db.open();
	  int i=db.updateLikeStatus(postid,likestatus,likecnt);
	      db.close();
		}
	 private int deletePostFromLocal(String postid) {
			// TODO Auto-generated method stub
		      DBHelper db=new DBHelper(getApplicationContext());
		      
		      db.open();
		  int i=    db.deletePost(postid);
		      db.close();
		      
		      return i;
		}
	class PostCommentAdapter extends BaseAdapter {

		 private class ViewHolder {
			//public Button button;
			public ImageView image;
			public TextView comment;
			public TextView username;
			public TextView date;
		}
		    private Context activity;
		    private ImageLoader cLoader;
		    private ArrayList<HashMap<String, String>> data;
		   
		    
		    private LayoutInflater inflater=null;

		    public PostCommentAdapter(Context a, ArrayList<HashMap<String, String>> d) {
		        activity = a;
		        data=d;
		       inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		       cLoader = new ImageLoader(activity);
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
			public View getView(int position, View convertView, ViewGroup parent) {
		        View view=convertView;
		/*        if(convertView==null)
		        {
		        	//LayoutInflater	inflater1 = LayoutInflater.from(activity);
				convertView = getLayoutInflater().inflate(R.layout.row_comment, parent, false);
		        TextView comment = (TextView)convertView.findViewById(R.id.textViewComment); // comment
		       
		        ImageView thumb_image=(ImageView)convertView.findViewById(R.id.imageProfile1); // thumb image
		        
		        //final Button btn=(Button)vi.findViewById(R.id.btnInvite); // thumb image
		        
		        HashMap<String, String> friend = new HashMap<String, String>();
		        friend = data.get(position);
		        final int pos=position;
		        System.out.println("position"+pos+"comment"+friend.get("userComment"));
		        comment.setText(friend.get("userComment"));
		       
		        }		        
		        return convertView;*/
		    	
		    	ViewHolder holder;
				if (convertView == null) {
					view = getLayoutInflater().inflate(R.layout.row_comment, parent, false);
					holder = new ViewHolder();
					//holder.button = (Button) view.findViewById(R.id.btnInvite);
					holder.image = (ImageView) view.findViewById(R.id.imageViewProfile);
					holder.comment = (TextView)view.findViewById(R.id.textViewComment);
					holder.username = (TextView)view.findViewById(R.id.textViewName);
					holder.date = (TextView)view.findViewById(R.id.textViewDate);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				 HashMap<String, String> postComment = new HashMap<String, String>();
				 postComment = data.get(position);
			     final int pos=position;
			      
			    holder.comment.setText(postComment.get("userComment"));
			    holder.username.setText(postComment.get("firstname")+" "+postComment.get("lastname"));
			    holder.date.setText(postComment.get("created_time"));
			    
			    
			    cLoader.DisplayImage(postComment.get("photourl"), holder.image);
			    return view;
		    }
        
	}
	
	private class LongOperation extends AsyncTask<String, Void, String> {

	      @Override
	      protected String doInBackground(String... params) {
	    	  
	            //  while(true)
	             // {
	              	if(GlobalMethods.checkInternetConnection(getApplicationContext()))
	              	{	
	              	   try {
	              		 JSONParser jParser = new JSONParser();
	              		 SharedPreferences codePixPref=getSharedPreferences("CodePixPref", 0);
	              		 Map<String, String> map = new HashMap<String, String>();
	         	        String userid=codePixPref.getString("userid", "");
	         	    	 map.put("getComments", "1");
	         	    	 map.put("userid", userid);
	         	    	 map.put("postid", postid);	 
	         	    	 map.put("likeStatus", "0");
	         	    	
	         	 
	         	        // Getting JSON from URL
	         	        final JSONObject json = jParser.getJSONFromUrl(map);
	         	        
	         	        if(json.getString("status").equals("success"))
	         	        {
	         	        	
	         	        	System.out.println("response in PostDetails"+json.toString());
	         	        	JSONArray comments = json.getJSONArray("comments");
	         	        	
	         	        	if(comments!=null)
	         	        	{
	         	        		 for(int i = 0; i < comments.length(); i++){
	         		                JSONObject c = comments.getJSONObject(i);
	         		                
	         		               HashMap<String, String> commentMap = new HashMap<String, String>();
	         		               
	         		              commentMap.put("userid", c.getString("userid"));
	         		              commentMap.put("social_id", c.getString("social_id"));
	         		              commentMap.put("firstname", c.getString("firstname"));
	         		              commentMap.put("email", c.getString("email"));
	         		              commentMap.put("lastname", c.getString("lastname"));
	         		              commentMap.put("photourl", c.getString("photourl"));
	         		              commentMap.put("social_type", c.getString("social_type"));
	         		              commentMap.put("userComment", c.getString("userComment"));
	         		              commentMap.put("created_time", c.getString("created_time"));
	         		             System.out.println("response in PostDetails usercomment"+c.getString("userComment"));      
	         		            commentlist.clear(); 
	         		             commentlist.add(commentMap);
	         	        		 }
	         	        	}
	         	        	else
	         	        	{
	         	        		runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											GlobalMethods.showMessage(getApplicationContext(), json.getString("message"));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
	         	        	}	
	         	        }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						return "false";
					}
	              	
						}
	             // }
					return "true";
	      }        

	     
			@Override
	      protected void onPostExecute(String result) { 
				if(result.equals("true")==true)
				{
					pd.dismiss();
					adapter.notifyDataSetChanged();
					textViewCommentCount.setText(commentlist.size()+"");
					
					//displayCommentList();
				}
				
	      }

	      @Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	  }
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void likeUnlikeToServer()
	{

		
		if(GlobalMethods.checkInternetConnection(getApplicationContext())==true)
		{
			pd.show();
			final LongLikeUnlikeOperation task=new LongLikeUnlikeOperation();
			
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
				    	 
				    	  runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								pd.dismiss();
						    	  GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));	
							}
						});
				    	  
				    	  task.cancel(true);
				    				    	  
				      }	  
				  }
				}, 10*1000);
		}
		else
		{
			GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
		}	
	}
    
    
    private class LongLikeUnlikeOperation extends AsyncTask<String, Void, String[]> {

    	 

		@Override
	      protected String[] doInBackground(String... params) {
	    	  
	            //  while(true)
	             // {
			 //String []str=null;
	              	if(GlobalMethods.checkInternetConnection(getApplicationContext()))
	              	{	
	              	   try {
	              		 JSONParser jParser = new JSONParser();
	              		 SharedPreferences codePixPref=getSharedPreferences("CodePixPref", 0);
	              		 Map<String, String> map = new HashMap<String, String>();
	         	        String userid=codePixPref.getString("userid", "");
	         	    	 map.put("likeUnlikePost", "1");
	         	    	 map.put("userid", userid);
	         	    	 map.put("postid", postid);
	         	    	map.put("like", likestatus);
	         	    	 
	         	    	
	         	 
	         	        // Getting JSON from URL
	         	        final JSONObject json = jParser.getJSONFromUrl(map);
	         	        
	         	        if(json.getString("status").equals("success"))
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="true";
	         	        	str[1]=json.getString("message");
	         	        	System.out.println("response in addcomment"+json.toString());
	         	        	return str;
	         	        }
	         	        else
	         	        {
	         	        	String[] str=new String[2];
	         	        	
	         	        	str[0]="false";
	         	        	str[1]=json.getString("message");
							return str;
	         	        }	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						String[] str=new String[2];
         	        	
         	        	str[0]="false";
         	        	str[1]=getString(R.string.internet_error);
						return str;
					}
	              	
						}
	             // }
	              	String[] str=new String[2];
     	        	
     	        	str[0]="false";
     	        	str[1]=getString(R.string.internet_error);
					return str;
	      }        

	     
			@Override
	      protected void onPostExecute(String[] result) { 
				pd.dismiss();
				if(result[0].equals("true")==true)
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
					
					int likecnt=Integer.parseInt(textViewLikeCount.getText().toString());
					
					if(likestatus.equals("0"))
					{
						likecnt=likecnt-1;
						
					}
					else
					{
						likecnt=likecnt+1;
					}	
					textViewLikeCount.setText(likecnt+"");
					
					updateLikeStatus(postid,likestatus,likecnt);
				}
				else
				{
					GlobalMethods.showMessage(getApplicationContext(), result[1]);
				}
				
	      }

	     


		@Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	  }         
	private ImageView imageView;
	private ImageLoader mLoader;
	private ImageView imageProfile;
	private TextView textViewName;
	private TextView textViewCaption;
	private ListView listViewComment;
	private ArrayList<HashMap<String, String>> commentlist;
	private String postid;

    private ProgressDialog pd;
	private LinearLayout commentLayout;
	private Button buttonComment;
	private PostCommentAdapter adapter;
	private TextView textViewCommentCount;
	private TextView textViewLikeCount;
	private String likestatus;
	private Button buttonLike;
	private Button btnDelete;
	private SharedPreferences codePixPref;
	private DBHelper db;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void deletePostFromServer() {
		// TODO Auto-generated method stub
		if(GlobalMethods.checkInternetConnection(getApplicationContext())==true)
		{
			pd.show();
			final LongDeletePostOperation task=new LongDeletePostOperation();
			
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
				    	 
				    	  runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								pd.dismiss();
						    	  GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));	
							}
						});
				    	  
				    	  task.cancel(true);
				    				    	  
				      }	  
				  }
				}, 10*1000);
		}
		else
		{
			GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
		}	
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void getCommentsFromServer()
	{

		
		if(GlobalMethods.checkInternetConnection(getApplicationContext())==true)
		{
			pd.show();
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
				    	 
				    	  runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								pd.dismiss();
						    	  GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));	
							}
						});
				    	  
				    	  task.cancel(true);
				    				    	  
				      }	  
				  }
				}, 10*1000);
		}
		else
		{
			GlobalMethods.showMessage(getApplicationContext(), getString(R.string.internet_error));
		}	
	}
	
	
	  @SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_postdetails);
		
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		
		 codePixPref=getSharedPreferences("CodePixPref", 0);
		
		imageView=(ImageView)findViewById(R.id.imageView2);
		imageProfile=(ImageView)findViewById(R.id.imageView1);
		textViewName=(TextView)findViewById(R.id.textViewName);
		textViewCaption=(TextView)findViewById(R.id.textViewCaption);
		
		buttonComment=(Button)findViewById(R.id.buttonComment);
		buttonLike=(Button)findViewById(R.id.btnLike);
		btnDelete=(Button)findViewById(R.id.btnDelete);
		
		
		//commentLayout=(LinearLayout)findViewById(R.id.commentLayout);
		listViewComment=(ListView)findViewById(R.id.listViewComment);
		textViewCommentCount=(TextView)findViewById(R.id.textViewCommentCount);
		textViewLikeCount=(TextView)findViewById(R.id.textViewLikeCount);

        try {
			db = new DBHelper(this);
			
			db.open();
			postid=b.getString("postid", "0");
			//Toast.makeText(this, "postid"+postid, Toast.LENGTH_LONG).show();
			Cursor c = db.getPostDetails(Integer.parseInt(postid));
			 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
			startManagingCursor(c);
			
			c.moveToNext();
			
			String image=c.getString(c.getColumnIndex(DBHelper.KEY_DISTORTED_IMAGE_URL));
			String imageProfileURL=c.getString(c.getColumnIndex(DBHelper.KEY_IMAGE_URL));
			String name=c.getString(c.getColumnIndex(DBHelper.KEY_FIRST_NAME))+" "+c.getString(c.getColumnIndex(DBHelper.KEY_LAST_NAME)) ;
			String caption=c.getString(c.getColumnIndex(DBHelper.KEY_CAPTION)) ;
			String comments=c.getString(c.getColumnIndex(DBHelper.KEY_COMMENTS_COUNT)) ;
			likestatus=c.getString(c.getColumnIndex(DBHelper.KEY_LIKE_STATUS));
			String unlockStatus=c.getString(c.getColumnIndex(DBHelper.KEY_STATUS));
			String likesCount=c.getString(c.getColumnIndex(DBHelper.KEY_LIKE_COUNT));
			String uploadedby=c.getString(c.getColumnIndex(DBHelper.KEY_UPLOADEDBBY));
			if(image.length()==0)
				image=c.getString(c.getColumnIndex(DBHelper.KEY_ORIGINAL_IMAGE_URL));
			
			if(unlockStatus.equals("1")==true)
			{
				image=c.getString(c.getColumnIndex(DBHelper.KEY_ORIGINAL_IMAGE_URL));
			}
			else
			{
				image=c.getString(c.getColumnIndex(DBHelper.KEY_DISTORTED_IMAGE_URL));
			}
			
			
			if(!codePixPref.getString("userid", "0").equals(uploadedby))
			{
				btnDelete.setText("");
				btnDelete.setEnabled(false);
				btnDelete.setWidth(0);
				btnDelete.setHeight(0);
				btnDelete.setBackgroundColor(Color.TRANSPARENT);
			}
			
			
			mLoader = new ImageLoader(this);

			mLoader.DisplayImage(image, imageView);
			mLoader.DisplayImage(imageProfileURL, imageProfile);
			
			textViewName.setText(name);
			textViewCaption.setText(caption);
			textViewCommentCount.setText(comments);
			textViewLikeCount.setText(likesCount);
			
			commentlist=new ArrayList<HashMap<String,String>>();
			
			
			
//	System.out.println("list count"+commentlist.size());
			adapter=new PostCommentAdapter(getApplicationContext(),commentlist);
			listViewComment.setAdapter(adapter);
			
			
			
			//ListView lv = (ListView)findViewById(R.id.myListView);  // your listview inside scrollview
/*		listViewComment.setOnTouchListener(new ListView.OnTouchListener() {
			        @Override
			        public boolean onTouch(View v, MotionEvent event) {
			            int action = event.getAction();
			            switch (action) {
			            case MotionEvent.ACTION_DOWN:
			                // Disallow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(true);
			                break;

			            case MotionEvent.ACTION_UP:
			                // Allow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(false);
			                break;
			            }

			            // Handle ListView touch events.
			            v.onTouchEvent(event);
			            return true;
			        }
			    });*/
			setListViewScrollable(listViewComment);
			adapter.notifyDataSetChanged();
			 pd = new ProgressDialog(this);
				
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				
				buttonComment.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						callAddComment();
					}
				});	
				
				if(likestatus.equals("1")==true)
				{
					//likestatus="1";
					
					buttonLike.setEnabled(true);
					buttonLike.setBackgroundColor(Color.parseColor("#FF0000"));
				}
				else
				{
					//likestatus="0";
					buttonLike.setEnabled(true);
					buttonLike.setBackgroundColor(Color.parseColor("#CCCCCC"));
				}	
				
				buttonLike.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(likestatus.equals("0")==true)
						{
							likestatus="1";
							
							buttonLike.setEnabled(true);
							buttonLike.setBackgroundColor(Color.parseColor("#FF0000"));
						}
						else
						{
							likestatus="0";
							buttonLike.setEnabled(true);
							buttonLike.setBackgroundColor(Color.parseColor("#CCCCCC"));
						}	
						
						likeUnlikeToServer();
					}
				});
				
				btnDelete.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						deletePostFromServer();
					}

					
				});	
				
					
			getCommentsFromServer();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
    
	  private void setListViewScrollable(final ListView list) {
		    list.setOnTouchListener(new OnTouchListener() {
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		            int listViewTouchAction = event.getAction();
		            if (listViewTouchAction == MotionEvent.ACTION_MOVE)
		            {
		                list.scrollBy(0, 1);
		            }
		            return false;
		        }
		    });
		    list.setOnScrollListener(new OnScrollListener() {
		        @Override
		        public void onScrollStateChanged(AbsListView view,
		                int scrollState) {
		        }

		        @Override
		        public void onScroll(AbsListView view, int firstVisibleItem,
		                int visibleItemCount, int totalItemCount) {
		            int listViewTouchAction = 0;
					if (listViewTouchAction == MotionEvent.ACTION_MOVE)
		            {
		                list.scrollBy(0, -1);
		            }
		        }
		    });
		}
	  
	public void callAddComment()
	{
		//CLOSE APPLICATION
		// Create the alert box 
      AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

      // Set the message to display
     // alertbox.setMessage("Please add comment");
      alertbox.setTitle("ADD COMMENT");
      alertbox.setMessage("Please add comment first.");
		// Create TextView
		
		final EditText input = new EditText (this);
		alertbox.setView(input);
		alertbox.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onClick(DialogInterface dialog, int whichButton) {
			
				 dialog.dismiss();		   
					   if(input.getText().length()==0)
						 {
							GlobalMethods.showMessage(PostDetailsActivity.this,"Please enter comment.");
							input.requestFocus();
							
						 }
					   else
					       {
							
							 pd.show();	
							 final LongAddCommentOperation task=new LongAddCommentOperation();
				        	  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				      			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input.getText().toString());
				      		else
				      			task.execute(input.getText().toString());
				        	  Handler handler = new Handler();
				  			handler.postDelayed(new Runnable()
				  			{
				  			  @Override
				  			  public void run() {
				  			      if ( task.getStatus() == AsyncTask.Status.RUNNING )
				  			      {
				  			    	  task.cancel(true);
				  			    	  
				  			    	  pd.dismiss();
				  			    	  
				  			    	  GlobalMethods.showMessage(PostDetailsActivity.this, getString(R.string.process_long));
				  			      }	  
				  			  }
				  			}, 10*1000);
							
						  }
					

		
			}});

		alertbox.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			  @Override
			public void onClick(DialogInterface dialog, int whichButton) {
			      // Canceled.
				  dialog.dismiss();
			  }
			});
		
      alertbox.show();
      // show the alert box
	}
	  
	public void displayCommentList()
	{
		
		textViewCommentCount.setText(commentlist.size()+"");
		for(int i=0;i<commentlist.size();i++)
		{
			View view=getLayoutInflater().inflate(R.layout.row_comment, commentLayout, true);	
			//ViewHolder holder = new ViewHolder();
			ImageView image = (ImageView) view.findViewById(R.id.imageViewProfile);
			TextView comment = (TextView)view.findViewById(R.id.textViewComment);
			TextView username = (TextView)view.findViewById(R.id.textViewName);
			TextView date = (TextView)view.findViewById(R.id.textViewDate);
			
			
			HashMap<String, String> postComment = new HashMap<String, String>();
			 postComment = commentlist.get(i);
		    // final int pos=position;
		      
		    comment.setText(postComment.get("userComment"));
		    username.setText(postComment.get("firstname")+" "+postComment.get("lastname"));
		    date.setText(postComment.get("created_time"));
		    
		    ImageLoader cLoader = new ImageLoader(getApplicationContext());    
		    cLoader.DisplayImage(postComment.get("photourl"),image);
		} 	
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
	
	 private class ViewHolder {
			//public Button button;
			public ImageView image;
			public TextView comment;
			public TextView username;
			public TextView date;
		}
}
