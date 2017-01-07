package filou.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 *
 * @author dark
 */
public class IntBuffer {

  private static final int INIT_SIZE = 16;
  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

  private int[] buffer;
  private int size;

  public IntBuffer(int initialCapacity) {
    if (initialCapacity > 0) {
      this.buffer = new int[initialCapacity];
    } else if (initialCapacity == 0) {
      this.buffer = new int[INIT_SIZE];
    } else {
      throw new IllegalArgumentException("Illegal Capacity: "
              + initialCapacity);
    }
  }

  public IntBuffer() {
    this.buffer = new int[INIT_SIZE];
  }

  public void trimToSize() {
    if (size < buffer.length) {
      buffer = (size == 0)
              ? new int[INIT_SIZE]
              : Arrays.copyOf(buffer, size);
    }
  }

  public void ensureCapacity(int minCapacity) {
    if (minCapacity - buffer.length > 0) {
      grow(minCapacity);
    }
  }

  private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = buffer.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0) {
      newCapacity = minCapacity;
    }
    if (newCapacity - MAX_ARRAY_SIZE > 0) {
      newCapacity = hugeCapacity(minCapacity);
    }
    // minCapacity is usually close to size, so this is a win:
    buffer = Arrays.copyOf(buffer, newCapacity);
  }

  private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
    {
      throw new OutOfMemoryError();
    }
    return (minCapacity > MAX_ARRAY_SIZE)
            ? Integer.MAX_VALUE
            : MAX_ARRAY_SIZE;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int indexOf(int i) {
    for (int index = 0; index < size; index++) {
      if (i == buffer[index]) {
        return index;
      }
    }
    return -1;
  }

  public int lastIndexOf(int i) {
    for (int index = size - 1; index >= 0; index--) {
      if (i == buffer[index]) {
        return index;
      }
    }
    return -1;
  }

  public int[] toArray() {
    return Arrays.copyOf(buffer, size);
  }

  public int get(int index) {
    rangeCheck(index);

    return buffer[index];
  }

  public int set(int index, int i) {
    rangeCheck(index);

    int oldValue = buffer[index];
    buffer[index] = i;
    return oldValue;
  }

  public boolean add(int i) {
    ensureCapacity(size + 1);
    buffer[size++] = i;
    return true;
  }

  public void add(int index, int i) {
    rangeCheckForAdd(index);

    ensureCapacity(size + 1);
    System.arraycopy(buffer, index, buffer, index + 1,
            size - index);
    buffer[index] = i;
    size++;
  }

  public int remove(int index) {
    rangeCheck(index);

    int oldValue = buffer[index];

    int numMoved = size - index - 1;
    if (numMoved > 0) {
      System.arraycopy(buffer, index + 1, buffer, index,
              numMoved);
    }

    return oldValue;
  }

  public void clear() {
    size = 0;
  }

  public boolean addAll(int[] is) {
    int numNew = is.length;
    ensureCapacity(size + numNew);
    System.arraycopy(is, 0, buffer, size, numNew);
    size += numNew;
    return numNew != 0;
  }

  public boolean addAll(int index, int[] is) {
    rangeCheckForAdd(index);

    int numNew = is.length;
    ensureCapacity(size + numNew);

    int numMoved = size - index;
    if (numMoved > 0) {
      System.arraycopy(buffer, index, buffer, index + numNew,
              numMoved);
    }

    System.arraycopy(is, 0, buffer, index, numNew);
    size += numNew;
    return numNew != 0;
  }

  public void removeRange(int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException();
    } else if (toIndex >= size) {
      throw new IndexOutOfBoundsException();
    }
    int numMoved = size - toIndex;
    System.arraycopy(buffer, toIndex, buffer, fromIndex,
            numMoved);

    size = size - (toIndex - fromIndex);
  }

  private void rangeCheck(int index) {
    if (index >= size) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  private void rangeCheckForAdd(int index) {
    if (index > size || index < 0) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  private String outOfBoundsMsg(int index) {
    return "Index: " + index + ", Size: " + size;
  }

  public InputStream asInputStream() {
    return new InputStream() {
      private int pos = 0;

      @Override
      public int read() throws IOException {
        if (size > pos) {
          int i = get(pos);
          pos++;
          return i;
        } else {
          return -1;
        }
      }
    };
  }

  public OutputStream asOutputStream() {
    return new OutputStream() {

      @Override
      public void write(int b) throws IOException {
        add(b);
      }
    };
  }

  public void in(InputStream stream) throws IOException {
    while (true) {
      int read = stream.read();
      if (read == -1) {
        return;
      }
      add(read);
    }
  }

  public void out(OutputStream stream) throws IOException {
    for (int i = 0; i < size; i++) {
      stream.write(buffer[i]);
    }
  }

  public void addString(String str) {
    str.chars().forEach(this::add);
  }

  @Override
  public String toString() {
    return new String(buffer, 0, size);
  }

}
