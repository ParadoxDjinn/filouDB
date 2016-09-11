package filou.util;

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

}
