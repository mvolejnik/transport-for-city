package app.tfc.server.status.cz.prg.dpp;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

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
		l.info("statusUpdates()::");
		Rss rss;
		List<RssItem> items;
		List<StatusUpdate> statusUpdates = new ArrayList<>();
		try {
			rss = new Rss20Impl(rssInputStream);
		} catch (RssException e) {
			l.error("DppStatusUpdates()::");
			throw new StatusUpdateException("Unable to parse RSS!", e);
		}

		items = rss.getRss().getChannel().getItem();
		for (RssItem item : items) {
			statusUpdates.add(parseStatusUpdate(item));
		}
		return statusUpdates;
	}

	private StatusUpdate parseStatusUpdate(RssItem rss) {
		l.info("parseStatusUpdate()::");
		String title = null;
		String description = null;
		String type = null;
		String line = null;
		URL infoReference = null;
		List<Object> attrs = rss.getTitleOrDescriptionOrLink();
		for (Object attr : attrs) {
			if (attr instanceof JAXBElement<?>) {// TODO generics
				JAXBElement e = (JAXBElement) attr;
				Class clazz = ((JAXBElement) attr).getDeclaredType();
				l.debug("parseStatusUpdate():: Attribute of type [%s].", clazz);
				String name = ((JAXBElement) attr).getName().getNamespaceURI() + ":" + ((JAXBElement) attr).getName().getLocalPart();
				Object value = ((JAXBElement) attr).getValue();
				if (value != null) {
					switch (name) {
					case ":title":
						l.debug("parseStatusUpdate():: Processing title");
						title = value.toString().trim();
						break;
					case ":description":
						l.debug("parseStatusUpdate():: Processing description");
						// description = value;
						break;
					case ":emergency_types":
						l.debug("parseStatusUpdate():: Processing emergency_types");
						// TODO
						break;
					case ":time_start":
						l.debug("parseStatusUpdate():: Processing time_start");
						// TODO
						break;
					case ":time_stop":
						l.debug("parseStatusUpdate():: Processing time_stop");
						// TODO
						break;
					case ":integrated_rescue_system":
						l.debug("parseStatusUpdate():: Processing integrated_rescue_system");
						// TODO
						break;
					case ":aff_line_types":
						l.debug("parseStatusUpdate():: Processing aff_line_types");
						// TODO
						// type = value;
						break;
					case ":aff_lines":
						// line = value;
						break;
					default:
						l.debug("parseStatusUpdate():: unable to parse %s", name);
					}
				}
			} else if (attr instanceof Element) {
				Element element = (Element) attr;
				String name = element.getLocalName();
				switch (name) {
				case "content_encoded":
					l.debug("parseStatusUpdate():: Processing content_encoded");
					CharacterData text = (CharacterData) element.getFirstChild();
					try {
						parseContent(text.getData());
					} catch (DOMException | XMLStreamException e) {
						l.warn("Unable to parse RSS Item Content Data", e);
						l.debug("parseStatusUpdate():: [%s]", text);
					}
					break;
				default:
					l.debug("parseStatusUpdate():: unable to parse %s", name);
				}
			}
		}
		StatusUpdate update = new StatusUpdateImpl(title, description, type, line, infoReference);
		return update;// TODO
	}
	
	private Content parseContent(String xmlContent) throws XMLStreamException{
		Content content = new Content();
		StringBuilder xml = new StringBuilder();
		xml.append("<content>").append(xmlContent).append("</content>");
		XMLInputFactory f = XMLInputFactory.newInstance();
		XMLStreamReader r = f.createXMLStreamReader(IOUtils.toInputStream(xml.toString(), Charset.forName("UTF-8")));
		while(r.hasNext()) {
		    int t = r.next();
		    if (r.isStartElement()){
		    	//TODO Content
		    }
		}
		return content;
	}
	
	private static class Content {
		private String section;
		private String emergencyType;
		private String start;
		private String stop;
		private String finalStop;
		private String integratedRescueSystem;
		private List<String> lineTypes;
		private String lines;
		public String getSection() {
			return section;
		}
		public void setSection(String section) {
			this.section = section;
		}
		public String getEmergencyType() {
			return emergencyType;
		}
		public void setEmergencyType(String emergencyType) {
			this.emergencyType = emergencyType;
		}
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getStop() {
			return stop;
		}
		public void setStop(String stop) {
			this.stop = stop;
		}
		public String getFinalStop() {
			return finalStop;
		}
		public void setFinalStop(String finalStop) {
			this.finalStop = finalStop;
		}
		public String getIntegratedRescueSystem() {
			return integratedRescueSystem;
		}
		public void setIntegratedRescueSystem(String integratedRescueSystem) {
			this.integratedRescueSystem = integratedRescueSystem;
		}
		public List<String> getLineTypes() {
			return lineTypes;
		}
		public void setLineTypes(List<String> lineTypes) {
			this.lineTypes = lineTypes;
		}
		public String getLines() {
			return lines;
		}
		public void setLines(String lines) {
			this.lines = lines;
		}
		
	}

}
