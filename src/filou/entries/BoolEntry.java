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
public final class BoolEntry implements ValueEntry<Boolean, BoolEntry> {

  private ChangeSupport<BoolEntry> changeSupport;
  private final Descriptor descriptor;
  private boolean value;

  public BoolEntry(Descriptor descriptor) {
    this(descriptor, false);
  }

  public BoolEntry(Descriptor descriptor, boolean value) {
    descriptor.checkType(Type.Boolean);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<BoolEntry>> void addListener(ChangeListener<BoolEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<BoolEntry>> void removeListener(ChangeListener<BoolEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(boolean value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public boolean get() {
    return value;
  }

  @Override
  public void setValue(Boolean value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Boolean getValue() {
    return value;
  }

  @Override
  public BoolEntry copy() {
    return new BoolEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeBoolean(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readBoolean();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Boolean.toString(value);
  }

  @Override
  public BoolChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new BoolChangeEvent();
    }
    return changeEvent;
  }
  private BoolChangeEvent changeEvent;

  public class BoolChangeEvent implements ValueChangeEvent<Boolean, BoolEntry> {

    private boolean old;

    public BoolChangeEvent() {
    }

    public boolean getOldBool() {
      return old;
    }

    public boolean getNewBool() {
      return value;
    }

    @Override
    public Boolean getOldValue() {
      return old;
    }

    @Override
    public Boolean getNewValue() {
      return value;
    }

    @Override
    public BoolEntry getObservable() {
      return BoolEntry.this;
    }

  }

}
