package me.minitrabajo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
//import android.support.v4.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResponseAPI {

    NavigationView navigationView;

    View headerLayout;
    ImageView imgAvatar;
    TextView txtName;
    TextView txtDescription;
    String token = "";
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Set navigation profile
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        imgAvatar = (ImageView)headerLayout.findViewById(R.id.nav_head_imgAvatar);
        txtName = (TextView)headerLayout.findViewById(R.id.nav_head_txtName);
        txtDescription = (TextView)headerLayout.findViewById(R.id.nav_head_txtDescription);
        //get user

        token = getIntent().getStringExtra("token");
        Log.v("MainActivity:Token",token);

        //Set default fragment
        FragmentManager fragmentManager = this.getFragmentManager();//this.getSupportFragmentManager()
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new SearchFragment())
                .commit();
        setTitle("Search");
        Log.v("onCreate()","nav_search");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        FragmentManager fragmentManager = this.getFragmentManager();//this.getSupportFragmentManager()
        int id = item.getItemId();

        if (id == R.id.nav_profile)
        {
            Log.v("NavigationItemSelected","nav_profile");// Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new ProfileFragment())
                    .commit();
            setTitle("Profile");

        }
        else if (id == R.id.nav_search)
        {
            Log.v("NavigationItemSelected","nav_search");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new SearchFragment())
                    .commit();
            setTitle("Search");
        }
        else if (id == R.id.nav_result)
        {
            Log.v("NavigationItemSelected","nav_result");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new ListFragment())
                    .commit();
            setTitle("Results");
        }
        else if (id == R.id.nav_setting)
        {
            Log.v("NavigationItemSelected","nav_setting");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new SettingFragment())
                    .commit();
            setTitle("Settings");
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }
        else if (id == R.id.nav_about)
        {
            Log.v("NavigationItemSelected","nav_about");
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new AboutFragment())
                    .commit();
            setTitle("About");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        Log.w("processFinish", output);
        String name = "",pass="";
        try
        {
            JSONObject myJson = new JSONObject(output);
            mUser = new User();
            mUser.setName(myJson.optString("name"));
            mUser.setDescription(myJson.optString("description"));
            mUser.setAddress(myJson.optString("address"));
            mUser.setHourRate(Double.parseDouble(myJson.optString("hour_rate")));
            mUser.setDayRate(Double.parseDouble(myJson.optString("day_rate")));
            mUser.setRegisteredLongitude(Double.parseDouble(myJson.optString("registered_longitude")));
            mUser.setRegisteredLatitude(Double.parseDouble(myJson.optString("registered_latitude")));
            mUser.setImageAsBase64(myJson.optString("image"));

            //Set Navigation Profile
            txtName.setText(mUser.getName());
            txtDescription.setText(mUser.getDescription());
            imgAvatar.setImageBitmap(mUser.getImageAsBitmap());
        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }

    }

    private void LoadProfile()
    {
        User user;

        File file = new File("profile.dat");
        if(file.exists())
        {
            Log.v("LoadProfile","From File");
            try
            {
                //Get stored file
                FileInputStream fis = openFileInput("profile.dat");
                ObjectInputStream is = new ObjectInputStream(fis);
                user = (User) is.readObject();
                is.close();
                fis.close();

                //Set View Header Fields
                imgAvatar.setImageBitmap(user.getImageAsBitmap());
                txtName.setText(user.getName());
                txtDescription.setText(user.getDescription());

            }
            catch (Exception e)
            {
                Log.v("LoadProfile",e.getMessage());
            }
        }
        else
        {
            //Get file from Post Call
            Log.v("LoadProfile","From Post");
            String url = getResources().getString(R.string.net_profile_url); //"http://192.168.1.100:3003/api/profile";
            String parameters = "token="+ token;
            PostAPI asyncTask =new PostAPI();
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,"");
        }

    }

    private void SaveProfile()
    {
       /* User user = new User();
        user.setName(txtName.getText());
        user.setMobile();
        user.setEmail();
        user.setAddress();
        user.setDescription();
        user.setHourRate();
        user.setDayRate()
        user.setRegisteredLatitude();
        user.setRegisteredLongitude();
        user.setCurrentLatitude();
        user.setCurrentLongitude();*/

        try
        {
            FileOutputStream fos = openFileOutput("profile.dat" , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mUser);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            Log.v("SaveProfile",e.getMessage());
        }
    }


}