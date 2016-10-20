package filou.media.types;

import filou.media.Register;
import filou.media.Type;
import java.io.*;

/**
 *
 * @author dark
 * @param <V> value
 */
public abstract class DataType<V> extends Type<V> {

  public abstract V in(Register register, String key, String fileSuffix,
          DataInputStream stream) throws IOException;

  public abstract void out(Register register, String key, String fileSuffix,
          V value, DataOutputStream stream) throws IOException;

  @Override
  public final V in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    try (DataInputStream is = new DataInputStream(stream)) {
      return in(register, key, fileSuffix, is);
    }
  }

  @Override
  public final void out(Register register, String key, String fileSuffix,
          V value, OutputStream stream) throws IOException {
    try (DataOutputStream os = new DataOutputStream(stream)) {
      out(register, key, fileSuffix, value, os);
    }
  }

}
