package me.minitrabajo.view;

import android.util.Log;
import android.view.View;

/**
 * Created by Scott on 20/10/2016.
 */
public class MyProfileFragment extends ProfileFragment
{
    public void onEditClick(View view)
    {
        try
        {
            ((MainActivity)getActivity()).showAccountFragment();
        }
        catch (Exception ex)
        {
            Log.v("MyProfile:onMessage:Err", ex.getMessage());
        }
    }
}
