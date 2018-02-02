package app.ptd.server.remoteresources.http;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResourcesTest {

  private static Server server;

  private static final int PORT = 8088;

  @BeforeAll
  static void initAll() throws Exception {
    try {
      server = new Server(PORT);
      server.setStopAtShutdown(true);
      WebAppContext webAppCtx = new WebAppContext();
      webAppCtx.setContextPath("/test");
      webAppCtx.setResourceBase("src/test/webapp");
      webAppCtx.setClassLoader(HttpResourcesTest.class.getClassLoader());
      server.setHandler(webAppCtx);

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
        InputStream is = httpResource.content(new URL("http://localhost:" + PORT + "/test/simple.json")).get().content()
        ) {
      byte[] b = new byte[17];
      is.read(b);      assertEquals("{\"test\": \"test\"}", new String(b).substring(0, 16), "Unexpected Remote Resource Content.");
    } finally {
      tearDownAll();
    }
  }

}
