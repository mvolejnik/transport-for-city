package app.tfc.server.status.cz.prg.dpp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import app.tfc.server.status.StatusUpdate;
import app.tfc.server.status.StatusUpdateException;

public class DppFactoryTest {

	private static final String RSS_DPP_SIMPLE = "src/test/resources/cz.dpp/simple.xml";
	
	@Test
	public void testStatusUpdatesSimpleTitle() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected RSS item title!", "ul. Povltavská, Pod lisem a Trojská (28.09. 09:30 - 28.09. 12:30)", su.getTitle());
	}
	
	@Test
	public void testStatusUpdatesSimpleDescription() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected RSS item description!", "DESCRIPTION", su.getDescription());
	}
	
	@Test
	public void testStatusUpdatesLines() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertFalse("Unuexpected empty lines list.", su.getLines().isEmpty());
	}
	
	@Test
	public void testStatusUpdatesLine() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertTrue("Expected line is not present.", su.getLines().contains("112"));
	}
	
	@Ignore
	@Test	
	public void testStatusUpdatesSimpleType() throws StatusUpdateException, IOException {
		DppFactory dppFactory = new DppFactory();
		List<StatusUpdate> updates = dppFactory.statusUpdates(FileUtils.openInputStream(new File(RSS_DPP_SIMPLE)));
		StatusUpdate su = updates.get(0);
		assertEquals("Unexpected RSS item type!", "BUS", su.getType());
	}
	
}
