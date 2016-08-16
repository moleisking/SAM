package me.minitrabajo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private Button btnSave;
    private TextView txtName, txtText, txtAddress, txtPrice;
    private ImageView imgItem;
    private Users mUsers;
    private int position;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Define Objects
        txtName = (TextView)findViewById(R.id.txtName);
        txtText = (TextView)findViewById(R.id.txtText);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        btnSave = (Button)findViewById(R.id.btnSave);
        imgItem = (ImageView)findViewById(R.id.imgItem);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onSaveClick(View v) {

    }

}
