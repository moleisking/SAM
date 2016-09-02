package me.minitrabajo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import android.app.Fragment;

public class AboutFragment extends Fragment {

    private  FloatingActionButton btnEmail;
    private TextView txtName, txtDescription, txtAddress;
    private ImageView imgItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Define Objects
        txtName = (TextView)container.findViewById(R.id.txtName);
        txtDescription = (TextView)container.findViewById(R.id.txtDescription);
        txtAddress = (TextView)container.findViewById(R.id.txtAddress);
        imgItem = (ImageView)container.findViewById(R.id.imgItem);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    public void onEmailClick(View view)
    {
        Snackbar.make(view, "Email", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
