package com.android.provider.helpers;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProductHelper {
	public ProductHelper() {}
	public static final class Products implements BaseColumns {
		private Products() {}
		public static final Uri CONTENT_URI = Uri.parse("content://com.android.provider.ProductProvider/products");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.buylist.products";
		public static final String NAME = "name";
		public static final String BAR_CODE = "bar_code";
	}
}