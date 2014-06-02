package com.codepix.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepix.main.R;
import com.codepix.utilz.JSONParser;

public class LazyAdapter extends BaseAdapter {
    
    private Context activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
   
    
    public LazyAdapter(Context a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
    }

    private void addFriend(final int position) {
		// TODO Auto-generated method stub
           new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences codePixPref = activity.getSharedPreferences("CodePixPref", 0);
				 HashMap<String, String> friend = new HashMap<String, String>();
			        friend = data.get(position);
				
				JSONParser jParser = new JSONParser();
                
                Map<String, String> mapCodePix = new HashMap<String, String>();
             	
                mapCodePix.put("addfriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("social_type"));
                
             // Getting JSON from URL
                try {
                JSONObject json = jParser.getJSONFromUrl(mapCodePix);
                Intent broadcast=new Intent();
                broadcast.setAction("SHOW_ACTION");
		    	  broadcast.putExtra("msg", json.getString("message"));
		    	  
		    	  activity.sendBroadcast(broadcast);
               					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // comment
       
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        final Button btn=(Button)vi.findViewById(R.id.btnInvite); // thumb image
        
        HashMap<String, String> friend = new HashMap<String, String>();
        friend = data.get(position);
        final int pos=position;
        
        title.setText(friend.get("name"));
       
        if(friend.get("is_user").equalsIgnoreCase("Y")==true)
        {
        	btn.setText("Add");
        }
        else  if(friend.get("is_user").equalsIgnoreCase("N")==true)
	        {
	        	btn.setText("Invite");	
	        }	
        	
        
        if(friend.get("status").equalsIgnoreCase("P")==true)
        {
        	btn.setText("Invited");
        }
    	
    	else if(friend.get("status").equalsIgnoreCase("F")==true)
        {
        	btn.setText("Remove");	
        }	
       
        btn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doHandleButtonClick(v,pos,btn.getText().toString());
			}
		});
        
        return vi;
    }

	private void inviteFriend(final int position) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences codePixPref = activity.getSharedPreferences("CodePixPref", 0);
				 HashMap<String, String> friend = new HashMap<String, String>();
			        friend = data.get(position);
				
				JSONParser jParser = new JSONParser();
                
                Map<String, String> mapCodePix = new HashMap<String, String>();
             	
                mapCodePix.put("invitefriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("socialid"));
                
             // Getting JSON from URL
                try {
                JSONObject json = jParser.getJSONFromUrl(mapCodePix);
                Intent broadcast=new Intent();
                broadcast.setAction("SHOW_ACTION");
		    	  broadcast.putExtra("msg", json.getString("message"));
		    	  
		    	  activity.sendBroadcast(broadcast);
               					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void removeFriend(final int position) {
		// TODO Auto-generated method stub
               new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences codePixPref = activity.getSharedPreferences("CodePixPref", 0);
				 HashMap<String, String> friend = new HashMap<String, String>();
			        friend = data.get(position);
				
				JSONParser jParser = new JSONParser();
                
                Map<String, String> mapCodePix = new HashMap<String, String>();
             	
                mapCodePix.put("removefriend", "1");
                mapCodePix.put("socialid", friend.get("socialid"));
                mapCodePix.put("userid", codePixPref.getString("userid",""));
                mapCodePix.put("friendid", friend.get("friendid"));
                mapCodePix.put("type", friend.get("socialid"));
                
             // Getting JSON from URL
                try {
                JSONObject json = jParser.getJSONFromUrl(mapCodePix);
                Intent broadcast=new Intent();
                broadcast.setAction("SHOW_ACTION");
		    	  broadcast.putExtra("msg", json.getString("message"));
		    	  
		    	  activity.sendBroadcast(broadcast);
               					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
   
    
}