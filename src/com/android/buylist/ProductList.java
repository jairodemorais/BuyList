package com.android.buylist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.providers.ProductProvider;
import com.android.providers.helpers.ProductHelper.Products;

public class ProductList extends ListActivity {
	ProductProvider provider;
	Cursor productsList = null;
	ArrayAdapter<String> adapter;
	List<Integer> checkedProducts;
	String[] stg1;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buylist);
		checkedProducts = new ArrayList<Integer>();
		productsList = managedQuery(Products.CONTENT_URI, null, null, null, null);
		String[] displayFields = new String[] {
				Products.NAME, 
				Products._ID
        };

		int[] displayViews = new int[] { 
				android.R.id.text1, 
                android.R.id.text2 
        };
		this.setListAdapter(new SimpleCursorAdapter(this, 
                android.R.layout.simple_list_item_checked, productsList, 
                displayFields, displayViews));
				
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		CheckedTextView textview = (CheckedTextView)v;
		
		//checkedProducts.add(textview);
	    textview.setChecked(!textview.isChecked());
	}
	public void addToList(View v) {
		Intent newIntent = new Intent(v.getContext(), Save.class);
		startActivity(newIntent);
    }
	public void FinishBuy(View v) {
		//provider.delete(Products.CONTENT_URI, Products._ID, whereArgs)
		
    } 
}
