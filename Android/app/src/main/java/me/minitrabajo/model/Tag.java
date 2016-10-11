package me.minitrabajo.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Scott on 03/10/2016.
 */
public class Tag implements Serializable
{
    private static final long serialVersionUID = 8653566573642203225L;
    private int id=0;
    private String text="";

    public Tag (int id,String text)
    {
        this.id = id;
        this.text = text;
    }

    public int getID()
    {
        return id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
