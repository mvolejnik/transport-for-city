package app.ptd.server.remoteresources;

import java.io.InputStream;
import java.util.Optional;

public interface Resource {

  public InputStream content();
  
  public Optional<String> fingerprint();
  
  public boolean hasContent();
  
  public boolean modified();

}
