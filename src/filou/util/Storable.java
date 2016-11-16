package filou.util;

import filou.entries.StructEntry;
import filou.media.Register;

/**
 *
 * @author dark
 */
public interface Storable {

  public StructEntry save(String key, Register register);
}
