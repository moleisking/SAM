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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.model.Users;

public class ListFragment extends Fragment {

    private ListView listView;

    private ArrayList<String> userList;
    private ArrayAdapter<String> userAdapter;
    String[] items = {"Apple","Banana","Grape"};
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new ItemList());

        //Get List Data
        users = (Users)getActivity().getIntent().getSerializableExtra("users");

        //Fill ListView with Data
        userAdapter = new ArrayAdapter<String>(container.getContext(), R.layout.row_list, R.id.txtItem,items);
        listView.setAdapter(userAdapter);
        Log.w("ListView", "List loaded with data");

        return view;
    }


    protected class ItemList implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ViewGroup vg = (ViewGroup)view;
            TextView tv = (TextView)vg.findViewById(R.id.txtItem);
            Toast.makeText(getContext(),tv.getText().toString(),Toast.LENGTH_SHORT).show();
            Log.w("ItemClick", "List Item Clicked");
        }
    }
}
