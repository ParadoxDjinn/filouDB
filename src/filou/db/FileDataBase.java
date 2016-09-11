package filou.db;

import java.io.File;
import java.time.LocalDateTime;

/**
 *
 * @author dark
 */
public abstract class FileDataBase extends DataBase {

  public abstract String getName();

  public abstract String getLocation();

  public abstract LocalDateTime lastModified();
  
  public abstract File getFile();

}
