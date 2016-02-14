package app.tfc.server.model;

public interface Identifiable extends Comparable<Identifiable>{
	
	public String getCode();
	
	public String getName();
}
