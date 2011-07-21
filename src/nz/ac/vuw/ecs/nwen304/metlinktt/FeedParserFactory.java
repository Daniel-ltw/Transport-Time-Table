package nz.ac.vuw.ecs.nwen304.metlinktt;

import android.content.Context;

public abstract class FeedParserFactory {
	static String versionFeed = "http://ecs.vuw.ac.nz/~ian/nwen304/versions.xml";
	static String routesFeed = "http://ecs.vuw.ac.nz/~ian/nwen304/routes.xml";
	static String tripsFeed = "http://ecs.vuw.ac.nz/~ian/nwen304/trips.xml";
	static String stopTimesFeed = "http://ecs.vuw.ac.nz/~ian/nwen304/stop_times.xml";
	static String stopsFeed = "http://ecs.vuw.ac.nz/~ian/nwen304/stops.xml";

	public static FeedParser getParser(Context context){
		return getParser(DocumentType.VERSION, context);
	}
	
	public static FeedParser getParser(DocumentType type, Context context){
		switch(type) {
		case VERSION: 
			return new XmlPullFeedParser(versionFeed, context);
		case ROUTES: 
			return new XmlPullFeedParser(routesFeed, context);
		case TRIPS: 
			return new XmlPullFeedParser(tripsFeed, context);
		case STOPTIMES: 
			return new XmlPullFeedParser(stopTimesFeed, context);
		case STOPS: 
			return new XmlPullFeedParser(stopsFeed, context);
		default: return null;
		}
	}
}
