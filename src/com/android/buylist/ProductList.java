package com.android.buylist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.provider.ProductProvider;
import com.android.provider.helpers.ProductHelper.Products;

public class ProductList extends ListActivity {
	ProductProvider provider;
	Cursor productsList = null;
	ArrayAdapter<String> adapter;
	List<String> checkedProducts;
	String[] stg1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buylist);
		checkedProducts = new ArrayList<String>();
		fillData();
		Button finishButton = (Button)this.findViewById(R.id.finish);
		finishButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	ContentResolver cr = getContentResolver();
		    	for(String id : checkedProducts){
		    		String where  = Products._ID + "="+ id;
		    		cr.delete(Products.CONTENT_URI, where,null);
		    	}
				fillData();
		    }
		  });
		
	}
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    fillData();
	  } 
    
	public void onListItemClick(ListView parent, View v, int position, long id){
		CheckedTextView textview = (CheckedTextView)v;
		checkedProducts.add(Long.toString(id));
	    textview.setChecked(!textview.isChecked());
	}
	public void addToList(View v) {
		Intent newIntent = new Intent(v.getContext(), Save.class);
		startActivity(newIntent);
    }
	private void fillData() {
		
		String where  = Products.IN_LIST + "='true'";
		String[] displayFields = new String[] {
				Products.NAME,
				Products._ID,
				Products.IN_LIST
        };
		productsList = managedQuery(Products.CONTENT_URI, displayFields, where, null, null);
		

		int[] displayViews = new int[] { 
				android.R.id.text1
        };
		this.setListAdapter(new SimpleCursorAdapter(this, 
                android.R.layout.simple_list_item_checked, productsList, 
                displayFields, displayViews));
		
		ListView listview = getListView();
		listview.setItemChecked(0, true);
    }
}
