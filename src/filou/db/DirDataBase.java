package filou.db;

import filou.media.Register;
import filou.media.sources.DirSource;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 *
 * @author dark
 */
public class DirDataBase extends FileDataBase {

  private final File dirFile;

  public DirDataBase(String dirFile) throws IOException {
    this(new File(dirFile));
  }

  public DirDataBase(File dirFile) throws IOException {
    if (!dirFile.exists()) {
      dirFile.mkdir();
    } else if (!dirFile.isDirectory()) {
      throw new IllegalArgumentException("file is a dir");
    }
    this.dirFile = dirFile;
  }

  @Override
  public String getName() {
    return this.dirFile.getName();
  }

  @Override
  public String getLocation() {
    return this.dirFile.getParent();
  }

  @Override
  public LocalDateTime lastModified() {
    return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this.dirFile.lastModified()),
            ZoneId.systemDefault());
  }

  @Override
  public void load() throws IOException {
    DirSource source = new DirSource();
    source.in(dirFile);
    load(source);
  }

  @Override
  public void save() throws IOException {
    DirSource source = new DirSource();
    save(source);
    source.out(dirFile);
  }

  public static void load(Register register, File db) throws IOException {
    DirSource source = new DirSource();
    source.in(db);
    register.load(source);
  }

  public static void save(Register register, File db) throws IOException {
    DirSource source = new DirSource();
    register.save(source);
    source.out(db);
  }

  @Override
  public String toString() {
    return dirFile.toString();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.dirFile);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final DirDataBase other = (DirDataBase) obj;
    return Objects.equals(this.dirFile, other.dirFile);
  }

  @Override
  public File getFile() {
    return dirFile;
  }

}
