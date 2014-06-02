package com.codepix.utilz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class HttpClient {
        private String url;
    private HttpURLConnection con;
    private OutputStream os;
    Context context;
        private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
	public SharedPreferences codePixPref;

        public HttpClient(String url) {                
                this.url = url;
        }
        
        public HttpClient(String url2, Context applicationContext) {
			// TODO Auto-generated constructor stub
        	 this.url = url2;
        	 context=applicationContext;
        	 codePixPref = context.getApplicationContext().getSharedPreferences(
                     "CodePixPref", 0);
		}

		public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
                os.write( (delimiter + boundary + "\r\n").getBytes());
                os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
                os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
                os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
                os.write("\r\n".getBytes());
   
                os.write(data);
                
                os.write("\r\n".getBytes());
        }

        public void addFormPart(String paramName, String value) throws Exception {
                writeParamData(paramName, value);
        }
        
        public void connectForMultipart() throws Exception {
        	try{
                con = (HttpURLConnection) ( new URL(url)).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                con.connect();
                os = con.getOutputStream();
        	}catch(MalformedURLException e)
        	{
        		System.out.println("Exception:-"+e.getMessage());
        	}
        	catch(Exception e)
        	{
        		System.out.println("Exception:-"+e.getMessage());
        	}
        }
        
        public byte[] downloadImage(String imgName) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                        System.out.println("URL ["+url+"] - Name ["+imgName+"]");
                        
                        HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
                        con.setRequestMethod("POST");
                        con.setDoInput(true);
                        con.setDoOutput(true);
                        con.connect();
                        con.getOutputStream().write( ("name=" + imgName).getBytes());
                        
                        InputStream is = con.getInputStream();
                        byte[] b = new byte[1024];
                        
                        while ( is.read(b) != -1)
                                baos.write(b);
                        
                        con.disconnect();
                }
                catch(Throwable t) {
                        t.printStackTrace();
                }
                
                return baos.toByteArray();
        }
        
        public void finishMultipart() throws Exception {
                os.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
        }
        
        
        public String[] getResponse() throws Exception {
                InputStream is = con.getInputStream();
               /* byte[] b1 = new byte[1024];
                StringBuffer buffer = new StringBuffer();
                
                while ( is.read(b1) != -1)
                        buffer.append(new String(b1));
                */
               
                String json = null;
                try {
         		   Charset charset = Charset.forName("UTF8");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, charset), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
               Log.d("JSONParser", "response from server while uploading "+json);
                // try parse the string to a JSON object
               
               JSONObject  jObj = null; 
                try {
                 jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                
               
                
                String []res=new String[2];
                  String message="success";
                  String status="done";
                  	 JSONArray user;
						try {
							//user = jObj.getJSONArray("success");
							 //JSONObject c = user.getJSONObject(0);
							 
	                        status=jObj.getString("status");
	                       // message=jObj.get();
	                        
	                          if(jObj.getString("first_name")!=null)
	                        	  codePixPref.edit().putString("first_name", jObj.getString("first_name")).commit();
	                          if(jObj.getString("last_name")!=null)
	                        	  codePixPref.edit().putString("last_name", jObj.getString("last_name")).commit();
	                          if(jObj.getString("phone")!=null)
	                        	  codePixPref.edit().putString("phone", jObj.getString("phone")).commit();
	                          if(jObj.getString("Birth_date")!=null)
	                        	  codePixPref.edit().putString("Birth_date", jObj.getString("Birth_date")).commit();
	                          if(jObj.getString("gender")!=null)
	                        	  codePixPref.edit().putString("gender", jObj.getString("gender")).commit();
	                          if(jObj.getString("message")!=null)
	                        	  message= jObj.getString("Birth_date");
	                          if(jObj.getString("image_url")!=null)
	                        	  message= jObj.getString("image_url");
	                          
	                          if(jObj.getJSONObject("userinfo") != null)
	  	                        {
	  	                        	JSONObject userInfo=jObj.getJSONObject("userinfo");
	  	                        	
	  	                        	//for(int i=0;i<userInfo.length();i++)
	  	                        	//{
	  	                        	codePixPref.edit().putString("userid",userInfo.getString("userid")).commit();
	  	                        	codePixPref.edit().putString("first_name",userInfo.getString("first_name")).commit();
	  	                        	codePixPref.edit().putString("last_name",userInfo.getString("last_name")).commit();
	  	                        	codePixPref.edit().putString("email",userInfo.getString("email")).commit();
	  	                        	codePixPref.edit().putString("social_id",userInfo.getString("social_id")).commit();
	  	                        	codePixPref.edit().putString("image_url",userInfo.getString("image_url")).commit();
	  	                        	codePixPref.edit().putString("gender",userInfo.getString("gender")).commit();
	  	                        	codePixPref.edit().putString("Birth_date",userInfo.getString("Birth_date")).commit();
	  	                        	codePixPref.edit().putString("login_type",userInfo.getString("login_type")).commit();
	  	                        	codePixPref.edit().putString("profile_page_url",userInfo.getString("profile_page_url")).commit();
	  	                        	
	  	                        	
	  	                        }
	                         
	                        
	                        res[0]=status;
	                        res[1]=message;
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							res[0]=status;
	                        res[1]=message;
							e.printStackTrace();
						}
                      
						 
	                        con.disconnect();
                  return res;
                
                 
                
               // return buffer.toString();
        }
        

        
        private void writeParamData(String paramName, String value) throws Exception {
                
                
                os.write( (delimiter + boundary + "\r\n").getBytes());
                os.write( "Content-Type: text/plain\r\n".getBytes());
                os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
                os.write( ("\r\n" + value + "\r\n").getBytes());
                        
                
        }
}
