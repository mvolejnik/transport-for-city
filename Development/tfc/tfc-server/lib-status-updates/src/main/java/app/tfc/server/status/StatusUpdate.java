package app.tfc.server.status;

import java.net.URL;

public interface StatusUpdate {

	public String getTitle();
	
	public String getDescription();
	
	public String getType();
	
	public String getLine();
	
	public URL getInfoReference();
	
}
