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
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private View headerLayout;
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtDescription;
    private UserAccount mUserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up toolbar and drawer menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //Set default fragment
        FragmentManager fragmentManager = this.getFragmentManager();//this.getSupportFragmentManager()
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new SettingFragment())
                .commit();
        setTitle("Search");
        Log.v("onCreate()","nav_search");

        //Load user passed from login via file or token pass
        try{
            //Get sent object
            mUserAccount = new UserAccount(this);
            mUserAccount = (UserAccount) getIntent().getSerializableExtra("UserAccount");

            //Set View Header Fields
            imgAvatar.setImageBitmap(mUserAccount.getImageAsBitmap());
            txtName.setText(mUserAccount.getName());
            txtDescription.setText(mUserAccount.getDescription());
        }catch (Exception ex){Log.v("onCreate():User",ex.getMessage());}
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
            User user=mUserAccount.getUser();
            getIntent().putExtra("User",user );
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame , new ProfileFragment())
                    .commit();
            setTitle("Profile");
/*try{
           User u = (User)mUserAccount;
u.print();
    mUserAccount.print();
} catch (Exception ex){Log.v("PRINT USER",ex.getMessage());}*/
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
            //SettingFragment settingFragment = new SettingFragment();
            //settingFragment.setUserAccount(mUserAccount);
            getIntent().putExtra("UserAccount", mUserAccount);
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

    public void onFragmentViewClick(View v) {
        //Handles onclick events from xml Fragments FloatingActionButton
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null && fragment.isVisible()) {
            if (fragment instanceof ProfileFragment) {
                Log.w("MainActivity", "ProfileFragmentClick");
                ((ProfileFragment) fragment).onMessageClick(v);
            }
            else if (fragment instanceof SearchFragment) {
                Log.w("MainActivity", "SearchFragmentClick");
                ((SearchFragment) fragment).onSearchClick(v);
            }
            else if (fragment instanceof SettingFragment) {
                Log.w("MainActivity", "SettingFragmentClick");
                ((SettingFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof AboutFragment) {
                Log.w("MainActivity", "AboutFragmentClick");
                ((AboutFragment) fragment).onEmailClick(v);
            }
        }
    }

}
