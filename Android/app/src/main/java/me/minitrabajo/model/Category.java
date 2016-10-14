package me.minitrabajo.model;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 03/10/2016.
 */
public class Category implements Serializable
{
    private static final long serialVersionUID = 8653566573642203223L;
    private int id=0;
    private String name="";
    private String description="";
    //private transient Context mContext;
    private List<Tag> tags;

    public Category ()
    {
        //mContext = context;
        tags = new ArrayList();
    }

   /* public Category (String name)
    {
        //mContext = context;
        this.name = name;
    }*/

    public Category (int id,String name,String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = new ArrayList();
    }

    protected Tag getTag(int i)
    {
        return tags.get(i);
    }

    protected int size()
    {
        return tags.size();
    }

    protected void add(Tag t)
    {
        tags.add(t);
    }

    public List<Tag> getTagList()
    {
        return tags;
    }

    protected void setTagList(List<Tag> arr)
    {
        this.tags = arr;
    }

    public int getID()
    {
        return id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String[] getTagStringArray()
    {
        String[] names = new String[tags.size()];
        for (int i = 0; i < tags.size();i++ )
        {
            names[i] = tags.get(i).getText();
        }
        return names;
    }

    public int findTagID(String name)
    {
        int result = 0;

        for (int i =0; i < tags.size();i++)
        {
            if (tags.get(i).getText().equals(name))
            {
                result = tags.get(i).getID();
                break;
            }
        }

        return result;
    }

    public String toString()
    {
        return name;
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
            Log.v("Categories:saveToString", nosex.getMessage());
        }
        catch (Exception ex)
        {
            Log.v("Categories:saveToString", ex.getMessage());
        }
        return output;
    }

    public void loadFromString( String str )
    {
        try
        {
            byte [] data = Base64.decode( str,0 );
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
            Category c  = (Category)ois.readObject();
            ois.close();

            this.setID(c.getID());
            this.setName(c.getName());
            this.setDescription(c.getDescription());
            this.setTagList(c.getTagList());
        }
        catch (Exception ex)
        {
            Log.v("Categories:deserialize", ex.getMessage());
        }
    }
}
