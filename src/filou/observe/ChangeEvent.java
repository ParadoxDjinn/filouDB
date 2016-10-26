package filou.observe;

/**
 *
 * @author dark
 * @param <O> Observable
 */
public interface ChangeEvent<O extends Observable<O>> {

  public O getObservable();

}
