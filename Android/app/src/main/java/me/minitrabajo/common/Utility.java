package me.minitrabajo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import me.minitrabajo.R;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;

public class Utility {

    public static boolean hasFile(Context context, String filename)
    {
        boolean result = false;
        try
        {
            File file = context.getFileStreamPath(filename);
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
            Log.v("Base:hasFile",ioex.getMessage());
        }
        finally
        {
            return result;
        }
    }

    public static void saveObject(Context context, Object obj)
    {
        try
        {
            if (obj instanceof Categories)
            {
                FileOutputStream fos = context.openFileOutput(Categories.CATEGORIES_FILE_NAME , Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(obj);
                os.close();
                fos.close();
            }
            else if (obj instanceof Conversations)
            {
                FileOutputStream fos = context.openFileOutput(Conversations.CONVERSATIONS_FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(obj);
                os.close();
                fos.close();
            }
            else if (obj instanceof UserAccount)
            {
                FileOutputStream fos = context.openFileOutput(UserAccount.USER_ACCOUNT_FILE_NAME , Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(obj);
                os.close();
                fos.close();
            }
            else if (obj instanceof Users)
            {
                FileOutputStream fos = context.openFileOutput(Users.USERS_FILE_NAME , Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(obj);
                os.close();
                fos.close();
            }
            Log.v("Base:saveFile","Success");
        }
        catch (Exception e)
        {
            Log.v("Base:saveFile",e.getMessage());
        }
    }

    public static void deleteObject(Context context, String filename)
    {
        try
        {
            context.deleteFile(filename);
            Log.v("Base:deleteFile","Success");
        }
        catch (Exception e)
        {
            Log.v("Base:deleteFile",e.getMessage());
        }
    }

    public static Object loadObject(Context context, String filename)
    {
        Object obj = null;
        try
        {
            if (filename.equals(Categories.CATEGORIES_FILE_NAME))
            {
                FileInputStream fis = context.openFileInput(filename);
                ObjectInputStream is = new ObjectInputStream(fis);
                obj = (Categories) is.readObject();
                is.close();
                fis.close();
            }
            else if (filename.equals(Conversations.CONVERSATIONS_FILE_NAME))
            {
                FileInputStream fis =  context.openFileInput(filename);
                ObjectInputStream is = new ObjectInputStream(fis);
                obj = (Conversations) is.readObject();
                is.close();
                fis.close();
            }
            else if (filename.equals(UserAccount.USER_ACCOUNT_FILE_NAME))
            {
                FileInputStream fis = context.openFileInput(filename);
                ObjectInputStream is = new ObjectInputStream(fis);
                obj = (UserAccount) is.readObject();
                is.close();
                fis.close();
            }
            else if (filename.equals(Users.USERS_FILE_NAME))
            {
                FileInputStream fis = context.openFileInput(filename);
                ObjectInputStream is = new ObjectInputStream(fis);
                obj = (Users) is.readObject();
                is.close();
                fis.close();
            }

            Log.v("Base:loadFromFile","Success");
        }
        catch (Exception e)
        {
            Log.v("Base:loadFromFile",e.getMessage());
        }

        return obj;
    }

}
