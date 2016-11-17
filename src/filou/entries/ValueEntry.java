package filou.entries;

import filou.util.ValueChangeEvent;
import filou.media.Register;

/**
 *
 * @author dark
 * @param <V> value
 * @param <E> self
 */
public interface ValueEntry<V, E extends ValueEntry<V, E>> extends Entry<E> {

  public void setValue(V value);

  public V getValue();

  @Override
  public ValueEntry<V, E> copy();

  @Override
  public ValueChangeEvent<V, E> changeEvent();

  @Override
  public default void init(Register register) {
  }

  @Override
  public default void uninit(Register register) {
  }

}
