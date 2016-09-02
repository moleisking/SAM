package me.minitrabajo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//import com.google.android.maps.GeoPoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;

public class User implements Serializable {
		
	private static final long serialVersionUID = -5964311117196182558L;
	private int mId;
	private int mType;
	private String mName;
	private String mMobile;
	private String mEmail;
	private String mDescription;
	private String mAddress;
	private Double mHourRate;
	private Double mDayRate;
	private Double mRegisteredLatitude;
	private Double mRegisteredLongitude;
	private Double mCurrentLatitude;
	private Double mCurrentLongitude;
	private Byte[] mImage = null;

	public static final int PROFILE_ACCOUNT = 0, PROFILE_RESULT = 1;
		
	public User()
	{
		super();
	}

	public User(int id, int type, String name,  String description,  String email, String mobile, String address,
				Double hour_rate, Double day_rate )
	{
		this.mId= id;
		this.mType= type;
		this.mName = name;
		this.mMobile = mobile;
		this.mEmail = email;
		this.mDescription = description;
		this.mHourRate = hour_rate;
		this.mDayRate = day_rate;
	}

	public User(int id, int type, String name,  String description,  String email, String mobile, String address,
				Double hour_rate, Double day_rate, Double registered_latitude, Double registered_longitude, Double current_latitude, Double current_longitude )
	{
		this.mId= id;
		this.mType= type;
		this.mName = name;
		this.mMobile = mobile;
		this.mEmail = email;
		this.mDescription = description;
		this.mHourRate = hour_rate;
		this.mDayRate = day_rate;
		this.mCurrentLatitude =  current_latitude;
		this.mCurrentLongitude = current_longitude;
		this.mRegisteredLatitude = registered_latitude;
		this.mRegisteredLongitude = registered_longitude;
	}

	public User(String string)
	{
		super();
	}

	public int getID()
	{
		return mId;
	}

	public void setID(int id)
	{
		this.mId = id;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public int getType()
	{
		return mType;
	}

	public void setType(int type)
	{
		this.mType = type;
	}

	public String getEmail()
	{
		return mEmail;
	}

	public void setEmail(String email)
	{
		this.mEmail = email;
	}

	public double getHourRate()
	{
		return mHourRate;
	}

	public void setHourRate(Double hour_rate)
	{
		this.mHourRate = hour_rate;
	}

	public double getDayRate()
	{
		return mDayRate;
	}

	public void setDayRate(Double day_rate)
	{
		this.mDayRate = day_rate;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String description)
	{
		this.mDescription = description;
	}

	public String getAddress()
	{
		return mAddress;
	}

	public void setAddress(String address)
	{
		this.mAddress = address;
	}
	public String getMobile()
	{
		return mMobile;
	}

	public void setMobile(String mobile)
	{
		this.mMobile = mobile;
	}

	public double getRegisteredLatitude()
	{
		return mRegisteredLatitude;
	}

	public void setRegisteredLatitude(Double registered_latitude)
	{
		this.mRegisteredLatitude = registered_latitude;
	}

	public double getRegisteredLongitude()
	{
		return mRegisteredLongitude;
	}

	public void setRegisteredLongitude(Double registered_longitude)
	{
		this.mRegisteredLongitude = registered_longitude;
	}

	public double getCurrentLatitude()
	{
		return mCurrentLatitude;
	}

	public void setCurrentLatitude(Double current_latitude)
	{
		this.mCurrentLatitude = current_latitude;
	}

	public double getCurrentLongitude()
	{
		return mCurrentLongitude;
	}

	public void setCurrentLongitude(Double current_longitude)
	{
		this.mCurrentLongitude = current_longitude;
	}

	public void setImage(Byte[] image)
	{
		if (!image.equals(null)) { this.mImage = image; }
	}

	public Byte[] getImage()
	{
		return mImage;
	}

	public byte[] getImageAsPrimitive()
	{
		byte[] bytes = new byte[mImage.length];
		for(int i = 0; i < mImage.length; i++){
			bytes[i] = mImage[i];
		}
		return bytes;
	}

	public void setImageFromPrimitive(byte[] bytesPrim)
	{
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim) bytes[i++] = b; //Autoboxing
		mImage = bytes;
	}

	public Bitmap getImageAsBitmap()
	{
		ByteArrayInputStream inputStream = new ByteArrayInputStream (this.getImageAsPrimitive());
		Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
		return mBitmap;
	}

	public Bitmap getImageFromBase64(String str)
	{
		byte[] arr = Base64.decode(str, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(arr,0,arr.length);
	}


	public void setImageAsBase64(String str)
	{
		setImageFromPrimitive(Base64.decode(str, Base64.DEFAULT));
	}

	/*Barcode.GeoPoint getRegisteredGeoPoint()
	{
		double lat = mRegisteredLatitude ;
		double lon = mRegisteredLongitude;
		return new Barcode.GeoPoint((int)(lat * 1E6) , (int)(lon * 1E6)) ;
	}*/


	public String toString()
	{
		String output = "";
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.close();
			output = Base64.encodeToString(baos.toByteArray(),0);
		}
		catch (Exception ex)
		{
			Log.v("User:toString()", ex.getMessage());
		}
		return output;
	}

	public User fromString( String str )
	{
		Object output = null;
		try
		{
			byte [] data = Base64.decode( str,0 );
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
			output  = ois.readObject();
			ois.close();
		}
		catch (Exception ex)
		{
			Log.v("User:fromString()", ex.getMessage());
		}
		return (User)output;
	}


	public void print()
	{
		Log.v("User:", "Object");
		Log.v("ID:", String.valueOf(mId));
		Log.v("Name:" , this.mName);
		Log.v("Description:" , this.mDescription);
		Log.v("Address:", this.mAddress);
		Log.v("Mobile:", this.mMobile);
		Log.v("HourRate:",String.valueOf(this.mHourRate));
		Log.v("DayRate:",String.valueOf(this.mDayRate));
		Log.v("CurrentLongitude:",String.valueOf(this.mCurrentLongitude));
		Log.v("CurrentLatitude:",String.valueOf(this.mCurrentLatitude));
		Log.v("RegisteredLongitude:",String.valueOf(this.mRegisteredLongitude));
		Log.v("RegisteredLatitude:",String.valueOf(this.mRegisteredLatitude));
	}
	
}
