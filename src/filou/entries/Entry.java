package filou.entries;

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
public abstract class Entry<E extends Entry<E>> implements Observable<E>, SelfObservable<E> {

  public abstract Descriptor getDescriptor();

  public abstract E copy();

  public Type getType() {
    return getDescriptor().getType();
  }

  public abstract void out(Register register, DataOutput output) throws IOException;

  public abstract void in(Register register, DataInput input) throws IOException;

  @Override
  public void init(filou.media.Entry<E> entry) {
    init(entry.getRegister());
  }

  @Override
  public void uninit(filou.media.Entry<E> entry) {
    uninit(entry.getRegister());
  }

  void init(Register register) {
  }

  void uninit(Register register) {
  }

}
