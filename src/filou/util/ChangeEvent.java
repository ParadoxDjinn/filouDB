package filou.util;

/**
 *
 * @author dark
 * @param <E> entry
 */
public interface ChangeEvent<E extends Entry<E>> {

  public E getEntry();

  public default Type getType() {
    return getEntry().getType();
  }

}
