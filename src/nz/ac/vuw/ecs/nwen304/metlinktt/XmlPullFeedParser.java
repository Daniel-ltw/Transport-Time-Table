package nz.ac.vuw.ecs.nwen304.metlinktt;

import org.xmlpull.v1.XmlPullParser;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.util.Xml;

public class XmlPullFeedParser extends BaseFeedParser {

	private static DbAdapter mDbHelper;
	private Cursor mCursor;
	private Context context;

	public XmlPullFeedParser(String feedUrl, Context context) {
		super(feedUrl);
		this.context = context;
		mDbHelper = new DbAdapter(context);
		mDbHelper.open();
	}

	public void parse(DocumentType type) {
		XmlPullParser parser = Xml.newPullParser();
		Resources res = context.getResources();

		String route_id = null, route_short_name = null, route_type = null, 
		service_id = null, trip_id = null, direction_id = null, block_id = null, 
		shape_id = null, stop_id = null, stop_sequence = null, pickup_type = null, 
		drop_off_type = null, stop_check = null, version = null, 
		route_desc = null, agency_id = null, route_long_name = null, 
		trip_headsign = null, stop_name = null, arrival_time = null, departure_time = null, 
		data = null, shape_dist_traveled = null, stop_lat = null, stop_lon = null;

		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				String name = null;
				switch (eventType){
				case XmlPullParser.START_DOCUMENT:
					// check if there are any document flaws 
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(RECORD)){
						// new data, need to read on to next tag to get more details
						// 
						// this will be necessary if there are datatypes for each type
						// of document
					} else {
						if (name.equalsIgnoreCase(res.getString(R.string.route_id))){
							route_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.route_short_name))){
							route_short_name = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.route_type))){
							route_type = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.service_id))){
							service_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.trip_id))){
							trip_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.direction_id))){
							direction_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.block_id))){
							block_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.shape_id))){
							shape_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_id))){
							stop_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_sequence))){
							stop_sequence = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.pickup_type))){
							pickup_type = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.drop_off_type))){
							drop_off_type = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_check))){
							stop_check = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.shape_dist_traveled))){
							shape_dist_traveled = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_lat))){
							stop_lat = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_lon))){
							stop_lon = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.route_desc))){
							route_desc = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.agency_id))){
							agency_id = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.route_long_name))){
							route_long_name = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.trip_headsign))){
							trip_headsign = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.stop_name))){
							stop_name = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.arrival_time))){
							arrival_time = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.departure_time))){
							departure_time = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.data))){
							data = parser.nextText();
						} else if (name.equalsIgnoreCase(res.getString(
								R.string.version))){
							version = parser.nextText();
						}
					}
					break;
				case XmlPullParser.END_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(RECORD)){
						switch(type) {
						case VERSION:
							if (data == null || version == null) {
								throw new RuntimeException("VERSION Error: one" +
								" input is null");
							}
							FeedParser feed;
							mCursor = mDbHelper.fetchInput(data);
							String[] versiondata = {data, version};
							if (mCursor.getCount() <= 0) {
								// all needed details of a data version 
								// is grabbed creating new input to 
								// VERSION table
								if (mDbHelper.createInput(DocumentType.VERSION, 
										versiondata) < 0) {
									throw new RuntimeException("Version" +
									" create error");
								} else { 
									if (data.equalsIgnoreCase("routes.xml")) {
										feed = FeedParserFactory.getParser(
												DocumentType.ROUTES, context);
										feed.parse(DocumentType.ROUTES);
									} else if (data.equalsIgnoreCase("trips.xml")) {
										feed = FeedParserFactory.getParser(
												DocumentType.TRIPS, context);
										feed.parse(DocumentType.TRIPS);
									} else if (data.equalsIgnoreCase("stop_times.xml")) {
										feed = FeedParserFactory.getParser(
												DocumentType.STOPTIMES, context);
										feed.parse(DocumentType.STOPTIMES);
									} else if (data.equalsIgnoreCase("stops.xml")) {
										feed = FeedParserFactory.getParser(
												DocumentType.STOPS, context);
										feed.parse(DocumentType.STOPS);
									}
								}
							} else if (mCursor.getInt(1) != Integer.parseInt(version)){
								// need to verify what needs to be updated and make 
								// the necessary update to that specific table 
								if ( mDbHelper.updateInput(DocumentType.VERSION, 
										versiondata) ) {
									String[] temp = data.split("\\.");
									if (temp[0].equalsIgnoreCase("routes")) {
										feed = FeedParserFactory.getParser(
												DocumentType.ROUTES, context);
										feed.parse(DocumentType.ROUTES);
									} else if (temp[0].equalsIgnoreCase("stop_times")) {
										feed = FeedParserFactory.getParser(
												DocumentType.STOPTIMES, context);
										feed.parse(DocumentType.STOPTIMES);
									} else if (temp[0].equalsIgnoreCase("stops")) {
										feed = FeedParserFactory.getParser(
												DocumentType.STOPS, context);
										feed.parse(DocumentType.STOPS);
									} else if (temp[0].equalsIgnoreCase("trips")) {
										feed = FeedParserFactory.getParser(
												DocumentType.TRIPS, context);
										feed.parse(DocumentType.TRIPS);
									} 
								} 
							}
							mCursor.deactivate();
							break;
						case ROUTES:
							if (route_id == null || agency_id == null || 
									route_short_name == null || 
									route_long_name == null || route_type == null) {
								throw new RuntimeException(
								"ROUTES Error: one input is null");
							}
							mCursor = mDbHelper.fetchInput(DocumentType.ROUTES, 
									Integer.parseInt(route_id));

							String[] routesdata = {route_id, agency_id, 
									route_short_name, route_long_name, 
									route_desc, route_type};

							if (mCursor.getCount() <= 0) {
								if (mDbHelper.createInput(DocumentType.ROUTES, 
										routesdata) < 0) {
									throw new RuntimeException("Routes" +
									" create error");
								} 
							} else {
								// actually we could fetch the data and 
								// check if the version is change and 
								// take any necessary action
								mDbHelper.updateInput(DocumentType.ROUTES, 
										routesdata);
							}
							mCursor.deactivate();
							break;
						case TRIPS:
							if (route_id == null || service_id == null || 
									trip_id == null || shape_id == null ||
									direction_id == null || block_id == null) {
								throw new RuntimeException(
								"TRIPS Error: one input is null");
							}
							mCursor = mDbHelper.fetchInput(DocumentType.TRIPS, 
									Integer.parseInt(trip_id));

							String[] tripsdata = {route_id, service_id, 
									trip_id, trip_headsign, 
									direction_id, block_id, shape_id};

							if (mCursor.getCount() <= 0) {
								if (mDbHelper.createInput(DocumentType.TRIPS, 
										tripsdata) < 0) {
									throw new RuntimeException("Trips" +
									" create error");
								} 
							} else {
								// update necessary data if the previous entry exist
								mDbHelper.updateInput(DocumentType.TRIPS, 
										tripsdata);
							}
							mCursor.deactivate();
							break;
						case STOPTIMES:
							if (trip_id == null || arrival_time == null || 
									departure_time == null || stop_id == null ||
									stop_sequence == null || pickup_type == null || 
									drop_off_type == null) {
								throw new RuntimeException(
								"STOPTIMES Error: one input is null");
							}
							mCursor = mDbHelper.fetchInput(DocumentType.STOPTIMES, 
									Integer.parseInt(trip_id), 
									Integer.parseInt(stop_id));

							String[] stoptimesdata = {trip_id, arrival_time, 
									departure_time, stop_id, stop_sequence, 
									pickup_type, drop_off_type, shape_dist_traveled};

							if (mCursor.getCount() <= 0) {
								if (mDbHelper.createInput(DocumentType.STOPTIMES, 
										stoptimesdata) < 0) {
									throw new RuntimeException("Stoptimes" +
									" create error");
								} 
							} else/* if (mCursor.getInt(0) != 
								Integer.parseInt(trip_id) || 
								mCursor.getString(1)
								.equalsIgnoreCase(arrival_time) || 
								mCursor.getString(2)
								.equalsIgnoreCase(departure_time) || 
								mCursor.getInt(3) != 
									Integer.parseInt(stop_id) || 
									mCursor.getInt(4) != 
										Integer.parseInt(stop_sequence) || 
										mCursor.getInt(5) != 
											Integer.parseInt(pickup_type) || 
											mCursor.getInt(6) != 
												Integer.parseInt(drop_off_type) || 
												mCursor.getDouble(7) != 
													Double.parseDouble(shape_dist_traveled))*/ {
								// update necessary data if the previous entry exist
								mDbHelper.updateInput(DocumentType.STOPTIMES, 
										stoptimesdata);
							}
							mCursor.deactivate();
							break;
						case STOPS: 
							if (stop_id == null || stop_check == null || 
									stop_name == null || stop_lat == null ||
									stop_lon == null) {
								throw new RuntimeException(
								"STOPTIMES Error: one input is null");
							}
							mCursor = mDbHelper.fetchInput(DocumentType.STOPS, 
									Integer.parseInt(stop_id));

							String[] stopsdata = {stop_id, stop_check, 
									stop_name, stop_lat, stop_lon};

							if (mCursor.getCount() <= 0) {
								if (mDbHelper.createInput(DocumentType.STOPS, 
										stopsdata) < 0) {
									throw new RuntimeException("Stoptimes" +
									" create error");
								} 
							} else {
								// update necessary data if the previous entry exist
								mDbHelper.updateInput(DocumentType.STOPTIMES, 
										stopsdata);
							}
							mCursor.deactivate();
							break;
						}
					} else if (name.equalsIgnoreCase(DOCUMENT)){
						done = true;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("PullFeedParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
