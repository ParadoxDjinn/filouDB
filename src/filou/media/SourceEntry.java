package filou.media;

import filou.io.IntBuffer;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Objects;

/**
 *
 * @author dark
 */
public class SourceEntry {

  public final String key;
  public final String fileSuffix;
  public final IntBuffer buffer = new IntBuffer();
  public final Instant creationTime;
  public final Instant lastModifiedTime;
  public final Instant lastAccessTime;

  public SourceEntry(String key, String fileSuffix,
          FileTime creationTime, FileTime lastModifiedTime,
          FileTime lastAccessTime) {
    this(key, fileSuffix,
            (creationTime == null) ? Instant.now() : creationTime.toInstant(),
            (lastModifiedTime == null) ? Instant.now() : lastModifiedTime.toInstant(),
            (lastAccessTime == null) ? Instant.now() : lastAccessTime.toInstant());
  }

  public SourceEntry(String key, String fileSuffix,
          Instant creationTime, Instant lastModifiedTime,
          Instant lastAccessTime) {
    if (key == null || key.isEmpty()) {
      throw new NullPointerException("name is empty");
    }
    this.key = key;
    this.fileSuffix = fileSuffix == null ? "" : fileSuffix;
    this.creationTime = Objects.requireNonNull(creationTime);
    this.lastModifiedTime = Objects.requireNonNull(lastModifiedTime);
    this.lastAccessTime = Objects.requireNonNull(lastAccessTime);
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
