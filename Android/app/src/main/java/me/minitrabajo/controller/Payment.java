package me.minitrabajo.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import me.minitrabajo.R;
import me.minitrabajo.model.UserAccount;
import me.minitrabajo.model.Users;
import me.minitrabajo.view.MainActivity;

/**
 * Created by Scott on 14/10/2016.
 */
public class Payment implements ResponseAPI
{
    private transient Context context;
    private UserAccount userAccount;

    public ResponsePay delegate = null;

    public Payment(Context context, UserAccount userAccount)
    {
        super();
        this.context = context;
        this.userAccount = userAccount;
    }

    @Override
    public void processFinish(String output)
    {
        Log.w("Payment:processFinish", output);

        try
        {
            //Get JSON and add to object
            JSONObject myJson = new JSONObject(output);
            Double creditReply = myJson.getDouble("addcredit");
            delegate.paymentSuccess(creditReply);
            //Add code to show what happens after payment.
        }
        catch (Exception ex)
        {
            Log.w("Search:Process:Err", ex.getMessage());
        }
    }

    protected void onPayment(Double value)
    {
        Log.v("Payment:onPayment()","Post");
        String url = context.getResources().getString(R.string.url_post_payment);
        String parameters = "value="+ String.valueOf(value);
        Log.v("Payment:Parameters ",parameters );
        PostAPI asyncTask =new PostAPI(context);
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,userAccount.getToken());
    }
}
