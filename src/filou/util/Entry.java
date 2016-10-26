package filou.util;

import filou.observe.Observable;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 * @param <E> self
 */
public interface Entry<E extends Entry<E>> extends Observable<E> {

  public Descriptor getDescriptor();

  public Entry copy();

  public default Type getType() {
    return getDescriptor().getType();
  }

  public void out(Register register, DataOutput output) throws IOException;

  public void in(Register register, DataInput input) throws IOException;
}
