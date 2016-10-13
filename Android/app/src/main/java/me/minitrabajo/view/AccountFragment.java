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
import me.minitrabajo.R;
import me.minitrabajo.controller.GPS;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.controller.ResponseGPS;
import me.minitrabajo.model.Categories;
import me.minitrabajo.model.Category;
import me.minitrabajo.model.UserAccount;
//import android.support.v4.app.Fragment;

/*Note: No calls to server through ResponseAPI. User object passed from ListFragment*/
public class AccountFragment extends Fragment implements ResponseAPI, ResponseGPS
{

    private EditText txtName, txtDescription, txtAddress, txtDayRate, txtHourRate, txtPassword, txtEmail;
    private ImageView imgProfile;
    private UserAccount userAccount;
    private AutoCompleteTextView txtCategory;
    private MultiAutoCompleteTextView txtTag;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private Categories categories;
    private GPS gps;
    private LatLng currentLatLng;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.v("AccountFra:onCreate","Started");
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout )inflater.inflate(R.layout.fragment_account, container, false);

        try {
            //Define Objects
            userAccount = new UserAccount(getActivity());
            try
            {
                //Try load from passed object
                Log.v("Account","Try load from passed object");
                userAccount = (UserAccount)getActivity().getIntent().getSerializableExtra("UserAccount");
            }
            catch (Exception uaex)
            {
                Log.v("Account","Failed to load user account from intent");
                if (userAccount.isEmpty())
                {
                    userAccount.loadFromFile();
                    Log.v("Account","Load user account from file");
                }
            }


            userAccount.print();
            gps = new GPS(getActivity());
            currentLatLng = gps.getLocation();
            Log.v("Search:Latitude",String.valueOf(currentLatLng.latitude));
            Log.v("Search:Longitude",String.valueOf(currentLatLng.longitude));

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
            //btnSave = (FloatingActionButton) container.findViewById(R.id.btnSave);

            Log.v("AccountFragment","Start try");
            //Fill Objects
            txtName.setText(userAccount.getName());
            txtDescription.setText(userAccount.getDescription());
            txtAddress.setText(userAccount.getAddress());
            txtHourRate.setText(Double.toString(userAccount.getHourRate()));
            txtDayRate.setText(Double.toString(userAccount.getDayRate()));
            imgProfile.setImageBitmap(userAccount.getImageAsBitmap());

            fillCategory();
        }
        catch (Exception ex)
        {
            Log.v("AccountFragment",ex.getMessage());
        }

        return ll;
    }

    protected void fillCategory()
    {
        categories = new Categories(this.getActivity());
        if(categories.hasFile())
        {
            categories.loadFromFile();
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity().getApplicationContext(),  R.layout.row_dropdown, R.id.txtItem, categories.getCategoryStringArray());
            txtCategory.setAdapter(adapter);
            txtCategory.setThreshold(2);
            Log.w("Account:onCreate", "Categories load from file");
        }
        else
        {
            Log.w("Account:onCreate", "Categories not found");
        }
    }

    protected void fillTag(String category)
    {
        Category c = categories.findCategory(category);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), R.layout.row_dropdown, R.id.txtItem, c.getTagStringArray() );
        txtTag.setText("");
        txtTag.setAdapter(adapter);
        txtTag.setThreshold(2);
        txtTag.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    public void onGPSConnectionResolutionRequest(ConnectionResult connectionResult )
    {
        try {
            connectionResult.startResolutionForResult(this.getActivity(), ResponseGPS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        }catch (Exception ex){Log.v("Account:onGPSConFail",ex.getMessage());}
    }

    @Override
    public void onGPSWarning(String message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void processFinish(String output)
    {
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


    @Override
    public void onResume() {
        super.onResume();
        gps.Resume();
        Log.i("Account:onResume()", "Called");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        gps.Pause();
        Log.v("Account:onPause()", "Called");
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
            userAccount.setRegisteredLatLng(gps.getLocation());
            userAccount.setImageRaw(((BitmapDrawable) imgProfile.getDrawable()).getBitmap());

            String url = getResources().getString(R.string.url_post_user_account_register);
            Bitmap bitmap = ((BitmapDrawable)imgProfile.getDrawable()).getBitmap();
            String parameters = userAccount.getUserAsParameters();
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
