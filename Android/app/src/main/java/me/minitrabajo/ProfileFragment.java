package me.minitrabajo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ObjectInputStream;

/*Note: No calls to server through ResponseAPI. User object passed from ListFragment*/
public class ProfileFragment extends Fragment {

    private FloatingActionButton btnMessage;
    private TextView txtName, txtDescription, txtAddress, txtDayRate, txtHourRate;
    private ImageView imgProfile;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Define Objects
        mUser = (User)getActivity().getIntent().getSerializableExtra("User");
        imgProfile = (ImageView)container.findViewById(R.id.imgItem);
        txtName = (TextView)container.findViewById(R.id.txtName);
        txtDescription = (TextView)container.findViewById(R.id.txtDescription);
        txtAddress = (TextView)container.findViewById(R.id.txtAddress);
        txtHourRate = (TextView)container.findViewById(R.id.txtHourRate);
        txtDayRate = (TextView)container.findViewById(R.id.txtDayRate);
        btnMessage = (FloatingActionButton) container.findViewById(R.id.btnMessage);

        //Fill Objects
        txtName.setText(mUser.getName());
        txtDescription.setText(mUser.getDescription());
        txtAddress.setText(mUser.getAddress());
        txtHourRate.setText(Double.toString(mUser.getHourRate()));
        txtDayRate.setText(Double.toString(mUser.getDayRate()));
        imgProfile.setImageBitmap(mUser.getImageAsBitmap());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onMessageClick(View view)
    {
        //Send message to user
        Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
