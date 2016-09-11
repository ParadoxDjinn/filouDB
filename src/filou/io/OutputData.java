package filou.io;

import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
@FunctionalInterface
public interface OutputData {

  public void out(DataOutput output) throws IOException;
}
