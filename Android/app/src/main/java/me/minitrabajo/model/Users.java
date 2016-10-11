package me.minitrabajo.model;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Users implements Serializable  {

	private static final String USERS_FILE_NAME = "users.dat";
	private static final long serialVersionUID = 8653566573642203221L;
	private ArrayList<User> users = new  ArrayList<User>(0);
	transient Context context;
	
	public Users(Context context)	{ this.context= context;	}
	
	public User get(int index)
	{
		return users.get(index);
	}
	
	public void add(User q)
	{
		try
		{
			users.add(q);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public void remove(User u)
	{
		users.remove(u);
	}
	
	public int size()
	{
		return users.size();
	}

	public ArrayList getArrayList()
	{
		return users;
	}

	protected void setArrayList(ArrayList<User> users)
	{
		this.users = users;
	}

	public boolean hasFile()
	{
		boolean result = false;
		try
		{
			File file = context.getFileStreamPath(USERS_FILE_NAME);
			if(file == null || !file.exists()) {
				result= false;
			}
			else
			{
				result = true;
			}
		}
		catch (Exception ioex)
		{
			result = false;
			Log.v("UserAccount",ioex.getMessage());
		}
		finally
		{
			return result;
		}
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

	public void loadFromString( String str )
	{
		try
		{
			byte [] data = Base64.decode( str,0 );
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
			Users u  = (Users)ois.readObject();
			ois.close();

			//Copy data to this object
			this.setArrayList(u.getArrayList());

		}
		catch (Exception ex)
		{
			Log.v("Users:fromString()", ex.getMessage());
		}
	}

	public void loadFromJSON(String json)
	{
		try
		{
			JSONArray users = new JSONObject(json).getJSONArray("search");
			for(int i =0;i < users.length();i++)
			{
				JSONObject userJSON = users.getJSONObject(i);
				User user = new User(0,
						userJSON.getString("name"),
						userJSON.getString("description"),
						userJSON.getString("email"),
						userJSON.getString("mobile"),
						userJSON.getString("address"),
						Double.parseDouble(userJSON.getString("hourRate")),
						Double.parseDouble(userJSON.getString("dayRate")),
						Double.parseDouble(userJSON.getString("regLat")),
						Double.parseDouble(userJSON.getString("regLng")),
						Double.parseDouble(userJSON.getString("curLat")),
						Double.parseDouble(userJSON.getString("curLng")));
				this.add(user);
			}
		}
		catch (Exception ex)
		{
			Log.v("Users:loadFromJSON()", ex.getMessage());
		}

	}

	void print()
	{
		try{
			Log.v("Users", "Object");
			Log.v("Users Size", String.valueOf(this.size()));
			for(int i = 0; i < users.size(); i++)
			{
				Log.v("Category" ,  users.get(i).getID() +":"+ users.get(i).getName());
				if (i==3){break;}
			}
		} catch (Exception ex){Log.v("Users:Err:print", ex.getMessage());}
	}
}

