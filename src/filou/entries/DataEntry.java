package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author dark
 * @param <V> value
 */
public final class DataEntry<V> extends ValueEntry<V, DataEntry<V>> {

  private ChangeSupport<DataEntry<V>> changeSupport;
  private final Descriptor descriptor;
  private Class<V> type;
  private Register register;
  private UUID key = UUID.randomUUID();
  private V value;

  public DataEntry(Descriptor descriptor, V value) {
    this(descriptor);
    set(value);
  }

  public DataEntry(Descriptor descriptor) {
    descriptor.checkType(Type.Data);
    this.descriptor = descriptor;
  }

  @Override
  public <T extends ChangeEvent<DataEntry<V>>>
          void addListener(ChangeListener<DataEntry<V>, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<DataEntry<V>>>
          void removeListener(ChangeListener<DataEntry<V>, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(V value) {
    setToReg(value);
  }

  private void setToReg(V value) {
    if (changeEvent != null) {
      changeEvent.old = getFromReg();
    }
    type = getValueType();
    final String keyStr = key.toString();
    this.value = value;
    if (register != null) {
      if (value != null) {
        register.set(keyStr, type, value);
      } else if (type != null) {
        register.remove(keyStr, type);
      }
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public V get() {
    return getFromReg();
  }

  @Override
  public void setValue(V value) {
    setToReg(value);
  }

  @Override
  public V getValue() {
    return getFromReg();
  }

  private V getFromReg() {
    if (type == null || register == null) {
      return value;
    }
    final String keyString = key.toString();
    return register.contains(keyString, type)
            ? register.get(keyString, type) : value;
  }

  public Class getValueType() {
    if (value != null) {
      return getSupportedValueType(value.getClass());
    }
    return type;
  }

  private Class getSupportedValueType(Class c) {
    if (register != null && c != null) {
      if (register.isSupported(c)) {
        return c;
      } else {
        Class c1 = getSupportedValueType(c.getSuperclass());
        if (c1 != null) {
          return c1;
        } else {
          for (Class aInterface : c.getInterfaces()) {
            Class c2 = getSupportedValueType(aInterface);
            if (c2 != null) {
              return c2;
            }
          }
        }
      }
    }
    return c;
  }

  @Override
  public DataEntry<V> copy() {
    DataEntry<V> dataEntry = new DataEntry<>(descriptor);
    dataEntry.type = type;
    dataEntry.value = getFromReg();
    return dataEntry;
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    checkReg(register);
    output.writeUTF(key.toString());
    type = getValueType();
    output.writeUTF(type == null ? "" : type.getName());
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    checkReg(register);
    if (changeEvent != null) {
      changeEvent.old = getFromReg();
    }
    this.key = UUID.fromString(input.readUTF());
    String tempType = input.readUTF();
    try {
      this.type = tempType.isEmpty() ? null : (Class) Class.forName(tempType);
    } catch (ClassNotFoundException ex) {
      throw new IOException(ex);
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  private void checkReg(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    } else if (this.register == null) {
      this.register = register;
      final String keyStr = key.toString();
      type = getValueType();
      if (value != null) {
        register.set(keyStr, type, value);
      } else if (type != null) {
        register.remove(keyStr, type);
      }
    }
  }

  @Override
  public String toString() {
    return Objects.toString(getFromReg());
  }

  @Override
  public DataEntry.DataChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new DataChangeEvent();
    }
    return changeEvent;
  }
  private DataEntry.DataChangeEvent changeEvent;

  public class DataChangeEvent implements ValueChangeEvent<V, DataEntry<V>> {

    private V old;

    public DataChangeEvent() {
    }

    @Override
    public V getOldValue() {
      return old;
    }

    @Override
    public V getNewValue() {
      return getFromReg();
    }

    @Override
    public DataEntry<V> getObservable() {
      return DataEntry.this;
    }

  }

  @Override
  void init(Register register) {
    checkReg(register);
  }

  @Override
  void uninit(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    }
    this.register = null;
  }

  public Register getRegister() {
    return register;
  }

}
