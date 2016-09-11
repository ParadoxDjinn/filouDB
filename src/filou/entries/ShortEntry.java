package filou.entries;

import filou.util.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class ShortEntry implements ValueEntry<Short, ShortEntry> {

  private ChangeSupport<ShortEntry> changeSupport;
  private final Descriptor descriptor;
  private short value;

  public ShortEntry(Descriptor descriptor) {
    this(descriptor, Short.MIN_VALUE);
  }

  public ShortEntry(Descriptor descriptor, short value) {
    descriptor.checkType(Type.Short);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<ShortEntry>> void addListener(ChangeListener<ShortEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<ShortEntry>> void removeListener(ChangeListener<ShortEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(short value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public short get() {
    return value;
  }

  @Override
  public void setValue(Short value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Short getValue() {
    return value;
  }

  @Override
  public ShortEntry copy() {
    return new ShortEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(DataOutput output) throws IOException {
    output.writeShort(value);
  }

  @Override
  public void in(DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readShort();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Short.toString(value);
  }

  @Override
  public ShortChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new ShortChangeEvent();
    }
    return changeEvent;
  }
  private ShortChangeEvent changeEvent;

  public class ShortChangeEvent implements ValueChangeEvent<Short, ShortEntry> {

    private short old;

    public ShortChangeEvent() {
    }

    public short getOldShort() {
      return old;
    }

    public short getNewShort() {
      return value;
    }

    @Override
    public Short getOldValue() {
      return old;
    }

    @Override
    public Short getNewValue() {
      return value;
    }

    @Override
    public ShortEntry getEntry() {
      return ShortEntry.this;
    }

  }

}
