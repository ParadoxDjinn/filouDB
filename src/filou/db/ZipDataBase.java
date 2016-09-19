package filou.db;

import filou.media.Register;
import filou.media.sources.ZipSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 *
 * @author dark
 */
public final class ZipDataBase extends FileDataBase {

  private final File zipFile;

  public ZipDataBase(String zipFile) throws IOException {
    this(new File(zipFile));
  }

  public ZipDataBase(File zipFile) throws IOException {
    if (!zipFile.exists()) {
      zipFile.createNewFile();
    } else if (zipFile.isDirectory()) {
      throw new IllegalArgumentException("file is a dir");
    }
    this.zipFile = zipFile;
  }

  @Override
  public String getName() {
    return this.zipFile.getName();
  }

  @Override
  public String getLocation() {
    return this.zipFile.getParent();
  }

  @Override
  public LocalDateTime lastModified() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.zipFile.lastModified()),
            ZoneId.systemDefault());
  }

  @Override
  public void load() throws IOException {
    ZipSource source = new ZipSource();
    source.in(new FileInputStream(zipFile));
    load(source);
  }

  @Override
  public void save() throws IOException {
    ZipSource source = new ZipSource();
    save(source);
    source.out(new FileOutputStream(zipFile));
  }

  public static void load(Register register, File db) throws IOException {
    ZipSource source = new ZipSource();
    source.in(new FileInputStream(db));
    register.load(source);
  }

  public static void save(Register register, File db) throws IOException {
    ZipSource source = new ZipSource();
    register.save(source);
    source.out(new FileOutputStream(db));
  }

  @Override
  public String toString() {
    return zipFile.toString();
  }

  @Override
  public File getFile() {
    return zipFile;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.zipFile);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final ZipDataBase other = (ZipDataBase) obj;
    return Objects.equals(this.zipFile, other.zipFile);
  }

}
