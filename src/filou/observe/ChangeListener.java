package filou.observe;

/**
 *
 * @author dark
 * @param <O> Observable
 * @param <C> changeEvent
 */
@FunctionalInterface
public interface ChangeListener<O extends Observable<O>, C extends ChangeEvent<O>> {

  public void changed(C event);
}
