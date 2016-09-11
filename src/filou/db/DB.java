package filou.db;

import filou.media.REG;
import filou.util.Descriptor;

/**
 *
 * @author dark
 */
public final class DB {

  public static <T extends filou.util.Entry> void setSingle(DataBase db, T entry) {
    REG.setEntry(db.getRegister(), entry.getDescriptor().toString(), entry);
  }

  public static <T extends filou.util.Entry> T getSingle(DataBase db, Descriptor descriptor) {
    return REG.getEntry(db.getRegister(), descriptor.toString());
  }

  public static boolean containsSingle(DataBase db, Descriptor descriptor) {
    return REG.containsEntry(db.getRegister(), descriptor.toString());
  }

  public static boolean contains(DataBase db, String key) {
    return REG.containsEntry(db.getRegister(), key);
  }

  private DB() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

}
