package nz.ac.vuw.ecs.nwen304.metlinktt;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser implements FeedParser {

	// names of the XML tags
	static final String DOCUMENT = "document";
	static final String RECORD = "record";
	
	private final URL feed;

	protected BaseFeedParser(String feed){
		try {
			this.feed = new URL(feed);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return feed.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}