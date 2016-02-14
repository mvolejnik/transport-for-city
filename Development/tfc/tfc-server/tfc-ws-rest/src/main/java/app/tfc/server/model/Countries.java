package app.tfc.server.model;

public class Countries extends AbstractIdentificationList<Country> {

	public static final Countries PROTOTYPE;
	
	private static final String NAME = "countries" ;

	static {
		PROTOTYPE = new Countries();
		PROTOTYPE.add(new Country("CZ", "Czech Republic"));
		PROTOTYPE.add(new Country("DE", "Germany"));
		PROTOTYPE.add(new Country("UK", "United Kingdom"));
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
