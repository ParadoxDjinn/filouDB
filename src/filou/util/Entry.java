package filou.util;

import filou.io.DataIO;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 * @param <E> self
 */
public interface Entry<E extends Entry<E>> extends DataIO {

  public <T extends ChangeEvent<E>> void addListener(ChangeListener<E, T> listener);

  public <T extends ChangeEvent<E>> void removeListener(ChangeListener<E, T> listener);

  public Descriptor getDescriptor();

  public Entry copy();

  public default Type getType() {
    return getDescriptor().getType();
  }

  public static Entry inEntry(DataInput input) throws IOException {
    Entry entry = Descriptor.inDescriptor(input).buildEntry();
    entry.in(input);
    return entry;
  }

  public static void outEntry(Entry entry, DataOutput output) throws IOException {
    final Descriptor descriptor = entry.getDescriptor();
    Descriptor.outDescriptor(descriptor, output);
    entry.out(output);
  }

  public ChangeEvent<E> changeEvent();

}
