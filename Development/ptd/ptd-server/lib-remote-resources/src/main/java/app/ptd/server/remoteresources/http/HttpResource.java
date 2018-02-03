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

  public Optional<Resource> content(URL resourceUrl) throws RemoteResourceException {
    return content(resourceUrl, null);
  }

  public Optional<Resource> content(URL resourceUrl, String etag) throws RemoteResourceException {
    try {
      Optional<Resource> resource;
      HttpGet httpGet;
      httpGet = new HttpGet(resourceUrl.toURI());
      if (etag != null) {
        httpGet.addHeader(ETAG_IF_NONE_MATCH, etag);
      }
      CloseableHttpResponse response = httpclient.execute(httpGet);
      ResponseStatusCode statusCode = new ResponseStatusCode(response.getStatusLine().getStatusCode());
      if (statusCode.isOk()) {
        HttpEntity entity = response.getEntity();
        resource = Optional.ofNullable(new ResourceImpl(entity.getContent()));
      } else if (statusCode.isClientError()) {
        l.error("Unable to get resource '{}' due to client error: '{}' ({}).", resourceUrl, statusCode.statusCode,
            response.getStatusLine().getReasonPhrase());
        throw new RemoteResourceException(String.format("Unable to get resource '%s' due to client error: '%s' (%s).",
            resourceUrl, statusCode.statusCode, response.getStatusLine().getReasonPhrase()));
      } else {
        l.warn("Resource '{}' unexpected response '{}' ({}).", resourceUrl, statusCode.statusCode,
            response.getStatusLine().getReasonPhrase());
        HttpEntity entity = response.getEntity();
        resource = entity == null ? Optional.empty() : Optional.ofNullable(new ResourceImpl(entity.getContent()));
      }

      return resource;
    } catch (URISyntaxException | IOException e) {
      l.error("content::", e);
      throw new RemoteResourceException(e);
    }

  }

  @Override
  public void close() throws IOException {
    httpclient.close();
  }

  private static class ResponseStatusCode {
    int statusCode;

    ResponseStatusCode(int statusCode) {
      if (statusCode < 100 || statusCode > 599) {
        throw new IllegalArgumentException("Illegal HTTP Status code, expected range: 100 - 599.");
      }
      this.statusCode = statusCode;
    }

    public boolean isClientError() {
      return statusCode >= 400 && statusCode <= 499;
    }

    public boolean isOk() {
      return statusCode == 200 || statusCode == 304;
    }

  }

}
