package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;

/**
 *
 * @author dark
 */
public final class TimestampEntry extends ValueEntry<Instant, TimestampEntry> {

  private ChangeSupport<TimestampEntry> changeSupport;
  private final Descriptor descriptor;
  private Instant value;

  public TimestampEntry(Descriptor descriptor) {
    this(descriptor, Instant.now());
  }

  public TimestampEntry(Descriptor descriptor, Instant value) {
    descriptor.checkType(Type.Timestamp);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<TimestampEntry>> void addListener(ChangeListener<TimestampEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<TimestampEntry>> void removeListener(ChangeListener<TimestampEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(Instant value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Instant get() {
    return value;
  }

  @Override
  public void setValue(Instant value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Instant getValue() {
    return value;
  }

  @Override
  public TimestampEntry copy() {
    return new TimestampEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeLong(value.toEpochMilli());
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = Instant.ofEpochMilli(input.readLong());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public TimestampChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new TimestampChangeEvent();
    }
    return changeEvent;
  }
  private TimestampChangeEvent changeEvent;

  public class TimestampChangeEvent implements ValueChangeEvent<Instant, TimestampEntry> {

    private Instant old;

    public TimestampChangeEvent() {
    }

    public Instant getOldInstant() {
      return old;
    }

    public Instant getNewInstant() {
      return value;
    }

    @Override
    public Instant getOldValue() {
      return old;
    }

    @Override
    public Instant getNewValue() {
      return value;
    }

    @Override
    public TimestampEntry getObservable() {
      return TimestampEntry.this;
    }

  }

}
