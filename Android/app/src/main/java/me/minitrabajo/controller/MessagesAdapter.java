package me.minitrabajo.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.Message;
import me.minitrabajo.model.Messages;
import me.minitrabajo.model.User;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 13/10/2016.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {

    private Users users;
    //private User toUser;

    public MessagesAdapter(Context context, ArrayList<Message> messages, Users users) {
        super(context, 0, messages);
        this.users = users;
        //this.toUser = toUser;
    }

    public MessagesAdapter(Context context, Messages messages, Users users) {
        super(context, 0, messages.getMessageList());
        this.users = users;
        //this.toUser = toUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message, parent, false);
        }

        if (message.getFrom().equals(users.getAccountUser().getEmail()))
        {
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtItemMessage);
            txtMessage.setBackgroundColor(getContext().getResources().getColor(R.color.color_background_light));
            txtMessage.setText(message.getText());
        }
        else if (message.getTo().equals(users.getNonAccountUsers().getUser(0).getEmail()))
        {
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtItemMessage);
            txtMessage.setBackgroundColor(getContext().getResources().getColor(R.color.color_background_dark));
            txtMessage.setText(message.getText());
        }
        else
        {
            Log.v("MessagesAdapter", "Message Rejected:" + message.toString());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
