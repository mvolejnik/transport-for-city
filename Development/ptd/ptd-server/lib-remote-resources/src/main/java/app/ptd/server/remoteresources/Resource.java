package app.ptd.server.remoteresources;

import java.io.InputStream;
import java.util.Optional;

public interface Resource {

  public Optional<InputStream> content();
  
  public Optional<String> fingerprint();
  
  public Optional<byte[]> digest();

}