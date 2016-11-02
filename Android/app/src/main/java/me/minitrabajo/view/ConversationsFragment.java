package me.minitrabajo.view;

/**
 * Created by Scott on 12/08/2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.controller.ConversationsAdapter;
import me.minitrabajo.controller.ResponseMSG;
import me.minitrabajo.controller.UsersAdapter;
import me.minitrabajo.model.Conversation;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.Messages;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;

//import android.support.v4.app.Fragment;

public class ConversationsFragment extends Fragment implements ResponseMSG {

    private ListView listView;
    private UserAccount userAccount;
    private Conversations conversations;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Instantiate objects and listeners
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new ItemList());

        //Get List Data
        userAccount = ((MainActivity)getActivity()).getUserAccount();
        //userAccount.loadFromFile();
        //userAccount.print();

        //Fill ListView with Data
        ConversationsAdapter conversationsAdapter = new  ConversationsAdapter(this.getActivity(),  conversations);
        listView.setAdapter(conversationsAdapter);
        Log.w("Conversations:onCreateV", "List loaded with data");

        return view;
    }

    @Override
    public void updateConversations(Conversations conversations)
    {
        Log.w("Conversations", "updateConversations");
        conversations.print();
        /*
    *
    * Conversations conversationsSaved = new Conversations(context);
                    conversationsSaved.loadFromFile();
                    conversationsSaved.add(conversations);
                    conversationsSaved.saveToFile();
                    conversationsSaved
    *
    * */
    }

    protected class ItemList implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ViewGroup vg = (ViewGroup)view;
            TextView txtName = (TextView)vg.findViewById(R.id.txtItemName);

            TextView txtConversation = (TextView)vg.findViewById(R.id.txtItemConversationId);

            //User user = users.findUser(txtName.getText().toString());
            getActivity().getIntent().putExtra("ConversationId", txtConversation.getText() );
            ((MainActivity)getActivity()).showMessagesFragment();

            Log.w("Conversations", "onItemClick");
            //Toast.makeText(getContext(),tv.getText().toString(),Toast.LENGTH_SHORT).show();*/


        }
    }

}
