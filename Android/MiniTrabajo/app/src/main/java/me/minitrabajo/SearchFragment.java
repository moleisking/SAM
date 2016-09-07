package me.minitrabajo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

public class SearchFragment extends Fragment implements ResponseAPI{

    private TextView txtSearch;
    private RadioGroup radRadius;
    private FloatingActionButton btnSearch;
    private Users mUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Define Objects
        txtSearch = (TextView)container.findViewById(R.id.txtName);
        radRadius = (RadioGroup) container.findViewById(R.id.radRadius);
        btnSearch = (FloatingActionButton)container.findViewById(R.id.btnRegister);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void processFinish(String output)
    {
        //Here you will receive the result fired from async class
        //Example Reply: {
        //"data": {
        //  "name": "username",
        //  "pass": "$2a$10$oCHXQeU4SsMCquduC2E8Y.ehM7vrzKQJmUz0PuZTlvbAjijLam4O6"
        //  }
        //}
        Log.w("Search:processFinish", output);

        try
        {
            //Get JSON and add to object
            JSONObject myJson = new JSONObject(output);
            mUsers = new Users();
            mUsers.importUsers( myJson.toString());

            //Pass users to list, then load list
            Users users = new Users();
            Log.w("onSearchClick", "Search button clicked");
            Intent intent = new Intent(this.getActivity(), ListFragment.class);
            intent.putExtra("users", users.toString());
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }
    }

    protected void onSearchClick(View view)
    {
        Log.v("Search:onSearchClick()","Post");
        String url = getResources().getString(R.string.net_search_url); //"http://192.168.1.100:3003/api/profile";
        String parameters = "search="+ txtSearch.getText() + "&radius="+radRadius;
        PostAPI asyncTask =new PostAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,"");
    }

}
