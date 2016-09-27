package me.minitrabajo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> , ResponseAPI
{
    private static final int REQUEST_READ_CONTACTS = 0;
    //private static final String ACCOUNT_TOKEN = "token";
    //private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private View mProgressView;
    private View mLoginFormView;
   // private SharedPreferences mSharedPreference;
    private UserAccount mUserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAccount = new UserAccount(this);
        mUserAccount.getToken();

        if(mUserAccount.hasToken()) //!getTokenCookie().equals("")
        {
            //Already logged in go straight to main application
            // loadMainActivity( getTokenCookie());
            loadMainActivity( mUserAccount);
        }
        else
        {
           //No saved account yet
            setContentView(R.layout.activity_login);
            txtEmail = (AutoCompleteTextView) findViewById(R.id.txtEmail);
            txtPassword = (EditText) findViewById(R.id.txtPassword);
            btnLogin = (Button) findViewById(R.id.btnLogin);
           // mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        onLoginClick(txtPassword);
                        return true;
                    }
                    return false;
                }
            });

           /* mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });*/

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }

    private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    protected void onLoginClick(View view)
    {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        //mEmailView.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
       // String email = mEmailView.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        // Check for a valid email address.
       /* if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //call authenticate
            String url = getResources().getString(R.string.net_authenticate_url); //"http://192.168.1.100:3003/api/authenticate";
            String parameters = "email=" + txtEmail.getText().toString() +"&pass=" + txtPassword.getText().toString();  //"name=scott&pass=12345&email=moleisking%40gmail.com";
            PostAPI asyncTask =new PostAPI(this);
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,"");
        }
    }
    protected void onRegisterClick(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isEmailValid(String email) { return email.contains("@"); }

    private boolean isPasswordValid(String password) { return password.length() > 4; }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        //Shows the progress UI and hides the login form.
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
       /* ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);*/
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    //Scott Added this
    @Override
    public void processFinish(String output)
    {
        showProgress(false);
        //Here you will receive the result fired from async class
        Log.w("processFinish", output);
        //Example Reply: {"token":"JWT..."}
        String token ="";
        try
        {
            JSONObject myJson = new JSONObject(output);
            token = myJson.optString("token");
        }
        catch (Exception ex)
        {
            Log.v("Login:ProFin",ex.getMessage());
        }

        if (!token.equals(""))
        {
            //setTokenCookie(token);
            //loadMainActivity(token);
            mUserAccount.setToken(token);
            loadMainActivity(mUserAccount);
        }
        else
        {
            txtPassword.setError(getString(R.string.error_incorrect_password));
            txtPassword.requestFocus();
           //Toast.makeText(this.getContext(),"Login Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMainActivity(UserAccount userAccount)
    {
        //Load Main Activity
        Log.v("loadMainActivity", "intent");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserAccount", userAccount);
        startActivity(intent);
    }

    /*private void setTokenCookie(String token)
    {
        //Save previous successful login
        mSharedPreference = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(ACCOUNT_TOKEN, token);
        editor.commit();
    }

    private String getTokenCookie()
    {
        //Get previous successful login
        mSharedPreference = this.getPreferences(Context.MODE_PRIVATE);
        //TODO: Check token is valid
        return mSharedPreference.getString(ACCOUNT_TOKEN,"");
    }*/






}

