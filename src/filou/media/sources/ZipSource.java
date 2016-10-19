package filou.media.sources;

import filou.io.DataUtil;
import filou.media.AbstractSource;
import filou.media.SourceEntry;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;
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

        entry.setCreationTime(FileTime.from(e.creationTime));
        entry.setLastModifiedTime(FileTime.from(e.lastModifiedTime));
        entry.setLastAccessTime(FileTime.from(e.lastAccessTime));

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
        FileTime creationTime = entry.getCreationTime();
        FileTime lastModifiedTime = entry.getLastModifiedTime();
        FileTime lastAccessTime = entry.getLastAccessTime();

        SourceEntry e = new SourceEntry(splitName[0], splitName[1],
                creationTime, lastModifiedTime, lastAccessTime);

        e.buffer.in(stream);
        entries.add(e);
        stream.closeEntry();
      }
    }
  }

}
