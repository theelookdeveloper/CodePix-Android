package com.codepix.utilz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONObject getJSONFromUrl(Map<String, String> map) {
        
    	String baseurl=Global.url+"?test=test";
    	Iterator it = map.entrySet().iterator();
        
        System.out.println("URL in JSON Parsing"+baseurl);
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            baseurl=baseurl+"&"+pairs.getKey() + "=" + pairs.getValue();
            
            it.remove(); // avoids a ConcurrentModificationException
        }
    	
    	URL url = null;
		try {
			url = new URL(baseurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        URI uri = null;
		try {
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			baseurl = uri.toURL().toString();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("Url in json parser:-"+baseurl);
        // Making HTTP request
    	
       
    	
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(baseurl);
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
           // httpPut.setEntity(new StringEntity(body, HTTP.UTF_8));
           // httpPost.setEntity(new StringEntity(body, HTTP.UTF_8));
            Log.d("JSONParser", "response code"+httpResponse.getStatusLine().getStatusCode());
           if (httpResponse != null && httpResponse.getStatusLine().getStatusCode()==200) {
        	   is = httpEntity.getContent(); 
        	   
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
              Log.d("JSONParser", "response from server"+json);
               // try parse the string to a JSON object
               try {
                   jObj = new JSONObject(json);
               } catch (JSONException e) {
                   Log.e("JSON Parser", "Error parsing data " + e.toString());
               }
            }
           else
           {
        	   JSONObject jo = new JSONObject();
        	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   

        	  
        	   return jObj; 
           }
        	   
 
        } catch (UnsupportedEncodingException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        } catch (ClientProtocolException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        } catch (IOException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        }
 
       
 
        // return JSON String
        return jObj;

    	
        
 
    }

	public JSONObject sendJSONFromUrl(String userid, String json) {
		String baseurl=Global.url+"?test=test";
    	//Iterator it = map.entrySet().iterator();
        
            baseurl=baseurl+"&userid=" + userid+"&uploadfriend=1";
        
    	
    	URL url = null;
		try {
			url = new URL(baseurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        URI uri = null;
		try {
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			baseurl = uri.toURL().toString();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("Url in json parser:-"+baseurl);
        // Making HTTP request
    	
       
    	
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(baseurl);
            
            
           
            
          /*  httpPost.setHeader("Accept", "application/json");
            StringEntity se = new StringEntity("json="+test);
            
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            
            System.out.println("JSON:-"+json.toString());
            httpPost.setEntity(se);*/
       
            //httpPost.setHeader("Content-type", "application/json");
            
            StringEntity params =new StringEntity("json="+json);
            httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(params);
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
          //  httpPost.setEntity(new StringEntity(body, HTTP.UTF_8));
          // httpPost.setEntity(new StringEntity(body, HTTP.UTF_8));
            Log.d("JSONParser", "response code"+httpResponse.getStatusLine().getStatusCode());
           if (httpResponse != null && httpResponse.getStatusLine().getStatusCode()==200) {
        	   is = httpEntity.getContent(); 
        	   
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
              Log.d("JSONParser", "response from server"+json);
               // try parse the string to a JSON object
               try {
                   jObj = new JSONObject(json);
               } catch (JSONException e) {
                   Log.e("JSON Parser", "Error parsing data " + e.toString());
               }
            }
           else
           {
        	   JSONObject jo = new JSONObject();
        	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   

        	  
        	   return jObj; 
           }
        	   
 
        } catch (UnsupportedEncodingException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        } catch (ClientProtocolException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        } catch (IOException e) {
        	JSONObject jo = new JSONObject();
     	   try {
				jo.put("newsid", 0);
				
				 JSONArray ja = new JSONArray();
	        	   ja.put(jo);

	        	   //JSONObject mainObj = new JSONObject();
	        	   jObj.put("response", ja);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				jObj=null;
			}
     	   

     	  
     	   return jObj; 
        }
 
       
 
        // return JSON String
        return jObj;

    	
	}
}
