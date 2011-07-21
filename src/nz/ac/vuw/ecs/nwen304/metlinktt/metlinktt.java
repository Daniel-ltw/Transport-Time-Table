package nz.ac.vuw.ecs.nwen304.metlinktt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class metlinktt extends Activity {

	SharedPreferences pref;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo nI = cM.getActiveNetworkInfo();
		pref = getPreferences(MODE_PRIVATE);
		SharedPreferences p = getPreferences(MODE_PRIVATE);
		boolean fR = p.getBoolean("First_Run", true);
		
		if (fR && nI.isConnected()) {
		FeedParser feed = FeedParserFactory.getParser(this);
		feed.parse(DocumentType.VERSION);
		p.edit().putBoolean("First_Run", false).commit();
		} else if (!fR && nI.isConnected()) {
			FeedParser feed = FeedParserFactory.getParser(this);
			feed.parse(DocumentType.VERSION);
		} else if (fR && !nI.isConnected()) {
			return;
		}
		startActivity(new Intent(this, options.class));
	}
}