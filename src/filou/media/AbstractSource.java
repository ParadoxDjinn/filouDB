package filou.media;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dark
 */
public abstract class AbstractSource implements Source {

  protected final Set<SourceEntry> entries = new HashSet<>();

  @Override
  public final Set<SourceEntry> getContent() {
    return Collections.unmodifiableSet(entries);
  }

  @Override
  public final void setContent(Set<SourceEntry> entries) {
    this.entries.clear();
    this.entries.addAll(entries);
  }
}
