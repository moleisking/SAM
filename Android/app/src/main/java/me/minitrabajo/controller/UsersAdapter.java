package me.minitrabajo.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.model.Messages;
import me.minitrabajo.model.User;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 12/10/2016.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class UsersAdapter extends ArrayAdapter<User>
{
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    public UsersAdapter(Context context, Users users) {
        super(context, 0, users.getUserList());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        try {
            // Get the data item for this position
            User user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user, parent, false);
            }
            // Lookup view for data population
            TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
            TextView txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            TextView txtDistance = (TextView) convertView.findViewById(R.id.txtDistance);
            ImageView img = (ImageView) convertView.findViewById(R.id.imgAvatar);
            // Populate the data into the template view using the data object
            txtName.setText(user.getName());
            txtDescription.setText(user.getDescription());
            txtDistance.setText(user.getDistanceKilometers());
            img.setImageDrawable(user.getImageAsRoundedBitmapSmall());
            //img.setImageBitmap(user.getImageAsBitmap());
            // Return the completed view to render on screen
        }catch (Exception ex){ Log.v("UsersAdapter",ex.getMessage()); }
        return convertView;
    }
}
