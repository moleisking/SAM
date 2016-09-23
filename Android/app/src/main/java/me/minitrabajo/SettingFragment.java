package me.minitrabajo;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v4.preference.PreferenceFragment;
//import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingFragment extends PreferenceFragment {

    private FloatingActionButton btnResetPassword,btnContactUs, btnSave;
    private RadioButton radNotification;
    private RadioGroup radDefaultSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Define Objects
        //btnResetPassword = (FloatingActionButton)container.findViewById(R.id.btnResetPassword);
        //btnContactUs = (FloatingActionButton)container.findViewById(R.id.btnContactUs);
        //btnSave = (FloatingActionButton)container.findViewById(R.id.btnSave);
        //radNotification = (RadioButton)container.findViewById(R.id.radNotification);
        //radDefaultSearch = (RadioGroup)container.findViewById(R.id.radDefaultSearch);

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }*/

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
