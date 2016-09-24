package me.minitrabajo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Scott on 23/09/2016.
 */
public class Token
{
    private String mText;
    private Context mContext;

    private static final String FILE_NAME = "minitrabajo_shared_preferances";

    private static final String TOKEN_NAME = "token";

    public String getToken()
    {
        if (mText.equals(""))
        {
            SharedPreferences mSharedPreference = mContext.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
            mText = mSharedPreference.getString(TOKEN_NAME,"");
        }

        return mText;
    }

    public void setToken(String token)
    {
        this.mText = token;
        SharedPreferences mSharedPreference = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(TOKEN_NAME, token);
        editor.commit();
    }

    public void setContext(Context context)
    {
        this.mContext = context;
    }
}
