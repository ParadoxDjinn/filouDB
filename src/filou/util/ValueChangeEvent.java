package filou.util;

import filou.observe.ChangeEvent;
import filou.observe.Observable;

/**
 *
 * @author dark
 * @param <V> value
 * @param <O> Observable
 */
public interface ValueChangeEvent<V, O extends Observable<O>> extends ChangeEvent<O> {

  public V getOldValue();

  public V getNewValue();

}
