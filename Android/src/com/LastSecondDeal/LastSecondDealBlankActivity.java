package com.LastSecondDeal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LastSecondDealBlankActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Blank tab, Again");
        setContentView(textview);
    }
}
