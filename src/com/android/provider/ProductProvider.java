package com.android.provider;

import java.util.HashMap;

import com.android.provider.helpers.ProductHelper.Products;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ProductProvider extends ContentProvider{
	private static final String TAG = "ProductContentProvider";
	private static final String DATABASE_NAME = "buylist.db";
	private static final int DATABASE_VERSION = 1;
	private static final String PRODUCTS_TABLE_NAME = "products";
	public static final String AUTHORITY = "com.android.provider.ProductProvider";
	private static final UriMatcher sUriMatcher;
	private static final int PRODUCTS = 1;
	private static final int PRODUCTS_ID = 2;
	private static HashMap<String, String> productsProjectionMap;
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	db.execSQL("CREATE TABLE " + PRODUCTS_TABLE_NAME + " (" + Products._ID
	                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Products.NAME + " VARCHAR(255)," + Products.BAR_CODE
	                    + " TEXT" + ");");
        }
	        
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
	                    + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
	            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;
    
    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
       SQLiteDatabase db = dbHelper.getWritableDatabase();
       int count;
       switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                count = db.delete(PRODUCTS_TABLE_NAME, where, whereArgs);
                break;
            case PRODUCTS_ID:
                count = db.delete(PRODUCTS_TABLE_NAME, where, whereArgs);
                break;
            default:
            	throw new IllegalArgumentException("Unknown URI " + uri);
	        }

       getContext().getContentResolver().notifyChange(uri, null);
       return count;
    }
	@Override
	public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
            	return Products.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
	@Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != PRODUCTS) { 
        	throw new IllegalArgumentException("Unknown URI " + uri); 
    	}
        ContentValues values;
        if (initialValues != null) {
        	values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
	
        long rowId = db.insert(PRODUCTS_TABLE_NAME, Products.BAR_CODE, values);
        if (rowId > 0) {
            Uri productUri = ContentUris.withAppendedId(Products.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(productUri, null);
            return productUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                qb.setTables(PRODUCTS_TABLE_NAME);
                qb.setProjectionMap(productsProjectionMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                count = db.update(PRODUCTS_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PRODUCTS_TABLE_NAME, PRODUCTS);
        sUriMatcher.addURI("AUTHORITY", PRODUCTS_TABLE_NAME +"/#", PRODUCTS_ID);
        productsProjectionMap = new HashMap<String, String>();
        productsProjectionMap.put(Products._ID, Products._ID);
        productsProjectionMap.put(Products.NAME, Products.NAME);
        productsProjectionMap.put(Products.BAR_CODE, Products.BAR_CODE);
    }
}



