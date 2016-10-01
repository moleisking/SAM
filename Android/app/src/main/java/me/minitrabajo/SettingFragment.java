package me.minitrabajo;
/*
* Note
* 1) implementations of Preference.OnPreferenceClickListener do not fire
* 2) error related to network error on android device. A hard reboot of device can fix the issue.
 * V/Get:IOException: Unable to resolve host "minitrabajo.me": No address associated with hostname
* */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
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

import org.json.JSONObject;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener ,ResponseAPI
{

    private RadioButton radNotification;
    private RadioGroup radDefaultSearch;
    SharedPreferences sharedPreferences;
    private UserAccount mUserAccount;
    private Context mParentContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        //Get Parent Context
        mParentContext = this.getActivity();

        //Get saved settings
        sharedPreferences = getPreferenceScreen().getSharedPreferences();

        //Note* User account set in MainActivity
        mUserAccount = new UserAccount(this.getActivity());
        mUserAccount = (UserAccount) getActivity().getIntent().getSerializableExtra("UserAccount");

        //Set build version value
        try
        {
        PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0);
        String version = packageInfo.versionName;
        Preference pref = findPreference("pref_item_build_version");
        pref.setSummary(version);
        }
        catch (Exception ex)
        {
            Log.v("onCreate:PackInfo",ex.getMessage());
        }

        //Set action listeners
       Preference prefDeleteStoredAccount = findPreference("pref_item_delete_stored_account");
        prefDeleteStoredAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.v("SettingFragment","onDeleteStoredAccount()");
                onDeleteStoredAccount();
                return true;
            }
        });

        Preference prefRefreshStoredAccount = findPreference("pref_item_refresh_stored_account");
        prefRefreshStoredAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.v("SettingFragment","onRefreshStoredAccount()");
                onRefreshStoredAccount();
                return true;
            }
        });

        Preference prefChangePassword = findPreference("pref_item_change_password");
        prefChangePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Log.v("SettingFragment","onChangePassword()");
                onChangePassword();
                return true;
            }
        });
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("processFinish", output);
        try
        {
            mUserAccount.loadFromJSON(output);
            mUserAccount.saveToFile();
            mUserAccount.print();

            //Set Navigation Profile
            View headerLayout = ((NavigationView)this.getActivity().findViewById(R.id.nav_view)).getHeaderView(0);
            ImageView imgAvatar = (ImageView)headerLayout.findViewById(R.id.nav_head_imgAvatar);
            TextView txtName = (TextView)headerLayout.findViewById(R.id.nav_head_txtName);
            TextView txtDescription = (TextView)headerLayout.findViewById(R.id.nav_head_txtDescription);
            imgAvatar.setImageBitmap(mUserAccount.getImageAsBitmap());
            txtName.setText(mUserAccount.getName());
            txtDescription.setText(mUserAccount.getDescription());

        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }
        Toast.makeText(this.getActivity(), "Stored Account Refresh", Toast.LENGTH_LONG).show();
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

    private void onDefaultResultClick(View v)
    {
        // Is the button now checked?
        int iselected = ((RadioGroup) v).getCheckedRadioButtonId();
        View checked = v.findViewById(iselected);

        // Check which radio button was clicked
        /*if (checked.getId() ==  R.id.radMap)
        {
                //ShowMessage("Save Map option");
        }
        else if (checked.getId() ==  R.id.radList)
        {
            //ShowMessage("Save List option");
        }*/


    }

    protected void onSaveClick(View view)
    {

    }

    protected void onDeleteStoredAccount()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete your stored account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Delete local file
                        mUserAccount.deleteFile();
                        Toast.makeText(mParentContext , "Stored Account Deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
    }

    protected void onRefreshStoredAccount()
    {
        //Get file from Post Call
        Log.v("SettingFragment","onRefreshStoredAccount()");
        String url = getResources().getString(R.string.url_get_user_account_profile);
        GetAPI asyncTask =new GetAPI(this.getActivity()); //Could be problem in the future with SSL here
        asyncTask.delegate = this;
        asyncTask.execute(url,"",mUserAccount.getToken());
    }

    protected void onChangePassword()
    {
        //Get file from Post Call
        Log.v("SettingFragment","onChangePassword()");
        String url = getResources().getString(R.string.url_get_user_account_password_change);
        GetAPI asyncTask =new GetAPI(this.getActivity()); //Could be problem in the future with SSL here
        asyncTask.delegate = this;
        asyncTask.execute(url,"",mUserAccount.getToken());
    }

    protected void onNotificationClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rad1km:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.rad5km:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    public void setUserAccount(UserAccount user_account)
    {
        mUserAccount = user_account;
        Log.v("UserAccount",mUserAccount.toString());
    }

}
