package me.minitrabajo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.minitrabajo.R;
import me.minitrabajo.controller.GetAPI;
import me.minitrabajo.controller.PostAPI;
import me.minitrabajo.controller.ResponseAPI;
import me.minitrabajo.model.*;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
/*, LoaderCallbacks<Cursor> ,*/
public class LoginActivity extends AppCompatActivity implements  ResponseAPI
{
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private View mProgressView;
    private View mLoginFormView;
    private UserAccount userAccount;
    private Categories categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        txtEmail = (AutoCompleteTextView) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        /*txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    onLoginClick(txtPassword);
                    return true;
                }
                return false;
            }
        });*/
        btnLogin = (Button) findViewById(R.id.btnLogin);

        userAccount = new UserAccount(this);
        categories = new Categories(this);

        if(!categories.hasFile())
        {
            Log.v("LoginActivity:onCreate","No category file found");
            getCategories();
        }

        if(userAccount.hasFile())
        {
            Log.v("LoginActivity:onCreate","File found");
            userAccount.loadFromFile();
            userAccount.print();
            if(userAccount.hasToken())
            {
                //Already logged in go straight to main application
                Log.v("LoginActivity:onCreate","Token found");
                loadMainActivity();
            }
            if (userAccount !=  null && userAccount.getToken().equals("") && userAccount.getEmail().equals(""))
            {
                //Check for corrupt file
                Log.v("LoginActivity:onCreate","Found corrupt file");
                userAccount.deleteFile();
            }
        }
    }

    /*private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }*/

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            /*Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });*/
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // populateAutoComplete();
            }
        }
    }

    protected void onLoginClick(View view)
    {
        // Reset errors text
        txtEmail.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (password == null || !password.trim().equals("") || (password.length() < 4))
        {
            // Check for a valid password, if the user entered one.
            txtPassword.setError(getString(R.string.error_invalid_password));
            txtPassword.requestFocus();
        }
        else if (email == null || email.trim().equals(""))
        {
            // Error email is empty
            txtEmail.setError(getString(R.string.error_field_required));
            txtEmail.requestFocus();
        }
        else if (email.contains("@") )
        {
            // Error text is not email
            txtEmail.setError(getString(R.string.error_invalid_email));
            txtEmail.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            showProgress(true);
            postAuthentication();
        }
    }
    protected void onRegisterClick(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        //Shows the progress UI and hides the login form.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }*/

   /* @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }*/

   /* @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/



   /* private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }*/

    @Override
    public void processFinish(String output)
    {
        showProgress(false);
        Log.w("processFinish", output);
        try
        {
            String header = (output.length()>=20) ? output.substring(0,19).toLowerCase() : output;
            if (header.contains("token"))
            {
                JSONObject myJson = new JSONObject(output);
                userAccount.setToken( myJson.optString("token"));
                Log.v("LoginActivity", "Token download success");
                getUserAccount();
            }
            else if (header.contains("name"))
            {
                Log.v("LoginActivity", "Profile download success");
                userAccount.loadFromJSON(output);
                userAccount.saveToFile();
                loadMainActivity();
            }
            else if(header.contains("authentication failed"))
            {
                Log.v("LoginActivity", "Authentication failed");
                txtPassword.setError(getString(R.string.error_incorrect_password));
                txtPassword.requestFocus();
                Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
            else if(header.contains("categories"))
            {
                Log.v("LoginActivity", "Saving categories.");
                Categories categories = new Categories(this);
                categories.loadFromJSON(output);
                categories.saveToFile();
            }
            else
            {
                Log.v("LoginActivity", "No Reply");
            }
        }
        catch (Exception ex)
        {
            Log.v("LoginActivity:pFinish",ex.getMessage());
        }
    }

    private void loadMainActivity()
    {
        //Load Main Activity
        Log.v("loadMainActivity", "intent");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserAccount", userAccount);
        startActivity(intent);
    }

    private void getUserAccount()
    {
        String url = getResources().getString(R.string.url_get_user_account_profile);
        GetAPI asyncTask =new GetAPI(this);
        asyncTask.delegate = this;
        asyncTask.execute(url,"",userAccount.getToken());
    }

    protected void getCategories()
    {
        Log.v("LoginActivity","getCategories started file download");
        String url = this.getString(R.string.url_get_categories);
        GetAPI asyncTask =new GetAPI(this);
        asyncTask.delegate = this;
        asyncTask.execute(url,"","");
    }

    private void postAuthentication()
    {
        String url = getResources().getString(R.string.url_post_user_account_authenticate);
        String parameters = "email=" + txtEmail.getText().toString() +"&pass=" + txtPassword.getText().toString();
        PostAPI asyncTask =new PostAPI(this);
        asyncTask.delegate = this;
        asyncTask.execute(url,parameters,"");
    }

}

