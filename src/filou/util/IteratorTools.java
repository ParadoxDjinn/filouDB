package filou.util;

import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author dark
 */
public final class IteratorTools {

  public static <V> Iterator<V> fromArray(V[] values) {
    return new Iterator<V>() {
      private int index = 0;

      @Override
      public boolean hasNext() {
        return index < (values != null ? values.length : 0);
      }

      @Override
      public V next() {
        final V value = values[index];
        index++;
        return value;
      }
    };
  }

  public static <V, R> Iterator<R> map(
          Iterator<V> iterator, Function<V, R> function) {
    return new Iterator<R>() {

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public R next() {
        return function.apply(iterator.next());
      }
    };
  }

  private IteratorTools() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

}
