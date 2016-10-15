package me.minitrabajo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import me.minitrabajo.R;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;

/**
 * Created by Scott on 13/10/2016.
 */
public class MessageFragment extends Fragment implements ResponseAPI
{
    private TextView txtMessage, txtHistory;
    private UserAccount userAccount;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.v("Message:onCreate","Started");
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout )inflater.inflate(R.layout.fragment_profile, container, false);

        try
        {
            Log.v("Message:onCreate","Start try");

            //Define Objects
            user = new User(getActivity());
            user = (User)getActivity().getIntent().getSerializableExtra("User");
            user.print();

            //Define Objects
            userAccount = new UserAccount(getActivity());
            userAccount = (UserAccount)getActivity().getIntent().getSerializableExtra("UserAccount");
            userAccount.print();

            txtHistory = (TextView) ll.findViewById(R.id.txtHistory);
            txtMessage = (TextView) ll.findViewById(R.id.txtMessage);

            //Fill Objects
            txtHistory.setText("My chat history");
        }
        catch (Exception ex)
        {
            Log.v("Message:onCreate:Err",ex.getMessage());
        }

        return ll;
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Search:processFinish", output);

        try
        {
            //Get JSON and add to object
          /*  JSONObject myJson = new JSONObject(output);
            users = new Users(this.getActivity());
            users.loadFromJSON( myJson.toString());


            if(users.size() == 0)
            {
                Toast.makeText(this.getActivity(),"No results",Toast.LENGTH_LONG).show();
            }
            else
            {
                //Pass users to list, then load list
                users.saveToFile();

                //Move to list fragment
                ((MainActivity)getActivity()).showListFragment();
            }
            Log.w("Search:Process:Users", "Print");
            users.print();*/
        }
        catch (Exception ex)
        {
            Log.w("Search:Process:Err", ex.getMessage());
        }
    }

    public void onMessageClick(View view)
    {
        try
        {
            Log.v("Search:onSearchClick()","Post");
            String url = getResources().getString(R.string.url_post_search);
            String parameters = "category=";
            Log.v("Search:Parameters ",parameters );
            PostAPI asyncTask =new PostAPI(this.getActivity());
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,userAccount.getToken());
        }
        catch (Exception ex)
        {
            Log.v("Message:onMessage:Err", ex.getMessage());
        }

        Toast.makeText(this.getActivity(), "Message sent", Toast.LENGTH_LONG).show();
    }
}
