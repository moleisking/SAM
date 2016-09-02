package me.minitrabajo;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class Users implements Serializable  {
	
	private static final long serialVersionUID = 8653566573642203226L;
	private ArrayList<User> mUsers = new  ArrayList<User>(0);

	
	public Users()	{	}
	
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
			Log.v("Users:toString()", ex.getMessage());
		}
		return output;
	}

	public Users fromString( String str )
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
			Log.v("Users:fromString()", ex.getMessage());
		}
		return (Users)output;
	}

	public void importUsers(String json)
	{
		Users users = new Users();
		try
		{
			JSONObject data = new JSONObject(json).getJSONObject("data");
			for(int i =0;i < data.getJSONArray("Users").length();i++)
			{
			User user = new User(0,0,
					data.getJSONArray("User").getJSONObject(i).getString("name"),
					data.getJSONArray("User").getJSONObject(i).getString("description"),
					data.getJSONArray("User").getJSONObject(i).getString("email"),
					data.getJSONArray("User").getJSONObject(i).getString("mobile"),
					data.getJSONArray("User").getJSONObject(i).getString("address"),
					Double.parseDouble(data.getJSONArray("User").getJSONObject(i).getString("hour_rate")),
					Double.parseDouble(data.getJSONArray("User").getJSONObject(i).getString("day_rate")));
				this.add(user);
			}
		}
		catch (Exception ex)
		{

		}

	}

	void print()
	{
		Log.v("Users:", "Object Array");
		Log.v("Users:Size", String.valueOf(this.size()));
		for (int j = 0; j < mUsers.size(); j++ )
		{
			mUsers.get(j).print();
		}
	}
}

