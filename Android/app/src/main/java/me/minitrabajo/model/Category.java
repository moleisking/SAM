package me.minitrabajo.model;

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

    protected List<Tag> getArrayList()
    {
        return tags;
    }

    protected void setArrayList(List<Tag> arr)
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

    public List getTagList()
    {
        return tags;
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
}
