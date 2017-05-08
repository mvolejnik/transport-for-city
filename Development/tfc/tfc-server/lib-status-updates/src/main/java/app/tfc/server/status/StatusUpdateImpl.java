package app.tfc.server.status;

import java.net.URL;
import java.util.Collection;

public class StatusUpdateImpl implements StatusUpdate {
	
	private String title;
	private String description;
	private String type;
	private Collection<String> lines;
	private URL infoReference;
	
	public StatusUpdateImpl(String title, String description, String type, Collection<String> lines, URL infoReference) {
		super();
		this.title = title;
		this.description = description;
		this.type = type;
		this.lines = lines;
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
	public Collection<String> getLines() {
		return lines;
	}

	@Override
	public URL getInfoReference() {
		return infoReference;
	}

}
