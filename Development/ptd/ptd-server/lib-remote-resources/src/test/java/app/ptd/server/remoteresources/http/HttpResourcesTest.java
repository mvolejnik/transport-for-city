package app.ptd.server.remoteresources.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;
import app.ptd.server.remoteresources.RemoteResourceException;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResourcesTest {

  @Test
  public void testRemoteResource() throws RemoteResourceException, IOException {
    HttpResource httpResource = new HttpResource();
    try (InputStream is = httpResource.content(new URL("http://google.com"))){
      byte[] b = new byte[100];
      is.read(b);
      assertEquals("<!doctype html>", new String(b).substring(0, 16), "Unexpected Remote Resource Content."); 
    }
    
  }

}
