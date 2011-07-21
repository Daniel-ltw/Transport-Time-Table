package nz.ac.vuw.ecs.nwen304.metlinktt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class map extends MapActivity {

	private MapView mapView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt, menu);
		menu.removeItem(R.id.fav);
		SubMenu sub = menu.findItem(R.id.views).getSubMenu();
		sub.removeItem(R.id.mapV);
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
		case R.id.favourites:
			intent = new Intent(this, favourites.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
