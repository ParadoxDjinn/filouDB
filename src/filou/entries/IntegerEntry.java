package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class IntegerEntry extends ValueEntry<Integer, IntegerEntry> {

  private ChangeSupport<IntegerEntry> changeSupport;
  private final Descriptor descriptor;
  private int value;

  public IntegerEntry(Descriptor descriptor) {
    this(descriptor, ' ');
  }

  public IntegerEntry(Descriptor descriptor, int value) {
    descriptor.checkType(Type.Integer);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<IntegerEntry>> void addListener(ChangeListener<IntegerEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<IntegerEntry>> void removeListener(ChangeListener<IntegerEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(int value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public int get() {
    return value;
  }

  @Override
  public void setValue(Integer value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Integer getValue() {
    return value;
  }

  @Override
  public IntegerEntry copy() {
    return new IntegerEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeInt(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readInt();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public IntegerChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new IntegerChangeEvent();
    }
    return changeEvent;
  }
  private IntegerChangeEvent changeEvent;

  public class IntegerChangeEvent implements ValueChangeEvent<Integer, IntegerEntry> {

    private int old;

    public IntegerChangeEvent() {
    }

    public int getOldInt() {
      return old;
    }

    public int getNewInt() {
      return value;
    }

    @Override
    public Integer getOldValue() {
      return old;
    }

    @Override
    public Integer getNewValue() {
      return value;
    }

    @Override
    public IntegerEntry getObservable() {
      return IntegerEntry.this;
    }

  }
}
