package app.tfc.server.status;

import java.net.URL;

public class StatusUpdateImpl implements StatusUpdate {
	
	private String title;
	private String description;
	private String type;
	private String line;
	private URL infoReference;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getLine() {
		return line;
	}

	@Override
	public URL getInfoReference() {
		return infoReference;
	}

}
