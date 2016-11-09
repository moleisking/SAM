package me.minitrabajo.view;

/**
 * Created by Scott on 12/08/2016.
 */
import android.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import me.minitrabajo.R;
import me.minitrabajo.common.Utility;
import me.minitrabajo.controller.UsersAdapter;
import me.minitrabajo.model.User;
import me.minitrabajo.model.Users;

public class ResultsFragment extends Fragment {

    private ListView listView;
    private Users users;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Instantiate objects and listeners
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new ItemList());

        //Get List Data
        users =  (Users) Utility.loadObject(this.getActivity() ,Users.USERS_FILE_NAME);
        //users.loadFromFile(this.getActivity());
        users.print();

        //Fill ListView with Data
        //UsersAdapter usersAdapter = new UsersAdapter(this.getActivity(), (ArrayList<User>) users.getUserList());
        UsersAdapter usersAdapter = new UsersAdapter(this.getActivity(), users);
        listView.setAdapter(usersAdapter);
        Log.w("Results:onCreateView", "List loaded with data");

        return view;
    }

    protected class ItemList implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ViewGroup vg = (ViewGroup)view;
            TextView txtName = (TextView)vg.findViewById(R.id.txtName);
            User user = users.getUser(txtName.getText().toString(),"");
            getActivity().getIntent().putExtra("User", user );
            ((MainActivity)getActivity()).showProfileFragment();

            Log.w("Results:onItemClick", txtName.getText().toString());
            //Toast.makeText(getContext(),tv.getText().toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
