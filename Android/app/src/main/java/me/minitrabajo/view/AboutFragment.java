package me.minitrabajo.view;

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
import android.widget.Toast;

import me.minitrabajo.R;
import me.minitrabajo.model.User;

//import android.app.Fragment;

public class AboutFragment extends Fragment {

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
        imgItem = (ImageView)container.findViewById(R.id.imgLogo);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    public void onMessageClick(View view)
    {
        User user = new User("0","Administrator", "moleisking@gmail.com");
        this.getActivity().getIntent().putExtra("User", user );
        ((MainActivity)getActivity()).showMessagesFragment();
        Toast.makeText(this.getActivity(), "Message", Toast.LENGTH_LONG).show();
    }

}
