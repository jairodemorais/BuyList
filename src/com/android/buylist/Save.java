package com.android.buylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.data.DataManipulator;

public class Save extends Activity implements OnClickListener {
    private DataManipulator dh;
    static final int DIALOG_ID = 0;
    static final String SUCCESS_MESSAGE = "Information saved successfully ! Add Another Product";
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
                String contents = intent.getStringExtra("SCAN_RESULT");                
                TextView code =  (TextView)findViewById(R.id.code);
                code.setText(contents);
                
                // Handle successful scan
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
             String myEditText1= name.getText().toString();
             String myEditText2= barCode.getText().toString();
             
             this.dh = new DataManipulator(this);
             this.dh.insert(myEditText1,myEditText2);
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
}