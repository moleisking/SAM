package me.minitrabajo.model;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 03/10/2016.
 */
public class Categories extends ArrayList implements Serializable
{
    private static final long serialVersionUID = 8653566573642203224L;
    public static final String CATEGORY_FILE_NAME = "categories.dat";
    private transient Context context;
    private List<Category> categories;

    public Categories(Context context)
    {
        this.context = context;
        this.categories = new ArrayList();
    }

    protected Category getCategory(int i)
    {
        return categories.get(i);
        //return (Category)this.get(i);
    }

    public int size()
    {
        return categories.size();
        //return this.size();
    }

    protected void add(Category c)
    {
        categories.add(c);
        //this.add(c);
    }

    public List<Category> getCategoryList()
    {
        return categories;
    }

    protected void setCategoryList(List<Category> categories)
    {
        this.categories = categories;
    }

    public String[] getCategoryStringArray()
    {
        String[] names = new String[categories.size()];
        for (int i = 0; i < categories.size();i++ )
        {
            names[i] = categories.get(i).getName();
        }

        /*String[] names = new String[this.size()];
        for (int i = 0; i < this.size();i++ )
        {
            names[i] = ((Category)this.get(i)).getName();
        }*/

        return names;
    }

    public boolean isEmpty()
    {
        return this.size() == 0 ? true : false;
    }


    public boolean hasFile()
    {
        boolean result = false;
        try
        {
            File file = context.getFileStreamPath(CATEGORY_FILE_NAME);
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
            Log.v("Categories:hasFile",ioex.getMessage());
        }
        finally
        {
            return result;
        }
    }

   /* public void deleteFile()
    {
        context.deleteFile(CATEGORY_FILE_NAME);
    }*/

    public Category findCategory(String name)
    {
        Category result = null;

        for (int i =0; i < categories.size();i++)
        {
            if (categories.get(i).getName().equals(name))
            {
                result = categories.get(i);
                break;
            }
        }

       /*for (int i =0; i < this.size();i++)
        {
            if (((Category)this.get(i)).getName().equals(name))
            {
                result = (Category)this.get(i);
                break;
            }
        }*/

        return result;
    }

    /*
    *   Save Functions
    * */

    public void saveToFile()
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(CATEGORY_FILE_NAME , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            Log.v("Categories:Save",e.getMessage());
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
            Log.v("Categories:saveToString", nosex.getMessage());
        }
        catch (Exception ex)
        {
            Log.v("Categories:saveToString", ex.getMessage());
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
            FileInputStream fis = context.openFileInput(CATEGORY_FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            Categories cc = (Categories) is.readObject();
            is.close();
            fis.close();

            this.setCategoryList(cc.getCategoryList());
            //this.addAll(cc);

            Log.v("Categories:loadFromFile","Load from file success");
        }
        catch (Exception e)
        {
            Log.v("Categories:loadFromFile",e.getMessage());
        }
    }

    public void addAll(Categories cc)
    {
        for (int i = 0; i < cc.size(); i++)
        {
            this.add(cc);
        }

    }

    public void loadFromString( String str )
    {
        try
        {
            byte [] data = Base64.decode( str,0 );
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
            Categories cc  = (Categories)ois.readObject();
            ois.close();

            //this.setCategoryList(c.getCategoryList());
            this.addAll(cc);
        }
        catch (Exception ex)
        {
            Log.v("Categories:deserialize", ex.getMessage());
        }
    }

    public void loadFromJSON(String json)
    {
        try
        {
            JSONArray categories = new JSONObject(json).getJSONArray("categories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject categoryJSON = categories.getJSONObject(i);
                Category category = new Category(categoryJSON.getInt("id"),categoryJSON.getString("name"),categoryJSON.getString("description") );

                JSONArray tags = categoryJSON.getJSONArray("tags");
                for (int j = 0; j < tags.length(); j++)
                {
                    JSONObject tagJSON = tags.getJSONObject(j);
                    Tag tag = new Tag(tagJSON.getInt("id"),tagJSON.getString("text"));
                    category.add(tag);
                }
                this.add(category);
            }
            Log.v("Categories:JSON:OK", String.valueOf(this.size()));
        }
        catch (Exception ex)
        {
            Log.v("Categories:JSON:ERR", ex.getMessage());
        }
    }

    public void print()
    {
        try{
            Log.v("Categories", "Object");
            Log.v("Categories Size", String.valueOf(this.size()));
            for(int i = 0; i < categories.size(); i++)
            {
                Log.v("Category" ,  categories.get(i).getID() +":"+ categories.get(i).getName());
                List<Tag> tags = categories.get(i).getTagList();
                Log.v("Tag Size" , String.valueOf(tags.size()));
                if (i==3){break;}
                for(int j = 0; j < tags.size(); j++)
                {
                    Log.v("Tag" ,  tags.get(j).getID() +":"+ tags.get(j).getText());
                    if (j==3){break;}
                }
            }
        } catch (Exception ex){Log.v("Categories:Err:print", ex.getMessage());}

        /*try{
            Log.v("Categories", "Object");
            Log.v("Categories Size", String.valueOf(this.size()));
            for(int i = 0; i < this.size(); i++)
            {
                Log.v("Category" ,  ((Category)this.get(i)).getID() +":"+ ((Category)this.get(i)).getName());
                List<Tag> tags = ((Category)this.get(i)).getArrayList();
                Log.v("Tag Size" , String.valueOf(tags.size()));
                if (i==3){break;}
                for(int j = 0; j < tags.size(); j++)
                {
                    Log.v("Tag" ,  tags.get(j).getID() +":"+ tags.get(j).getText());
                    if (j==3){break;}
                }
            }
        } catch (Exception ex){Log.v("Categories:Err:print", ex.getMessage());}*/
    }

}
