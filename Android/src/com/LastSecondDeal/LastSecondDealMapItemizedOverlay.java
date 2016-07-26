package com.LastSecondDeal;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.*;

public class LastSecondDealMapItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
		
	public LastSecondDealMapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
		
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	public void removeItem(int i){
        mOverlays.remove(i);
        populate();
    }
	
	public void clear()
    {
        mOverlays.clear();
        populate();
    }

	@Override
	public int size() {		
		return mOverlays.size();
	}
	
	public void addOverlayItem(OverlayItem overlayItem) {		
		mOverlays.add(overlayItem);
        populate();
    }


    public void addOverlayItem(int lat, int lon, String title) {
        try {
            GeoPoint point = new GeoPoint(lat, lon);
            OverlayItem overlayItem = new OverlayItem(point, title, null);
            addOverlayItem(overlayItem);    
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  System.out.print("Click"+item.getTitle()+ "," + item.getSnippet());	 
	  return true;
	}
}
