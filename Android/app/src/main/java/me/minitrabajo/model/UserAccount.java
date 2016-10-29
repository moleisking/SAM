package me.minitrabajo.model;

import android.content.Context;
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
    private static final long serialVersionUID = 8653566573642203222L;
    public static final String USER_ACCOUNT_FILE_NAME = "user_account.dat";
    private transient Context context;
    private String token ="";
    private String password="";

    public UserAccount(Context context)
    {
        super(context);
        this.token = "";
        this.context = context;
    }

    /* * *  PROPERTIES * * */

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    /* * *  FUNCTIONS * * */

    public boolean hasToken()
    {
        return token != null && !token.equals("") ? true : false;
    }

    public boolean hasFile()
    {
        boolean result = false;
        try
        {
            File file = context.getFileStreamPath(USER_ACCOUNT_FILE_NAME);
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
            Log.v("UserAccount:Hasfile",ioex.getMessage());
        }
        finally
        {
            return result;
        }
    }

    /*public void deleteFile()
    {
        //ERROR: Need to test delete. Activity probably needed not context.
        try
        {
            try {
                File file = new File(USER_ACCOUNT_FILE_NAME);
                if(file.exists())
                {

                    file.delete();
                    Log.v("UserA:deleteFile:f1","File deleted");
                }
                else
                {
                    Log.v("UserA:deleteFile:f1","File does not exist");
                }
            }catch (Exception e)
            {Log.v("UserA:deleteFile:f1:err", e.getMessage());}


            //setToken("");
            try {
                context.deleteFile(USER_ACCOUNT_FILE_NAME);
                Log.v("UserAccount:deleteFile","File deleted");
            }catch (Exception e)
            {Log.v("UserA:deleteFile:c:err", e.getMessage());}
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:deleteFile","File not found");
            Log.v("UserAccount:deleteFile",ex.getMessage());
        }
    }*/

    public boolean isEmpty()
    {
        boolean result = true;
        if (this.getEmail().equals(""))
        {
           result = true;
        }
        else
        {
           result = false;
        }
        return result;
    }

    @Override
    public void setContext(Context context)
    {
        this.context = context;
    }

    public User getUser()
    {
        User user = new User(context);
        user.setId(this.getId());
        user.setName(this.getName());
        user.setUrl(this.getUrl());
        user.setDescription(this.getDescription());
        user.setAddress(this.getAddress());
        user.setMobile(this.getMobile());
        user.setCategoryId(this.getCategoryId());
        user.setTags(this.getTags());
        user.setEmail(this.getEmail());
        user.setCurrentLatLng(this.getCurrentLatLng());
        user.setRegisteredLatLng(this.getRegisteredLatLng());
        user.setDayRate(this.getDayRate());
        user.setHourRate(this.getHourRate());
        user.setImageRaw(this.getImageRaw());
        //Tags user source as from AccountUser
        user.setAccountStatus(true);
        return user;
    }

    public String getUserParameters()
    {
        return "name=" + this.getName() +
                "&pass=" + this.getPassword() +
                "&email=" + this.getEmail().replace("@","%40")  +
                "&address=" + this.getAddress() +
                "&category=" + this.getCategoryId() +
                "&dayRate=" + this.getDayRate() +
                "&hourRate=" + this.getHourRate() +
                "&regLat=" + this.getRegisteredLatLng().latitude +
                "&regLng=" + this.getRegisteredLatLng().longitude +
                "&image=" +  this.getImageRaw() ;
    }

    /* * * SAVE FUNCTIONS * * */

    public void saveToFile()
    {
        try
        {
            //Write file
            FileOutputStream fos = context.openFileOutput(USER_ACCOUNT_FILE_NAME , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            Log.v("UserAccount:Save",e.getMessage());
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

    /* * * LOAD FUNCTIONS * * */

    public void loadFromFile()
    {
        try
        {
            //Read file
            FileInputStream fis = context.openFileInput(USER_ACCOUNT_FILE_NAME);
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
            this.setHourRate(ua.getHourRate());
            this.setDayRate(ua.getDayRate());
            this.setRegisteredLatLng(ua.getRegisteredLatLng());
            this.setImageRaw(ua.getImageRaw());
            this.setToken(ua.getToken());
            Log.v("LoadUserAccount","Load from file success");

        }
        catch (Exception e)
        {
            Log.v("LoadUserAccount",e.getMessage());
            Log.v("LoadUserAccount",e.getStackTrace().toString());
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
            this.setToken(ua.getToken());
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
            JSONObject data = new JSONObject(json).getJSONObject("myuser");

            try{this.setName(data.getString("name"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setUrl(data.getString("nameurl"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setDescription(data.getString("description"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setEmail(data.getString("email"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setAddress(data.getString("address"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setMobile(data.getString("mobile"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setCategoryId(data.getString("category"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setTags(data.getString("tags"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setHourRate(Double.parseDouble(data.getString("hourRate")));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setDayRate(Double.parseDouble(data.getString("dayRate")));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setRegisteredLatLng(Double.parseDouble(data.getString("regLat")), Double.parseDouble(data.getString("regLng")));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
            try{this.setImageRaw(data.getString("image"));}catch (Exception e){Log.v("UserAccount:JSON:err", e.getMessage());}
        }
        catch (Exception ex)
        {
            Log.v("UserAccount:JSON:err", ex.getMessage());
        }
    }

    @Override
    public void print()
    {
        Log.v("UserAccount", "Object");
        Log.v("ID", String.valueOf(this.getId()));
        Log.v("Name" , this.getName());
        Log.v("Url" , this.getUrl());
        Log.v("Description" , this.getDescription());
        Log.v("Address", this.getAddress());
        Log.v("Mobile", this.getMobile());
        Log.v("HourRate",String.valueOf(this.getHourRate()));
        Log.v("DayRate",String.valueOf(this.getDayRate()));
        Log.v("CurrentLatLng",String.valueOf(this.getCurrentLatLng().toString()));
        Log.v("RegisteredLatLng",String.valueOf(this.getRegisteredLatLng()));
        Log.v("ImageRaw",this.getImageRaw());
        Log.v("Token",this.getToken());
    }

    public void test()
    {
        //Copy data to this object
        this.setToken("JWT XXXTOKENXXX");
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
        this.setImageRaw("data:image/jpeg;base64,XXXIMAGEXXX");
    }
}
