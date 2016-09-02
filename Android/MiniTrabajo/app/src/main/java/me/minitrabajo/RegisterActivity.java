package me.minitrabajo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

//http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
//http://blog.teamtreehouse.com/beginners-guide-location-android

public class RegisterActivity extends AppCompatActivity implements ResponseAPI , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private FloatingActionButton btnRegister;
    private TextView txtName, txtPassword, txtAddress, txtEmail, txtPrice;
    private ImageView imgProfile;

    private User mUser;
   // private long id;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_LOCATION = 2;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private String selectedImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnRegister);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onRegisterClick(view);
            }
        });

        //Define Objects
        txtName = (TextView)findViewById(R.id.txtName);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        btnRegister = (FloatingActionButton) findViewById(R.id.btnRegister);

        //Set Default Settings
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);



        //Test
        txtName.setText("scott");
        txtPassword.setText("12345");
        txtEmail.setText("moleisking@gmail.com");

        Log.w("onCreate", "Create the GoogleApiClient object");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Log.w("onCreate", "Create the LocationRequest object");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
           /* case R.id.btnHome:
                NavUtils.navigateUpFromSameTask(this);*/
           /* case R.id.btnSearch:
                this.startActivity(new Intent(this,SettingActivity.class));
                return true;*/
           /* case R.id.btnAbout:
                Toast.makeText(RegisterActivity.this,R.string.app_description,Toast.LENGTH_SHORT).show();
                return true;*/
           /* default:
                return super.onOptionsItemSelected(menuItem);
        }
    }*/

    public void onRegisterClick(View view)
    {
        //Example: "name=scott&pass=12345&email=moleisking%40gmail.com";
        Log.w("onRegisterClick", "Register button clicked");
        String url = getResources().getString(R.string.net_register_url); //"http://192.168.1.100:3003/api/signup";
        Bitmap bitmap = ((BitmapDrawable)imgProfile.getDrawable()).getBitmap();
        String parameters = "name=" + txtName.getText().toString() +
                            "&pass=" + txtPassword.getText().toString() +
                            "&email=" + txtEmail.getText().toString().replace("@","%40")  +
                            "&address=" + txtAddress.getText().toString() +
                            "&rate=" + txtAddress.getText().toString() +
                            "&latitude=" + String.valueOf(currentLatitude) +
                            "&longitude=" + String.valueOf(currentLongitude) +
                            "&image=" + ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
        PostAPI asyncTask =new PostAPI();
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,"");
    }

    private String base64Encode(Bitmap image)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private  Bitmap base64Decode(String str)
    {
        byte[] decodedString = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public void processFinish(String output)
    {
        //Here you will receive the result fired from async class
        //Example Reply: {
        //"data": {
        //  "name": "username",
        //  "pass": "$2a$10$oCHXQeU4SsMCquduC2E8Y.ehM7vrzKQJmUz0PuZTlvbAjijLam4O6"
        //  }
        //}
        Log.w("processFinish", output);
        String name = "",pass="";
        try
        {
            JSONObject myJson = new JSONObject(output);
            name = myJson.optString("name");
            pass = myJson.optString("pass");
        }
        catch (Exception ex)
        {
            Log.w("Register:ProFin", ex.getMessage());
        }

        if (!name.equals("")&&!pass.equals(""))
        {
            //on successful register go to Login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i("Location", "Location services connected.");

        if ( ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Log.v("Position:con",String.valueOf(currentLatitude)+":"+ String.valueOf(currentLongitude));
        }
    }


    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i("Location", "Location services suspended. Please reconnect.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Log.i("onResume()", "Called");
    }

    @Override
    protected void onPause()
    {
        Log.v("onPause()", "Called");
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        Log.v("onConnectionFailed()", "Failed");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.v("Position:cha",String.valueOf(currentLatitude)+":"+ String.valueOf(currentLongitude));

    }

    /**
     *  Load Image into ImageView
     */

    public void onImageUpload(View image)
    {
        Log.v("onImageUpload:Click","Clicked" );
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload image")
                .setItems(R.array.image_source, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      switch (which)
                        {
                            case 0:
                            {
                                Log.v("onImageUpload:Click","Camera" );
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            }
                            case 1:
                            {
                                Log.v("onImageUpload:Click","Library" );
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                            }
                            case 2:
                            {
                                Log.v("onImageUpload:Click","Cancel" );
                            }
                            default:
                            {
                                Log.v("onImageUpload:Click", String.valueOf(which) );
                            }
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgProfile.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(thumbnail);
    }



    //@Override
    //public void processCanceled() { //Nothing }

}
