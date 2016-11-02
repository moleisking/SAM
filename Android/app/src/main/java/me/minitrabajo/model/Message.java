package me.minitrabajo.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Scott on 15/10/2016.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 8653577673612803339L;
    private String id = UUID.randomUUID().toString();
    private String to="";
    private String from="";
    private String text="";
    private long timestamp;

    public Message (String to,String from,String text, long timestamp)
    {
        this.to = to;
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Message (String to,String from,String text)
    {
        this.to = to;
        this.from = from;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
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

    public long getTimestamp()
    {
        return timestamp;
    }


}
