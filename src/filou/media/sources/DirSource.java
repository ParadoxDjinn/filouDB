package filou.media.sources;

import filou.io.DataUtil;
import filou.media.AbstractSource;
import filou.media.SourceEntry;
import java.io.*;

/**
 *
 * @author dark
 */
public class DirSource extends AbstractSource {

  public void out(File dir) throws IOException {

    for (SourceEntry e : entries) {
      final File file = new File(dir, e.key
              + (e.fileSuffix.isEmpty() ? "" : '.' + e.fileSuffix));
      e.buffer.out(new FileOutputStream(file));
    }
  }

  public void in(File dir) throws IOException {
    entries.clear();

    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        in(dir);
      }

      String[] splitName = DataUtil.splitFileName(file.getName());
      SourceEntry e = new SourceEntry(splitName[0], splitName[1]);
      e.buffer.in(new FileInputStream(file));
      entries.add(e);
    }
  }
}
