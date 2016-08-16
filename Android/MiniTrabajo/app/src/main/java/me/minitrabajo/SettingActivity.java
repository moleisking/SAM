package me.minitrabajo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {

    private Button btnResetPassword,btnContactUs;
    private RadioButton radNotification;
    private RadioGroup radDefaultSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Define Objects
        btnResetPassword = (Button)findViewById(R.id.btnResetPassword);
        btnContactUs = (Button)findViewById(R.id.btnContactUs);
        radNotification = (RadioButton) findViewById(R.id.radNotification);
        radDefaultSearch = (RadioGroup)findViewById(R.id.radDefaultSearch);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void onDefaultResultClick(View v)
    {
        // Is the button now checked?
        int iselected = ((RadioGroup) v).getCheckedRadioButtonId();
        View checked = v.findViewById(iselected);

        // Check which radio button was clicked
        if (checked.getId() ==  R.id.radMap)
        {
                ShowMessage("Save Map option");
        }
        else if (checked.getId() ==  R.id.radList)
        {
            ShowMessage("Save List option");
        }


    }

    private void onContactUsClick(View v)
    {

    }

    private void onSaveClick(View v)
    {

    }

    public void onNotificationClicked(View view) {
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

    public void ShowMessage(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowAboutUs()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Application created by Scott Johnston");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
