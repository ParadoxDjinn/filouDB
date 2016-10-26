package filou.observe;

import java.util.Arrays;
import javafx.beans.WeakListener;

/**
 *
 * @author dark
 * @param <O> Observable
 */
public abstract class ChangeSupport<O extends Observable<O>> {

  public static <T extends Observable<T>, U extends ChangeEvent<T>> ChangeSupport<T>
          addListener(ChangeSupport<T> changeSupport, T entry, ChangeListener<T, U> listener) {
    if ((entry == null) || (listener == null)) {
      throw new NullPointerException();
    }
    return (changeSupport == null) ? new SingleSupport(entry, listener) : changeSupport.addListener(listener);
  }

  public static <T extends Observable<T>, U extends ChangeEvent<T>> ChangeSupport<T>
          removeListener(ChangeSupport<T> changeSupport, ChangeListener<T, U> listener) {
    if (listener == null) {
      throw new NullPointerException();
    }
    return (changeSupport == null) ? null : changeSupport.removeListener(listener);
  }

  public static <T extends Observable<T>, U extends ChangeEvent<T>> void
          fireChangedEvent(ChangeSupport<T> changeSupport) {
    if (changeSupport != null) {
      changeSupport.fireChangedEvent();
    }
  }

  protected abstract ChangeSupport<O> addListener(ChangeListener<O, ? extends ChangeEvent<O>> listener);

  protected abstract ChangeSupport<O> removeListener(ChangeListener<O, ? extends ChangeEvent<O>> listener);

  protected abstract void fireChangedEvent();

  private static class SingleSupport<O extends Observable<O>> extends ChangeSupport<O> {

    private final Observable observable;
    private final ChangeListener listener;

    private SingleSupport(O observable, ChangeListener<O, ? extends ChangeEvent<O>> listener) {
      this.observable = observable;
      this.listener = listener;
    }

    @Override
    protected void fireChangedEvent() {
      ChangeEvent changeEvent = observable.changeEvent();
      listener.changed(changeEvent);
    }

    @Override
    protected ChangeSupport<O> addListener(ChangeListener<O, ? extends ChangeEvent<O>> listener) {
      return new MultiSupport(observable, this.listener, listener);
    }

    @Override
    protected ChangeSupport<O> removeListener(ChangeListener<O, ? extends ChangeEvent<O>> listener) {
      return null;
    }

  }

  private static class MultiSupport<O extends Observable<O>> extends ChangeSupport<O> {

    private final Observable observable;
    private ChangeListener[] listeners;

    private int size;
    private boolean locked;

    private MultiSupport(O observable, ChangeListener<O, ?> listener0, ChangeListener<O, ?> listener1) {
      this.observable = observable;
      this.listeners = new ChangeListener[]{listener0, listener1};
    }

    @Override
    protected void fireChangedEvent() {
      final ChangeListener[] curList = listeners;
      final int curSize = size;

      try {
        locked = true;
        ChangeEvent event = observable.changeEvent();
        for (int i = 0; i < curSize; i++) {
          try {
            curList[i].changed(event);
          } catch (Exception e) {
            Thread.currentThread().getUncaughtExceptionHandler()
                    .uncaughtException(Thread.currentThread(), e);
          }
        }
      } finally {
        locked = false;
      }

    }

    @Override
    protected ChangeSupport<O> addListener(ChangeListener<O, ? extends ChangeEvent<O>> listener) {
      if (listeners == null) {
        listeners = new ChangeListener[]{listener};
        size = 1;
      } else {
        final int oldCapacity = listeners.length;
        if (locked) {
          final int newCapacity = (size < oldCapacity) ? oldCapacity : (oldCapacity * 3) / 2 + 1;
          listeners = Arrays.copyOf(listeners, newCapacity);
        } else if (size == oldCapacity) {
          size = trim(size, listeners);
          if (size == oldCapacity) {
            final int newCapacity = (oldCapacity * 3) / 2 + 1;
            listeners = Arrays.copyOf(listeners, newCapacity);
          }
        }
        listeners[size++] = listener;
      }
      return this;
    }

    @Override
    protected ChangeSupport<O> removeListener(ChangeListener<O, ? extends ChangeEvent<O>> listener) {
      if (listeners != null) {
        for (int index = 0; index < size; index++) {
          if (listener.equals(listeners[index])) {
            switch (size) {
              case 1:
                listeners = null;
                size = 0;
                return this;
              case 2:
                return new SingleSupport(observable, listeners[1 - index]);
              default:
                final int numMoved = size - index - 1;
                final ChangeListener[] oldListeners = listeners;
                if (locked) {
                  listeners = new ChangeListener[listeners.length];
                  System.arraycopy(oldListeners, 0, listeners, 0, index);
                }
                if (numMoved > 0) {
                  System.arraycopy(oldListeners, index + 1, listeners, index, numMoved);
                }
                size--;
                if (!locked) {
                  listeners[size] = null; // Let gc do its work
                }
                return this;
            }
          }
        }
      }
      return this;
    }

    protected static int trim(int size, Object[] listeners) {
      for (int index = 0; index < size; index++) {
        final Object listener = listeners[index];
        if (listener instanceof WeakListener) {
          if (((WeakListener) listener).wasGarbageCollected()) {
            final int numMoved = size - index - 1;
            if (numMoved > 0) {
              System.arraycopy(listeners, index + 1, listeners, index, numMoved);
            }
            listeners[--size] = null; // Let gc do its work
            index--;
          }
        }
      }
      return size;
    }

  }

}
