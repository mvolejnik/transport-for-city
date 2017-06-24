package app.ptd.server.remoteresources.http;

import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResourcesTest {

  @Test
  public void testRemoteResource() throws Exception {

    try (HttpResource httpResource = new HttpResource(); InputStream is = httpResource.content(new URL("http://google.com"))){
      byte[] b = new byte[16];
      is.read(b);
      assertEquals("<!doctype html>", new String(b).substring(0, 15), "Unexpected Remote Resource Content."); 
    }
    
  }

}
