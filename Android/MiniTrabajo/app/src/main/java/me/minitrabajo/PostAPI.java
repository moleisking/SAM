/*    -Example of Authenticate-
   POST /authenticate HTTP/1.1
   Host: localhost:3003
   Cache-Control: no-cache
   Postman-Token: 87497dd5-0cf0-375f-213f-83818a54a1a0
   Content-Type: application/x-www-form-urlencoded
   name=scott&pass=12345&email=moleisking%40gmail.com
   */

package me.minitrabajo;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
//import com.google.common.net.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.*;

//http://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
//https://www.youtube.com/watch?v=ryY7Dy3z-7Q
//http://stackoverflow.com/questions/2938502/sending-post-data-in-android
//https://blog.dahanne.net/2009/08/16/how-to-access-http-resources-from-android/



public class PostAPI extends AsyncTask <String, String, String>
{
    //Call back interface
    public ResponseAPI delegate = null;


    //private Context mContext;
    //private String parameters = "name=scott&pass=12345&email=moleisking%40gmail.com";

	/*public  APIConnect()
	{
		//mUrl = url;
		//mContext = context;
	}*/


    @Override
    protected void onPreExecute() { super.onPreExecute(); }


    @Override
    protected String doInBackground(String... params)
    {
        Log.w("doInBackground", "Start");
        if(params.length >=2)
        {
            Log.w("doInBackground[0]", params[0]);
            Log.w("doInBackground[1]", params[1]);

            HttpURLConnection connection = null;
            String reply = "No Reply";

            try {
                //Set Network IO
                URL url = new URL(params[0].toString());
                connection = (HttpURLConnection) url.openConnection();

                //Build Header
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setFixedLengthStreamingMode(params[1].getBytes().length);
                Log.w("doInBackground:HEAD", "Built");

                //Write Parameters
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(params[1]);
                out.close();
                Log.w("doInBackground:PARAM", "Sent");

                //Get Reply Status 400, 404, 200
                String http_code = String.valueOf(connection.getResponseCode());
                String http_message = connection.getResponseMessage();
                Log.w(http_code, http_message);

                //Read Response
                InputStream in = connection.getInputStream();
                reply = this.convertStreamToString(in);
                Log.w("doInBackground:REPLY", reply);
            } catch (IOException ioe) {
                Log.v("Post:IOException", ioe.getMessage());
            } catch (Exception ex) {
                Log.v("Post:Exception", ex.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                Log.w("doInBackground", "Connection Closed");
            }

            return reply;
        }
        else
        {
            Log.w("doInBackground", "Not Enough Parameters");
            return "Not Enough Parameters";
        }
    }


    @Override
    protected void onPostExecute(String result) { delegate.processFinish(result); }

    /*@Override
    protected void onCancelled() { delegate.processCancel(); }*/

	private String convertStreamToString(java.io.InputStream is)
    {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }

}