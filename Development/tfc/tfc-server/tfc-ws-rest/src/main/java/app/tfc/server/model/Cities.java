package app.tfc.server.model;

public class Cities extends AbstractIdentificationList<City> {

	public static final Cities PROTOTYPE;
	
	private static final String NAME = "cities" ;
	
	static {
		PROTOTYPE = new Cities();
		PROTOTYPE.add(new City("PRG", "Prague"));
		PROTOTYPE.add(new City("BER", "Berlin"));
		PROTOTYPE.add(new City("LON", "London"));
		PROTOTYPE.add(new City("PLZ", "Plzeň"));
	}

	@Override
	public String getName() {
		return NAME;
	}

}
