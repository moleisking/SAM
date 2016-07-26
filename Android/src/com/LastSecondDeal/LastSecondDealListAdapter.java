package com.LastSecondDeal;

import java.util.ArrayList;

import android.content.*;
import android.view.*;
import android.widget.*;

public class LastSecondDealListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Item> mItems;
		 
		        public LastSecondDealListAdapter(Context context, Items items) {
		            mInflater = LayoutInflater.from(context);
		            mItems = items.Items;
		        }
		 
		        public int getCount() {
		            return mItems.size();
		        }
		 
		        public Object getItem(int position) {
		            return position;
		        }
		 
		        public long getItemId(int position) {
		            return position;
		        }
		 
		        public View getView(int position, View convertView, ViewGroup parent) {
		            ViewHolder holder;
		            if (convertView == null) {
		                convertView = mInflater.inflate(R.layout.last_second_deal_list_row, null);
		                holder = new ViewHolder();
		                holder.text1 = (TextView) convertView.findViewById(R.id.txtRow);
		                holder.image1 = (ImageView)convertView.findViewById(R.id.imgRow);		                
		                convertView.setTag(holder);
		            } else {
		                holder = (ViewHolder) convertView.getTag();
		            }
		 
		            holder.text1.setText(mItems.get(position).getText());
		            try{
		            holder.image1.setImageBitmap(mItems.get(position).getImage());
		            } catch (Exception ex){}
		        
		            return convertView;
		        }
		 
		        static class ViewHolder {
		            TextView text1;
		            ImageView image1;
		            
		        }
}
