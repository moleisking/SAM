package me.minitrabajo.view;

import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
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
import me.minitrabajo.controller.LocalService;
import me.minitrabajo.controller.MessageService;
import me.minitrabajo.controller.ResponseMSG;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ResponseMSG {

    private FloatingActionButton fab;
    private NavigationView navigationView;
    private View headerLayout;
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtDescription;
    private UserAccount userAccount;
    MessageService messageService;
    boolean messageServiceBound = false;
    Intent intentMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            Log.v("Main:onCreate()","Started");
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
            userAccount = new UserAccount(this);
            userAccount = (UserAccount) getIntent().getSerializableExtra("UserAccount");

            //Set View Header Fields
            //imgAvatar.setImageBitmap(mUserAccount.getImageAsBitmap());
            imgAvatar.setImageDrawable(userAccount.getImageAsRoundedBitmap());
            txtName.setText(userAccount.getName());
            txtDescription.setText(userAccount.getDescription());

            //Set default fragment
            showResultsFragment();

            //Intent serviceIntent = new Intent(this, MessageService.class);
            //startService(new Intent(getBaseContext(), MessageService.class));
            //startService(serviceIntent);
            Log.v("Main:onCreate","Finished");
        }
        catch (Exception ex)
        {
            Log.v("Main:onCreate:Err",ex.getMessage());
        }
    }

    // Method to start the service
    public void startService() {

        //int num = mService.getRandomNumber();
        messageService.startMessageService();
       // mService.startMessageService();
        //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();

        //startService(new Intent(getBaseContext(), MessageService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MessageService.class));
    }

    /*LocalService mService;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    // Defines callbacks for service binding, passed to bindService()
    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };*/

    public void updateConversations(Conversations conversations)
    {
        Log.v("MainActivity","updateConversations");
        conversations.print();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Main","onStart()");
        // Bind to LocalService
        intentMessages = new Intent(this, MessageService.class);
        bindService(intentMessages, messageServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Main","onStop()");
        // Unbind from the service
        if (messageServiceBound) {
            unbindService(messageServiceConnection);
            messageServiceBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection messageServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MessageService.MessageBinder binder = (MessageService.MessageBinder) service;
            messageService = binder.getService();
            messageServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            messageServiceBound = false;
        }
    };

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        showResultsFragment();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("MAIN","KEYPRESSED");
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER))
        {
            Log.v("MAIN","ENTER");
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Log.v("MAIN","MENU");
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
            showResultsFragment();
        }
        else if (id == R.id.nav_setting)
        {
            Log.v("NavigationItemSelected","nav_setting");
            showSettingFragment();
        }
        else if (id == R.id.nav_message)
        {
            Log.v("NavigationItemSelected","nav_share");
            showConversationsFragment();
        }
        else if (id == R.id.nav_share)
        {
            Log.v("NavigationItemSelected","nav_share");
            startService();
            //int num = mService.getRandomNumber();
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
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
        if (fragment != null && fragment.isVisible())
        {
            if (fragment instanceof ProfileFragment)
            {
                Log.w("MainActivity", "ProfileFragmentClick");
                ((ProfileFragment) fragment).onMessageClick(v);
            }
            else if (fragment instanceof MyProfileFragment)
            {
                Log.w("MainActivity", "MyProfileFragmentClick");
                ((MyProfileFragment) fragment).onEditClick(v);
            }
            else if (fragment instanceof SearchFragment)
            {
                Log.w("MainActivity", "SearchFragmentClick");
                ((SearchFragment) fragment).onSearchClick(v);
            }
            else if (fragment instanceof SettingFragment)
            {
                Log.w("MainActivity", "SettingFragmentClick");
                ((SettingFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof AboutFragment)
            {
                Log.w("MainActivity", "AboutFragmentClick");
                ((AboutFragment) fragment).onMessageClick(v);
            }
            else if (fragment instanceof AccountFragment)
            {
                Log.w("MainActivity", "SaveClick");
                ((AccountFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof WordFragment)
            {
                Log.w("MainActivity", "SaveClick");
                ((WordFragment) fragment).onSaveClick(v);
            }
            else if (fragment instanceof MessagesFragment)
            {
                Log.w("MainActivity", "MessagesClick");
                ((MessagesFragment) fragment).onMessageClick(v);
            }
            else if (fragment instanceof ConversationsFragment)
            {
                Log.w("MainActivity", "ConversationsClick");
                //((ConversationsFragment) fragment).onMessageClick(v);
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
        //getIntent().putExtra("UserAccount", userAccount);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new SettingFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_setting));

        fab.setVisibility(View.INVISIBLE);
    }

    public void showAccountFragment()
    {
        //Load account fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        //getIntent().putExtra("UserAccount", userAccount);
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

    public void showMessagesFragment()
    {
        //Pass user account and user to message fragment
        User user = (User)getIntent().getSerializableExtra("User");
        getIntent().putExtra("User", user );

        //Load word fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new MessagesFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_message));

        fab.setVisibility(View.INVISIBLE);
    }

    public void showConversationsFragment()
    {
        //Load word fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new ConversationsFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_conversation));

        fab.setVisibility(View.INVISIBLE);
    }

    public void showMyProfileFragment()
    {
        //Pass user object to profile fragment
        User user=userAccount.getUser();
        getIntent().putExtra("User",user );

        //Load profile fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new MyProfileFragment())
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
        setTitle(getResources().getString(R.string.title_about));

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

    protected void showResultsFragment()
    {
        //Load list fragment
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame , new ResultsFragment())
                .commit();
        setTitle(getResources().getString(R.string.title_results));

        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        fab.setVisibility(View.INVISIBLE);
    }

}
