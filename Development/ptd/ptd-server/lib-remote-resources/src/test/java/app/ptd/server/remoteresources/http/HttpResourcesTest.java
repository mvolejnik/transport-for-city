package app.ptd.server.remoteresources.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

import static org.junit.jupiter.api.Assertions.*;

public class HttpResourcesTest {

  private static Server server;

  private static final int PORT = 8088;
  
  private static final String NOT_MODIFIED = "NOT_MODIFY" ;
  private static final String NOT_EXISTING_RESOURCE = "/not-existing-resource" ;

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
          if (NOT_MODIFIED.equals(request.getHeader("If-None-Match"))) {
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
        InputStream is = httpResource.content(new URL("http://localhost:" + PORT + "/test/simple.json"))
            .get()
            .content()) {
      byte[] b = new byte[17];
      is.read(b);
      assertEquals("{\"test\": \"test\"}", new String(b).substring(0, 16), "Unexpected Remote Resource Content.");
    } finally {
      tearDownAll();
    }
  }

  @Test
  public void testnotModify() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();){
        assertFalse(httpResource.content(new URL("http://localhost:" + PORT + "/test/simple.json"), NOT_MODIFIED).isPresent(), "Unmodified resource shouldn't be returned.");
    } finally {
      tearDownAll();
    }
  }
  
  @Test
  public void testNotExistingResource() throws Exception {
    initAll();
    try (HttpResource httpResource = new HttpResource();){
        assertThrows(RemoteResourceException.class, () -> httpResource.content(new URL("http://localhost:" + PORT + NOT_EXISTING_RESOURCE)));
    } finally {
      tearDownAll();
    }
  }

}
