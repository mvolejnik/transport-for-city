package app.tfc.server.status;

import java.net.URL;
import java.util.Collection;

public interface StatusUpdate {

	public String getTitle();
	
	public String getDescription();
	
	public String getType();
	
	public Collection<String> getLines();
	
	public URL getInfoReference();
	
}
