package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author dark
 */
public final class UUIDEntry implements ValueEntry<UUID, UUIDEntry> {

  private ChangeSupport<UUIDEntry> changeSupport;
  private final Descriptor descriptor;
  private UUID value;

  public UUIDEntry(Descriptor descriptor) {
    this(descriptor, UUID.randomUUID());
  }

  public UUIDEntry(Descriptor descriptor, UUID value) {
    descriptor.checkType(Type.UUID);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<UUIDEntry>> void addListener(ChangeListener<UUIDEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<UUIDEntry>> void removeListener(ChangeListener<UUIDEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(UUID value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public UUID get() {
    return value;
  }

  @Override
  public void setValue(UUID value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public UUID getValue() {
    return value;
  }

  @Override
  public UUIDEntry copy() {
    return new UUIDEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeLong(value.getMostSignificantBits());
    output.writeLong(value.getLeastSignificantBits());
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = new UUID(input.readLong(), input.readLong());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public UUIDChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new UUIDChangeEvent();
    }
    return changeEvent;
  }
  private UUIDChangeEvent changeEvent;

  public class UUIDChangeEvent implements ValueChangeEvent<UUID, UUIDEntry> {

    private UUID old;

    public UUIDChangeEvent() {
    }

    public UUID getOldUUID() {
      return old;
    }

    public UUID getNewUUUID() {
      return value;
    }

    @Override
    public UUID getOldValue() {
      return old;
    }

    @Override
    public UUID getNewValue() {
      return value;
    }

    @Override
    public UUIDEntry getObservable() {
      return UUIDEntry.this;
    }

  }
}
