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
public final class DoubleEntry extends ValueEntry<Double, DoubleEntry> {

  private ChangeSupport<DoubleEntry> changeSupport;
  private final Descriptor descriptor;
  private double value;

  public DoubleEntry(Descriptor descriptor) {
    this(descriptor, Double.NaN);
  }

  public DoubleEntry(Descriptor descriptor, double value) {
    descriptor.checkType(Type.Double);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<DoubleEntry>> void addListener(ChangeListener<DoubleEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<DoubleEntry>> void removeListener(ChangeListener<DoubleEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(double value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public double get() {
    return value;
  }

  @Override
  public void setValue(Double value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Double getValue() {
    return value;
  }

  @Override
  public DoubleEntry copy() {
    return new DoubleEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeDouble(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readDouble();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }

  @Override
  public DoubleChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new DoubleChangeEvent();
    }
    return changeEvent;
  }
  private DoubleChangeEvent changeEvent;

  public class DoubleChangeEvent implements ValueChangeEvent<Double, DoubleEntry> {

    private double old;

    public DoubleChangeEvent() {
    }

    public double getOldDouble() {
      return old;
    }

    public double getNewDouble() {
      return value;
    }

    @Override
    public Double getOldValue() {
      return old;
    }

    @Override
    public Double getNewValue() {
      return value;
    }

    @Override
    public DoubleEntry getObservable() {
      return DoubleEntry.this;
    }

  }
}
