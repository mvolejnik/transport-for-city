package app.tfc.server.status.cz.prg.dpp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.tfc.libs.rss.Rss;
import app.tfc.libs.rss.RssException;
import app.tfc.libs.rss.impl.Rss20Impl;
import app.tfc.libs.rss.jaxb.rss20.RssItem;
import app.tfc.server.status.StatusUpdate;
import app.tfc.server.status.StatusUpdateException;
import app.tfc.server.status.StatusUpdateImpl;

public class DppFactory {

	private static final Logger l = LogManager.getLogger(DppFactory.class);

	public List<StatusUpdate> statusUpdates(InputStream rssInputStream) throws StatusUpdateException {
		Rss rss;
		List<RssItem> items;
		List<StatusUpdate>statusUpdates = new ArrayList<>();
		try {
			rss = new Rss20Impl(rssInputStream);
		} catch (RssException e) {
			l.error("DppStatusUpdates()::");
			throw new StatusUpdateException("Unable to parse RSS!", e);
		}

		items = rss.getRss().getChannel().getItem();
		for (RssItem item : items) {
			parseStatusUpdate(item);
		}
		return statusUpdates;
	}

	private StatusUpdate parseStatusUpdate(RssItem rss) {
		l.info("parseStatusUpdate()::");
		String title, description, type, line, infoReference;
		List<Object> attrs = rss.getTitleOrDescriptionOrLink();
		for (Object attr : attrs) {
			if (attr instanceof JAXBElement<?>) {// TODO generics
				JAXBElement e = (JAXBElement) attr;
				Class clazz = ((JAXBElement) attr).getDeclaredType();
				l.debug("parseStatusUpdate():: Attribute of type [%s].", clazz);
				if (clazz.getName().equals("String")){
					String name = ((JAXBElement) attr).getName().getNamespaceURI() + ":" + ((JAXBElement) attr).getName().getLocalPart();
					String value = ((JAXBElement) attr).getValue().toString();
					switch (name) {
					case "title":
						title = value;
						break;
					case "description":
						description = value;
						break;
					case "content_encoded":
						//TODO
						break;
					case "emergency_types":
						//TODO
						break;
					case "time_start":
						//TODO
						break;
					case "time_stop":
						//TODO
						break;
					case "integrated_rescue_system":
						//TODO
						break;
					case "aff_line_types":
						//TODO
						type = value;
						break;
					case "aff_lines":
						line = value;
						break;
					default:
						l.debug("parseStatusUpdate():: unable to parse %s", name);
					}
				}
			}
		}
		StatusUpdate update = new StatusUpdateImpl();
		return update;//TODO
	}

}
