package nz.ac.vuw.ecs.nwen304.metlinktt;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class favourites extends Activity{

	SharedPreferences pref;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourites);
		update();
	}

	public void onResume() {
		super.onResume();
		update();
	}

	private class MyOnItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

			String key = parent.getItemAtPosition(position).toString();

			int item = pref.getInt(key, 0);

			pref.edit().putInt("trip_id", item).commit();
			Intent intent = new Intent(getBaseContext(), TT.class);
			startActivity(intent);
		}
	}

	private void update() {

		Set keys = remove_basics();
		ListView list = (ListView) findViewById(R.id.listView1);
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, 
				keys.toArray());

		list.setAdapter(aa);
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(new MyOnItemClickListener());
		registerForContextMenu(list);

		pref.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {

			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				// need to update list view
				ListView list = (ListView) findViewById(R.id.listView1);
				Set keys = remove_basics();
				ArrayAdapter aa = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, 
						keys.toArray());

				list.setAdapter(aa);
				list.setOnItemClickListener(new MyOnItemClickListener());
			}
		});
	}

	private Set remove_basics() {
		pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		Set keys = pref.getAll().keySet();
		keys.remove("trip_id");

		return keys;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fav_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			AlertDialog.Builder alert = new AlertDialog.Builder(this)
			.setTitle("Name Input")
			.setMessage("Enter the new name for your favourite");
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
			update();
			return true;
		case R.id.delete:
			pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			pref.edit().remove(((TextView) info.targetView).getText().toString()).commit();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt, menu);
		menu.removeItem(R.id.fav);
		SubMenu sub = menu.findItem(R.id.views).getSubMenu();
		sub.removeItem(R.id.favourites);
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
		case R.id.options:
			intent = new Intent(this, options.class);
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
}