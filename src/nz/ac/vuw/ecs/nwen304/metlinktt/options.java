package nz.ac.vuw.ecs.nwen304.metlinktt;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class options extends Activity {

	SharedPreferences pref;
	private	ArrayAdapter<String> adapter; // Reusable adapter for array
	private	int route = 0, direction = 0;
	private Context mContext;
	private DbAdapter mDbHelper = new DbAdapter(this);
	private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		startManagingCursor(mCursor);

		mContext = this;

		pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		mDbHelper.open();

		// Setting up spinner for transport mode
		Spinner routeSpin = (Spinner) findViewById(R.id.routeSpin);

		mCursor = mDbHelper.fetchAllInput(DocumentType.ROUTES);
		ArrayList<String> array = new ArrayList<String>();
		while(!mCursor.isAfterLast()) {
			array.add(mCursor.getInt(0) + " - " + 
					mCursor.getString(3));
			mCursor.moveToNext();
		}

		adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_spinner_item, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		routeSpin.setAdapter(adapter);
		routeSpin.setOnItemSelectedListener(new MyOnItemSelectedListener());

		// Doing the same for route
		Spinner directionSpin = (Spinner) findViewById(R.id.directionSpin);
		ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(
				this, R.array.direction_array, android.R.layout.simple_spinner_item);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
		directionSpin.setAdapter(aa);
		directionSpin.setOnItemSelectedListener(new MyOnItemSelectedListener());
		Spinner tripSpin = (Spinner) findViewById(R.id.tripSpin);
		tripSpin.setOnItemSelectedListener(new MyOnItemSelectedListener());
		mCursor.deactivate();
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent,
				View view, int pos, long id) {


			switch (parent.getId()) {
			case R.id.routeSpin:
				route = Integer.parseInt(parent.getItemAtPosition(pos).toString().
						substring(0, 1).trim());
				break;
			case R.id.directionSpin: 
				String d = parent.getItemAtPosition(pos).toString();
				if (d.equalsIgnoreCase("inbound")) {
					direction = 1;
				} else if (d.equalsIgnoreCase("outbound")) {
					direction = 0;
				}
				Spinner tripSpin = (Spinner) findViewById(R.id.tripSpin);
				mCursor = mDbHelper.fetchInput(DocumentType.TRIPS, 
						route, direction);
				ArrayList<String> array = new ArrayList<String>();
				if (mCursor.getCount() > 0) {
					while(!mCursor.isAfterLast()) {
						array.add("" + mCursor.getInt(2));
						mCursor.moveToNext();
					}
				}

				adapter = new ArrayAdapter<String>
				(mContext, android.R.layout.simple_spinner_item, array);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
				tripSpin.setAdapter(adapter);
				mCursor.deactivate();
				break;
			case R.id.tripSpin: 
				int trip = Integer.parseInt(parent.getItemAtPosition(pos).toString()
						.trim());
				pref.edit().putInt("trip_id", trip).commit();
				break;
			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			//Spinner routeSpin = (Spinner) findViewById(R.id.routeSpin);
			//Spinner directionSpin = (Spinner) findViewById(R.id.directionSpin);
			switch (parent.getId()) {
			case R.id.tripSpin:
				//routeSpin.setAdapter(null);
				//directionSpin.setAdapter(null);
				break;
			case R.id.routeSpin:
				//directionSpin.setAdapter(null);
				break;
			case R.id.directionSpin: 
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt, menu);
		menu.removeItem(R.id.fav);
		SubMenu sub = menu.findItem(R.id.views).getSubMenu();
		sub.removeItem(R.id.options);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;  // Reusable Intent for each activity

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.timetable:
			intent = new Intent(this, TT.class);
			startActivity(intent);
			return true;
		case R.id.favourites:
			intent = new Intent(this, favourites.class);
			startActivity(intent);
			return true;
		case R.id.mapV:
			intent = new Intent(this, map.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mDbHelper.close();
		mCursor.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mDbHelper.close();
		mCursor.close();
	}
}