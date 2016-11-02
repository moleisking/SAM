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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Scott on 15/10/2016.
 */
public class Messages implements Serializable {

    private static final long serialVersionUID = 8653577573642203229L;
    private static final String MESSAGE_FILE_NAME = "messages.dat";
    private transient Context context;
    private List<Message> messages;

    public Messages (Context context)
    {
        this.context = context;
        this.messages = new ArrayList();
    }

    public Messages(Context context, Message[] message)
    {
        this.context= context;
        this.messages = new  ArrayList<Message>(1);
        for (int i =0; i < message.length;i++) {
            this.messages.add(message[i]);
        }
    }

    public Messages (Context context, Messages messages)
    {
        this.context = context;
        this.messages = new  ArrayList<Message>();
        for (int i =0; i < messages.size();i++)
        {
            this.messages.add(messages.getMessage(i));
        }
    }

    public Message getMessage(int i)
    {
        return messages.get(i);
    }

    public int size()
    {
        return messages.size();
    }

    public void add(Message c)
    {
        messages.add(c);
    }

    public void add(Messages m)
    {
        for (int i =0; i < m.size();i++)
        {
            this.messages.add(m.getMessage(i));
        }
    }

    protected boolean contains(Message message)
    {
        boolean result = false;
        for (int i =0; i < messages.size();i++)
        {

            if (message.getText().equals(message.getText()) &&
                    message.getFrom().equals(message.getFrom()) &&
                        message.getTo().equals(message.getTo()))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public List<Message> getMessageList()
    {
        return messages;
    }

    protected void setMessageList(List<Message> messages)
    {
        this.messages = messages;
    }

    public String getSummary()
    {
        String message = getMessage(messages.size() - 1).getText();
        if (message.length() < 20)
        {
            return message + "...";
        }
        else
        {
            return message.substring(20) + "...";
        }
    }

    public boolean isEmpty()
    {
        return this.size() == 0 ? true : false;
    }

    public Message find(String name)
    {
        Message result = null;
        for (int i =0; i < messages.size();i++)
        {
            if (messages.get(i).getText().contains(name))
            {
                result = messages.get(i);
                break;
            }
        }
        return result;
    }

    public void sortById()
    {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2)
            {
                return  message1.getId().compareTo(message2.getId());
            }
        });
    }

    public void sortByEmail()
    {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2)
            {
                return  message1.getFrom().compareTo(message2.getFrom());
            }
        });
    }

    public Conversations sortByConversations(String deviceEmail)
    {
        //Get the number of conversations
        //Get the other email addresses
        ArrayList receivedEmails = new ArrayList();
        for (int i = 0; i < messages.size(); i++)
        {
           if (this.messages.get(i).getFrom().equals(deviceEmail))
           {
               receivedEmails.add(this.messages.get(i).getFrom());
           }
           else if (messages.get(i).getTo().equals(deviceEmail))
           {
               receivedEmails.add(this.messages.get(i).getTo());
           }
        }

        //Remove duplicate email addresses
        Set<String> hs = new HashSet<>();
        hs.addAll(receivedEmails);
        receivedEmails.clear();
        receivedEmails.addAll(hs);

        //Build new Conversations
        Conversations conversations = new Conversations(context);
        conversations.add(new Conversation(context,new Users(context)));
        for (int i = 0; i < receivedEmails.size(); i++)
        {
            String receivedEmail = receivedEmails.get(i).toString();
            Conversation conversation = new Conversation(context);

            Users u = new Users(context);
            u.add(new User(receivedEmail));
            u.add(new User( deviceEmail));

            Messages m = new Messages(context);

            for (int j = 0; j < this.messages.size(); j++)
            {
                if( this.messages.get(j).getFrom().equals(receivedEmail))
                {
                    m.add(messages.get(j));
                }
                else if( this.messages.get(j).getTo().equals(receivedEmail))
                {
                    m.add(messages.get(j));
                }
            }
            conversation.setUsers(u);
            conversation.setMessages(m);
            conversations.add(conversation);
        }

        return conversations;
    }

    public boolean hasFile()
    {
        boolean result = false;
        try
        {
            File file = context.getFileStreamPath(MESSAGE_FILE_NAME);
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
            Log.v("Messages:hasFile",ioex.getMessage());
        }
        finally
        {
            return result;
        }
    }

    public void deleteFile()
    {
        context.deleteFile(MESSAGE_FILE_NAME);
    }

    public void saveToFile()
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(MESSAGE_FILE_NAME , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            Log.v("Messages:Save",e.getMessage());
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
            Log.v("Messages:saveToString", nosex.getMessage());
        }
        catch (Exception ex)
        {
            Log.v("Messages:saveToString", ex.getMessage());
        }
        return output;
    }

    public void loadFromFile()
    {
        try
        {
            //Read file
            FileInputStream fis = context.openFileInput(MESSAGE_FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            Messages cc = (Messages) is.readObject();
            is.close();
            fis.close();

            for (int i = 0; i < cc.size(); i++)
            {
                this.add(new Message(cc.getMessage(i).getTo(), cc.getMessage(i).getFrom(),cc.getMessage(i).getText(), cc.getMessage(i).getTimestamp()));
            }
            this.setMessageList(cc.getMessageList());

            Log.v("Messages:loadFromFile","Load from file success");
        }
        catch (Exception e)
        {
            Log.v("Messages:loadFromFile",e.getMessage());
        }
    }

    public void loadFromString( String str )
    {
        try
        {
            byte [] data = Base64.decode( str,0 );
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
            Messages cc  = (Messages)ois.readObject();
            ois.close();

            for (int i = 0; i < cc.size(); i++)
            {
                this.add(new Message(cc.getMessage(i).getTo(), cc.getMessage(i).getFrom(),cc.getMessage(i).getText(), cc.getMessage(i).getTimestamp()));
            }
            this.setMessageList(cc.getMessageList());
        }
        catch (Exception ex)
        {
            Log.v("Messages:deserialize", ex.getMessage());
        }
    }

    public void loadFromJSON(String json)
    {
        try
        {
            JSONArray messages = new JSONObject(json).getJSONArray("messages");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject messageJSON = messages.getJSONObject(i);
                Message message = new Message(messageJSON.getString("to"),messageJSON.getString("from"),messageJSON.getString("text"), messageJSON.getLong("timestamp") );
                this.add(message);
            }
            Log.v("Messages:JSON:OK", String.valueOf(this.size()));
        }
        catch (Exception ex)
        {
            Log.v("Messages:JSON:ERR", ex.getMessage());
        }
    }

    public void print()
    {
        try{
            Log.v("Messages", "Object");
            Log.v("Messages Size", String.valueOf(this.size()));
            for(int i = 0; i < messages.size(); i++)
            {
                Log.v("Message" ,  messages.get(i).getTo() +":"+ messages.get(i).getText());
                if (i==3){break;}

            }
        } catch (Exception ex){Log.v("Messages:Err:print", ex.getMessage());}

    }
}
