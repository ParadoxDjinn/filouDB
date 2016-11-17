package filou.entries;

import filou.observe.ChangeListener;
import filou.observe.ChangeEvent;
import filou.observe.ChangeSupport;
import filou.media.Register;
import filou.util.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import javafx.util.Pair;

/**
 *
 * @author dark
 */
public final class StructEntry implements Entry<StructEntry>, Iterable<Pair<Descriptor, Entry>> {

  private ChangeSupport<StructEntry> changeSupport;
  private Register register;
  private final Descriptor descriptor;
  private final TreeMap<String, Descriptor> types = new TreeMap<>();
  private final HashMap<Descriptor, Entry> content = new HashMap<>();

  public StructEntry(Descriptor descriptor) {
    if (descriptor.getType() != Type.Struct) {
      throw new IllegalArgumentException();
    }
    this.descriptor = descriptor;
    for (Descriptor des : descriptor) {
      this.types.put(des.getKey(), des);
      this.content.put(des, des.buildEntry());
    }
  }

  public StructEntry(StructEntry struct) {
    this.descriptor = struct.getDescriptor();
    struct.content().forEach(entry -> {
      final Descriptor key = entry.getKey();
      this.types.put(key.getKey(), key);
      this.content.put(key, entry.getValue().copy());
    });
  }

  @Override
  public <T extends ChangeEvent<StructEntry>> void addListener(ChangeListener<StructEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<StructEntry>> void removeListener(ChangeListener<StructEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public boolean contains(String key) {
    return types.containsKey(key);
  }

  public Type getType(String key) {
    if (types.containsKey(key)) {
      return types.get(key).getType();
    } else {
      return Type.Void;
    }
  }

  public Descriptor getDescriptor(String key) {
    return types.get(key);
  }

  private Descriptor checkType(String key, Type type) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() != type) {
        throw new IllegalStateException("Invalid type:" + type
                + " setted: " + desKey.toString());
      }
      return desKey;
    }
    throw new IllegalStateException("Key not contained:" + key);
  }

  public void setBoolean(String key, boolean value) {
    ((BoolEntry) content.get(checkType(key, Type.Boolean))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public boolean getBoolean(String key) {
    return ((BoolEntry) content.get(checkType(key, Type.Boolean))).get();
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Boolean) {
        return ((BoolEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setByte(String key, byte value) {
    ((ByteEntry) content.get(checkType(key, Type.Byte))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte getByte(String key) {
    return ((ByteEntry) content.get(checkType(key, Type.Byte))).get();
  }

  public byte getByte(String key, byte defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Byte) {
        return ((ByteEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setChar(String key, char value) {
    ((CharEntry) content.get(checkType(key, Type.Char))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public char getChar(String key) {
    return ((CharEntry) content.get(checkType(key, Type.Char))).get();
  }

  public char getChar(String key, char defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Char) {
        return ((CharEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setString(String key, String value) {
    ((StringEntry) content.get(checkType(key, Type.String))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public String getString(String key) {
    return ((StringEntry) content.get(checkType(key, Type.String))).get();
  }

  public String getString(String key, String defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.String) {
        return ((StringEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setShort(String key, short value) {
    ((ShortEntry) content.get(checkType(key, Type.Short))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public short getShort(String key) {
    return ((ShortEntry) content.get(checkType(key, Type.Short))).get();
  }

  public short getShort(String key, short defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Short) {
        return ((ShortEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setInteger(String key, int value) {
    ((IntegerEntry) content.get(checkType(key, Type.Integer))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public int getInteger(String key) {
    return ((IntegerEntry) content.get(checkType(key, Type.Integer))).get();
  }

  public int getInteger(String key, int defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Integer) {
        return ((IntegerEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setLong(String key, long value) {
    ((LongEntry) content.get(checkType(key, Type.Long))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public long getLong(String key) {
    return ((LongEntry) content.get(checkType(key, Type.Long))).get();
  }

  public long getLong(String key, long defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Long) {
        return ((LongEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setFloat(String key, float value) {
    ((FloatEntry) content.get(checkType(key, Type.Float))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public float getFloat(String key) {
    return ((FloatEntry) content.get(checkType(key, Type.Float))).get();
  }

  public float getFloat(String key, float defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Float) {
        return ((FloatEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setDouble(String key, double value) {
    ((DoubleEntry) content.get(checkType(key, Type.Double))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public double getDouble(String key) {
    return ((DoubleEntry) content.get(checkType(key, Type.Double))).get();
  }

  public double getDouble(String key, double defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Double) {
        return ((DoubleEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setTimestamp(String key, Instant value) {
    ((TimestampEntry) content.get(checkType(key, Type.Timestamp))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Instant getTimestamp(String key) {
    return ((TimestampEntry) content.get(checkType(key, Type.Timestamp))).get();
  }

  public Instant getTimestamp(String key, Instant defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Timestamp) {
        return ((TimestampEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public void setUUID(String key, UUID value) {
    ((UUIDEntry) content.get(checkType(key, Type.UUID))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public UUID getUUID(String key) {
    return ((UUIDEntry) content.get(checkType(key, Type.UUID))).get();
  }

  public UUID getUUID(String key, UUID defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.UUID) {
        return ((UUIDEntry) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public <V> void setData(String key, V value) {
    ((DataEntry<V>) content.get(checkType(key, Type.Data))).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public <V> V getData(String key) {
    return ((DataEntry<V>) content.get(checkType(key, Type.Data))).get();
  }

  public <V> V getData(String key, V defaultValue) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Data) {
        return ((DataEntry<V>) content.get(desKey)).get();
      }
    }
    return defaultValue;
  }

  public Class<?> getDataType(String key) {
    if (types.containsKey(key)) {
      Descriptor desKey = types.get(key);
      if (desKey.getType() == Type.Data) {
        return ((DataEntry<?>) content.get(desKey)).getValueType();
      }
    }
    return null;
  }

  public void setArray(String key, ArrayEntry value) {
    getArray(key).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public ArrayEntry getArray(String key) {
    return (ArrayEntry) content.get(checkType(key, Type.Array));
  }

  public ArrayEntry getArray(String key, ArrayEntry defaultValue) {
    final Descriptor checkType = checkType(key, Type.Array);
    if (!checkType.equals(defaultValue.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    return (ArrayEntry) content.getOrDefault(checkType, (Entry) defaultValue);
  }

  public void setStruct(String key, StructEntry value) {
    getStruct(key).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public void setStruct(StructEntry value) {
    getStruct(value.getDescriptor().getKey()).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public StructEntry getStruct(String key) {
    return (StructEntry) content.get(checkType(key, Type.Struct));
  }

  public StructEntry getStruct(String key, StructEntry defaultValue) {
    final Descriptor checkType = checkType(key, Type.Struct);
    if (!checkType.equals(defaultValue.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    return (StructEntry) content.getOrDefault(checkType, (Entry) defaultValue);
  }

  public void setBlob(String key, byte[] value) {
    final Descriptor checkType = checkType(key, Type.Blob);
    content.put(checkType, new BlobEntry(checkType, value));
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte[] getBlob(String key) {
    final Descriptor checkType = checkType(key, Type.Blob);
    return ((BlobEntry) content.get(checkType)).getData();
  }

  public byte[] getBlob(String key, byte[] defaultValue) {
    final Descriptor checkType = checkType(key, Type.Blob);
    return ((BlobEntry) content.getOrDefault(checkType,
            new BlobEntry(checkType, defaultValue))).getData();
  }

  public void setEntry(String key, Entry value) {
    if (!types.get(key).equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    content.put(value.getDescriptor(), value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Entry getEntry(String key) {
    return content.get(types.get(key));
  }

  public Entry getEntry(String key, Entry defaultValue) {
    if (!types.containsKey(key)) {
      return defaultValue;
    }
    return content.getOrDefault(types.get(key), defaultValue);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  public void set(StructEntry struct) {
    if (!this.descriptor.equals(struct.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    struct.content().forEach(entry -> {
      this.content.put(entry.getKey(), entry.getValue().copy());
    });
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Stream<Pair<Descriptor, Entry>> content() {
    return content.entrySet().stream().map(
            entry -> new Pair<>(entry.getKey(), entry.getValue()));
  }

  @Override
  public Iterator<Pair<Descriptor, Entry>> iterator() {
    return IteratorTools.map(content.entrySet().iterator(),
            entry -> new Pair<>(entry.getKey(), entry.getValue()));
  }

  @Override
  public StructEntry copy() {
    return new StructEntry(this);
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    for (Descriptor key : types.values()) {
      content.get(key).out(register, output);
    }
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    for (Descriptor key : types.values()) {
      content.get(key).in(register, input);
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return content.toString();
  }

  @Override
  public StructChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new StructChangeEvent();
    }
    return changeEvent;
  }
  private StructChangeEvent changeEvent;

  public class StructChangeEvent implements ChangeEvent<StructEntry> {

    public StructChangeEvent() {
    }

    @Override
    public StructEntry getObservable() {
      return StructEntry.this;
    }

  }

  @Override
  public void init(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    } else {
      this.register = register;
    }
    for (Entry entry : content.values()) {
      entry.init(register);
    }
  }

  @Override
  public void uninit(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    } else {
      this.register = register;
    }
    for (Entry entry : content.values()) {
      entry.uninit(register);
    }
  }

}
