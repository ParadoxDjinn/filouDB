package filou.media.types;

import filou.media.Register;
import filou.media.Type;
import filou.entries.Descriptor;
import filou.entries.Entry;
import java.io.*;

/**
 *
 * @author dark
 */
public final class EntryType extends Type<Entry> {

  public static final EntryType TYPE = new EntryType();

  public EntryType() {
  }

  @Override
  public Class<Entry> getKey() {
    return Entry.class;
  }

  @Override
  public String getDefaultFileSuffix() {
    return "entry";
  }

  @Override
  public Entry in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    try (DataInputStream is = new DataInputStream(stream)) {
      final Descriptor descriptor = Descriptor.inDescriptor(is);
      Entry entry = descriptor.buildEntry();
      entry.in(register, is);
      return entry;
    }
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          Entry value, OutputStream stream) throws IOException {
    try (DataOutputStream os = new DataOutputStream(stream)) {
      Descriptor.outDescriptor(value.getDescriptor(), os);
      value.out(register, os);
    }
  }

}
