package me.minitrabajo.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.minitrabajo.R;
import me.minitrabajo.controller.CategoriesAdapter;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseGPS;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.UserAccount;
//import android.support.v4.app.Fragment;

/*Note: No calls to server through ResponseAPI. User object passed from ResultsFragment*/
public class AccountFragment extends Fragment implements ResponseAPI, ResponseGPS
{

    private EditText txtName, txtDescription, txtAddress, txtDayRate, txtHourRate, txtPassword, txtEmail;
    private ImageView imgProfile;
    private UserAccount userAccount;
    private AutoCompleteTextView txtCategory;
    private MultiAutoCompleteTextView txtTag;

    private static final int SELECT_PICTURE = 1;
    //private String selectedImagePath;
    private CategoriesAdapter categoryAdapter;
    private Categories categories;
    private LatLng currentLatLng;
    private GPS gps;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.v("Account:onCreate","Started");
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout )inflater.inflate(R.layout.fragment_account, container, false);

        try {
            //Define Objects
            userAccount = new UserAccount(getActivity());
            userAccount = ((MainActivity)getActivity()).getUserAccount();

            categories = new Categories(this.getActivity());
            categories.loadFromFile();

            //Start GPS
            currentLatLng = new LatLng(0.0d,0.0d);
            gps = new GPS(getActivity());
            gps.delegate = this;
            gps.Start();
            Log.v("Search:Latitude",String.valueOf(currentLatLng.latitude));
            Log.v("Search:Longitude",String.valueOf(currentLatLng.longitude));

            //Define view
            imgProfile = (ImageView)ll.findViewById(R.id.imgProfile);
            txtName = (EditText) ll.findViewById(R.id.txtName);
            txtEmail = (EditText) ll.findViewById(R.id.txtEmail);
            txtPassword = (EditText) ll.findViewById(R.id.txtPassword);
            txtDescription = (EditText)ll.findViewById(R.id.txtDescription);
            txtAddress = (EditText)ll.findViewById(R.id.txtAddress);
            txtHourRate = (EditText)ll.findViewById(R.id.txtHourRate);
            txtDayRate = (EditText)ll.findViewById(R.id.txtDayRate);
            txtCategory= (AutoCompleteTextView)ll.findViewById(R.id.txtCategory);
            txtCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                    Log.v("SELECTED", id +":" + adapterView.getItemAtPosition(position).toString());
                    fillTag(adapterView.getItemAtPosition(position).toString());
                }
            });
            txtTag= (MultiAutoCompleteTextView) ll.findViewById(R.id.txtTag);

            //Fill Objects
            txtName.setText(userAccount.getName());
            txtDescription.setText(userAccount.getDescription());
            txtAddress.setText(userAccount.getAddress());
            txtHourRate.setText(Double.toString(userAccount.getHourRate()));
            txtDayRate.setText(Double.toString(userAccount.getDayRate()));
            imgProfile.setImageBitmap(userAccount.getImageAsBitmap());

            //Fill Category
            categoryAdapter = new CategoriesAdapter(getActivity(), (ArrayList<Category>) categories.getCategoryList());
            txtCategory.setAdapter(categoryAdapter);
            txtCategory.setThreshold(2);
            Log.v("Account:onCreate","Finished");
        }
        catch (Exception ex)
        {
            Log.v("Account:onCreate:Err",ex.getMessage());
        }

        return ll;
    }

    protected void fillTag(String category)
    {
        Category c = categories.findCategory(category);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), R.layout.row_tag, R.id.txtItemTag, c.getTagStringArray() );
        txtTag.setText("");
        txtTag.setAdapter(adapter);
        txtTag.setThreshold(2);
        txtTag.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    public void onGPSConnectionResolutionRequest(ConnectionResult connectionResult )
    {
        try
        {
            connectionResult.startResolutionForResult(this.getActivity(), ResponseGPS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        }
        catch (Exception ex)
        {
            Log.v("Account:onGPSConFail",ex.getMessage());
        }
    }

    @Override
    public void onGPSWarning(String message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGPSPositionResult(LatLng position) {
        try{
            this.currentLatLng = position;
            Log.v("Search:GPSPosRes", position.toString());
            gps.Stop();
        }
        catch (Exception ex)
        {
            Log.v("Search:GPSPosRes:Err", ex.getMessage());
        }
    }

    @Override
    public void processFinish(String output)
    {
        //Get Response from save
        Log.w("Account:processFinish", output);

        try
        {
             Toast.makeText(this.getActivity(),"Saved",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Log.w("Account:pFinish", ex.getMessage());
        }
    }

    /**
     *  Load Image into ImageView
     */

    public void onImageUpload(View image)
    {
        Log.v("Account:onImageUpload","Clicked" );
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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
                                Log.v("Account:onImageUpload","Library" );
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                            }
                            case 2:
                            {
                                Log.v("Account:onImageUpload","Cancel" );
                            }
                            default:
                            {
                                Log.v("Account:onImageUpload", String.valueOf(which) );
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
                bm = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), data.getData());
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

    public void loadCategoryFromBackend()
    {
        String url = this.getActivity().getString(R.string.url_get_categories);
        GetAPI asyncTask =new GetAPI(this.getActivity());
        asyncTask.delegate = this;
        asyncTask.execute(url,"","");
    }

    public void onSaveClick(View view)
    {
        try {
            UserAccount userAccount  = new UserAccount(this.getActivity());
            userAccount.setName(txtName.getText().toString());
            userAccount.setPassword(txtPassword.getText().toString());
            userAccount.setEmail(txtEmail.getText().toString());
            userAccount.setAddress(txtAddress.getText().toString());
            userAccount.setHourRate(Double.parseDouble(txtHourRate.getText().toString()));
            userAccount.setDayRate(Double.parseDouble(txtDayRate.getText().toString()));
            userAccount.setRegisteredLatLng(currentLatLng);
            userAccount.setImageRaw(((BitmapDrawable) imgProfile.getDrawable()).getBitmap());

            String url = getResources().getString(R.string.url_post_user_account_register);
            Bitmap bitmap = ((BitmapDrawable)imgProfile.getDrawable()).getBitmap();
            String parameters = userAccount.getUserParameters();
            PostAPI asyncTask =new PostAPI(this.getActivity());
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,"");
            Log.w("Account:onSaveClick", "Save button clicked");
        }
        catch (Exception ex)
        {
            Log.v("Account:onSaveClick:Err", ex.getMessage());
        }

        Toast.makeText(this.getActivity(), "Saved", Toast.LENGTH_LONG).show();
    }
}
