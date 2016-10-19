package filou.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 *
 * @author dark
 */
public abstract class FileDataBase extends DataBase {

  protected final File file;

  public FileDataBase(File file) {
    this.file = file;
  }

  public final String getName() {
    return this.file.getName();
  }

  public final String getLocation() {
    return this.file.getParent();
  }

  public final BasicFileAttributes getFileAttributes() throws IOException {
    return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
  }

  public final File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return file.toString();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.file);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final FileDataBase other = (FileDataBase) obj;
    return Objects.equals(this.file, other.file);
  }

}
