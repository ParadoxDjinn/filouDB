package filou.io;

import java.io.DataInput;
import java.io.IOException;

/**
 *
 * @author dark
 */
@FunctionalInterface
public interface InputData {

  public void in(DataInput input) throws IOException;
}
