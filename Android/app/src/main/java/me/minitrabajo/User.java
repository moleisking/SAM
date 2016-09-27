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

import org.json.JSONObject;

public class User implements Serializable {
		
	private static final long serialVersionUID = -5964311117196182558L;
	private int mId;
	private int mType;
	private String mName;
	private String mMobile;
	private String mEmail;
	private String mDescription;
	private String mAddress;
	private String mCategory;
	private String mTags;
	private Double mHourRate;
	private Double mDayRate;
	private Double mRegisteredLatitude;
	private Double mRegisteredLongitude;
	private Double mCurrentLatitude;
	private Double mCurrentLongitude;
	private Byte[] mImageByteArray = null;
	private String mImageType = "";
	private Bitmap mImage = null;

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

	public String getCategory()
	{
		return mCategory;
	}

	public void setCategory(String category)
	{
		this.mCategory = category;
	}

	public String getTags()
	{
		return mTags;
	}

	public void setTags(String tags)
	{
		this.mTags = tags;
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

	public Bitmap getImage()
	{
		return mImage;
	}

	public void setImage(Bitmap image)
	{
		this.mImage = image;
	}

	public void setImageByteArray(Byte[] image)
	{
		if (!image.equals(null)) { this.mImageByteArray = image; }
	}

	public Byte[] getImageByteArray()
	{
		return mImageByteArray;
	}

	public byte[] getImageAsPrimitive()
	{
		byte[] bytes = new byte[mImageByteArray.length];
		for(int i = 0; i < mImageByteArray.length; i++){
			bytes[i] = mImageByteArray[i];
		}
		return bytes;
	}

	public void setImageFromPrimitive(byte[] bytesPrim)
	{
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim) bytes[i++] = b; //Autoboxing
		mImageByteArray = bytes;
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

	public Bitmap getImageFromRawString(String str)
	{
		//Extract "data:image/png;base64,"
		String header = str.substring(0,20);
		if (header.contains("data")&& header.contains("image")&&header.contains("base64"))
		{
			if (header.contains("png"))
			{
				mImageType = "png";
			}
			else if (header.contains("gif"))
			{
				mImageType = "gif";
			}
			else if (header.contains("bmp"))
			{
				mImageType = "bmp";
			}
			else if (header.contains("jpeg"))
			{
				mImageType = "jpeg";
			}
			else if (header.contains("tiff"))
			{
				mImageType = "tiff";
			}
		}
		else
		{
			Log.v("User","No raw image found");
		}

		//Remove "data:image/png;base64,"
		str = str.replaceFirst("data:image/"+mImageType + "base64,","");

		//Convert base64 to array
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


	public String serializeToString()
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

	public User deserializeFromString( String str )
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

	public void parseJSONtoObject(String json)
	{
		try
		{
			JSONObject data = new JSONObject(json).getJSONObject("data");

			this.mName = data.getString("name");
			this.mDescription = data.getString("description");
			this.mEmail = data.getString("email");
			this.mAddress = data.getString("address");
			this.mMobile = data.getString("mobile");
			//this.mCategory = data.getString("category");
			//this.mTags = data.getString("tags");
			//this.mHourRate = Double.parseDouble(data.getString("hour_rate"));
			//this.mDayRate = Double.parseDouble(data.getString("day_rate"));
			//this.mRegisteredLongitude = Double.parseDouble(data.getString("regLat"));
			//this.mRegisteredLatitude = Double.parseDouble(data.getString("regLng"));
			//this.mCurrentLongitude = Double.parseDouble(data.getString("curLat"));
			//this.mCurrentLatitude = Double.parseDouble(data.getString("curLng"));
			//this.mCredit = Double.parseDouble(data.getString("credit"));
			//this.setImage(this.getImageFromRawString(data.getString("image")));
		}
		catch (Exception ex)
		{

		}
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
