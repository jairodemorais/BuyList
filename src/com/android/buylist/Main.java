package com.android.buylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Main extends Activity implements OnClickListener{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        View productList = findViewById(R.id.product_list);
        productList.setOnClickListener(this);
        View newProduct = findViewById(R.id.new_product);
        newProduct.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_list:
			Intent listIntent = new Intent(v.getContext(), ProductList.class);
			startActivity(listIntent);
			break;
		case R.id.new_product:
			Intent newIntent = new Intent(v.getContext(), Save.class);
			startActivity(newIntent);
		default:
			break;
		}
		
		
	}
}
