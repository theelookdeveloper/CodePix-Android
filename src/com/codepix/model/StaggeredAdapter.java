package com.codepix.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.codepix.db.DBHelper;
import com.codepix.loader.ImageLoader;
import com.codepix.loader.ImageLoader1;
import com.codepix.main.R;

import com.codepix.views.ScaleImageView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class StaggeredAdapter extends ArrayAdapter<String> {

	static class ViewHolder {
		ScaleImageView imageView;
		LinearLayout   linearLayout1;
	    ImageView imageProfile;
	    TextView textViewName;
		
	}
	
	String objects1[];
	String objects[];
	private ArrayList<HashMap<String, String>> data;
	

	Context context;

	public StaggeredAdapter(Context context, int textViewResourceId,
			String[] objects,ArrayList<HashMap<String, String>> postdata) {
		
		
		super(context, textViewResourceId, objects);
		
		this.data=postdata;
		this.context=context;
		
	}

	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		String[] color = new String[]{"#765AE6","#F511F1","#11F520","#ECF808","#8B8C72"};
		Random rand = new Random();
		int index = 0;

		index = rand.nextInt(color.length);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map=data.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			
			holder.imageProfile = (ImageView) convertView .findViewById(R.id.imageProfile);
			
			holder.textViewName = (TextView) convertView .findViewById(R.id.textViewName);
			holder.linearLayout1=(LinearLayout)convertView.findViewById(R.id.linearLayout1);
			holder.linearLayout1.setBackgroundColor(Color.parseColor(color[index]));
			
			String name=map.get(DBHelper.KEY_FIRST_NAME)+" "+map.get(DBHelper.KEY_LAST_NAME);
			holder.textViewName.setText(name);
			String postid=map.get(DBHelper.KEY_POST);
			convertView.setTag(holder);
			System.out.println("postid"+postid);
			holder.imageView.setTag(map);
			holder.imageView.setContentDescription(postid);
			
			//holder.imageView.setColorFilter(Integer.parseInt(postid));
	        holder.imageView.setId(Integer.parseInt(postid));
			
			
			/*holder.imageView.setOnClickListener(new ImageView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					HashMap<String, String> map =(HashMap<String, String>) v.getTag();
					callPostDetails(v.getContentDescription().toString());
					Toast.makeText(context, "postid"+v.getId(), Toast.LENGTH_LONG).show();
				}
			});*/
	        
	        holder = (ViewHolder) convertView.getTag();
			
			String url=map.get(DBHelper.KEY_DISTORTED_IMAGE_URL);
			
			if(map.get(DBHelper.KEY_STATUS).equals("1")==true)
			{
				url=map.get(DBHelper.KEY_ORIGINAL_IMAGE_URL);
			}
			else
			{
				url=map.get(DBHelper.KEY_DISTORTED_IMAGE_URL);
			}
			
			if(url.length()==0)
			{
				url=map.get(DBHelper.KEY_ORIGINAL_IMAGE_URL);
				
				
					
				
			}
			final String profileUrl=map.get(DBHelper.KEY_IMAGE_URL);
			ImageLoader1 mLoader1 = new ImageLoader1(context);
			ImageLoader mLoader = new ImageLoader(context);
			mLoader1.DisplayImage(url, holder.imageView);
			mLoader.DisplayImage(profileUrl, holder.imageProfile);

		}

		
		return convertView;
	}
}
