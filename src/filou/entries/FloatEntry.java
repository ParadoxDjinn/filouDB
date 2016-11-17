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
public final class FloatEntry extends ValueEntry<Float, FloatEntry> {

  private ChangeSupport<FloatEntry> changeSupport;
  private final Descriptor descriptor;
  private float value;

  public FloatEntry(Descriptor descriptor) {
    this(descriptor, Float.NaN);
  }

  public FloatEntry(Descriptor descriptor, float value) {
    descriptor.checkType(Type.Float);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<FloatEntry>> void addListener(ChangeListener<FloatEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<FloatEntry>> void removeListener(ChangeListener<FloatEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(float value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public float get() {
    return value;
  }

  @Override
  public void setValue(Float value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Float getValue() {
    return value;
  }

  @Override
  public FloatEntry copy() {
    return new FloatEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeFloat(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readFloat();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Float.toString(value);
  }

  @Override
  public FloatChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new FloatChangeEvent();
    }
    return changeEvent;
  }
  private FloatChangeEvent changeEvent;

  public class FloatChangeEvent implements ValueChangeEvent<Float, FloatEntry> {

    private float old;

    public FloatChangeEvent() {
    }

    public float getOldFloat() {
      return old;
    }

    public float getNewFloat() {
      return value;
    }

    @Override
    public Float getOldValue() {
      return old;
    }

    @Override
    public Float getNewValue() {
      return value;
    }

    @Override
    public FloatEntry getObservable() {
      return FloatEntry.this;
    }

  }
}
