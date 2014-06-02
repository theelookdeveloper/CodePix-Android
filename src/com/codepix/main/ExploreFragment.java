package com.codepix.main;



import java.util.ArrayList;
import java.util.HashMap;

import com.codepix.db.DBHelper;

import com.codepix.model.StaggeredEAdapter;
import com.codepix.utilz.GlobalMethods;
import com.origamilabs.library.views.StaggeredGridView;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExploreFragment extends Fragment  {
	
	/*private String urls[] = { 
			"http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
			"http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
			"http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
			"http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
			"http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
			"http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
			"http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
			"http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
			"http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
			"http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
			"http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
			"http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
			"http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
			"http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
			"http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
			"http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
			"http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
			"http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
			"http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
			"http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
			"http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
			"http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
			"http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
			"http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
			"http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
			"http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg"
	};*/
	
	private class LongOperation extends AsyncTask<String, Void, String> {
	
	      @Override
	      protected String doInBackground(String... params) {
	    	  
	            //  while(true)
	             // {
	              	if(GlobalMethods.checkInternetConnection(getActivity()))
	              	{	
	              	   try {
						GlobalMethods.doSearchPost(getActivity(),2);
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
				  if(pd.isShowing())
					pd.dismiss();
				adapter.notifyDataSetChanged();
				}
				
	      }
	
	      @Override
	      protected void onPreExecute() {
	      }
	
	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	  }
	private String urls[];
	 private Cursor c;
	ArrayList<HashMap<String, String>> postlist;
	private StaggeredEAdapter adapter;
	private ProgressDialog pd;
	

 private SharedPreferences codePixPref;

BroadcastReceiver postsReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
      	
      	Bundle b=intent.getExtras();
      	//GlobalMethods.showMessage(getActivity(), "action"+b.getString("action"));
         if(b.getString("action").equals("refresh"));
         {
      	   ExploreFragment fragment2 = new ExploreFragment();
			    FragmentManager fragmentManager =getFragmentManager();
			    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			   // fragmentTransaction.replace(R.id.frame_container, fragment2);
			   // fragmentTransaction.addToBackStack("newslist");
			    fragmentTransaction.attach(fragment2);
			    fragmentTransaction.commit(); 
         }
      }
  };
private String excludesids;

protected void callPostDetails(String postid, String status) {
	// TODO Auto-generated method stub
	//Toast.makeText(context, "test"+postid, Toast.LENGTH_LONG).show();
  Intent intent =null;
  if(status.equals("1")==true)
	intent = new Intent(getActivity(),PostDetailsActivity.class);
  else
	  intent = new Intent(getActivity(),ImageUnlockGestureActivity.class);
	intent.putExtra("postid", postid);
	getActivity().startActivity(intent);
}
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void getUpdatedPosts() {
		// TODO Auto-generated method stubLongOperation task=new LongOperation();
		
		if(GlobalMethods.checkInternetConnection(getActivity())==true)
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
				    	  
				    	  pd.dismiss();
				    	  task.cancel(true);
				    				    	  
				      }	  
				  }
				}, 30*1000);
		}
		
		
		 
		
	}
  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
	 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
     StrictMode.setThreadPolicy(policy);
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_explore, container, false);
        
        postlist=new ArrayList<HashMap<String, String>>();
        DBHelper db = new DBHelper(getActivity());
        
		db.open();
		codePixPref=getActivity().getSharedPreferences("CodePixPref", 0);
		
		 String userid=codePixPref.getString("userid", "");
 
		c = db.getExploreListPost(userid);
        
		getActivity().startManagingCursor(c);
  
 
		int[] t = new int[] { android.R.id.text1 };   
		
		urls=new String[c.getCount()];
		
		//String[] urls1=new String[c.getCount()];
		int i=0;
		
		 if (c != null) {
                if (c.moveToNext()) {
                    do {
                        
                        HashMap<String, String> map = new HashMap<String, String>();
            			
						//Element e = (Element) nl.item(i);
            			// adding each child node to HashMap key => value
                       map.put(DBHelper.KEY_FIRST_NAME, c.getString(c.getColumnIndex(DBHelper.KEY_FIRST_NAME)));
                       map.put(DBHelper.KEY_LAST_NAME, c.getString(c.getColumnIndex(DBHelper.KEY_LAST_NAME)));
                       map.put(DBHelper.KEY_IMAGE_URL, c.getString(c.getColumnIndex(DBHelper.KEY_IMAGE_URL)));
                       map.put(DBHelper.KEY_POST, c.getString(c.getColumnIndex(DBHelper.KEY_POST)));
                       map.put(DBHelper.KEY_ORIGINAL_IMAGE_URL, c.getString(c.getColumnIndex(DBHelper.KEY_ORIGINAL_IMAGE_URL)));
                       map.put(DBHelper.KEY_DISTORTED_IMAGE_URL, c.getString(c.getColumnIndex(DBHelper.KEY_DISTORTED_IMAGE_URL)));
                       map.put(DBHelper.KEY_STATUS, c.getString(c.getColumnIndex(DBHelper.KEY_STATUS)));
            			urls[i++]=c.getString(c.getColumnIndex(DBHelper.KEY_DISTORTED_IMAGE_URL));
            			postlist.add(map);
                    } while (c.moveToNext());
                }
            }
        
        
        
        
        StaggeredGridView gridView = (StaggeredGridView) V.findViewById(R.id.staggeredGridView2);
		
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		
		gridView.setItemMargin(margin); // set the GridView margin
		
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		
		
		
        gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
			
			@Override
			public void onItemClick(StaggeredGridView parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				HashMap<String, String> map = new HashMap<String, String>();
				map=postlist.get(position);
				callPostDetails(map.get(DBHelper.KEY_POST),map.get(DBHelper.KEY_STATUS));
				//Toast.makeText(getActivity(), "position"+map.get(DBHelper.KEY_POST), Toast.LENGTH_LONG).show();
			}
		});
		 adapter = new StaggeredEAdapter(getActivity(), R.id.imageView1, urls,postlist);
		
		gridView.setAdapter(adapter);
		
		
		 pd = new ProgressDialog(getActivity());
			
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			 getUpdatedPosts();

        return V;
    }
  
  @Override
public void onPause() {
	  super.onPause();
	  getActivity().unregisterReceiver(postsReceiver);
  }
  
  @Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	adapter.notifyDataSetChanged();
	IntentFilter filter = new IntentFilter();
    filter.addAction("EXPLORE_ACTION");
    getActivity().registerReceiver(postsReceiver, filter);
    
   
}
  
 
}