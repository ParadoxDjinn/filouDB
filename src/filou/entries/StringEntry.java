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
public final class StringEntry extends ValueEntry<String, StringEntry> {

  private ChangeSupport<StringEntry> changeSupport;
  private final Descriptor descriptor;
  private String value;

  public StringEntry(Descriptor descriptor) {
    this(descriptor, "");
  }

  public StringEntry(Descriptor descriptor, String value) {
    descriptor.checkType(Type.String);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<StringEntry>> void addListener(ChangeListener<StringEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<StringEntry>> void removeListener(ChangeListener<StringEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(String value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public String get() {
    return value;
  }

  @Override
  public void setValue(String value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public StringEntry copy() {
    return new StringEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeUTF(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readUTF();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public StringChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new StringChangeEvent();
    }
    return changeEvent;
  }
  private StringChangeEvent changeEvent;

  public class StringChangeEvent implements ValueChangeEvent<String, StringEntry> {

    private String old;

    public StringChangeEvent() {
    }

    public String getOldString() {
      return old;
    }

    public String getNewString() {
      return value;
    }

    @Override
    public String getOldValue() {
      return old;
    }

    @Override
    public String getNewValue() {
      return value;
    }

    @Override
    public StringEntry getObservable() {
      return StringEntry.this;
    }

  }
}
