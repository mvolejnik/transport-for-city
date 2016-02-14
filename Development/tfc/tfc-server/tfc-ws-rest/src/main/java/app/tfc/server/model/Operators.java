package app.tfc.server.model;

public class Operators extends AbstractIdentificationList<Operator> {
	public static final Operators PROTOTYPE;

	private static final String NAME = "operators";

	static {
		PROTOTYPE = new Operators();
		// PROTOTYPE.add(new Operator("", ""));
		PROTOTYPE.add(new Operator("DPP", "Dopravní podnik hl. m. Prahy, http://www.dpp.cz"));
		PROTOTYPE.add(new Operator("TFL", "Transport for London, https://tfl.gov.uk/"));
		PROTOTYPE.add(new Operator("BVG", "Berliner Verkehrsbetriebe, http://www.bvg.de/"));
		PROTOTYPE.add(new Operator("Tokyo Metro", "東京メトロ  (Tokyo Metro Co., Ltd.),  http://www.tokyometro.jp/ "));

	}

	@Override
	public String getName() {
		return NAME;
	}
}
