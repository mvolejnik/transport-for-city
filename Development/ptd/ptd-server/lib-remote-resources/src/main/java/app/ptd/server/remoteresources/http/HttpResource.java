package app.ptd.server.remoteresources.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.ptd.server.remoteresources.RemoteResourceException;
import app.ptd.server.remoteresources.Resource;
import app.ptd.server.remoteresources.ResourceImpl;

public class HttpResource implements AutoCloseable {
  
  private static final Logger l = LogManager.getLogger(HttpResource.class);
  
  CloseableHttpClient httpclient;
  
  public HttpResource() {
    httpclient = HttpClients.createDefault();
  }

  public Resource content(URL resource) throws RemoteResourceException{
    return content(resource, null);
  }
  
  public Resource content(URL resource, String etag) throws RemoteResourceException{
    try {
      HttpGet httpGet;
      httpGet = new HttpGet(resource.toURI());
      httpGet.addHeader(arg0, arg1);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      return new ResourceImpl(entity.getContent());
    } catch (URISyntaxException | IOException e) {
      l.error("remoteResource::", e);
      throw new RemoteResourceException(e);
    }    
    
  }

  @Override
  public void close() throws IOException {
    httpclient.close();
  }
  
}
