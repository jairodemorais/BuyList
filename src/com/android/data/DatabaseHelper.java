package com.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.provider.helpers.ProductHelper.Products;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "buylist.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "ProductContentProvider";
	public static final String PRODUCTS_TABLE_NAME = "products";
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE " + PRODUCTS_TABLE_NAME + " (" + Products._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Products.NAME + " VARCHAR(255)," + Products.BAR_CODE
                    + " TEXT,"+ Products.IN_LIST + " Boolean);");
    }
        
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
            onCreate(db);
    }
}
