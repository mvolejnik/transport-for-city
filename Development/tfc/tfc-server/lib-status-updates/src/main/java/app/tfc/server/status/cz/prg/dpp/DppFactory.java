package app.tfc.server.status.cz.prg.dpp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

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
			l.error("statusUpdates::");
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
		Collection<String> lines = new ArrayList<String>();
		Calendar startAt, finishAt, expectedFinishAt;
		URL infoReference = null;
		List<Object> attrs = rss.getTitleOrDescriptionOrLink();
		for (Object attr : attrs) {
			if (attr instanceof JAXBElement<?>) {// TODO generics
				JAXBElement e = (JAXBElement) attr;
				Class clazz = ((JAXBElement) attr).getDeclaredType();
				l.trace("parseStatusUpdate():: Attribute of type [%{}].", clazz);
				String name = ((JAXBElement) attr).getName().getNamespaceURI() + ":"
						+ ((JAXBElement) attr).getName().getLocalPart();
				Object value = ((JAXBElement) attr).getValue();
				if (value != null) {
					switch (name) {
					case ":title":
						l.debug("parseStatusUpdate():: Processing title");
						title = value.toString().trim();
						break;
					case ":description":
						l.debug("parseStatusUpdate():: Processing description");
						description = StringUtils.trim(value.toString());
						break;
					/*
					 * case ":emergency_types": l.
					 * debug("parseStatusUpdate():: Processing emergency_types"
					 * ); // String emergency =
					 * StringUtils.trim(value.toString()); break; case
					 * ":time_start":
					 * l.debug("parseStatusUpdate():: Processing time_start");
					 * String start = StringUtils.trim(value.toString()); try {
					 * startAt =
					 * DateUtils.toCalendar(DateUtils.parseDateStrictly(start,
					 * "yyyy-MM-dd HH:mm:ss")); } catch (ParseException e2) {
					 * l.warn("Unable to parse time [%s]", start); } break; case
					 * ":time_stop":
					 * l.debug("parseStatusUpdate():: Processing time_stop");
					 * String finish = StringUtils.trim(value.toString()); try {
					 * finishAt =
					 * DateUtils.toCalendar(DateUtils.parseDateStrictly(finish,
					 * "yyyy-MM-dd HH:mm:ss")); } catch (ParseException e2) {
					 * l.warn("Unable to parse time [%s]", finish); } break;
					 * case ":integrated_rescue_system": l.
					 * debug("parseStatusUpdate():: Processing integrated_rescue_system"
					 * ); // TODO break; case ":aff_line_types":
					 * l.debug("parseStatusUpdate():: Processing aff_line_types"
					 * ); //TODO break; case ":aff_lines": lines = new
					 * ArrayList<>(); String affLines =
					 * StringUtils.trim(value.toString()); for (String line :
					 * StringUtils.split(affLines, ",")){ lines.add(line); }
					 * break;
					 */
					case ":link":
						String link = value.toString();
						if (StringUtils.isNotEmpty(link)) {
							try {
								infoReference = new URL(link);
							} catch (MalformedURLException e1) {
								l.warn("Unable to parse link [{}]", link);
							}
						}
						break;
					default:
						l.debug("parseStatusUpdate():: unable to parse [{}]", name);
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
						Content c = parseContent(text.getData());
						lines.addAll(c.getLines());
						type = c.getEmergencyType();
					} catch (DOMException | XMLStreamException e) {
						l.warn("Unable to parse RSS Item Content Data", e);
						l.debug("parseStatusUpdate():: [{}]", text);
					}
					break;
				default:
					l.debug("parseStatusUpdate():: unable to parse [{}]", name);
				}
			}
		}
		StatusUpdate update = new StatusUpdateImpl(title, description, type, lines, infoReference);
		return update;
	}

	private Content parseContent(String xmlContent) throws XMLStreamException {
		l.debug("parseContent():: Parsing content_encoded element.");
		Content content = new Content();
		StringBuilder xml = new StringBuilder();
		xml.append("<content>").append(xmlContent).append("</content>");
		XMLInputFactory f = XMLInputFactory.newInstance();
		XMLStreamReader r = f.createXMLStreamReader(IOUtils.toInputStream(xml.toString(), Charset.forName("UTF-8")));
		if (r.hasNext()) {
			r.next();
			if (r.isStartElement()) {
				String rootElementName = r.getName().getLocalPart();
				l.trace("parseContent():: Root Start Element [{}]", rootElementName);
				if ("content".equals(rootElementName)) {
					int depth = 0;
					while (r.hasNext() && depth >= 0) {
						r.next();
						if (r.isStartElement()) {
							depth++;
							String name = r.getName().getLocalPart();
							switch (name) {
							case "emergency_types": ///Provoz omezen, Zpoždění spojů, Provoz zastaven
								l.trace("parseContent():: Parsing emergency types.");
								content.setEmergencyType(r.getElementText());
								break;
							case "time_start":
								l.trace("parseContent():: Parsing start time.");
								content.setStart(parseTimeStart());
								break;
							case "time_stop":
								l.trace("parseContent():: Parsing stop time.");
								content.setStop(parseTimeStop());
								break;
							case "time_final_stop":
								l.trace("parseContent():: Parsing final stop time.");
								content.setFinalStop(parseTimeFinalStop());
								break;
							case "integrated_rescue_system":
								l.trace("parseContent():: Parsing integrated rescue system involved.");
								// do nothing
								break;
							case "aff_line_types":
								l.trace("parseContent():: Parsing line types.");
								// do nothing
								break;
							case "aff_lines":
								l.trace("parseContent():: Parsing affected lines.");								
								content.setLines(parseAffectedLines(r.getElementText()));
								break;
							default:
								l.info("parseContent(): Unsupported content type [{}]", name);
							}
						}
					}
				}
			}
		}
		return content;
	}

	private String parseSection() {

		String section = null;

		return section;
	}

	private String parseEmergencyType() {
		String type = null;

		return type;
	}

	private String parseTimeStart() {
		String start = null;

		return start;
	}

	private String parseTimeStop() {
		String stop = null;

		return stop;
	}

	private String parseTimeFinalStop() {
		String finalStop = null;

		return finalStop;
	}

	private Collection<String> parseAffectedLines(String linesStr) {		
		l.debug("parseAffectedLines:: Parsing lines [{}]", linesStr);
		List<String> lines = new ArrayList<String>();
		if (linesStr != null && linesStr.trim().length() > 0){
			String[] rawLines = linesStr.split(",");
			for (String l : rawLines){
				lines.add(l.trim());
			}
		}
		return Collections.unmodifiableCollection(lines);
	}

	private static class Content {
		private String section;
		private String emergencyType;
		private String start;
		private String stop;
		private String finalStop;
		private String integratedRescueSystem;
		private List<String> lineTypes;
		private Collection<String> lines = Collections.<String>emptyList();

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

		public Collection<String> getLines() {
			return lines;
		}

		public void setLines(Collection<String> lines) {
			this.lines = new ArrayList<>();
			this.lines.addAll(lines);
		}

	}

}
