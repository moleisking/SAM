package me.minitrabajo.view;

import android.app.Fragment;
import android.os.Bundle;
//import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import me.minitrabajo.R;
import me.minitrabajo.model.User;

/*Note: No calls to server through ResponseAPI. User object passed from ListFragment*/
public class ProfileFragment extends Fragment {

    private TextView txtName, txtDescription, txtAddress, txtDayRate, txtHourRate;
    private ImageView imgProfile;
    private RatingBar ratScore;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.v("Profile:onCreate","Started");
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout )inflater.inflate(R.layout.fragment_profile, container, false);

        //Define Objects
        user = new User(getActivity());
        user = (User)getActivity().getIntent().getSerializableExtra("User");
        user.print();

        imgProfile = (ImageView)ll.findViewById(R.id.imgProfile);
        txtName = (TextView) ll.findViewById(R.id.txtName);
        txtDescription = (TextView)ll.findViewById(R.id.txtDescription);
        txtAddress = (TextView)ll.findViewById(R.id.txtAddress);
        txtHourRate = (TextView)ll.findViewById(R.id.txtHourRate);
        txtDayRate = (TextView)ll.findViewById(R.id.txtDayRate);
        ratScore = (RatingBar)ll.findViewById(R.id.ratScore);
        //btnMessage = (FloatingActionButton) container.findViewById(R.id.btnMessage);

        try {
            Log.v("ProfileFragment","Start try");
            //Fill Objects
            txtName.setText(user.getName());
            txtDescription.setText(user.getDescription());
            txtAddress.setText(user.getAddress());
            txtHourRate.setText(Double.toString(user.getHourRate()));
            txtDayRate.setText(Double.toString(user.getDayRate()));
            imgProfile.setImageBitmap(user.getImageAsBitmap());
        }
        catch (Exception ex)
        {
            Log.v("ProfileFragment",ex.getMessage());
        }


        return ll;
    }

    public void onMessageClick(View view)
    {
        try
        {
            ((MainActivity)getActivity()).showMessageFragment();
        }
        catch (Exception ex)
        {
            Log.v("Profile:onMessage:Err", ex.getMessage());
        }

        Toast.makeText(this.getActivity(), "Message sent", Toast.LENGTH_LONG).show();
    }
}
