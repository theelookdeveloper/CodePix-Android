package com.codepix.db;

import java.util.Arrays;

import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;

import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import android.os.Build;
import android.util.Log;



public class DBHelper {



	// Initial Configuration

	private static class DatabaseHelper extends SQLiteOpenHelper {



		public DatabaseHelper(Context context) {

			super(context, DB_NAME, null, DATABASE_VER);

		}



		@Override

		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CREATE_POST);
			//db.execSQL(CREATE_USER);
			
			
		}



		@Override

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "

					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS "+TABLE_POST);
			//db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
			
			onCreate(db);

		}



	}

	public static final String DB_NAME = "CodepixDB";

	private static final int DATABASE_VER = 1;



	private static final String TAG = "DBAdapter";

	private final Context context;

	private DatabaseHelper DBHelper;

	

	



	// Set the Tables Key Words

	private SQLiteDatabase db;
	public static final String TABLE_POST = "tblpost";
	


	
	
	// Keys

	public static final String TABLE_USER="user";

	public static final String ID = "_id";

	public static final String KEY_CAPTION = "caption";

	public static final String KEY_POST_DATE = "post_date";

	public static String KEY_SECREATE_MESSAGE="secrete_message";

	public static String KEY_PUBLIC="public";
	
	public static String KEY_ORIGINAL_IMAGE_URL="original_image_url";

	public static final String KEY_DISTORTED_IMAGE_URL = "distorted_image_url";

	public static final String KEY_DISPLAY_IMAGE_URL = "display_image_url";
	
	public static final String KEY_POST = "post_id";
	    
	public static final String KEY_WIDTH = "width";
	public static final String KEY_HEIGHT = "height";
	public static final String KEY_GESTURE_TYPE = "gesture_type";
	public static final String KEY_GESTURE_CORDINATES = "gesture_cordinates";
	
	public static final String KEY_UPLOADEDBBY= "uploadedby";

	public static final String KEY_STATUS="status";
	public static final String KEY_LIKE_STATUS="like_status";
	public static final String KEY_FIRST_NAME ="first_name";
	
	
	
	public static final String KEY_LAST_NAME="last_name";

   public static final String KEY_IMAGE_URL = "image_url";
   public static final String KEY_LOGIN_TYPE = "login_type";
   public static final String KEY_LIKE_COUNT = "like_count";
   public static final String KEY_COMMENTS_COUNT = "comments_count";
// Table Queries
  
   private static final String CREATE_POST = "CREATE TABLE "+TABLE_POST
		                                    +" (_id INTEGER PRIMARY KEY, "
		                                    +KEY_CAPTION+" TEXT,"
                                            +KEY_COMMENTS_COUNT+" INTEGER , "
   		                                    +KEY_DISPLAY_IMAGE_URL+" TEXT , "
                                            +KEY_DISTORTED_IMAGE_URL+" TEXT , "
   		                                    +KEY_FIRST_NAME+" TEXT , "
                                            +KEY_GESTURE_CORDINATES+" TEXT,"
                                            +KEY_GESTURE_TYPE+" INTEGER , "
   		                                    +KEY_HEIGHT+" TEXT , "
                                            +KEY_IMAGE_URL+" TEXT , "
   		                                    +KEY_LAST_NAME+" TEXT , "
                                            +KEY_LIKE_STATUS+" TEXT,"
                                            +KEY_LIKE_COUNT+" INTEGER , "
   		                                    +KEY_LOGIN_TYPE+" TEXT , "
                                            +KEY_ORIGINAL_IMAGE_URL+" TEXT , "
   		                                    +KEY_POST+" INTEGER,"
		                                    +KEY_POST_DATE+ " TEXT,"
   		                                    +KEY_PUBLIC+" TEXT, "
                                            +KEY_SECREATE_MESSAGE+" TEXT,"
                                            +KEY_STATUS+" INTEGER , "
   		                                    +KEY_UPLOADEDBBY+" TEXT , "
                                            +KEY_WIDTH+" TEXT);";

//private static final String width = null;



	


	//private static final String CREATE_USER = "CREATE TABLE "+TABLE_USER+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_USERNAME+" TEXT,"
	//		  +KEY_PASSWORD+" TEXT);";



	public DBHelper(Context ctx) {

		this.context = ctx;

		DBHelper = new DatabaseHelper(context);

	}

	


	
	public synchronized int  checkPostid(int postid) {
		// TODO Auto-generated method stub
		
		Cursor cur=db.query(TABLE_POST,null, KEY_POST + "=" + postid , null, null,null, null, null);
	System.out.println("Cursor"+cur.getCount());
	   return cur.getCount();
	}







	public void close() {
        

		DBHelper.close();

	}

	

	public Cursor getExploreListPost(String userid) {
		// TODO Auto-generated method stub
		return db.query(TABLE_POST,null,KEY_UPLOADEDBBY+" !=?",new String[] {userid},null,null, KEY_POST+" desc");
	}
	
	
	public String getExploreListIDs(String userid) {
		// TODO Auto-generated method stub
		
		try {
			Cursor c=db.query(TABLE_POST,new String[]{KEY_POST},KEY_UPLOADEDBBY+" !=?",new String[] {userid},null,null, KEY_POST+" desc");
			String[] ids=new String[c.getCount()];
			int i=0;
			 if (c != null) {
			     if (c.moveToNext()) {
			         do {
			        	
						ids[i++]= c.getString(c.getColumnIndex(KEY_POST)).trim();
			        	 
			         } while (c.moveToNext());
			     }
			 }
			 String str=Arrays.toString(ids);
			 
			 System.out.println("substring "+str);
			 str=str.substring(1);System.out.println("substring "+str);
			 str=str.substring(0,str.length()-1);System.out.println("substring "+str);
			return str;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "";
		}
	}



	/*public long insertUser(String username, String password) {

		ContentValues initialValues = new ContentValues();
        
		initialValues.put(KEY_USERNAME, username);
		
		initialValues.put(KEY_PASSWORD, password);

		
		return db.insert(TABLE_USER, null, initialValues);

	}*/

	public synchronized Cursor getListPost(String userid) {

		//System.out.println("listUser");

		//return db.query(TABLE_POST,null,null, null, null, null, KEY_POST_DATE+" desc");
		return db.query(TABLE_POST,null,KEY_UPLOADEDBBY+" ="+userid, null, null, null, KEY_POST+" desc");



	}



	public Cursor getPostDetails(int postid) {
		// TODO Auto-generated method stub
		
		Cursor cur=db.query(TABLE_POST,null, KEY_POST + "=" + postid , null, null,null, null, null);
		return cur;
	}



	public long insertPost(String caption, String count, String display_image_url, String distorted_image_url, String first_name, String gesture_cordinates, String gesture_type, String height, String image_url, String last_name, String like_status, String like_count, String login_type, String original_iamge_url, int post, String post_date, String post_public, String secreate_message, String status, String uploadedby,String width) {

		ContentValues initialValues = new ContentValues();
        
		initialValues.put(KEY_CAPTION, caption);
		
		initialValues.put(KEY_COMMENTS_COUNT, count);

		initialValues.put(KEY_DISPLAY_IMAGE_URL, display_image_url);
		
		initialValues.put(KEY_DISTORTED_IMAGE_URL, distorted_image_url);
		
		initialValues.put(KEY_FIRST_NAME, first_name);
		initialValues.put(KEY_GESTURE_CORDINATES, gesture_cordinates);
		initialValues.put(KEY_GESTURE_TYPE, gesture_type);
		
		initialValues.put(KEY_HEIGHT, height);
		
		initialValues.put(KEY_IMAGE_URL, image_url);

		initialValues.put(KEY_LAST_NAME, last_name);
		
		initialValues.put(KEY_LIKE_STATUS, like_status);
		
		initialValues.put(KEY_LIKE_COUNT, like_count);
		initialValues.put(KEY_LOGIN_TYPE, login_type);
		initialValues.put(KEY_ORIGINAL_IMAGE_URL, original_iamge_url);
		
		initialValues.put(KEY_POST, post);
		//initialValues.put(KEY_POST_DATE, post_date);
		initialValues.put(KEY_PUBLIC, post_public);
        
		initialValues.put(KEY_SECREATE_MESSAGE, secreate_message);
		initialValues.put(KEY_STATUS, status);
		initialValues.put(KEY_UPLOADEDBBY, uploadedby);
		
		initialValues.put(KEY_WIDTH, width);
		//int postid=Integer.parseInt(post);
        if(checkPostid(post)==0)
        {
        	return db.insert(TABLE_POST, null,initialValues);
        }
        else
        {
        	return db.update(TABLE_POST, initialValues, KEY_POST+"=?", new String[]{String.valueOf(post)});
        }
        
		//return db.update(TABLE_POST, null,initialValues);

	}



	public DBHelper open() throws SQLException {

		db = DBHelper.getWritableDatabase();



		return this;

	}



	public int updateUnlockedPost(String postid) {
		ContentValues initialValues=new ContentValues();
		
		initialValues.put(KEY_STATUS, "1");
		// TODO Auto-generated method stub
		return db.update(TABLE_POST, initialValues, KEY_POST+"=?", new String[]{String.valueOf(postid)});
	}



	public int deletePost(String postid) {
		// TODO Auto-generated method stub
	  return db.delete(TABLE_POST, KEY_POST+"=?", new String[]{String.valueOf(postid)});
	}



	public int updateLikeStatus(String postid, String likestatus, int likecnt) {
		// TODO Auto-generated method stub
ContentValues initialValues=new ContentValues();
		
		initialValues.put(KEY_LIKE_STATUS, likestatus);
		initialValues.put(KEY_LIKE_COUNT, likecnt);
		// TODO Auto-generated method stub
		return db.update(TABLE_POST, initialValues, KEY_POST+"=?", new String[]{String.valueOf(postid)});
		
	}

}
