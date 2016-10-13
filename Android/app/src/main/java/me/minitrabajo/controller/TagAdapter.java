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
import me.minitrabajo.model.Tag;
import me.minitrabajo.model.User;

/**
 * Created by Scott on 13/10/2016.
 */
public class TagAdapter extends ArrayAdapter<Tag>
{
    public TagAdapter(Context context, ArrayList<Tag> tags) {
        super(context, 0, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Tag tag = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user, parent, false);
        }
        // Lookup view for data population
        TextView txtName = (TextView) convertView.findViewById(R.id.txtItemTag);

        // Populate the data into the template view using the data object
        txtName.setText(tag.getText());
        // Return the completed view to render on screen
        return convertView;
    }
}
