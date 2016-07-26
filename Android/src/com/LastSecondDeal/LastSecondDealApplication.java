package com.LastSecondDeal;

import android.app.Application;

public class LastSecondDealApplication extends Application{

	private String WEBSERVICE_IP;
	private String WEBSERVICE_PORT;
	private Items mItems;
		
	public Items getItems()
	{		
		return mItems;
	}
	
	public void setItems(Items items)
	{
		mItems=items;
	}
	
	public void FillItems()
	{
		try
		{
			WEBSERVICE_IP = this.getResources().getString(R.string.webservice_ip);
	        WEBSERVICE_PORT = this.getResources().getString(R.string.webservice_port);  
			mItems = new Items(WEBSERVICE_IP, WEBSERVICE_PORT, this.getApplicationContext());
			mItems.FillFromWebservice(mItems);
		}
		catch (Exception ex)
		{
			System.out.println("LastSecondDealApplication: Webservice unreachable");
		}
	}	
}
