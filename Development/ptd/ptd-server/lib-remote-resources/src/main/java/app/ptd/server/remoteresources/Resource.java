package app.ptd.server.remoteresources;

import java.io.InputStream;

public interface Resource {

  public InputStream content();
  
  public boolean hasContent();
  
  public boolean modified();

}
