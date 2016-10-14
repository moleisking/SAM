package me.minitrabajo.view;

import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;

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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.minitrabajo.R;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private NavigationView navigationView;
    private View headerLayout;
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtDescription;
    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set drawer menu
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
        fab = (FloatingActionButton)findViewById(R.id.fab);

        //Load user passed from login via file or token pass
        try{
            //Get sent object
            userAccount = new UserAccount(this);
            userAccount = (UserAccount) getIntent().getSerializableExtra("UserAccount");

            //Set View Header Fields
            //imgAvatar.setImageBitmap(mUserAccount.getImageAsBitmap());
            imgAvatar.setImageDrawable(userAccount.getImageAsRoundedBitmap());
            txtName.setText(userAccount.getName());
            txtDescription.setText(userAccount.getDescription());
        }catch (Exception ex){Log.v("onCreate():UserAccount",ex.getMessage());}

        //Set default fragment
        showListFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        showProfileFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
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
            showMyProfileFragment();
        }
        else if (id == R.id.nav_search)
        {
            Log.v("NavigationItemSelected","nav_search");
            showSearchFragment();
        }
        else if (id == R.id.nav_result)
        {
            Log.v("NavigationItemSelected","nav_result");
            showListFragment();
        }
        else if (id == R.id.nav_setting)
        {
            showSettingFragment();
        }
        else if (id == R.id.nav_share)
        {
            Log.v("NavigationItemSelected","nav_share");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected UserAccount getUserAccount()
    {
        return this.userAccount;
    }

    public void onFragmentViewClick(View v)
    {
        //Handles onclick events from app_bar_main to get active Fragment + FloatingActionButton
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
                ((AboutFragment) fragment).onMessageClick(v);
            }
            else if (fragment instanceof AccountFragment) {
                Log.w("MainActivity", "SaveClick");
                ((AccountFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof WordFragment) {
                Log.w("MainActivity", "SaveClick");
                ((WordFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof MessageFragment) {
                Log.w("MainActivity", "MessageClick");
                ((MessageFragment) fragment).onMessageClick(v);
            }
        }
    }

    private void shareWhatsApp()
    {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        try {
            this.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,"Whatsapp have not been installed.",Toast.LENGTH_SHORT);
        }
    }

    public void showSearchFragment()
    {
        //Load search fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new SearchFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_search));

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_24dp));
        }
    }

    public void showSettingFragment()
    {
        //Load setting fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        getIntent().putExtra("UserAccount", userAccount);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new SettingFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_setting));

        fab.setVisibility(View.INVISIBLE);
    }

    public void showAccountFragment()
    {
        //Pass user account object to account fragment
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserAccount", userAccount);

        //Load account fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        getIntent().putExtra("UserAccount", userAccount);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new AccountFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_account));

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        }
    }

    public void showWordFragment()
    {
        //Load word fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new AccountFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_word));

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        }
    }

    public void showMessageFragment()
    {
        //Pass user account and user to message fragment
        User user = (User)getIntent().getSerializableExtra("User");
        getIntent().putExtra("UserAccount", userAccount);
        getIntent().putExtra("User", user );

        //Load word fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new MessageFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_message));

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));
        }
    }

    public void showMyProfileFragment()
    {
        //Pass user object to profile fragment
        User user=userAccount.getUser();
        getIntent().putExtra("User",user );

        //Load profile fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new ProfileFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_account));

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));
        }
    }

    public void showProfileFragment()
    {
        //User object sent by list fragment

        //Load profile fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new ProfileFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_profile));

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));
        }
    }

    protected void showAboutFragment()
    {
        //Load about fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new AboutFragment())
                .commit();
        setTitle("About");

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp, this.getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));
        }
    }

    protected void showListFragment()
    {
        //Load list fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new ListFragment())
                .commit();
        setTitle("List");

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.INVISIBLE);
    }

}
