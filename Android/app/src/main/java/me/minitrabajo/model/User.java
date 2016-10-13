package me.minitrabajo.model;
/*
* Bitmap class is non serializable amd therefore not used
* */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import me.minitrabajo.view.RoundImage;

public class User implements Serializable {
		
	private static final long serialVersionUID = -5964317817196182551L;
	private int id=0;
	private int score = 0;
	private int credit = 0;
	private String name="";
	private String mobile="";
	private String email="";
	private String description="";
	private String address="";
	private String category="";
	private String tags="";
	private Double hourRate = 10d;
	private Double dayRate = 200d;
	private Double registeredLatitude = 40.431075;
	private Double registeredLongitude = -3.702203;
	private Double currentLatitude = 40.431075;
	private Double currentLongitude = -3.702203;
	private String imageRaw = "";
	private transient Context context;

	public User(Context context)
	{
		super();
		context = context;
	}

	public User(int id, String name)
	{
		this.id= id;
		this.name = name;
	}

	public User(int id, String name,  String description,  String email, String mobile, String address,
				Double hourRate, Double dayRate, Double currentLatitude,  Double currentLongitude, Double registeredLatitude , Double registeredLongitude )
	{
		this.id= id;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.description = description;
		this.hourRate = hourRate;
		this.dayRate = dayRate;
		this.currentLatitude =  currentLatitude;
		this.currentLongitude = currentLongitude;
		this.registeredLatitude = registeredLatitude;
		this.registeredLongitude = registeredLongitude;
	}

	public User(int id, String imageRaw , String name,  String description,  String email, String mobile, String address,
				Double hourRate, Double dayRate, Double currentLatitude,  Double currentLongitude, Double registeredLatitude , Double registeredLongitude )
	{
		this.id= id;
		this.imageRaw = imageRaw;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.description = description;
		this.hourRate = hourRate;
		this.dayRate = dayRate;
		this.currentLatitude =  currentLatitude;
		this.currentLongitude = currentLongitude;
		this.registeredLatitude = registeredLatitude;
		this.registeredLongitude = registeredLongitude;
	}

	public int getID()
	{
		return id;
	}
	public void setID(int id)
	{
		this.id = id;
	}
	public int getScore()
	{
		return score;
	}
	public void setScore(int id)
	{
		this.score = score;
	}
	public int getCredit()
	{
		return credit;
	}
	public void setCredit(int credit)
	{
		this.credit = credit;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public double getHourRate()
	{
		return hourRate;
	}
	public void setHourRate(Double hour_rate)
	{
		this.hourRate = hour_rate;
	}
	public double getDayRate()
	{
		return dayRate;
	}
	public void setDayRate(Double day_rate)
	{
		this.dayRate = day_rate;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getTags()
	{
		return tags;
	}
	public void setTags(String tags)
	{
		this.tags = tags;
	}
	public LatLng getRegisteredLatLng()
	{
		return new LatLng(registeredLatitude, registeredLatitude);
	}
	public void setRegisteredLatLng(LatLng latLng)
	{
		this.registeredLongitude = latLng.longitude;
		this.registeredLatitude = latLng.latitude;
	}
	public void setRegisteredLatLng(Double registeredLatitude, Double registeredLongitude)
	{
		this.registeredLongitude = registeredLongitude;
		this.registeredLatitude = registeredLatitude;
	}
	public LatLng  getCurrentLatLng()
	{
		return new LatLng(currentLatitude, currentLatitude);
	}
	public void setCurrentLatLng(LatLng latLng )
	{
		this.currentLongitude = latLng.longitude;
		this.currentLatitude = latLng.latitude;
	}
	public void setCurrentLatLng(Double currentLatitude, Double currentLongitude )
	{
		this.currentLongitude = currentLongitude;
		this.currentLatitude = currentLatitude;
	}
	public void setContext(Context context)
	{
		this.context = context;
	}
	public String getImageRaw()
	{
		return imageRaw;
	}
	public void setImageRaw(String rawImage)
	{
		String header = rawImage.substring(0,25);
		if (header.contains("data")&& header.contains("image")&&header.contains("base64")) {
			this.imageRaw = rawImage;
		}
		else
		{
			Log.v("User","Raw image reprocessed");
		}
	}
	public void setImageRaw(Bitmap bitmap)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
		this.imageRaw = "data:image/png;base64," + imageString;
	}
	public String getImageEncoded()
	{
		//Extract XXX from "data:image/png;base64,XXX"
		return imageRaw.split(",",2)[1];
	}
	public String getImageType()
	{
		String header = imageRaw.substring(0,20);
		String type = "";
		if (header.contains("png"))
		{
			type = "png";
		}
		else if (header.contains("gif"))
		{
			type ="gif";
		}
		else if (header.contains("bmp"))
		{
			type ="bmp";
		}
		else if (header.contains("jpeg"))
		{
			type ="jpeg";
		}
		else if (header.contains("tiff"))
		{
			type ="tiff";
		}
		return type;
	}

	public Bitmap getImageAsBitmap()
	{
		//gets string from encoded data of raw image
		byte[] arr = Base64.decode(this.getImageEncoded(), Base64.DEFAULT);
		ByteArrayInputStream inputStream = new ByteArrayInputStream (arr);
		return BitmapFactory.decodeByteArray(arr,0,arr.length);
	}

	public RoundImage getImageAsRoundedBitmap()
	{
		RoundImage roundedImage = new RoundImage(getImageAsBitmap());
		return roundedImage;
	}

	public String saveToString()
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

	public void loadFromString( String str )
	{
		Object output = null;
		try
		{
			byte [] data = Base64.decode( str,0 );
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
			User u  = (User)ois.readObject();
			ois.close();

			//Copy data to this object
			this.setName(u.getName());
			this.setDescription(u.getDescription());
			this.setEmail(u.getEmail());
			this.setAddress(u.getAddress());
			this.setMobile(u.getMobile());
			this.setCategory(u.getCategory());
			this.setTags(u.getTags());
			this.setHourRate(u.getHourRate());
			this.setDayRate(u.getDayRate());
			this.setRegisteredLatLng(u.getRegisteredLatLng());
			this.setImageRaw(u.getImageRaw());
		}
		catch (Exception ex)
		{
			Log.v("User:fromString()", ex.getMessage());
		}
	}

	public void loadFromJSON(String json)
	{
		try
		{
			JSONObject data = new JSONObject(json).getJSONObject("data");

			//this.id= data.getString("id");
			this.name = data.getString("name");
			this.description = data.getString("description");
			this.email = data.getString("email");
			this.address = data.getString("address");
			this.mobile = data.getString("mobile");
			this.category = data.getString("category");
			this.tags = data.getString("tags");
			this.hourRate = Double.parseDouble(data.getString("hourRate"));
			this.dayRate = Double.parseDouble(data.getString("dayRate"));
			this.registeredLongitude = Double.parseDouble(data.getString("regLat"));
			this.registeredLatitude = Double.parseDouble(data.getString("regLng"));
			this.currentLongitude = Double.parseDouble(data.getString("curLat"));
			this.currentLatitude = Double.parseDouble(data.getString("curLng"));
			this.credit = Integer.parseInt(data.getString("credit"));
			this.score = Integer.parseInt(data.getString("score"));
			this.setImageRaw(data.getString("image"));
		}
		catch (Exception ex)
		{

		}
	}

	public void print()
	{
		Log.v("User", "Object");
		Log.v("ID", String.valueOf(id));
		Log.v("Name" , this.name);
		Log.v("Description" , this.description);
		Log.v("Address", this.address);
		Log.v("Mobile", this.mobile);
		Log.v("HourRate",String.valueOf(this.hourRate));
		Log.v("DayRate",String.valueOf(this.dayRate));
		Log.v("CurrentLongitude",String.valueOf(this.currentLongitude));
		Log.v("CurrentLatitude",String.valueOf(this.currentLatitude));
		Log.v("RegisteredLongitude",String.valueOf(this.registeredLongitude));
		Log.v("RegisteredLatitude",String.valueOf(this.registeredLatitude));
		Log.v("Credit",String.valueOf(this.credit));
		Log.v("Score",String.valueOf(this.score));
		Log.v("Image",this.imageRaw);
	}
	
}
