package filou.media.types;

import filou.io.DataUtil;
import filou.media.Register;
import filou.media.Type;
import filou.util.Descriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author dark
 */
public final class DescriptorType extends Type<Descriptor> {

  public static final DescriptorType TYPE = new DescriptorType();

  public DescriptorType() {
  }

  @Override
  public Class<Descriptor> getKey() {
    return Descriptor.class;
  }

  @Override
  public String getDefaultFileSuffix() {
    return "dscrptr";
  }

  @Override
  public Descriptor in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    return DataUtil.load(stream, Descriptor::inDescriptor);
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          Descriptor value, OutputStream stream) throws IOException {
    DataUtil.save(stream, value, Descriptor::outDescriptor);
  }

}
