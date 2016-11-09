package me.minitrabajo.view;

/*
*  private FloatingActionButton btnSearch; not necessary due to events being passed back to MainActivity in onFragmentViewClick
* */
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.common.Utility;
import me.minitrabajo.controller.CategoriesAdapter;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseGPS;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;

public class SearchFragment extends Fragment implements ResponseAPI, ResponseGPS
{

    private AutoCompleteTextView txtCategory;
    private MultiAutoCompleteTextView txtTag;
    //private RadioGroup radRadius;
    private Categories categories;
    private Users users;
    private GPS gps;
    private LatLng currentLatLng;
    private UserAccount userAccount;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.v("Search:OnCreate","Started");

        //Define account
        userAccount = new UserAccount();
        userAccount = ((MainActivity)getActivity()).getUserAccount();

        //Define view
        LinearLayout ll = (LinearLayout )inflater.inflate(R.layout.fragment_search, container, false);
        //txtSearch = (TextView)container.findViewById(R.id.txtName);
        //radRadius = (RadioGroup) container.findViewById(R.id.radRadius);
        txtCategory= (AutoCompleteTextView)ll.findViewById(R.id.txtCategory);
        txtCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Log.v("SELECTED", id +":" + adapterView.getItemAtPosition(position).toString());
                fillTag(adapterView.getItemAtPosition(position).toString());
                /*TextView tv = ((TextView)adapterView.getItemAtPosition(position));
                Category c = new Category();
                c.loadFromString(tv.getHint().toString());
                fillTag(c);*/

            }
        });
        txtTag= (MultiAutoCompleteTextView) ll.findViewById(R.id.txtTag);

        //Start GPS
        currentLatLng = new LatLng(0.0d,0.0d);
        gps = new GPS(this.getActivity());
        gps.delegate = this;
        gps.Start();

        fillCategory();
        Log.v("Search:Latitude",String.valueOf(currentLatLng.latitude));
        Log.v("Search:Longitude",String.valueOf(currentLatLng.longitude));

        return ll;
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Search:processFinish", output);

        try
        {
            //Get JSON and add to object
            JSONObject myJson = new JSONObject(output);
            users = new Users();
            users.loadFromJSON( myJson.toString());

            if(users.size() == 0)
            {
                Toast.makeText(this.getActivity(),"No results",Toast.LENGTH_LONG).show();
            }
            else
            {
                //Set distance field for each user
                for(int i = 0; i < users.size(); i++)
                {
                    users.getUser(i).setDistance(currentLatLng);
                }

                //Pass users to list, then load list
                Utility.saveObject(this.getActivity(), users);

                //Move to list fragment
                ((MainActivity)getActivity()).showResultsFragment();
            }
            Log.w("Search:Process:Users", "Print");
            users.print();
        }
        catch (Exception ex)
        {
            Log.w("Search:Process:Err", ex.getMessage());
        }
    }

    @Override
    public void onGPSConnectionResolutionRequest(ConnectionResult connectionResult )
    {
        try {
            connectionResult.startResolutionForResult(this.getActivity(), ResponseGPS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        }catch (Exception ex){Log.v("Search:onGPSConFail",ex.getMessage());}
    }

    @Override
    public void onGPSWarning(String message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPSPositionResult(LatLng position)
    {
        this.currentLatLng = position;
        gps.Stop();
        Log.v("Search:GPSPosRes",position.toString());
    }

    protected void fillCategory()
    {
        Log.w("Search:fillCategory", "Started");
        categories = new Categories();
        //if(categories.hasFile(this.getActivity()))
        if(Utility.hasFile(this.getActivity() ,Categories.CATEGORIES_FILE_NAME))
        {
            //categories.loadFromFile(this.getActivity());
            categories =  (Categories)Utility.loadObject(this.getActivity() ,Categories.CATEGORIES_FILE_NAME);
            CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), (ArrayList<Category>) categories.getCategoryList());
            //ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity().getApplicationContext(),  R.layout.row_category, R.id.txtItemCategory, categories.getCategoryStringArray());
            txtCategory.setAdapter(adapter);
            txtCategory.setThreshold(2);
            Log.w("Search:fillCategory", "Categories load from file");
        }
        else
        {
            Log.w("Search:fillCategory", "Categories not found");
        }
    }

    protected void fillTag(String category)
    {
        Category c = categories.findCategory(category);
        //TagsAdapter adapter = new TagsAdapter(getActivity(), (ArrayList<Tag>) category.getTagList());
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), R.layout.row_tag, R.id.txtItemTag, c.getTagStringArray() );
        txtTag.setText("");
        txtTag.setAdapter(adapter);
        txtTag.setThreshold(2);
        txtTag.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    protected void onSearchClick(View view)
    {
        currentLatLng = new LatLng( 40.4100743,-3.7054997);//change later

        Log.v("Search:onSearchClick()","Post");
        String url = getResources().getString(R.string.url_post_search);
        String parameters = "category="+ categories.findCategory(txtCategory.getText().toString()).getID() + "&radius="+ 5 + "&regLat=" + currentLatLng.latitude + "&regLng=" + currentLatLng.longitude;
        Log.v("Search:Parameters ",parameters );
        PostAPI asyncTask =new PostAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,userAccount.getToken());
    }

}
