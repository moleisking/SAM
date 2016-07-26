package com.LastSecondDeal;
//http://mobiforge.com/developing/story/using-google-maps-android
/*
//Generate New Key
keytool -genkey -alias LastSecondDeal -keyalg RSA -keystore LastSecondDeal.jks

//Use Debug key
keytool.exe -list -alias androiddebugkey -keystore "C:\Users\Scott.Johnston\.android\debug.keystore" -storepass android -keypass android

http://maps.google.com/maps/geo?key=0RJtbsGnGUTPOglGzRT5sJW0QnC4rY3n92eC4dw&output=xml&q=Calle+La+Cruz+3,+Madrid,+Spain
http://maps.google.com/maps/geo?key=0RJtbsGnGUTPOglGzRT5sJW0QnC4rY3n92eC4dw&output=csv&q=Calle+La+Cruz+3,+Madrid,+Spain

//Debug Certificate
Certificado (MD5): BC:AA:56:5E:CA:3D:FB:F4:E5:1B:86:35:00:36:5B:FD
Google Map API Key-0RJtbsGnGUTPOglGzRT5sJW0QnC4rY3n92eC4dw
keytool -list -alias LastSecondDeal -keystore LastSecondDeal.keystore

  * Keystore name: "debug.keystore"
  * Keystore password: "android"
  * Key alias: "androiddebugkey"
  * Key password: "android"
  * CN: "CN=Android Debug,O=Android,C=US"
*/

import com.google.android.maps.*;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class LastSecondDealMapActivity extends MapActivity   {

	private MapView mapView;		
	private Items mItems;		
	private LastSecondDealMapItemizedOverlay mOverlayItems;
	private OverlayItem mOverlayItem;	
	private MyLocationOverlay mLocationOverlayItem;
	private Drawable MARKER_COMMON;	
	
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.last_second_deal_map_activity);
	    
	    //Define Final	           
        MARKER_COMMON = this.getResources().getDrawable(R.drawable.iconr);
               
	    //Define Objects	   
        mapView = (MapView) findViewById(R.id.mapView);
               
        //Set objects
        mOverlayItems = new LastSecondDealMapItemizedOverlay(MARKER_COMMON, this);
               
        //Set My Location
        mLocationOverlayItem = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(mLocationOverlayItem);
        mLocationOverlayItem.enableCompass();
        mLocationOverlayItem.enableMyLocation();
        mLocationOverlayItem.runOnFirstFix(new Runnable() {
            public void run() {
            	mapView.getController().animateTo(mLocationOverlayItem.getMyLocation());           	
            }
        });
                                    
        //Get Data and Load data into list                       
        try
        {          	
        	mItems = ((LastSecondDealApplication)this.getApplication()).getItems();        	
    	} 
        catch (Exception ex )
        {
        	System.out.println("LastSecondDealMapActivity:Problem loading list items," + ex.getMessage());
        }   	        	
    	
        //Add Points to map
    	try
    	{		
    		//Load Points on Map
    		for (int i = 0; i < mItems.size() ; i++)
    		{    		
    			float lat = Float.valueOf(mItems.get(i).getLatitude()) ;
    			float lon = Float.valueOf(mItems.get(i).getLongitude());    		    		
    			GeoPoint point = new GeoPoint((int)(lat * 1E6) , (int)(lon * 1E6));     				
    			mOverlayItem = new OverlayItem (point, "Here", null);      		    		
    			mOverlayItems.addOverlay(mOverlayItem);    		
    		}  
    	  
    		//mOverlayItems.addOverlay(mLocationOverlayItem);
    		mapView.getOverlays().add(mOverlayItems);
    		mapView.setBuiltInZoomControls(true);
            mapView.displayZoomControls(true);
            mapView.getController().setZoom(17);  
              		
    	} 
    	catch (Exception ex) 
    	{
    		System.out.println("LastSecondDealMapviewActivity:"+ex.getMessage());	
    	}          
	}
	
	//**************************
	// Implement MapView
	//**************************
	
	protected boolean isRouteDisplayed() 
	{		
		return false;
	}		
}
