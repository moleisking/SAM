package com.LastSecondDeal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LastSecondDealItemActivity extends Activity implements OnClickListener {

	private Button btnPhone;
	private TextView txtName, txtText, txtAddress, txtPrice;
	private ImageView imgItem;
	private Items mItems;
	private int position;
	private long id;
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_second_deal_item_activity);
                
        //Define Objects
        txtName = (TextView)findViewById(R.id.txtName);
        txtText = (TextView)findViewById(R.id.txtText);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtPrice = (TextView)findViewById(R.id.txtPrice);        
        btnPhone = (Button)findViewById(R.id.btnPhone);       
        imgItem = (ImageView)findViewById(R.id.imgItem);
                
        //Action Listeners       
        btnPhone.setOnClickListener(this);
                       
        //Get passed values
        Intent intent = this.getIntent();
        id = intent.getLongExtra("ID", 0);
        position = intent.getIntExtra("Position", 0);
        
        try
        {        	
        	mItems = ((LastSecondDealApplication)this.getApplication()).getItems();        	
    	} 
        catch (Exception ex )
        {
        	System.out.println("LastSecondDealListActivity:Problem loading list items," + ex.getMessage());
        }
    	
    	//Load data into fields
    	txtName.setText(mItems.get(position).getName());
    	txtText.setText(mItems.get(position).getText());
    	txtAddress.setText(mItems.get(position).getAddress());
    	txtPrice.setText(mItems.get(position).getAddress());
    	btnPhone.setText(mItems.get(position).getPhone());
    	try{imgItem.setImageBitmap(mItems.get(position).getImage());
    	}catch (Exception ex){System.out.println("ListSecondDealItemActivity-ImageLoad:" + ex.getMessage());}
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) 
    	{
    	    case R.id.btnPhone:
        	{
        		
        		try 
        		{        		
        		        Intent callIntent = new Intent(Intent.ACTION_CALL);        		
        		        callIntent.setData(Uri.parse("tel:"+btnPhone.getText()));        		
        		        startActivity(callIntent);        		
        		} 
        		catch (ActivityNotFoundException ex) 
        		{
        		        System.out.println("Error btnPhone:" + ex.getMessage());        		
        		}		
        	}
        }
		
	}
}
