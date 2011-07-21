package nz.ac.vuw.ecs.nwen304.metlinktt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.TextView;

public class TT extends Activity {

	SharedPreferences pref;
	private DbAdapter mDbHelper = new DbAdapter(this);
	private Cursor mCursor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		setContentView(R.layout.tt);
		startManagingCursor(mCursor);

		mDbHelper.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt, menu);
		SubMenu sub = menu.findItem(R.id.views).getSubMenu();
		sub.removeItem(R.id.timetable);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.fav:
			AlertDialog.Builder alert = new AlertDialog.Builder(this)
			.setTitle("Name Input")
			.setMessage("Enter the name for your favourite");
			final EditText input = new EditText(this);
			alert.setView(input);
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					int fav = pref.getInt("trip_id", 0);
					if (input.getText().toString().length() == 0 || 
							input.getText().toString().matches("[ ]*")) {
						pref.edit().putInt("" + fav, fav).commit();
					} else {
						pref.edit().putInt(input.getText().toString(), fav).commit();
					}
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Do nothing.
				}
			}).show();
			return true;
		case R.id.options:
			intent = new Intent(this, options.class);
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
	protected void onStart() {
		super.onStart();
		populate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populate();
	}

	private void populate() {
		ArrayList<String> array = new ArrayList<String>();

		array.add("Stop id \t - \t Name \t - \t Arrival \t - \t Departure");

		try {
		mCursor = mDbHelper.fetchInput(DocumentType.STOPTIMES, 
				pref.getInt("trip_id", 0));
		} catch (Exception e) {

		}
		while(!mCursor.isAfterLast()) {
			String name = "";
			try {
				name = mDbHelper.fetchInput(DocumentType.STOPS, 
						mCursor.getInt(3)).getString(2);
			} catch (Exception e) {

			}

			array.add(mCursor.getInt(3) + "\t - \t" + 
					name + "\t - \t" + 
					mCursor.getString(1) + "\t - \t" + 
					mCursor.getString(2));


			mCursor.moveToNext();
		}
		StringBuilder sb = new StringBuilder();
		for (String c:array) {
			sb.append(c + "\n");
		}
		TextView tv = (TextView) findViewById(R.id.ttTable);
		tv.setText(sb);
		mCursor.deactivate();
	}
}
