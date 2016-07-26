package com.LastSecondDeal;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;

public class Items implements Serializable  {
	
	private static final long serialVersionUID = 8653566573642203226L;
	public ArrayList<Item> Items = new  ArrayList<Item>(0);
	private String mUrl=""; //"http://192.168.1.10:8080/LastSecondDealWebService/LastSecondDealWebService.php?f=getdeal&u=webservice"
	private String result = "";
	private Context mContext;
	
	public Items(String ip, String port, Context context)
	{
		mUrl = "http://" +ip +":" + port + "/LastSecondDealWebService/LastSecondDealWebService.php?f=getdeal&u=webservice";
		mContext = context;
	}
	
	public Item get(int index)
	{
		return Items.get(index);
	}
	
	public void add(Item q)
	{
		try
		{
			Items.add(q);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public void remove(Item q)
	{
		Items.remove(q);
	}
	
	public int size()
	{
		return Items.size();
	}
	
	void print()
	{
		System.out.println("Printing mItems");
		System.out.println("Items Size:" + this.size());
		for (int j = 0; j < Items.size(); j++ )
		{
			System.out.println("-------------");			
			Items.get(j).print();
			System.out.println("-------------");
		}	
		System.out.println("Result:" + result);
		
	}
	
	void FillFromXML()
	{
		

	}
	
	
	
	public Items FillFromWebservice(Items mItems)
	{			
		ItemsDownload itemsDownLoad = new ItemsDownload(mUrl, mContext);
		itemsDownLoad.execute(new Items[] { mItems });
		
		try
		{
			mItems = itemsDownLoad.get();
			//System.out.println("Print Result From HTTP:"+result);
		}
		catch (Exception ex)
		{
			System.out.println("FillFromWebservice" + ex.getMessage());
		}	
		
		//mItems.print();
		return mItems;
	}
	
	
	
	
}

