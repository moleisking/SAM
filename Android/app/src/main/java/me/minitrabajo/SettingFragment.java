package me.minitrabajo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{

    private RadioButton radNotification;
    private RadioGroup radDefaultSearch;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        sharedPreferences = getPreferenceScreen().getSharedPreferences();

        //Set build version value
        try
        {
            PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0);
            String version = packageInfo.versionName;
            Preference pref = findPreference("pref_item_build_version");
            pref.setSummary(version);

           // editTextPref
            //        .setSummary(sp.getString("thePrefKey", "Some Default Text"));
        }
        catch (Exception ex)
        {
            Log.v("SettingFragment:Err",ex.getMessage());
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
        if (key.equals("pref_item_privacy_policy"))
        {
            //Show your AlertDialog here!
        }
        else if (key.equals("pref_item_terms_of_user"))
        {

        }
        else if (key.equals("pref_item_delete_stored_account"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to delete your stored account?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //sharedPreferences. preference =  getPreferences(Context.MODE_PRIVATE);
                            //sharedPreferences.Editor editor = preference.edit();
                            //editor.putString(ACCOUNT_TOKEN, token);
                           // editor.commit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

        }


            Log.v("PreferenceChanged",key);
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
    }

    private void onDeleteStoredAccount()
    {

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

    protected void onSaveClick(View view) {

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

}
