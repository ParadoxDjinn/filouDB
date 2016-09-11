package filou.media;

import filou.io.IntBuffer;
import java.util.Objects;

/**
 *
 * @author dark
 */
public class SourceEntry {

  public final String key;
  public final String fileSuffix;
  public final IntBuffer buffer = new IntBuffer();

  public SourceEntry(String key, String fileSuffix) {
    if (key == null || key.isEmpty()) {
      throw new NullPointerException("name is empty");
    }
    this.key = key;
    this.fileSuffix = fileSuffix == null ? "" : fileSuffix;
  }

  @Override
  public String toString() {
    return key;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.key);
    hash = 67 * hash + Objects.hashCode(this.fileSuffix);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final SourceEntry other = (SourceEntry) obj;
    return Objects.equals(this.key, other.key)
            && Objects.equals(this.fileSuffix, other.fileSuffix);
  }

}
