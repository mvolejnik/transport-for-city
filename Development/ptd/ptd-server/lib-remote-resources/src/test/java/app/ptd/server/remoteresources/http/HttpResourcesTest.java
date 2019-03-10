package app.ptd.server.remoteresources.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import app.ptd.server.remoteresources.RemoteResourceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResourcesTest {

  private static Server server;

  private static final String MOCK_NOT_MODIFIED = "NOT_MODIFY" ;
  private static final String NOT_EXISTING_RESOURCE = "/not-existing-resource" ;
  private static final ZonedDateTime TIME_LAST_DOWNLOADED = ZonedDateTime.parse("2018-01-01T12:00:00.00Z");
  private static final ZonedDateTime TIME_RESOURCE_NOT_UPDATED = ZonedDateTime.parse("2018-01-01T10:00:00.00Z");
  private static final String SCHEME = "http";
  private static final String HOSTNAME = "localhost";
  private static final int PORT = 8088;
  private static final String URL_BASE = SCHEME + "://" + HOSTNAME + ":" + PORT ;
  private static final String URL_PATH_SIMPLE = "/test/simple.json";

  @BeforeAll
  static void initAll() throws Exception {
    try {
      server = new Server(PORT);
      server.setStopAtShutdown(true);
      WebAppContext webAppCtx = new WebAppContext();
      webAppCtx.setContextPath("/test");
      webAppCtx.setResourceBase("src/test/webapp");
      webAppCtx.setClassLoader(HttpResourcesTest.class.getClassLoader());
      Handler notModifyHandler = new AbstractHandler() {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
          if (MOCK_NOT_MODIFIED.equals(request.getHeader("If-None-Match"))) {
            response.setStatus(304);
            baseRequest.setHandled(true);           
          }
          String ifModifiedSince = request.getHeader("If-Modified-Since");
          if (ifModifiedSince != null
                  && TIME_RESOURCE_NOT_UPDATED.isBefore(ZonedDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(ifModifiedSince)))
                  && request.getHeader("If-None-Match") == null) {
            response.setStatus(304);
            baseRequest.setHandled(true);
          }
        }
      };
      HandlerList handlers = new HandlerList(notModifyHandler, webAppCtx);
      server.setHandler(handlers);
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @AfterAll
  static void tearDownAll() throws Exception {
    server.stop();
  }

  @Test
  public void testRemoteResource() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();
        InputStream is = httpResource.content(new URL(URL_BASE + URL_PATH_SIMPLE))
            .get()
            .content().get()) {
      byte[] b = new byte[17];
      is.read(b);
      assertEquals("{\"test\": \"test\"}", new String(b).substring(0, 16), "Unexpected Remote Resource Content.");
    } finally {
      tearDownAll();
    }
  }

  @Test
  public void testNotModifyETag() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();){
        assertTrue(httpResource.content(new URL(URL_BASE + URL_PATH_SIMPLE), Optional.of(MOCK_NOT_MODIFIED), Optional.empty()).isEmpty(), "Unmodified resource shouldn't be returned.");
    } finally {
      tearDownAll();
    }
  }
  
  @Test
  public void testNotModifyTime() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();){
      DateTimeFormatter.RFC_1123_DATE_TIME.format(TIME_LAST_DOWNLOADED);
        assertTrue(httpResource.content(new URL(URL_BASE + URL_PATH_SIMPLE), Optional.empty(), Optional.of(TIME_LAST_DOWNLOADED)).isEmpty(), "Unmodified resource shouldn't be returned.");
    } finally {
      tearDownAll();
    }
  }
  
  @Test
  public void testNotExistingResource() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();){
        assertThrows(RemoteResourceException.class, () -> httpResource.content(new URL(URL_BASE + NOT_EXISTING_RESOURCE)));
    } finally {
      tearDownAll();
    }
  }

}
