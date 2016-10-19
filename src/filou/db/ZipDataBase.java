package filou.db;

import filou.media.Register;
import filou.media.sources.ZipSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class ZipDataBase extends FileDataBase {

  public ZipDataBase(String zipFile) throws IOException {
    this(new File(zipFile));
  }

  public ZipDataBase(File zipFile) throws IOException {
    super(zipFile);
    if (!zipFile.exists()) {
      zipFile.createNewFile();
    } else if (zipFile.isDirectory()) {
      throw new IllegalArgumentException("file is a dir");
    }
  }

  @Override
  public void load() throws IOException {
    ZipSource source = new ZipSource();
    source.in(new FileInputStream(file));
    load(source);
  }

  @Override
  public void save() throws IOException {
    ZipSource source = new ZipSource();
    save(source);
    source.out(new FileOutputStream(file));
  }

  public static void load(Register register, File file) throws IOException {
    ZipSource source = new ZipSource();
    source.in(new FileInputStream(file));
    register.load(source);
  }

  public static void save(Register register, File file) throws IOException {
    ZipSource source = new ZipSource();
    register.save(source);
    source.out(new FileOutputStream(file));
  }

}
