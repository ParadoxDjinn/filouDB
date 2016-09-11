package filou.log;

import java.util.Objects;

/**
 *
 * @author dark
 */
public class Level {

  public static final Level OFF = new Level("OFF", Integer.MAX_VALUE);

  public static final Level SEVERE = new Level("SEVERE", 1000);

  public static final Level WARNING = new Level("WARNING", 900);

  public static final Level INFO = new Level("INFO", 800);

  public static final Level CONFIG = new Level("CONFIG", 700);

  public static final Level FINE = new Level("FINE", 500);

  public static final Level FINER = new Level("FINER", 400);

  public static final Level FINEST = new Level("FINEST", 300);

  public static final Level ALL = new Level("ALL", Integer.MIN_VALUE);

  private final String name;
  private final int wight;

  public Level(String name, int wight) {
    this.name = name;
    this.wight = wight;
  }

  public String getName() {
    return name;
  }

  public int getWight() {
    return wight;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.name);
    hash = 11 * hash + this.wight;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Level other = (Level) obj;
    return this.wight == other.wight
            && Objects.equals(this.name, other.name);
  }

}
