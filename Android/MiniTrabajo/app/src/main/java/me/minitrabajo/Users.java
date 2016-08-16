package me.minitrabajo;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;

public class Users implements Serializable  {
	
	private static final long serialVersionUID = 8653566573642203226L;
	private ArrayList<User> mUsers = new  ArrayList<User>(0);
	private String mUrl=""; //"http://192.168.1.10:8080/LastSecondDealWebService/LastSecondDealWebService.php?f=getdeal&u=webservice"
	private String result = "";
	private Context mContext;
	
	public Users(String ip, String port, Context context)
	{
		mUrl = "http://" +ip +":" + port + "/LastSecondDealWebService/LastSecondDealWebService.php?f=getdeal&u=webservice";
		mContext = context;
	}
	
	public User get(int index)
	{
		return mUsers.get(index);
	}
	
	public void add(User q)
	{
		try
		{
			mUsers.add(q);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public void remove(User q)
	{
		mUsers.remove(q);
	}
	
	public int size()
	{
		return mUsers.size();
	}

	public ArrayList getUsers()
	{
		return mUsers;
	}

	void print()
	{
		System.out.println("Printing mItems");
		System.out.println("Items Size:" + this.size());
		for (int j = 0; j < mUsers.size(); j++ )
		{
			System.out.println("-------------");			
			mUsers.get(j).print();
			System.out.println("-------------");
		}	
		System.out.println("Result:" + result);
		
	}
	
	void FillFromXML()
	{
		

	}
	
	
	
	/*public Users FillFromWebservice(Users mItems)
	{			
		ItemsDownload itemsDownLoad = new ItemsDownload(mUrl, mContext);
		itemsDownLoad.execute(new Users[] { mItems });
		
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
	}*/
	
	
	
	
}

