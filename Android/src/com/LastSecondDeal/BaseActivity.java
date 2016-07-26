package com.LastSecondDeal;

//import android.R;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class BaseActivity extends TabActivity{

	//private MenuItem mniQuit,mniAbout,mniMap;
	//private ActionBar mnuActionbar;
	private String TAB_LIST_NAME;	
	private String TAB_MAP_NAME;
	
    public void onCreate(Bundle savedInstanceState) {        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
       //ActionBar mnuActionBar = getActionBar();
        //mnuActionBar.setDisplayHomeAsUpEnabled(true);

        
        //Set Default Values
        TAB_LIST_NAME = this.getResources().getString(R.string.tab_list);
        TAB_MAP_NAME = this.getResources().getString(R.string.tab_map);
                
        //Set tabs
        TabHost tabHost = getTabHost();
        TabSpec spec; 
        Intent intent;
        
        //Link Tab to activity
        intent = new Intent().setClass(this, LastSecondDealListActivity.class); 
        spec = tabHost.newTabSpec(TAB_LIST_NAME).setIndicator(TAB_LIST_NAME, null).setContent(intent);
        tabHost.addTab(spec);

        //Link Tab to activity
        intent = new Intent().setClass(this, LastSecondDealMapActivity.class);
        spec = tabHost.newTabSpec(TAB_MAP_NAME).setIndicator(TAB_MAP_NAME, null).setContent(intent);
        tabHost.addTab(spec);
       
        //tabHost.setCurrentTab(1);
        
        // Define object
        //mniQuit = (MenuItem)findViewById(R.id.mniQuit);
                
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);        
        return true;
    }
       
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
		{			
			case R.id.mniAbout:
			{
				ShowAboutUs();        		
				return true;
			}
			case R.id.mniQuit:
			{
				this.finish();
				return true;
			}			
			default:
			{				
				return false;        	    	
			}
		}
    }
    
    
	public void ShowAboutUs()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Application created by Scott Johnston");
    	builder.setCancelable(true);
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            }
        });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
