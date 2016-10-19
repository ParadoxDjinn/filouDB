package filou.db;

import filou.media.Register;
import filou.media.sources.DirSource;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author dark
 */
public class DirDataBase extends FileDataBase {

  public DirDataBase(String dirFile) throws IOException {
    this(new File(dirFile));
  }

  public DirDataBase(File dirFile) throws IOException {
    super(dirFile);
    if (!dirFile.exists()) {
      dirFile.mkdir();
    } else if (!dirFile.isDirectory()) {
      throw new IllegalArgumentException("file is a dir");
    }
  }

  @Override
  public void load() throws IOException {
    DirSource source = new DirSource();
    source.in(file);
    load(source);
  }

  @Override
  public void save() throws IOException {
    DirSource source = new DirSource();
    save(source);
    source.out(file);
  }

  public static void load(Register register, File file) throws IOException {
    DirSource source = new DirSource();
    source.in(file);
    register.load(source);
  }

  public static void save(Register register, File file) throws IOException {
    DirSource source = new DirSource();
    register.save(source);
    source.out(file);
  }

}
