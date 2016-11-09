package me.minitrabajo.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Users implements Serializable  {

	public static final String USERS_FILE_NAME = "users.dat";
	private static final long serialVersionUID = 8653566573642203221L;
	private List<User> users;
	//private transient Context context;
	
	public Users()
	{
        //this.context= context;
		users = new  ArrayList<User>(0);
    }

	public Users( User[] user)
	{
		this.users = new  ArrayList<User>(2);
		for (int i =0; i < user.length;i++) {
			this.users.add(user[i]);
		}
	}

	public Users ( Users users)
	{
		this.users = new  ArrayList<User>();
		for (int i =0; i < users.size();i++)
		{
			this.users.add(users.getUser(i));
		}
	}

	public User getUser(int index)
	{
		return users.get(index);
	}

	public boolean contains(User user)
	{
		Users users = new Users();
		users.add(user);
		return contains(users);
	}

	public boolean contains(Users users)
	{
		boolean result = false;
		int counter = 0;
		for (int i =0; i < this.users.size();i++)
		{
			for (int j =0; j < users.size();j++)
			{
				if (this.users.get(i).getId().equals(users.getUser(j).getId())
						|| this.users.get(i).getName().equals(users.getUser(j).getName())
						|| this.users.get(i).getEmail().equals(users.getUser(j).getEmail()))
				{
					counter++;
					if (counter == users.size())
					{
						result = true;
						break;
					}
				}
			}
		}

		return result;
	}

	public User find(User user)
	{
		User result = null;

		for (int i =0; i < this.users.size();i++)
		{
			if ( this.users.get(i).getId().equals(user.getId())
					||this.users.get(i).getName().equals(user.getName())
					|| this.users.get(i).getEmail().equals(user.getEmail()))
			{
				result = this.users.get(i);
				break;
			}
		}

		return result;
	}

	public User getUser(String name, String email)
	{
		User result = null;

		for (int i =0; i < users.size();i++)
		{
			if (users.get(i).getName().equals(name)|| users.get(i).getEmail().equals(email))
			{
				result = users.get(i);
				break;
			}
		}

		return result;
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

	public List<User> getUserList()
	{
		return users;
	}

	protected void setUserList(List<User> users)
	{
		this.users = users;
	}

	/*public boolean hasFile(Context context)
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
			Log.v("Users:hasFile",ioex.getMessage());
		}
		finally
		{
			return result;
		}
	}*/

	/*public void deleteFile()
	{
		context.deleteFile(USERS_FILE_NAME);
	}*/

	public boolean isEmpty()
	{
		return this.size() == 0 ? true : false;
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



	public User getAccountUser()
	{
		User result = null;

		for (int i =0; i < users.size();i++)
		{
			if (users.get(i).getAccountStatus())
			{
				result = users.get(i);
				break;
			}
		}

		return result;
	}

	public Users getNonAccountUsers()
	{
		Users result = new Users();

		for (int i =0; i < users.size();i++)
		{
			if (!users.get(i).getAccountStatus())
			{
				result.add(users.get(i));
			}
		}

		return result;
	}

	public String getNonAccountUserNames()
	{
		String result = "";

		for (int i =0; i < users.size();i++)
		{
			if (!users.get(i).getAccountStatus())
			{
				result.concat(users.get(i).getName() + ",");
			}
		}

		if (result.length() >= 2)
		{
			return result.substring(0,result.length() -2);
		}
		else
		{
			return result;
		}
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
			this.setUserList(u.getUserList());
		}
		catch (Exception ex)
		{
			Log.v("Users:loadFromString", ex.getMessage());
		}
	}

	public void loadFromJSON(String json)
	{
		try
		{
			JSONArray usersJson = new JSONObject(json).getJSONArray("users");
			for(int i =0;i < usersJson.length();i++)
			{
				JSONObject userJSON = usersJson.getJSONObject(i);
				User user = new User();
				try{user.setId(userJSON.getString("id"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setName(userJSON.getString("name"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setUrl(userJSON.getString("url"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setDescription(userJSON.getString("description"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setEmail(userJSON.getString("email"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setAddress(userJSON.getString("address"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setMobile(userJSON.getString("mobile"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setCategoryId(userJSON.getString("category"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setTags(userJSON.getString("tags"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setHourRate(Double.parseDouble(userJSON.getString("hourRate")));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setDayRate(Double.parseDouble(userJSON.getString("dayRate")));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setRegisteredLatLng(Double.parseDouble(userJSON.getString("regLat")), Double.parseDouble(userJSON.getString("regLng")));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				try{user.setImageRaw(userJSON.getString("image"));}catch (Exception e){Log.v("Users:JSON:err", e.getMessage());}
				this.add(user);
			}
		}
		catch (Exception ex)
		{
			Log.v("Users:loadFromJSON()", ex.getMessage());
		}
	}

	/*public void loadFromFile(Context context)
	{
		try
		{
			//Read file
			FileInputStream fis = context.openFileInput(USERS_FILE_NAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			Users u = (Users) is.readObject();
			is.close();
			fis.close();

            this.setUserList(u.getUserList());
			Log.v("Users:loadFromFile","Success");
		}
		catch (Exception e)
		{
			Log.v("Users:loadFromFile",e.getMessage());
			Log.v("Users:loadFromFile",e.getStackTrace().toString());
		}
	}*/

	/*public void saveToFile(Context context)
	{
		try
		{
			//Write file
			FileOutputStream fos = context.openFileOutput(USERS_FILE_NAME , Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
			fos.close();
            Log.v("Users:saveToFile","Success");
		}
		catch (Exception e)
		{
			Log.v("Users:saveToFile:Err",e.getMessage());
		}
	}*/

	public void print()
	{
		try{
			Log.v("Users", "Object");
			Log.v("Users Size", String.valueOf(this.size()));
			for(int i = 0; i < users.size(); i++)
			{
				Log.v("User" ,  users.get(i).getId() +":"+ users.get(i).getName());
				if (i==3){break;}
			}
		} catch (Exception ex){Log.v("Users:print:Err", ex.getMessage());}
	}
}

