package me.minitrabajo;

/**
 * Created by Scott on 12/08/2016.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {

    private ListView mListView;
    private Button btnMap, btnList;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdpater;
    String[] items = {"Apple","Banana","Grape"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        //Define Objects
        mListView = (ListView)findViewById(R.id.listView);

        //Set Action Listeners
        mListView.setOnItemClickListener(new ItemList());
        Log.w("ListView", "ListView Listener started");

        //Fill ListView with Data
        arrayAdpater = new ArrayAdapter<String>(this, R.layout.list_row, R.id.txtItem,items);
        mListView.setAdapter(arrayAdpater);
        Log.w("ListView", "List loaded with data");

        //Set Look and Feel


    }

    public void onMapClick(View view)
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        Log.w("MapClick", "Map button Clicked");
    }

    public void onListClick(View view)
    {
        Log.w("ListClick", "Map button Clicked -> Do Nothing");
    }


    private class ItemList implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ViewGroup vg = (ViewGroup)view;
            TextView tv = (TextView)vg.findViewById(R.id.txtItem);
            Toast.makeText(ListActivity.this,tv.getText().toString(),Toast.LENGTH_SHORT).show();
            Log.w("ItemClick", "List Item Clicked");
        }
    }
}
