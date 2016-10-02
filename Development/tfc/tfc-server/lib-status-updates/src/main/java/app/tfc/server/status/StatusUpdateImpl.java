package app.tfc.server.status;

import java.net.URL;

public class StatusUpdateImpl implements StatusUpdate {
	
	private String title;
	private String description;
	private String type;
	private String line;
	private URL infoReference;
	
	public StatusUpdateImpl(String title, String description, String type, String line, URL infoReference) {
		super();
		this.title = title;
		this.description = description;
		this.type = type;
		this.line = line;
		this.infoReference = infoReference;
	}

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
