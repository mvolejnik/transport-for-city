package app.tfc.server.status.cz.prg.dpp;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import app.tfc.server.status.StatusUpdate;
import app.tfc.server.status.StatusUpdateException;

public class DppFactoryTest {

	private static final String RSS_DPP_SIMPLE = "src/test/resources/cz.dpp/simple.xml";
	private static final String RSS_DPP_EMPTY = "src/test/resources/cz.dpp/empty.xml";
	private static final String RSS_DPP_GUID_MISSING = "src/test/resources/cz.dpp/guid-missing.xml";
	private static final String RSS_DPP_GUID_EMPTY = "src/test/resources/cz.dpp/guid-empty.xml";
	
	@Test
	@Tag("fast")
	@DisplayName("Parsing RSS - title.")
	public void testStatusUpdatesSimpleTitle() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("ul. Povltavská, Pod lisem a Trojská (28.09. 09:30 - 28.09. 12:30)", su.getTitle(), "Unexpected RSS item title!");
	}
	
	
	@Test
	@Tag("fast")
	@DisplayName("Parsing RSS - description.")
	public void testStatusUpdatesSimpleDescription() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("DESCRIPTION", su.getDescription(), "Unexpected RSS item description!");
	}
	
	@Test
	@Tag("fast")
	@DisplayName("Parsing RSS - lines size")
	public void testStatusUpdatesLines() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertFalse(su.getLines().isEmpty(), "Unuexpected empty lines list.");
	}
	
	@Test
	@Tag("fast")
	@DisplayName("Parsing RSS - line")
	public void testStatusUpdatesLine() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertTrue(su.getLines().contains("112"), "Expected line is not present.");
	}
	
	@Test
	@Tag("fast")
	@DisplayName("Parsing RSS - transport means type")
	public void testStatusUpdatesType() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("Provoz omezen", su.getType(), "Unexpected outage type.");
	}
	
	/*
	@Disabled
	@Test
	public void testStatusUpdatesSimpleType() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("BUS", su.getType(), "Unexpected RSS item type!");
	}
	*/
	
	@Test
	public void testStatusUpdatesEmptyTitle() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_EMPTY)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected RSS item title!", "", su.getTitle());
	}
	
	@Test
	public void testStatusUpdatesEmptyDescription() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_EMPTY)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected RSS item description!", "", su.getDescription());
	}
	
	@Test
	public void testStatusUpdatesEmptyLine() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_EMPTY)));
		StatusUpdate su = updates.get(0);
		assertTrue("No lines should be found.", su.getLines().isEmpty());
	}
	
	@Test
	public void testStatusUpdatesEmptyType() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_EMPTY)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected outage type.", "", su.getType());
	}
	
	@Test
	public void testStatusUpdatesMissingGuid() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_GUID_MISSING)));
		StatusUpdate su = updates.get(0);
		assertNotNull("Unexpected UUID for missing GUID.", su.getUuid());
	}
	
	@Test
	public void testStatusUpdatesEmptyGuid() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_GUID_EMPTY)));
		StatusUpdate su = updates.get(0);
		assertNotNull("Unexpected UUID for empty GUID.", su.getType());
	}
}
