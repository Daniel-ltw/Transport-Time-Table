/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package nz.ac.vuw.ecs.nwen304.metlinktt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple database access helper class. Defines the basic CRUD operations
 * for the following example, and gives the ability to list all details as well as
 * retrieve or modify a specific detail.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbAdapter {

	public static final String KEY_ROWID = "_id";

	private static final String TAG = "DbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			/**
			 * Database creation sql statement
			 */

			// version table
			db.execSQL("create table VERSION "
					+ "(file text not null primary key, "
					+ "version integer not null);");
			// routes table
			db.execSQL("create table ROUTES "
					+ "(route_id integer not null primary key, "
					+ "agency_id text not null, " 
					+ "route_short_name text not null, " 
					+ "route_long_name text not null, " 
					+ "route_desc text, route_type integer not null);");
			// trips table
			db.execSQL("create table TRIPS "
					+ "(route_id integer not null, "
					+ "service_id integer not null, " 
					+ "trip_id integer not null primary key, " 
					+ "trip_headsign text, direction_id integer not null, " 
					+ "block_id integer not null, shape_id integer not null);");
			// stop times table
			db.execSQL("create table STOPTIMES "
					+ "(trip_id integer not null, "
					+ "arrival_time text not null, " 
					+ "departure_time text not null, " 
					+ "stop_id integer not null, " 
					+ "stop_sequence integer not null, " 
					+ "pickup_type integer not null, " 
					+ "drop_off_type integer not null, " 
					+ "shape_dist_traveled double);");
			// stops table
			db.execSQL("create table STOPS "
					+ "(stop_id integer not null primary key, "
					+ "stop_check integer not null, " 
					+ "stop_name text not null, " 
					+ "stop_lat double not null, " 
					+ "stop_lon double not null);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}


	/**
	 * Create a new information using the data and body provided. 
	 * If the information is successfully created return 
	 * the new rowId for that note, otherwise return a -1 to indicate failure.
	 * 
	 * @param data the data of the note
	 * @param body the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createInput(DocumentType type, String[] data) {
		ContentValues initialValues = new ContentValues();
		switch(type) {
		case VERSION:
			initialValues.put("file", data[0]);
			initialValues.put("version", Integer.parseInt(data[1]));
			break;
		case STOPTIMES:
			initialValues.put("trip_id", Integer.parseInt(data[0]));
			initialValues.put("arrival_time", data[1]);
			initialValues.put("departure_time", data[2]);
			initialValues.put("stop_id", Integer.parseInt(data[3]));
			initialValues.put("stop_sequence", Integer.parseInt(data[4]));
			initialValues.put("pickup_type", Integer.parseInt(data[5]));
			initialValues.put("drop_off_type", Integer.parseInt(data[6]));
			if (!data[7].equalsIgnoreCase("")) {
				initialValues.put("shape_dist_traveled", 
						Double.parseDouble(data[7]));
			} else {
				initialValues.put("shape_dist_traveled", 
						Double.parseDouble("0"));
			}
			break;
		case ROUTES: 
			initialValues.put("route_id", Integer.parseInt(data[0]));
			initialValues.put("agency_id", data[1]);
			initialValues.put("route_short_name", data[2]);
			initialValues.put("route_long_name", data[3]);
			initialValues.put("route_desc",data[4]);
			initialValues.put("route_type", Integer.parseInt(data[5]));
			break;
		case TRIPS: 
			initialValues.put("route_id", Integer.parseInt(data[0]));
			initialValues.put("service_id", Integer.parseInt(data[1]));
			initialValues.put("trip_id", Integer.parseInt(data[2]));
			initialValues.put("trip_headsign", data[3]);
			initialValues.put("direction_id", Integer.parseInt(data[4]));
			initialValues.put("block_id", Integer.parseInt(data[5]));
			initialValues.put("shape_id", Integer.parseInt(data[6]));
			break;
		case STOPS: 
			initialValues.put("stop_id", Integer.parseInt(data[0]));
			initialValues.put("stop_check", Integer.parseInt(data[1]));
			initialValues.put("stop_name", data[2]);
			initialValues.put("stop_lat", Double.parseDouble(data[3]));
			initialValues.put("stop_lon", Double.parseDouble(data[4]));
			break;
		}
		return mDb.insert(type.name(), null, initialValues);

	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId id of note to delete
	 * @return true if deleted, false otherwise
	 *//*
	public boolean deleteNote(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}*/

	/**
	 * Return a Cursor over the list of all information in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllInput(DocumentType type) {
		Cursor mCursor = null;
		switch (type) {
		case ROUTES: 
			mCursor = mDb.query(type.name(), new String[] {"route_id", 
				"agency_id", "route_short_name", "route_long_name", "route_desc", 
			"route_type"}, null, null, null, null, null);
			break;
		case TRIPS: 
			mCursor = mDb.query(type.name(), new String[] {"route_id", 
				"service_id", "trip_id", "trip_headsign", "direction_id", 
				"block_id", "shape_id"}, null, null, null, null, null);
			break;
		case STOPS: 
			mCursor = mDb.query(type.name(), new String[] {"stop_id", 
				"stop_check", "stop_name", "stop_lat", "stop_lon"}, null, null, null, 
				null, null);
			break;
		case STOPTIMES:
			mCursor =
				mDb.query(type.name(), new String[] {"trip_id", 
					"arrival_time", "departure_time", "stop_id", "stop_sequence", 
					"pickup_type", "drop_off_type", "shape_dist_traveled"}, 
					null, null, null, null, null);
		}
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Return a Cursor positioned at the information that matches the given rowId
	 * 
	 * @param rowId id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException if note could not be found/retrieved
	 */
	public Cursor fetchInput(DocumentType type, int key) throws SQLException {
		Cursor mCursor = null;
		switch (type) {
		case ROUTES: 
			mCursor =
				mDb.query(true, type.name(), new String[] {"route_id", 
					"agency_id", "route_short_name", "route_long_name", "route_desc", 
				"route_type"}, "route_id=" + key, null, null, null, null, null);
			break;
		case TRIPS: 
			mCursor =
				mDb.query(true, type.name(), new String[] {"route_id", 
					"service_id", "trip_id", "trip_headsign", "direction_id", 
					"block_id", "shape_id"}, "trip_id=" + key, null,
					null, null, null, null);
			break;
		case STOPS: 
			mCursor =
				mDb.query(true, type.name(), new String[] {"stop_id", 
					"stop_check", "stop_name", "stop_lat", "stop_lon"}, 
					"stop_id=" + key, null, null, null, null, null);
			break;
		case STOPTIMES:
			mCursor =
				mDb.query(true, type.name(), new String[] {"trip_id", 
					"arrival_time", "departure_time", "stop_id", "stop_sequence", 
					"pickup_type", "drop_off_type", "shape_dist_traveled"}, 
					"(trip_id=" + key + ")", null, null, 
					null, null, null);
			break;
		}

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Return a Cursor positioned at the information that matches the given title
	 * mainly use for version table
	 * 
	 * @param title title of item to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException if note could not be found/retrieved
	 */
	public Cursor fetchInput(String data) throws SQLException {
		Cursor mCursor = null;
		mCursor = mDb.query(true, DocumentType.VERSION.name(), new String[] {
			"file", "version"}, "(file='" + data + "')",
			null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchInput(DocumentType type, int key1, int key2) throws SQLException {
		Cursor mCursor = null;
		switch (type) {
		case STOPTIMES:
			mCursor =
				mDb.query(true, type.name(), new String[] {"trip_id", 
					"arrival_time", "departure_time", "stop_id", "stop_sequence", 
					"pickup_type", "drop_off_type", "shape_dist_traveled"}, 
					"(trip_id=" + key1 + ")AND(stop_id=" + key2 + ")", null, null, 
					null, null, null);
			break;
		case TRIPS:
			mCursor =
				mDb.query(true, type.name(), new String[] {"route_id", 
					"service_id", "trip_id", "trip_headsign", "direction_id", 
					"block_id", "shape_id"}, "(route_id=" + key1 + ")AND(direction_id="
					+ key2 + ")", null, null, null, null, null);
			break;
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Update the information using the details provided. The information to 
	 * be updated is specified using the rowId, and it is altered to use the 
	 * title and body values passed in. 
	 * 
	 * @param title value to set note title to
	 * @param version version number
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateInput(DocumentType type, String[] data) {

		ContentValues args = new ContentValues();

		switch (type) {
		case VERSION:
			args.put("file", data[0]);
			args.put("version", Integer.parseInt(data[1]));
			return mDb.update(type.name(), args, "file='" + 
					data[0] + "'", null) > 0;
		case ROUTES: 
			args.put("route_id", Integer.parseInt(data[0]));
			args.put("agency_id", data[1]);
			args.put("route_short_name", data[2]);
			args.put("route_long_name", data[3]);
			args.put("route_desc",data[4]);
			args.put("route_type", Integer.parseInt(data[5]));
			return mDb.update(type.name(), args, "route_id" 
					+ "=" + data[0], null) > 0;
		case TRIPS: 
			args.put("route_id", Integer.parseInt(data[0]));
			args.put("service_id", Integer.parseInt(data[1]));
			args.put("trip_id", Integer.parseInt(data[2]));
			args.put("trip_headsign", data[3]);
			args.put("direction_id", Integer.parseInt(data[4]));
			args.put("block_id", Integer.parseInt(data[5]));
			args.put("shape_id", Integer.parseInt(data[6]));
			return mDb.update(type.name(), args, "trip_id" 
					+ "=" + data[2], null) > 0;
		case STOPS: 
			args.put("stop_id", Integer.parseInt(data[0]));
			args.put("stop_check", Integer.parseInt(data[1]));
			args.put("stop_name", data[2]);
			args.put("stop_lat", Double.parseDouble(data[3]));
			args.put("stop_lon", Double.parseDouble(data[4]));
			return mDb.update(type.name(), args, "stop_id" 
					+ "=" + data[0], null) > 0;

					// stop times require
		}
		return false;

	}
}
