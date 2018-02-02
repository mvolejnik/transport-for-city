package app.ptd.server.remoteresources.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

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
  
  private static final String ETAG_IF_NONE_MATCH = "If-None-Match";

  CloseableHttpClient httpclient;
  
  public HttpResource() {
    httpclient = HttpClients.createDefault();
  }

  public Optional<Resource> content(URL resourceUrl) throws RemoteResourceException{
    return content(resourceUrl, null);
  }
  
  public Optional<Resource> content(URL resourceUrl, String etag) throws RemoteResourceException{
    try {
      Optional<Resource> resource;
      HttpGet httpGet;
      httpGet = new HttpGet(resourceUrl.toURI());
      if (etag != null) {
        httpGet.addHeader(ETAG_IF_NONE_MATCH, etag);
      }
      CloseableHttpResponse response = httpclient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == 403) {
        resource = Optional.empty();
      } else {
      HttpEntity entity = response.getEntity();
        resource = Optional.ofNullable(new ResourceImpl(entity.getContent()));
      }
      return resource;
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
