package filou.log;

import filou.media.SourceEntry;

/**
 *
 * @author dark
 */
public interface Observer {

  public void occurred(Level level, String msg);

  public void occurred(Level level, String msg, Exception exception);

  public void occurred(Level level, String msg, SourceEntry entry);
}
