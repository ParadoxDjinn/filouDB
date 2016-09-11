package filou.util;

/**
 *
 * @author dark
 * @param <E> entry
 * @param <C> changeEvent
 */
@FunctionalInterface
public interface ChangeListener<E extends Entry<E>, C extends ChangeEvent<E>> {

  public void changed(C event);
}
