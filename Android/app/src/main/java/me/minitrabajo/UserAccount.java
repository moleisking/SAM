package me.minitrabajo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private String mToken ="";
    private static final String PREFERENCE_FILE_NAME = "minitrabajo_shared_preferences";
    private static final String USER_ACCOUNT_FILE_NAME = "user_account.dat";
    private static final String TOKEN_NAME = "token";
    private transient Context mContext;
    private transient SharedPreferences mSharedPreference;

    /*public UserAccount()
    {
        mToken = "";
        mSharedPreference = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }*/

    public UserAccount(Context context)
    {
        //super();
        mToken = "";
        mContext = context;
        mSharedPreference = mContext.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

     /*
    *   Token Object
    * */

    public String getToken()
    {
        if (!hasToken())
        {
            //Get previously stored token
            mToken = mSharedPreference.getString(TOKEN_NAME,"");
            return mToken;
        }
        else
        {
            //Return empty string
            Log.v("UserAccount","No token found");
            return mToken;
        }
    }

    public void setToken(String token)
    {
        //Store token locally
        this.mToken = token;
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(TOKEN_NAME, token);
        editor.commit();
    }

    /*
    *   Quick Functions
    * */

    public boolean hasToken()
    {
        return mToken != null && !mToken.equals("") ? true : false;
    }

    public void deleteToken()
    {
        setToken("");
    }

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
            this.setImage(ua.getImage());

        }
        catch (Exception e)
        {
            Log.v("SaveProfile",e.getMessage());
        }
    }

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
            this.setImage(ua.getImage());
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:deserialize", ex.getMessage());
        }
    }

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
            this.setImage(this.getImageFromRawString(data.getString("image")));
        }
        catch (Exception ex)
        {

        }
    }
}
