package filou.entries;

import filou.observe.ValueChangeEvent;
import filou.media.Register;

/**
 *
 * @author dark
 * @param <V> value
 * @param <E> self
 */
public abstract class ValueEntry<V, E extends ValueEntry<V, E>> extends Entry<E> {

  public abstract void setValue(V value);

  public abstract V getValue();

  @Override
  public abstract E copy();

  @Override
  public abstract ValueChangeEvent<V, E> changeEvent();

}
