package filou.media.sources;

import filou.io.DataUtil;
import filou.media.AbstractSource;
import filou.media.SourceEntry;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author dark
 */
public class ZipSource extends AbstractSource {

  public void out(OutputStream outputStream) throws IOException {
    try (ZipOutputStream stream = new ZipOutputStream(outputStream)) {
      for (SourceEntry e : entries) {
        final ZipEntry entry = new ZipEntry(e.key
                + (e.fileSuffix.isEmpty() ? "" : '.' + e.fileSuffix));
        stream.putNextEntry(entry);
        e.buffer.out(stream);
        stream.closeEntry();
      }
    }
  }

  public void in(InputStream inputStream) throws IOException {
    entries.clear();

    try (ZipInputStream stream = new ZipInputStream(inputStream)) {
      while (true) {
        ZipEntry entry = stream.getNextEntry();
        if (entry == null) {
          break;
        }

        String[] splitName = DataUtil.splitFileName(entry.getName());
        SourceEntry e = new SourceEntry(splitName[0], splitName[1]);
        e.buffer.in(stream);
        entries.add(e);
        stream.closeEntry();
      }
    }
  }

}
