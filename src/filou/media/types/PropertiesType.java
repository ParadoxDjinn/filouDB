package filou.media.types;

import filou.media.Register;
import filou.media.Type;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author dark
 */
public final class PropertiesType extends Type<Properties> {

  public static final PropertiesType TYPE = new PropertiesType();

  public PropertiesType() {
  }

  @Override
  public Class<Properties> getKey() {
    return Properties.class;
  }

  @Override
  public String getDefaultFileSuffix() {
    return "properties";
  }

  @Override
  public Properties in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    Properties properties = new Properties();
    properties.load(stream);
    return properties;
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          Properties value, OutputStream stream) throws IOException {
    value.store(stream, key);
  }

}
