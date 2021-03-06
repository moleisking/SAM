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
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.User;

/**
 * Created by Scott on 13/10/2016.
 */
public class CategoriesAdapter extends ArrayAdapter<Category>
{

    public CategoriesAdapter(Context context, ArrayList<Category> categories)
    {
        super(context, 0, categories);
    }

    public CategoriesAdapter(Context context, Categories categories )
    {
        super(context, 0, categories.getCategoryList());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Category category = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_category, parent, false);
        }
        // Lookup view for data population
        TextView txtName = (TextView) convertView.findViewById(R.id.txtItemCategory);
        // Populate the data into the template view using the data object
        txtName.setText(category.getName());
        //txtName.setHint(category.saveToString());
        // Return the completed view to render on screen
        return convertView;
    }
}
