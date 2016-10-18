package me.minitrabajo.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import me.minitrabajo.R;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 07/10/2016.
 */
public class WordFragment  extends Fragment implements ResponseAPI {
    private TextView txtWord;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Define Objects
        txtWord = (TextView)container.findViewById(R.id.txtName);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Word:processFinish", output);

        try
        {
            //Get JSON and add to object
           /* JSONObject myJson = new JSONObject(output);
            users = new Users(this.getActivity());
            users.loadFromJSON( myJson.toString());*/

            //Pass users to list, then load list
           // Log.w("onSearchClick", "Search button clicked");
            Intent intent = new Intent(this.getActivity(), SearchFragment.class);
            //intent.putExtra("users", users);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }
    }

    protected void onSaveClick(View view)
    {
        Log.v("Word:onSearchClick()","Post");
        String url = getResources().getString(R.string.url_post_search);
        String parameters = "word="+ txtWord.getText();
        PostAPI asyncTask =new PostAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,"");
    }

}
