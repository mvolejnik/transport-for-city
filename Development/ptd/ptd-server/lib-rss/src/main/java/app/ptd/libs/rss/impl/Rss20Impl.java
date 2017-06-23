package app.ptd.libs.rss.impl;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import app.ptd.libs.rss.RssException;
import app.ptd.libs.rss.jaxb.rss20.Rss;

public class Rss20Impl implements app.ptd.libs.rss.Rss{
	
	private Rss rss;

	public Rss20Impl(InputStream rssInputStream) throws RssException{
		try {
	        JAXBContext ctx =  JAXBContext.newInstance(Rss.class);
	        Unmarshaller mrs = ctx.createUnmarshaller();
	        rss = (Rss) mrs.unmarshal(rssInputStream);
	        
        } catch (JAXBException e) {
	        throw new RssException(e);
        }
	}

	@Override
	public Rss getRss() {
		return rss;
	}
	
}
