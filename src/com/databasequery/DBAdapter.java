package com.databasequery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String KEY_ROW = "rowid";
	public static final String KEY_BUS_NUMBER = "busNumber";
	public static final String KEY_AMIC_DECK1 = "amicDeck1";
	public static final String KEY_AMIC_DECK2 = "amicDeck2";
	public static final String KEY_ESSELEN = "esselen";
	public static final String KEY_JUNCTION = "witsJunction";
	public static final String KEY_WEC = "wEC";
	public static final String KEY_KNOCKS = "knockando";
	public static final String KEY_PKV1_EOH = "[pKV1/EOH]";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "witsBusTimetables";
	private static final String DATABASE_TABLE = "circuitBusTimes";
	private static final int DATABASE_VERSION = 1;
	private static final String timeType = "CHAR";
	private static final String busNumberType = "CHAR";
	

	private static final String CREATE_DATABASE = "create table if not exists circuitBusTimes (rowid integer primary key autoincrement, "
			+ "busNumber " + busNumberType + ","
			+ "amicDeck1 " + timeType + ", "
			+ "esselen " + timeType + ","
			+ " wEC " + timeType + ","
			+ " knockando " + timeType + ","
			+ " pkvEOH" + timeType+","
			+ "amicDeck2 " + timeType + ");";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_DATABASE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a record into the database---
	public long insertRecord(String amicDeck, String esselen, String amicDeck2,
			String wEC, String knockando, String pkvEoh, String busNumber) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BUS_NUMBER, busNumber);
		initialValues.put(KEY_AMIC_DECK1, amicDeck);
		initialValues.put(KEY_ESSELEN, esselen);
		initialValues.put(KEY_AMIC_DECK2, amicDeck2);
		initialValues.put(KEY_KNOCKS, knockando);
		initialValues.put(KEY_PKV1_EOH, pkvEoh);
		initialValues.put(KEY_WEC, wEC);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular record---
	public boolean deleteRecord(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROW + "=" + rowId, null) > 0;
	}

	// ---retrieves all the records---
	public Cursor getAllRecords() {
		return db.query(DATABASE_TABLE, null, null, null, null, null, null, null);
	}

	// ---retrieves a particular record suing its primary key. this shows all fields---
	public Cursor getRecord(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_BUS_NUMBER, KEY_AMIC_DECK1, KEY_ESSELEN, KEY_AMIC_DECK2, KEY_WEC, KEY_KNOCKS, KEY_PKV1_EOH },
				KEY_BUS_NUMBER + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	//get Record by BusNumber
	public Cursor getRecord(String busNumber) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_BUS_NUMBER, KEY_AMIC_DECK1, KEY_ESSELEN, KEY_AMIC_DECK2, KEY_WEC, KEY_KNOCKS, KEY_PKV1_EOH },
				KEY_BUS_NUMBER + "=" + busNumber, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//--retrieves the source and destination times using the start and destination instead of the route ID--
	public Cursor getRecord(String start, String destination) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				start, destination, KEY_BUS_NUMBER },
				start+ "!=NULL" + " AND " + destination + " !=NULL" , null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a record---
	public boolean updateRecord(String routeId, String amicDeck, String esselen, String amicDeck2,
			String wEC, String knockando, String pkvEoh, String busNumber) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_BUS_NUMBER, busNumber);
		newValues.put(KEY_AMIC_DECK1, amicDeck);
		newValues.put(KEY_ESSELEN, esselen);
		newValues.put(KEY_AMIC_DECK2, amicDeck2);
		newValues.put(KEY_KNOCKS, knockando);
		newValues.put(KEY_PKV1_EOH, pkvEoh);
		newValues.put(KEY_WEC, wEC);
		return db.update(DATABASE_TABLE, newValues, KEY_BUS_NUMBER + "=" + routeId, null) > 0;
	}
}
