package com.LastSecondDeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.graphics.Color;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class LastSecondDealListActivity extends Activity implements OnItemClickListener {
    	
	private ListView lstItems;
	private Items mItems;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_second_deal_list_activity);
                
        //Define Object       
        lstItems = (ListView)findViewById(R.id.lstItems);
               
        //Action Listeners      
        lstItems.setOnItemClickListener(this);
        
        //Set Look
        //lstItems.setBackgroundColor(Color.TRANSPARENT);
        lstItems.invalidate();
                  
        //Get Data and Load data into list                       
        try
        {        
        	((LastSecondDealApplication)this.getApplication()).FillItems();
        	mItems = ((LastSecondDealApplication)this.getApplication()).getItems();
        	lstItems.setAdapter(new LastSecondDealListAdapter(this,mItems));
    	} 
        catch (Exception ex )
        {
        	System.out.println("LastSecondDealListActivity:Problem loading list items," + ex.getMessage());
        }
             
             
    }
	    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   	    	
    	Intent i = new Intent(this, LastSecondDealItemActivity.class);
		i.putExtra("Position", position);
		i.putExtra("ID", id);
		i.putExtra("Items", mItems);		
		this.startActivity(i);
		this.finish();
		System.out.println("Position:"+ position +",ID:"+id);
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