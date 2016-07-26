package com.LastSecondDeal;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class ItemsDownload extends AsyncTask<Items, Void, Items> {

	private String mUrl;
	private Context mContext;
		
	public ItemsDownload(String url, Context context)
	{
		mUrl = url;
		mContext = context;
	}
	
	@Override
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
	}	
	
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
