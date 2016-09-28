package app.tfc.libs.rss.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import app.tfc.libs.rss.RssException;
import app.tfc.libs.rss.jaxb.rss20.Rss;

public class Rss20ImplTest {
	
	private static String RSS_CZ_DPP_1 = "src/test/resources/dpp.cz/1.xml" ;

	@Test
	public void testGetRss() throws IOException, RssException {
		InputStream rssIs = FileUtils.openInputStream(new File(RSS_CZ_DPP_1));
		app.tfc.libs.rss.Rss rssReader = new Rss20Impl(rssIs);
		Rss rss = rssReader.getRss();
		int itemsSize = rss.getChannel().getItem().size();
		assertEquals("Expecting 423 items (title, link, description)!", 423, itemsSize);
	}

}
