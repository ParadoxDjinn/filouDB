package filou.util;

import filou.observe.Observable;
import filou.media.Register;
import filou.media.SelfObservable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 * @param <E> self
 */
public interface Entry<E extends Entry<E>> extends Observable<E>, SelfObservable<E> {

  public Descriptor getDescriptor();

  public Entry copy();

  public default Type getType() {
    return getDescriptor().getType();
  }

  public void out(Register register, DataOutput output) throws IOException;

  public void in(Register register, DataInput input) throws IOException;

  @Override
  public default void init(filou.media.Entry<E> entry) {
    init(entry.getRegister());
  }

  @Override
  public default void uninit(filou.media.Entry<E> entry) {
    uninit(entry.getRegister());
  }

  public void init(Register register);

  public void uninit(Register register);
}
