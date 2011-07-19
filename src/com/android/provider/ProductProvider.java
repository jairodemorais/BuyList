package com.android.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Path.FillType;
import android.net.Uri;

import com.android.data.DatabaseHelper;
import com.android.provider.helpers.ProductHelper.Products;

public class ProductProvider extends ContentProvider{
	public static final String AUTHORITY = "com.android.provider.ProductProvider";
	private static final UriMatcher sUriMatcher;
	private static final int PRODUCTS = 1;
	private static final int PRODUCTS_ID = 2;
	private static HashMap<String, String> productsProjectionMap;
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
                count = db.delete(DatabaseHelper.PRODUCTS_TABLE_NAME, where, whereArgs);
                break;
            case PRODUCTS_ID:
                count = db.delete(DatabaseHelper.PRODUCTS_TABLE_NAME, where, whereArgs);
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
	
        long rowId = db.insert(DatabaseHelper.PRODUCTS_TABLE_NAME, Products.BAR_CODE, values);
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
                qb.setTables(DatabaseHelper.PRODUCTS_TABLE_NAME);
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
                count = db.update(DatabaseHelper.PRODUCTS_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.PRODUCTS_TABLE_NAME, PRODUCTS);
        sUriMatcher.addURI("AUTHORITY", DatabaseHelper.PRODUCTS_TABLE_NAME +"/#", PRODUCTS_ID);
        productsProjectionMap = new HashMap<String, String>();
        productsProjectionMap.put(Products._ID, Products._ID);
        productsProjectionMap.put(Products.NAME, Products.NAME);
        productsProjectionMap.put(Products.BAR_CODE, Products.BAR_CODE);
        productsProjectionMap.put(Products.IN_LIST, Products.IN_LIST);
    }
}



