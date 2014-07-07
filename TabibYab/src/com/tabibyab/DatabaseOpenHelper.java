package com.tabibyab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final static String TABLE_NAME = "clinics";
	final static String _ID = "_id";
	final static String[] columns = { _ID, TAGS.TAG_NAME, TAGS.TAG_LONGITUDE, TAGS.TAG_LATITUDE, TAGS.TAG_ADDRESS, TAGS.TAG_TEL, TAGS.TAG_OPERATING_HOURS,
		TAGS.TAG_RATING, TAGS.TAG_INSURANCES, TAGS.TAG_SPECIALITY, TAGS.TAG_SPECIALITY_LEVEL, TAGS.TAG_TYPE, TAGS.TAG_WEBSITE_ADDRESS, TAGS.TAG_DESCRIPTION,
		TAGS.TAG_FAVORITE};

	final private static String CREATE_CMD =

	"CREATE TABLE clinics (" 
			+ _ID	+ " INTEGER PRIMARY KEY, "
			+ TAGS.TAG_NAME + " TEXT, "
			+ TAGS.TAG_LONGITUDE + " NUMERIC, "
			+ TAGS.TAG_LATITUDE + " NUMERIC, "
			+ TAGS.TAG_ADDRESS + " TEXT, "
			+ TAGS.TAG_TEL + " NUMERIC, "
			+ TAGS.TAG_OPERATING_HOURS + " TEXT, "
			+ TAGS.TAG_RATING + " NUMERIC, "
			+ TAGS.TAG_INSURANCES + " TEXT, "
			+ TAGS.TAG_SPECIALITY + " TEXT, "
			+ TAGS.TAG_SPECIALITY_LEVEL + " TEXT, "
			+ TAGS.TAG_TYPE + " TEXT, "
			+ TAGS.TAG_WEBSITE_ADDRESS + " TEXT, "
			+ TAGS.TAG_DESCRIPTION + " TEXT, "
			+ TAGS.TAG_FAVORITE + " TEXT "
			+")";

	
	
	final private static String DBNAME = "clinics_db4";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(DatabaseOpenHelper.class.getName(),CREATE_CMD);
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(DatabaseOpenHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + DBNAME);
		    onCreate(db);

	}

	void deleteDatabase() {
		mContext.deleteDatabase(DBNAME);
	}
}