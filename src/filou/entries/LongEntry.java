package filou.entries;

import filou.util.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class LongEntry implements ValueEntry<Long, LongEntry> {

  private ChangeSupport<LongEntry> changeSupport;
  private final Descriptor descriptor;
  private long value;

  public LongEntry(Descriptor descriptor) {
    this(descriptor, Long.MIN_VALUE);
  }

  public LongEntry(Descriptor descriptor, long value) {
    descriptor.checkType(Type.Long);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<LongEntry>> void addListener(ChangeListener<LongEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<LongEntry>> void removeListener(ChangeListener<LongEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(long value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public long get() {
    return value;
  }

  @Override
  public void setValue(Long value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Long getValue() {
    return value;
  }

  @Override
  public LongEntry copy() {
    return new LongEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(DataOutput output) throws IOException {
    output.writeLong(value);
  }

  @Override
  public void in(DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readLong();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Long.toString(value);
  }

  @Override
  public LongChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new LongChangeEvent();
    }
    return changeEvent;
  }
  private LongChangeEvent changeEvent;

  public class LongChangeEvent implements ValueChangeEvent<Long, LongEntry> {

    private long old;

    public LongChangeEvent() {
    }

    public long getOldLong() {
      return old;
    }

    public long getNewLong() {
      return value;
    }

    @Override
    public Long getOldValue() {
      return old;
    }

    @Override
    public Long getNewValue() {
      return value;
    }

    @Override
    public LongEntry getEntry() {
      return LongEntry.this;
    }

  }
}
