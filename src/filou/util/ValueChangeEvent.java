package filou.util;

/**
 *
 * @author dark
 * @param <V> value
 * @param <E> entry
 */
public interface ValueChangeEvent<V, E extends ValueEntry<V, E>> extends ChangeEvent<E> {

  public V getOldValue();

  public V getNewValue();

}
