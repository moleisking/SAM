package me.minitrabajo.view;
/*
* Note
* 1) implementations of Preference.OnPreferenceClickListener do not fire
* 2) error related to network error on android device. A hard reboot of device can fix the issue.
 * V/Get:IOException: Unable to resolve host "minitrabajo.me": No address associated with hostname
* */
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.preference.PreferenceFragment;
//import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;

import me.minitrabajo.R;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseGPS;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.UserAccount;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener ,ResponseAPI, ResponseGPS
{

    private RadioButton radNotification;
    private RadioGroup radDefaultSearch;
    private SharedPreferences sharedPreferences;
    private UserAccount userAccount;
    private Categories categories;
    private Context parentContext;
    private LatLng currentLatLng;
    private GPS gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        //Initialize objects
        parentContext = this.getActivity();
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        //categories = new Categories(parentContext);

        //Note* User account set in MainActivity
        userAccount = ((MainActivity)getActivity()).getUserAccount();

        //Start GPS sensor
        currentLatLng = new LatLng(0.0d,0.0d);
        gps = new GPS(getActivity());
        gps.delegate = this;
        gps.Start();


        try
        {
            //Set build version value
            PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0);
            String version = packageInfo.versionName;
            Preference pref = findPreference("pref_item_build_version");
            pref.setSummary(version);

            //Set action listeners
            Preference prefDeleteStoredAccount = findPreference("pref_item_delete_stored_account");
            prefDeleteStoredAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onDeleteStoredAccount()");
                    onDeleteStoredAccount();
                    return true;
                }
            });

            Preference prefRefreshStoredAccount = findPreference("pref_item_refresh_stored_account");
            prefRefreshStoredAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onRefreshStoredAccount()");
                    onRefreshStoredAccount();
                    return true;
                }
            });

            Preference prefRefreshCategories = findPreference("pref_item_refresh_categories");
            prefRefreshCategories.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onRefreshCategories()");
                    onCategoriesRefresh();
                    return true;
                }
            });

            Preference prefEditProfile = findPreference("pref_item_edit_profile");
            prefEditProfile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onRefreshCategories()");
                    onShowAccountFragment();
                    return true;
                }
            });

            Preference prefChangePassword = findPreference("pref_item_change_password");
            prefChangePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onChangePassword()");
                    onChangePassword();
                    return true;
                }
            });

            Preference prefAboutUs = findPreference("pref_item_about_us");
            prefAboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onShowAboutFragment()");
                    onShowAboutFragment();
                    return true;
                }
            });

            Preference prefSetDefaultLocation = findPreference("pref_item_refresh_registered_location");
            prefSetDefaultLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onSetRegisteredLocation()");
                    onSetRegisteredLocation();
                    return true;
                }
            });

            Preference prefSetDistanceUnit = findPreference("pref_item_distance_unit");
            prefSetDistanceUnit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate","onSetDistanceUnit()");
                    onSetDistanceUnit();
                    return true;
                }
            });

            Preference prefTermsAndConditions = findPreference("pref_item_terms_and_conditions");
            prefTermsAndConditions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate"," onTermsAndConditions()");
                    onTermsAndConditions();
                    return true;
                }
            });

            Preference prefPrivacyPolicyAndDataProtection = findPreference("pref_item_privacy_policy_and_data_protection");
            prefPrivacyPolicyAndDataProtection.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.v("Setting:onCreate"," onPrivacyPolicyAndDataProtection()");
                    onPrivacyPolicyAndDataProtection();
                    return true;
                }
            });
        }
        catch (Exception ex)
        {
            Log.v("Setting:onCreate:Err",ex.getMessage());
        }
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Setting:processFinish", output);
        try
        {
            String header = output.substring(0,50).toLowerCase();
            if (output.length()>=50)
            {
                if(header.contains("authentication failed"))
                {
                    //Found fail to get token
                    Log.v("SettingFragment", "Token authentication failed");
                    Toast.makeText(this.getActivity(), "Authentication failed", Toast.LENGTH_LONG).show();
                }
                else if (header.contains("token"))
                {
                    //Found token
                    Log.v("SettingFragment", "Token found");
                    Toast.makeText(this.getActivity(), "Authentication success", Toast.LENGTH_LONG).show();
                }
                else if (header.contains("myprofile"))
                {
                    //Found profile
                    userAccount.loadFromJSON(output);
                    userAccount.saveToFile();
                    userAccount.print();

                    //Set Navigation Profile
                    setNavigationProfile();

                    //Communicate change to end user
                    Log.v("SettingFragment", "UserAccount found");
                    Toast.makeText(this.getActivity(), "Stored Account Refreshed", Toast.LENGTH_LONG).show();
                }
                else if (header.contains("categories"))
                {
                    //Found category
                    categories.loadFromJSON(output);
                    categories.saveToFile();
                    categories.print();

                    //Communicate change to end users
                    Log.v("SettingFragment", "Categories found");
                    Toast.makeText(this.getActivity(), "Categories Refreshed", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                //Found no reply or null
                Log.v("SettingFragment", "No Reply");
                Log.v("SettingFragment", output);
            }

        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }
    }

    @Override
    public void onGPSConnectionResolutionRequest(ConnectionResult connectionResult )
    {
        try
        {
            connectionResult.startResolutionForResult(this.getActivity(), ResponseGPS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        }
        catch (Exception ex)
        {
            Log.v("Account:onGPSConFail",ex.getMessage());
        }
    }

    @Override
    public void onGPSWarning(String message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPSPositionResult(LatLng position) {
        try{
            this.currentLatLng = position;
            Log.v("Search:GPSPosRes", position.toString());
            gps.Stop();
        }
        catch (Exception ex)
        {
            Log.v("Search:GPSPosRes:Err", ex.getMessage());
        }
    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //Fired when actual value changes
        if (key.equals("pref_item_privacy_policy"))
        {
            //Show your AlertDialog here!
        }
        else if (key.equals("pref_item_terms_of_user"))
        {

        }
        else if (key.equals("pref_item_delete_stored_account"))
        {

        }
        else if (key.equals("pref_item_delete_stored_account"))
        {
            //onDeleteStoredAccount();
        }


        Log.v("PreferenceChanged",key);
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
    }

    /*private void onDefaultResultClick(View v)
    {
        // Is the button now checked?
        int iselected = ((RadioGroup) v).getCheckedRadioButtonId();
        View checked = v.findViewById(iselected);

        // Check which radio button was clicked
        //if (checked.getId() ==  R.id.radMap)
        //{
        //        //ShowMessage("Save Map option");
        //}
        //else if (checked.getId() ==  R.id.radList)
        //{
        //    //ShowMessage("Save List option");
        //}
    }*/

    protected void onSaveClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete your stored account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete local file
                        Toast.makeText(parentContext , "Yes clicked", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(parentContext , "No clicked", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    protected void onTestClick()
    {
        Log.v("Setting:onTestClick","Clicked");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Run my test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete local file
                        Toast.makeText(parentContext , "Yes clicked", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(parentContext , "No clicked", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    protected void onDeleteStoredAccount()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete your stored account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete local file
                        //userAccount.deleteFile();
                        ((MainActivity)getActivity()).getUserAccount().deleteFile();
                        System.exit(0);
                        Toast.makeText(parentContext , "Stored Account Deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    protected void onRefreshStoredAccount()
    {
        //Get file from Post Call
        Log.v("SettingFragment","onRefreshStoredAccount()");
        String url = getResources().getString(R.string.url_get_user_account_profile);
        GetAPI asyncTask =new GetAPI(this.getActivity()); //Could be problem in the future with SSL here
        asyncTask.delegate = this;
        asyncTask.execute(url,"",userAccount.getToken());
    }

    protected void onSetDistanceUnit()
    {
        Log.v("SettingFragment","onSetDistanceUnit()");
        sharedPreferences.edit().putString("distance_unit", "Elena");
        sharedPreferences.edit().commit();
    }

    protected void onCategoriesRefresh()
    {
        Log.v("SettingFragment","onRefreshCategories()");
        String url = this.getActivity().getString(R.string.url_get_categories);
        GetAPI asyncTask =new GetAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,"","");
    }

    protected void onSetRegisteredLocation()
    {
        Log.v("Setting:Position",currentLatLng.toString());
    }

    protected void onChangePassword()
    {
        //Get file from Post Call
        Log.v("SettingFragment","onChangePassword()");
        String url = getResources().getString(R.string.url_get_user_account_password_change);
        GetAPI asyncTask =new GetAPI(this.getActivity()); //Could be problem in the future with SSL here
        asyncTask.delegate = this;
        asyncTask.execute(url,"",userAccount.getToken());
    }

    protected void onShowAccountFragment()
    {
        ((MainActivity)getActivity()).showAccountFragment();
    }

    protected void onShowAboutFragment()
    {
        ((MainActivity)getActivity()).showAboutFragment();
    }

    /*protected void onNotificationClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rad1km:
                if (checked)
                    break;
            case R.id.rad5km:
                if (checked)
                    break;
        }
    }*/

    protected void onPrivacyPolicyAndDataProtection()
    {
        Log.v("Setting:onTerm&Con","Started");
        String text = "";
        try {
            String fileName = getResources().getString(R.string.file_privacy_policy_and_data_protection);
            InputStream is = this.getActivity().getAssets().open(fileName);

            // Read the entire asset into a local byte buffer.
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            text = new String(buffer);
            Log.v("Setting:onPriv&Pol",text);
        }
        catch (IOException e)
        {
            // Should never happen!
            Log.v("Setting:onPriv&Pol","File not found");
            throw new RuntimeException(e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(text)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(parentContext , "Stored Account Deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    protected void  onTermsAndConditions()
    {
        Log.v("Setting:onTerm&Con","Started");
        String text = "";
        try {
            String fileName = getResources().getString(R.string.file_terms_and_conditions);
            InputStream is = this.getActivity().getAssets().open(fileName);

            // Read the entire asset into a local byte buffer.
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            text = new String(buffer);
            Log.v("Setting:onTerm&Con",text);
        }
        catch (IOException e)
        {
            // Should never happen!
            Log.v("Setting:onTerm&Con","File not found");
            throw new RuntimeException(e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(text)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(parentContext , "Stored Account Deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    public void setNavigationProfile()
    {
        //Set Navigation Profile
        View headerLayout = ((NavigationView)this.getActivity().findViewById(R.id.nav_view)).getHeaderView(0);
        ImageView imgAvatar = (ImageView)headerLayout.findViewById(R.id.nav_head_imgAvatar);
        TextView txtName = (TextView)headerLayout.findViewById(R.id.nav_head_txtName);
        TextView txtDescription = (TextView)headerLayout.findViewById(R.id.nav_head_txtDescription);
        imgAvatar.setImageBitmap(userAccount.getImageAsBitmap());
        txtName.setText(userAccount.getName());
        txtDescription.setText(userAccount.getDescription());
    }

    public void setUserAccount(UserAccount user_account)
    {
        userAccount = user_account;
        Log.v("UserAccount",userAccount.toString());
    }

}
