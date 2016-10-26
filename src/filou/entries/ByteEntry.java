package filou.entries;

import filou.observe.ChangeListener;
import filou.observe.ChangeEvent;
import filou.observe.ChangeSupport;
import filou.media.Register;
import filou.util.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class ByteEntry implements ValueEntry<Byte, ByteEntry> {

  private ChangeSupport<ByteEntry> changeSupport;
  private final Descriptor descriptor;
  private byte value;

  public ByteEntry(Descriptor descriptor) {
    this(descriptor, Byte.MIN_VALUE);
  }

  public ByteEntry(Descriptor descriptor, byte value) {
    descriptor.checkType(Type.Byte);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<ByteEntry>> void addListener(ChangeListener<ByteEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<ByteEntry>> void removeListener(ChangeListener<ByteEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(byte value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte get() {
    return value;
  }

  @Override
  public void setValue(Byte value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Byte getValue() {
    return value;
  }

  @Override
  public ByteEntry copy() {
    return new ByteEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeByte(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readByte();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Byte.toString(value);
  }

  @Override
  public ByteChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new ByteChangeEvent();
    }
    return changeEvent;
  }
  private ByteChangeEvent changeEvent;

  public class ByteChangeEvent implements ValueChangeEvent<Byte, ByteEntry> {

    private byte old;

    public ByteChangeEvent() {
    }

    public byte getOldByte() {
      return old;
    }

    public byte getNewByte() {
      return value;
    }

    @Override
    public Byte getOldValue() {
      return old;
    }

    @Override
    public Byte getNewValue() {
      return value;
    }

    @Override
    public ByteEntry getObservable() {
      return ByteEntry.this;
    }

  }
}
