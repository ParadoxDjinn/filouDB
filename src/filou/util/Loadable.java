package filou.util;

import filou.entries.StructEntry;
import filou.media.Register;

/**
 *
 * @author dark
 */
public interface Loadable {

  public void load(Register register, StructEntry struct);
}
