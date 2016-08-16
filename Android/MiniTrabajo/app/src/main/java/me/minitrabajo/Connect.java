/*    -Example of Authenticate-
   POST /authenticate HTTP/1.1
   Host: localhost:3003
   Cache-Control: no-cache
   Postman-Token: 87497dd5-0cf0-375f-213f-83818a54a1a0
   Content-Type: application/x-www-form-urlencoded
   name=scott&pass=12345&email=moleisking%40gmail.com
   */

package me.minitrabajo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
//import com.google.common.net.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class Connect { //extends AsyncTask<Items, Void, Items>

	private String POST_SIGNUP = "http://192.168.1.10/signup";
    private String POST_AUTHENTICATE = "http://192.168.1.10/signup";
    private String POST_SEARCH = "http://192.168.1.10/search";
    private Context mContext;

	public  Connect(Context context)
	{
		//mUrl = url;
		mContext = context;
	}

    protected void Post(String url , String parameters){
        String ret;
        try {
            String urlParameters  = parameters; //"param1=a&param2=b&param3=c";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            URL purl = new URL(POST_AUTHENTICATE);

            //Set headers and connect to URL
            HttpURLConnection connection = (HttpURLConnection) purl.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();

            //Output parameters to node.js server
            DataOutputStream wr = new DataOutputStream( connection.getOutputStream());
            wr.write( postData );

        }
        catch (Exception ex)
        {
            System.out.println("BackendConnection:Post" + ex.getMessage());
        }
    }

    protected String Get(String url)
    {
        String ret = "";
        try {
            URL gurl = new URL(POST_AUTHENTICATE);
            HttpURLConnection connection = (HttpURLConnection) gurl.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();

        }
        catch (Exception ex)
        {
            System.out.println("BackendConnection:Get" + ex.getMessage());
        }

        return ret;
    }

	
	/*@Override
	protected Items doInBackground(Items... arrItems) {
		
		Items mItems = arrItems[0]; //Assume only one in the array, put some protection code here later
				
		try{
			//Get HTML
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpRequest = new HttpGet(mUrl);
			HttpResponse httpResponse = httpClient.execute(httpRequest);			
						
		 	//Get XML
		 	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(httpResponse.getEntity().getContent());
			Element root = dom.getDocumentElement();
	        NodeList items = root.getElementsByTagName("Item");
	        
	        for (int i=0;i<items.getLength();i++){                
	            Node item = items.item(i);
	            NodeList properties = item.getChildNodes();
	            Item mItem = new Item();
	            for (int j=0;j<properties.getLength();j++){
	                Node property = properties.item(j);
	                String name = property.getNodeName();
	                if (name.equalsIgnoreCase("id")){
	                   mItem.setID(Integer.valueOf( property.getFirstChild().getNodeValue())); 
	                } 
	                else if (name.equalsIgnoreCase("name")){
	                	mItem.setName( property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("text")){
	                	mItem.setText( property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("Phone")){
	                	mItem.setPhone( property.getFirstChild().getNodeValue());
	                } 	               
	                else if (name.equalsIgnoreCase("Price")){
	                	mItem.setPrice(Double.parseDouble( property.getFirstChild().getNodeValue()));
	                }
	                else if (name.equalsIgnoreCase("Image")){
	                	mItem.setImage( property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("HouseNumber")){
	                	mItem.setHouseNumber(property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("Street")){
	                	mItem.setStreet(property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("City")){
	                	mItem.setCity(property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("Country")){
	                	mItem.setCountry(property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("Longitude")){
	                	mItem.setLongitude(property.getFirstChild().getNodeValue());
	                }
	                else if (name.equalsIgnoreCase("Latitude")){
	                	mItem.setLatitude( property.getFirstChild().getNodeValue());
	                } 	                
	            }                
	            mItems.add(mItem);               
	        }
	        			
		}
		catch (IOException ex)
		{
			System.out.println("IOException ItemsDownload:" + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception ItemsDownload:" + ex.getMessage());
		}
		return mItems;		
	}
	
	@Override
	protected void onPostExecute(Items result) {
		//textView.setText(result);		
	}	*/
	
	public void ShowError(String msg)
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    	builder.setMessage(msg);
    	builder.setCancelable(true);
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            }
        });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
