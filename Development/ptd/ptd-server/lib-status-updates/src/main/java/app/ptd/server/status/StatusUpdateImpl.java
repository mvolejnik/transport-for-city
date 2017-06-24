package app.ptd.server.status;

import java.net.URL;
import java.util.Collection;
import java.util.UUID;

/**
 * TODO
 * 
 * @author mvolejnik
 *
 */
public class StatusUpdateImpl implements StatusUpdate {

  private UUID uuid;
  private String title;
  private String description;
  private String type;
  private Collection<String> lines;
  private URL infoReference;
	
  public StatusUpdateImpl(UUID uuid, String title, String description, String type, Collection<String> lines, URL infoReference) {
    super();
    this.uuid = uuid;
    this.title = title;
    this.description = description;
    this.type = type;
    this.lines = lines;
    this.infoReference = infoReference;
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getType() {
    return type;
  }
	
  @Override
  public Collection<String> getLines() {
    return lines;
  }

  @Override
  public URL getInfoReference() {
    return infoReference;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((infoReference == null) ? 0 : infoReference.hashCode());
    result = prime * result + ((lines == null) ? 0 : lines.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StatusUpdateImpl other = (StatusUpdateImpl) obj;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (infoReference == null) {
      if (other.infoReference != null)
        return false;
    } else if (!infoReference.equals(other.infoReference))
      return false;
    if (lines == null) {
      if (other.lines != null)
        return false;
    } else if (!lines.equals(other.lines))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "StatusUpdateImpl [uuid=" + uuid + ", title=" + title + ", description=" + description + ", type=" + type
        + ", lines=" + lines + ", infoReference=" + infoReference + "]";
  }

}
