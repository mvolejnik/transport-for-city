package app.ptd.server.model;

public interface Identifiable extends Comparable<Identifiable>{
	
	public String getCode();
	
	public String getName();
	
	public String getLocalName();
	
	
}
