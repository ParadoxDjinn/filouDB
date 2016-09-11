package filou.media;

import java.util.Set;

/**
 *
 * @author dark
 */
public interface Source {

  public Set<SourceEntry> getContent();

  public void setContent(Set<SourceEntry> entries);

}
