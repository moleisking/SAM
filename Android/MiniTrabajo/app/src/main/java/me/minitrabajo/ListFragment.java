package me.minitrabajo;

/**
 * Created by Scott on 12/08/2016.
 */
import android.app.Fragment;
import android.app.FragmentManager;
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

public class ListFragment extends Fragment {

    private ListView mListView;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdpater;
    String[] items = {"Apple","Banana","Grape"};
    Users mUsers;

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
        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new ItemList());

        //Get List Data
        mUsers = mUsers.fromString(getActivity().getIntent().getStringExtra("users"));

        //Fill ListView with Data
        arrayAdpater = new ArrayAdapter<String>(container.getContext(), R.layout.list_row_result, R.id.txtItem,items);
        mListView.setAdapter(arrayAdpater);
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
