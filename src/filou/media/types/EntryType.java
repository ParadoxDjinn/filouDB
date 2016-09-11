package filou.media.types;

import filou.io.DataUtil;
import filou.media.Register;
import filou.media.Type;
import filou.util.Descriptor;
import filou.util.Entry;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    return DataUtil.load(stream, dataInput -> {
      final Descriptor descriptor = Descriptor.inDescriptor(dataInput);
      Entry entry = descriptor.buildEntry();
      entry.in(dataInput);
      return entry;
    });
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          Entry value, OutputStream stream) throws IOException {
    DataUtil.save(stream, value, (entry, outputData) -> {
      Descriptor.outDescriptor(entry.getDescriptor(), outputData);
      entry.out(outputData);
    });
  }

}
