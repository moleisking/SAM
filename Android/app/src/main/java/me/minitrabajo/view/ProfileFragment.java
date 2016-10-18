package me.minitrabajo.view;

import android.app.Fragment;
import android.os.Bundle;
//import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import me.minitrabajo.R;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.model.User;

/*Note: No calls to server through ResponseAPI for profile. User object passed from ResultsFragment. UserAccount userd to post rating though.*/
public class ProfileFragment extends Fragment implements ResponseAPI
{

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
        ratScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratScore.getNumStars();

            }
        });
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

        //Hide keyboard
        this.getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return ll;
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Payment:processFinish", output);

        try
        {
            //Get JSON and add to object
            JSONObject myJson = new JSONObject(output);

            //fix later when postmaster understood
            if(output.contains("fail"))
            {
                Log.w("Profile:Process", "Fail");
            }
            else
            {
                Log.w("Profile:Process", "Success");
            }

            //Add code to show what happens after payment.
        }
        catch (Exception ex)
        {
            Log.w("Search:Process:Err", ex.getMessage());
        }
    }

    public void onMessageClick(View view)
    {
        try
        {
            //Send user and user account to main
            this.getActivity().getIntent().putExtra("User", user );
            ((MainActivity)getActivity()).showMessagesFragment();
        }
        catch (Exception ex)
        {
            Log.v("Profile:onMessage:Err", ex.getMessage());
        }
    }

    public void onSendRating(int id, int number)
    {
        Log.v("Profile:onSendRating()","Post");
        String url = this.getActivity().getResources().getString(R.string.url_post_rating);
        String parameters = "id"+ String.valueOf(id)+ "number="+ String.valueOf(number);
        Log.v("Profile:Parameters ",parameters );
        PostAPI asyncTask =new PostAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters, ((MainActivity)getActivity()).getUserAccount().getToken());
    }
}
