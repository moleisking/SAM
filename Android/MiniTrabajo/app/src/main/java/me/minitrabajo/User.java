package me.minitrabajo;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

//import com.google.android.maps.GeoPoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class User implements Serializable {
		
	private static final long serialVersionUID = -5964311117196182558L;
	private int mId;
	private Double mPrice;
	private String mName;
	private String mText;
	private String mAddress;
	private String mPhone;
	private String mLatitude;
	private String mLongitude;
	private byte[] mImage = null;
		
	public User()
	{
		super();
	}
	
	public User(int ID, String Name, String Text, String Address, String Phone, Double Price)
	{
		mId= ID;
		mName = Name;
		mText = Text;		
		mPhone = Phone;
		mPrice = Price;
	}
		
	//ID
	public int getID() { return mId; }

	public void setID(int a) { this.mId = a; }

	//Name
	public String getName() { return mName; }

	public void setName(String a)
	{
		this.mName = a;
	}

	//Price
	public Double getPrice() { return mPrice; }

	public void setPrice(Double a) { this.mPrice = a; }

	//Tags
	public String getText() { return mText; }

	public void setText(String a)
	{
		this.mText = a;
	}

	//Address
	public String getAddress() { return mAddress; }

	public void setAddress(String a)
	{
		this.mAddress = a;
	}

	//Phone
	public String getPhone()
	{
		return mPhone;
	}

	public void setPhone(String a)
	{
		this.mPhone = a;
	}

	//Latitude & Longitude

	public String getLatitude() { return mLatitude; }

	public void setLatitude(String a)
	{
		this.mLatitude = a;
	}

	public String getLongitude()
	{
		return mLongitude;
	}

	public void setLongitude(String a) { this.mLongitude = a; }

	/*GeoPoint getGeoPoint()
	{
		float lat = Float.valueOf(mLatitude) ;
		float lon = Float.valueOf(mLongitude);
		return new GeoPoint((int)(lat * 1E6) , (int)(lon * 1E6)) ;
	}*/

	//Images
	public Bitmap getImage()
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

	public void setImage(byte[] image)
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

	public void setImage(String str)
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

	public void print()
	{		
		System.out.println("ID:"+this.mId);
		System.out.println("Name:" + this.mName);
		System.out.println("Text:"+this.mText);
		System.out.println("Address:"+this.getAddress());
		System.out.println("Phone:"+this.mPhone);
		System.out.println("Price:"+this.mPrice);
		
	}
	
}
