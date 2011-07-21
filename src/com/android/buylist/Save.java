package com.android.buylist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.provider.helpers.ProductHelper.Products;

public class Save extends Activity implements OnClickListener {
    static final int DIALOG_ID = 0;
    static final String SUCCESS_MESSAGE = "Information saved successfully ! Add Another Product";
    static final String KEY ="AIzaSyAdPullFIgDyP7wG1uZH-qd69SV7fBBPzo";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        
        View add = findViewById(R.id.add);
        add.setOnClickListener(this);
        View scan = findViewById(R.id.scan);
        scan.setOnClickListener(this);
    } 
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String barCode = intent.getStringExtra("SCAN_RESULT");                
                String where  = Products.BAR_CODE + "="+ barCode;
                ContentValues values = new ContentValues();
                values.put(Products.IN_LIST.toString(), "true");
                if(getContentResolver().update(Products.CONTENT_URI, values, where, null)== 1){
                	Save.this.finish();
                }else{
                	//requestProduct(barCode);
                	TextView code =  (TextView)findViewById(R.id.code);
                	code.setText(barCode);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			 EditText name = (EditText) findViewById(R.id.name);
			 EditText barCode = (EditText) findViewById(R.id.code);
                 
             ContentValues values = new ContentValues();
             values.put(Products.NAME, name.getText().toString());
             values.put(Products.BAR_CODE, barCode.getText().toString());
             values.put(Products.IN_LIST.toString(), "true");
             getContentResolver().insert(Products.CONTENT_URI, values);
             showDialog(DIALOG_ID);
			break;
		case R.id.scan:
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
		default:
			break;
		}
	}
	protected final Dialog onCreateDialog(final int id){
		Dialog dialog = null;
		switch(id){
			case DIALOG_ID:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(SUCCESS_MESSAGE)
					   .setCancelable(false)
					   .setPositiveButton("No", new DialogInterface.OnClickListener() {
						   @Override
						   public void onClick(DialogInterface dialog, int which) {
							   Save.this.finish();
						   }
					   }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();	
							}
					});
				AlertDialog alert = builder.create();
				dialog = alert;
				break;
			default:
		}
		return dialog;
	}
	private void requestProduct(String barCode){
		BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://www.googleapis.com/shopping/search/v1/public/products?key="+KEY+"&country=AR&q="+barCode));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            System.out.println(page);
            }
        catch(Exception ex){
        	ex.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
	}
}