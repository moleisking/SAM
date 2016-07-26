package com.LastSecondDeal;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import com.google.android.maps.GeoPoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Item implements Serializable {
		
	private static final long serialVersionUID = -5964311117196182558L;
	public int mId;
	public Double mPrice;	
	public String mName;
	public String mText;
	public String mStreet;
	public String mHouseNumber;
	public String mCity;
	public String mState;
	public String mCountry;
	public String mPhone;	
	public String mLatitude;
	public String mLongitude;
	public byte[] mImage = null;
		
	public Item()
	{
		super();
	}
	
	public Item(int ID, String Name, String Text, String Address, String Phone, Double Price)
	{
		mId= ID;
		mPrice = Price;
		mName = Name;
		mText = Text;		
		mPhone = Phone;
	}
		
	//ID
	int getID()
	{
		return mId;
	}
	
	void setID(int a)
	{
		this.mId = a;
	}
	
	Double getPrice()
	{
		return mPrice;
	}
	
	void setPrice(Double a)
	{
		this.mPrice = a;
	}
	
	String getName()
	{
		return mName;
	}
	
	void setName(String a)
	{
		this.mName = a;
	}
	
	String getText()
	{
		return mText;
	}
	
	void setText(String a)
	{
		this.mText = a;
	}
	
	String getAddress()
	{
		return mStreet + mHouseNumber + "," + mCity + "," + mCountry;
	}
			
	String getHouseNumber()
	{
		return mHouseNumber;
	}
	
	void setHouseNumber(String a)
	{
		this.mHouseNumber = a;
	}
	
	String getStreet()
	{
		return mStreet;
	}
	
	void setStreet(String a)
	{
		this.mStreet = a;
	}
	
	String getCity()
	{
		return mCity;
	}
	
	void setCity(String a)
	{
		this.mCity = a;
	}
	
	String getState()
	{
		return mState;
	}
	
	void setState(String a)
	{
		this.mState = a;
	}
	
	String getCountry()
	{
		return mCountry;
	}
	
	void setCountry(String a)
	{
		this.mCountry = a;
	}
	
	
	String getPhone()
	{
		return mPhone;
	}
	
	void setPhone(String a)
	{
		this.mPhone = a;
	}
	
	String getLatitude()
	{
		return mLatitude;
	}
		
	void setLatitude(String a)
	{
		this.mLatitude = a;
	}
	
	String getLongitude()
	{
		return mLongitude;
	}
	
	GeoPoint getGeoPoint()
	{
		float lat = Float.valueOf(mLatitude) ;
		float lon = Float.valueOf(mLongitude);		    		
		return new GeoPoint((int)(lat * 1E6) , (int)(lon * 1E6)) ;
	}
	
	void setLongitude(String a)
	{
		this.mLongitude = a;
	}
	
	Bitmap getImage()
	{
		if (!mImage.equals(null))
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream (mImage);
			Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
			System.out.println("Image Found:getImage:ID:" + mId);
			return mBitmap;
		}
		else
		{
			System.out.println("Image Not Found:getImage");
			return null;
		}
	}
	
	void setImage(byte[] image)
	{
		if (!image.equals(null))
		{
			this.mImage = image;
			System.out.println("Image Found:setImage:ID:" + mId);
		}
		else
		{
			System.out.println("Image Not Found:setImage");
		}
	}
	
	void setImage(String str)
	{
		if (!str.equals(null) || str !="")
		{
			try
			{				
				this.mImage = Base64.decode(str, Base64.DEFAULT);				  		
				System.out.println("Image Found:setImage:ID:" + mId);
			}
			catch (Exception ex)
			{
				System.out.println("setImage error for string:"+ex.getMessage());
			}
			
			
		}
		else
		{
			System.out.println("Image Not Found:setImage");
		}
	}
	
	void print()
	{		
		System.out.println("ID:"+this.mId);
		System.out.println("Name:" + this.mName);
		System.out.println("Text:"+this.mText);
		System.out.println("Address:"+this.getAddress());
		System.out.println("Phone:"+this.mPhone);
		System.out.println("Price:"+this.mPrice);
		
	}
	
}
