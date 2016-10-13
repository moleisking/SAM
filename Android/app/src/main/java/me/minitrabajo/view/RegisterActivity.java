package me.minitrabajo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;
/*import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;*/

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.minitrabajo.R;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseGPS;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.User;
import me.minitrabajo.model.UserAccount;

//http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
//http://blog.teamtreehouse.com/beginners-guide-location-android

public class RegisterActivity extends AppCompatActivity implements ResponseAPI,ResponseGPS {

    private FloatingActionButton btnRegister;
    private TextView txtName, txtPassword, txtAddress, txtEmail, txtDayRate, txtHourRate;
    private ImageView imgProfile;
    private LatLng currentLatLng;

    private User mUser;
   // private long id;

    //Define a request code to send to Google Play services
   /* private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;//was 0
    private static final int REQUEST_LOCATION = 2;
    private final static int REQUEST_LOCATION_SETTING = 199;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;*/
    //private PendingResult<LocationSettingsResult> mLocationSettingRequestResult;
    private double currentLatitude;
    private double currentLongitude;
    private String selectedImagePath;
    private final static int REQUEST_LOCATION_SETTING = 199;
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;//was 0
    private static final int SELECT_FILE = 1;
    private Categories categories;
    private GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnRegister);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onRegisterClick(view);
            }
        });

        //Start GPS
        gps = new GPS (this);
        gps.delegate = this;
        gps.Start();

        //Define Objects
        txtName = (TextView)findViewById(R.id.txtName);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtHourRate = (TextView)findViewById(R.id.txtHourRate);
        txtDayRate = (TextView)findViewById(R.id.txtDayRate);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        btnRegister = (FloatingActionButton) findViewById(R.id.btnRegister);

        //Set Default Settings
        //setSupportActionBar(toolbar);


        //Test
        txtName.setText("scott");
        txtPassword.setText("12345");
        txtEmail.setText("moleisking@gmail.com");


        isNetworkConnected();

        //Check Permissions
        if ( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        categories = new Categories(this);
        if(categories.hasFile())
        {
            Log.w("onCreate", "Categories load from file");
            categories.loadFromFile();
        }
        else
        {
            Log.w("onCreate", "Categories load from backend");
            this.loadCategoryFromBackend();
        }
        categories.print();
    }

    public void loadCategoryFromBackend()
    {
        String url = this.getString(R.string.url_get_categories);
        GetAPI asyncTask =new GetAPI(this);
        asyncTask.delegate = this;
        asyncTask.execute(url,"","");
    }

    public boolean isNetworkConnected()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) )
        {
            Log.d("isNetworkConnected", "NETWORK UNAVAILABLE");
            showNoNetworkAlert();
            return false;
        }
        else if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
        {
            Log.d("isNetworkConnected", "GPS UNAVAILABLE");
            showNoGpsAlert();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void showNoGpsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showNoNetworkAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Network seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       // super.onActivityResult(requestCode, resultCode, data);
        Log.d("Reg:onActivityResult", Integer.toString(resultCode));
        //gps.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == Activity.RESULT_OK) && (requestCode == SELECT_FILE))
        {
            onSelectFromGalleryResult(data);
        }
        else if ((resultCode == Activity.RESULT_OK) && (requestCode == REQUEST_IMAGE_CAPTURE))
        {
            onCaptureImageResult(data);
        }
        else if ((resultCode == Activity.RESULT_CANCELED) && (requestCode == REQUEST_IMAGE_CAPTURE))
        {
            Toast.makeText(this, "Camera image cancelled by user.", Toast.LENGTH_LONG).show();
        }
        else if ((resultCode == Activity.RESULT_OK) && (requestCode == REQUEST_LOCATION_SETTING))
        {
            Toast.makeText(this, "Location enabled by user!", Toast.LENGTH_LONG).show();
        }

        else if ((resultCode == Activity.RESULT_CANCELED) && (requestCode == REQUEST_LOCATION_SETTING))
        {
            Toast.makeText(this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
        }
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
        UserAccount userAccount  = new UserAccount(this);
        userAccount.setName(txtName.getText().toString());
        userAccount.setPassword(txtPassword.getText().toString());
        userAccount.setEmail(txtEmail.getText().toString());
        userAccount.setAddress(txtAddress.getText().toString());
        userAccount.setHourRate(Double.parseDouble(txtHourRate.getText().toString()));
        userAccount.setDayRate(Double.parseDouble(txtDayRate.getText().toString()));
        userAccount.setRegisteredLatLng(currentLatLng);
        userAccount.setImageRaw(((BitmapDrawable) imgProfile.getDrawable()).getBitmap());

        //Example: "name=scott&pass=12345&email=moleisking%40gmail.com";
        Log.w("onRegisterClick", "Register button clicked");
        String url = getResources().getString(R.string.url_post_user_account_register);
        String parameters = userAccount.getUserAsParameters();
        PostAPI asyncTask =new PostAPI(this);
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,"");
    }

    /*private String base64Encode(Bitmap image)
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
    }*/
    @Override
    public void onGPSConnectionResolutionRequest(ConnectionResult connectionResult )
    {
        try {
            connectionResult.startResolutionForResult(this, ResponseGPS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        }catch (Exception ex){Log.v("REG:onGPSConnectionFail",ex.getMessage());}
    }

    @Override
    public void onGPSWarning(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPSPositionResult(LatLng position)
    {
        this.currentLatLng = position;
        gps.Stop();
        Log.v("Register:GPSPosRes",position.toString());
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


    /* * * Load Image into ImageView * * */

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
                                //startActivityForResult(intent, REQUEST_CAMERA);
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

}
