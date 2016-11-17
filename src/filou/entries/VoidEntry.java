package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author dark
 */
public final class VoidEntry implements ValueEntry<Entry, VoidEntry> {

  private ChangeSupport<VoidEntry> changeSupport;
  private final Descriptor descriptor;
  private Entry value;

  public VoidEntry(Descriptor descriptor) {
    this(descriptor, null);
  }

  public VoidEntry(Descriptor descriptor, Entry value) {
    descriptor.checkType(Type.Void);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<VoidEntry>> void addListener(ChangeListener<VoidEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<VoidEntry>> void removeListener(ChangeListener<VoidEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(Entry value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Entry get() {
    return value;
  }

  @Override
  public void setValue(Entry value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Entry getValue() {
    return value;
  }

  @Override
  public VoidEntry copy() {
    return new VoidEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    if (value != null) {
      final Descriptor valueDescriptor = value.getDescriptor();
      Descriptor.outDescriptor(valueDescriptor, output);
      value.out(register, output);
    } else {
      Descriptor.outDescriptor(descriptor, output);
    }
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    final Descriptor valueDescriptor = Descriptor.inDescriptor(input);
    if (valueDescriptor.equals(descriptor)) {
      this.value = null;
    } else {
      this.value = valueDescriptor.buildEntry();
      value.in(register, input);
      ChangeSupport.fireChangedEvent(changeSupport);
    }
  }

  @Override
  public String toString() {
    return Objects.toString(value);
  }

  @Override
  public VoidChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new VoidChangeEvent();
    }
    return changeEvent;
  }
  private VoidChangeEvent changeEvent;

  public class VoidChangeEvent implements ValueChangeEvent<Entry, VoidEntry> {

    private Entry old;

    public VoidChangeEvent() {
    }

    public Entry getOldEntry() {
      return old;
    }

    public Entry getNewEntry() {
      return value;
    }

    @Override
    public Entry getOldValue() {
      return old;
    }

    @Override
    public Entry getNewValue() {
      return value;
    }

    @Override
    public VoidEntry getObservable() {
      return VoidEntry.this;
    }

  }

}
