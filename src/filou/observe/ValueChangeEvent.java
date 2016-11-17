package filou.observe;

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
