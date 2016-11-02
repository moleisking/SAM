package me.minitrabajo.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.Conversation;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.Message;
import me.minitrabajo.model.Messages;
import me.minitrabajo.model.User;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 13/10/2016.
 */
public class ConversationsAdapter extends ArrayAdapter<Conversation> {

   // private User deviceUser;

    public ConversationsAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, 0, conversations);
    }

    public ConversationsAdapter(Context context, Conversations conversations) {
        super(context, 0, conversations.getConversationList());
        //this.deviceUser = deviceUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Conversation conversation = getItem(position);

        Users userNonAccount = conversation.getUsers().getNonAccountUsers();
        String summary = conversation.getMessages().getSummary();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_conversation, parent, false);
        }
        // Lookup view for data population
        ImageView img = (ImageView) convertView.findViewById(R.id.imgAvatar);
        TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
        TextView txtItemSummary = (TextView) convertView.findViewById(R.id.txtItemSummary);
        TextView txtItemConversationId = (TextView) convertView.findViewById(R.id.txtItemConversationId);
        // Populate the data into the template view using the data object
        if(userNonAccount.size() > 0)
        {
            img.setImageDrawable(userNonAccount.getUser(0).getImageAsRoundedBitmapSmall());
            txtItemName.setText(userNonAccount.getNonAccountUserNames());
            txtItemSummary.setText(summary);
            txtItemConversationId.setText(conversation.getId());
        }

        //txtName.setHint(category.saveToString());
        // Return the completed view to render on screen
        return convertView;
    }
}
