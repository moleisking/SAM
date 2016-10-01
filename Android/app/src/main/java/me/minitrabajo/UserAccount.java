package me.minitrabajo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Scott on 23/09/2016.
 */
public class UserAccount extends User implements Serializable
{
    private static final String PREFERENCE_FILE_NAME = "minitrabajo_shared_preferences";
    private static final String USER_ACCOUNT_FILE_NAME = "user_account.dat";
    private transient Context mContext;
    private transient SharedPreferences mSharedPreference;
    private String mToken ="";

    public UserAccount(Context context)
    {
        super(context);
        mToken = "";
        mContext = context;
        mSharedPreference = mContext.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

     /*
    *   Value Object
    * */

    public User getUser()
    {
        User user = new User(mContext);
        user.setID(this.getID());
        user.setType(this.getType());
        user.setName(this.getName());
        user.setDescription(this.getDescription());
        user.setAddress(this.getAddress());
        user.setMobile(this.getMobile());
        user.setCategory(this.getCategory());
        user.setTags(this.getTags());
        user.setEmail(this.getEmail());
        user.setCurrentLatitude(this.getCurrentLatitude());
        user.setCurrentLongitude(this.getCurrentLongitude());
        user.setRegisteredLatitude(this.getRegisteredLatitude());
        user.setRegisteredLongitude(this.getRegisteredLongitude());
        user.setDayRate(this.getDayRate());
        user.setHourRate(this.getHourRate());
        user.setImageRaw(this.getImageRaw());
        return user;
    }

    /*
    *   Properties
    * */

    public String getToken()
    {
        return mToken;
    }

    public void setToken(String token)
    {
        this.mToken = token;
    }

    /*
    *   Quick Functions
    * */

    public boolean hasToken()
    {
        return mToken != null && !mToken.equals("") ? true : false;
    }

    public boolean hasFile()
    {
        File f = new File(USER_ACCOUNT_FILE_NAME);
        return f.exists();
    }

    public void deleteFile()
    {
        setToken("");
        File f = new File(USER_ACCOUNT_FILE_NAME);
        f.delete();
    }

    @Override
    public void setContext(Context context)
    {
        this.mContext = context;
    }

    /*
    *   Save Functions
    * */

    public void saveToFile()
    {
        try
        {
            //Write file
            FileOutputStream fos = mContext.openFileOutput(USER_ACCOUNT_FILE_NAME , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            Log.v("SaveProfile",e.getMessage());
        }
    }

    @Override
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
        catch (NotSerializableException nosex)
        {
            System.out.print(nosex.getMessage());
            System.out.print(nosex.getStackTrace().toString());
            Log.v("UserAccount:serialize()", nosex.getMessage());
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:serialize()", ex.getMessage());
        }
        return output;
    }

    /*
    *   Load Functions
    * */

    public void loadFromFile()
    {
        try
        {
            //Read file
            FileInputStream fis = mContext.openFileInput(USER_ACCOUNT_FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            UserAccount ua = (UserAccount) is.readObject();
            is.close();
            fis.close();

            //Copy data to this object
            this.setName(ua.getName());
            this.setDescription(ua.getDescription());
            this.setEmail(ua.getEmail());
            this.setAddress(ua.getAddress());
            this.setMobile(ua.getMobile());
            //this.setCategory(ua.setCategory());
            //this.setTags(ua.getTags());
            //this.setHourRate(Double.parseDouble(ua.getHourRate()));
            //this.setDayRate(Double.parseDouble(ua.getDayRate(")));
            //this.setRegisteredLongitude(Double.parseDouble(ua.getRegisteredLongitude()));
            //this.setRegisteredLatitude(Double.parseDouble(ua.getRegisteredLatitude()));
            this.setImageRaw(ua.getImageRaw());

        }
        catch (Exception e)
        {
            Log.v("SaveProfile",e.getMessage());
        }
    }

    @Override
    public void loadFromString( String str )
    {
        try
        {
            byte [] data = Base64.decode( str,0 );
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
            UserAccount ua  = (UserAccount)ois.readObject();
            ois.close();

            //Copy data to this object
            this.setName(ua.getName());
            this.setDescription(ua.getDescription());
            this.setEmail(ua.getEmail());
            this.setAddress(ua.getAddress());
            this.setMobile(ua.getMobile());
            //this.setCategory(ua.setCategory());
            //this.setTags(ua.getTags());
            //this.setHourRate(Double.parseDouble(ua.getHourRate()));
            //this.setDayRate(Double.parseDouble(ua.getDayRate(")));
            //this.setRegisteredLongitude(Double.parseDouble(ua.getRegisteredLongitude()));
            //this.setRegisteredLatitude(Double.parseDouble(ua.getRegisteredLatitude()));
            this.setImageRaw(ua.getImageRaw());
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:deserialize", ex.getMessage());
        }
    }

    @Override
    public void loadFromJSON(String json)
    {
        try
        {
            JSONObject data = new JSONObject(json).getJSONObject("data");

            this.setName(data.getString("name"));
            this.setDescription(data.getString("description"));
            this.setEmail(data.getString("email"));
            this.setAddress(data.getString("address"));
            this.setMobile(data.getString("mobile"));
            //this.setCategory(data.getString("category"));
            //this.setTags(data.getString("tags"));
            //this.setHourRate(Double.parseDouble(data.getString("hour_rate")));
            //this.setDayRate(Double.parseDouble(data.getString("day_rate")));
            //this.setRegisteredLongitude(Double.parseDouble(data.getString("reglat")));
            //this.setRegisteredLatitude(Double.parseDouble(data.getString("reglon")));
            this.setImageRaw(data.getString("image"));
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:JSON", ex.getMessage());
        }
    }

    @Override
    public void print()
    {
        Log.v("UserAccount", "Object");
        Log.v("ID", String.valueOf(this.getID()));
        Log.v("Name" , this.getName());
        Log.v("Description" , this.getDescription());
        Log.v("Address", this.getAddress());
        Log.v("Mobile", this.getMobile());
        Log.v("HourRate",String.valueOf(this.getHourRate()));
        Log.v("DayRate",String.valueOf(this.getDayRate()));
        Log.v("CurrentLongitude",String.valueOf(this.getCurrentLongitude()));
        Log.v("CurrentLatitude",String.valueOf(this.getCurrentLatitude()));
        Log.v("RegisteredLongitude",String.valueOf(this.getRegisteredLongitude()));
        Log.v("RegisteredLatitude",String.valueOf(this.getRegisteredLatitude()));
        Log.v("ImageRaw",this.getImageRaw());
        Log.v("Token",this.getToken());
    }

    public void test()
    {
        //Copy data to this object
        this.setName("Name");
        this.setDescription("Description");
        this.setEmail("email@domain.com");
        this.setAddress("Address");
        this.setMobile("555");
        //this.setCategory(ua.setCategory());
        //this.setTags(ua.getTags());
        //this.setHourRate(Double.parseDouble(ua.getHourRate()));
        //this.setDayRate(Double.parseDouble(ua.getDayRate(")));
        //this.setRegisteredLongitude(Double.parseDouble(ua.getRegisteredLongitude()));
        //this.setRegisteredLatitude(Double.parseDouble(ua.getRegisteredLatitude()));
        this.setImageRaw("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2NjIpLCBxdWFsaXR5ID0gODUK/9sAQwAFAwQEBAMFBAQEBQUFBgcMCAcHBwcPCwsJDBEPEhI");
    }
}
