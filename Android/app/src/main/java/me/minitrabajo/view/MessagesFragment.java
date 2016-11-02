package me.minitrabajo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.MessagesAdapter;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseMSG;
import me.minitrabajo.controller.UsersAdapter;
import me.minitrabajo.model.Conversation;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.Message;
import me.minitrabajo.model.Messages;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 13/10/2016.
 */
public class MessagesFragment extends Fragment implements ResponseAPI,ResponseMSG
{
    private ListView listView;
    private TextView txtMessage;
    private UserAccount userAccount;
    private User fromUser;
    private User toUser;
    private Users users;
    //private Messages messages;
    private Conversations conversations;
    private Conversation conversation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        try {
           listView = (ListView)view.findViewById(R.id.listView);
           txtMessage = (TextView)view.findViewById(R.id.txtMessage);
           txtMessage.setOnKeyListener(new View.OnKeyListener() {
               public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        onMessageClick(v);
                        Log.v("Message:KeyListener", "Enter key presses");
                        return true;
                    }
                    return false;
                }
            });

            //Define account user
            userAccount = ((MainActivity)getActivity()).getUserAccount();

            //Get conversation
            conversation = (Conversation)getActivity().getIntent().getSerializableExtra("Conversation");
            conversation.print();

           //Define Conversation Messages
           conversations = new Conversations(this.getActivity());
           if (conversations.hasFile())
           {
               conversations.loadFromFile();
           }
           else
           {
               conversations = new Conversations(this.getActivity());
           }
        }
        catch (Exception ex){Log.v("Messages:onCreateView",ex.getMessage());}

        return view;
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Messages:processFinish", output);

        try
        {
            //Get JSON and add to object
            if (output.contains("add")) {
                JSONObject myJson = new JSONObject(output).getJSONObject("add");
                Message message = new Message(myJson.getString("to"), myJson.getString("from"), myJson.getString("text"), myJson.getLong("datestamp"));
                conversations.find(users).getMessages().add(message);
                conversations.saveToFile();
            }
            else if (output.contains("read"))
            {
                conversations.loadFromJSON(output,userAccount.getEmail());
            }
            else if (output.contains("err"))
            {
                Toast.makeText(this.getActivity(),"Message send error",Toast.LENGTH_LONG).show();
            }
            else
            {
                // is check for messages
                Toast.makeText(this.getActivity(),"No results",Toast.LENGTH_LONG).show();
            }

            //Check that a conversation object exists
            if (conversations.find(users) != null){
                //Conversation found so load it
                MessagesAdapter messagesAdapter = new MessagesAdapter(this.getActivity(), conversations.find(users).getMessages(), users);
                listView.setAdapter(messagesAdapter);
                Log.w("Messages:onCreateView", "List loaded with data");
            }
            else
            {
                //New conversation needed
                this.getMessages();
                Conversation conversation = new Conversation(this.getActivity(), users );
                conversations.add(conversation);
                Log.w("Messages:onCreateView", "New conversation started");
            }
        }
        catch (Exception ex)
        {
            Log.w("Messages:Process:Err", ex.getMessage());
        }
    }

    @Override
    public void updateConversations(Conversations conversations)
    {
        Log.w("Conversations", "updateConversations");
        conversations.print();
        this.conversations.addAll(conversations);
        Log.w("Conversations:after", "updateConversations");
        this.conversations.print();
    }

    public void onMessageClick(View view)
    {
        try
        {
            String to = users.getNonAccountUsers().getUser(0).getEmail();
            String from = users.getAccountUser().getEmail();
            String text = txtMessage.getText().toString();
            conversations.find(users).getMessages().add(new Message(to,from,text));
            /*Log.v("Messages:onMessageClick","Post");
            String url = getResources().getString(R.string.url_post_message);
            String parameters = "from=" + fromUser.getEmail() +  "to=" + toUser.getEmail()+  "text="+ txtMessage.getText() +"datestamp="+ System.currentTimeMillis();
            Log.v("Messages:Parameters ",parameters );
            PostAPI asyncTask =new PostAPI(this.getActivity());
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,userAccount.getToken());*/
        }
        catch (Exception ex)
        {
            Log.v("Messages:onMessage:Err", ex.getMessage());
        }

        Toast.makeText(this.getActivity(), "Message sent", Toast.LENGTH_LONG).show();
    }



    public void getMessages()
    {
        Log.v("Messages:getMessages","GET");
        String url = getResources().getString(R.string.url_post_search);
        String parameters = "";
        Log.v("Messages:Parameters ",parameters );
        GetAPI asyncTask =new GetAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,userAccount.getToken());
    }

}
