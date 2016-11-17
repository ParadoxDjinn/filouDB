package filou.entries;

import filou.observe.ChangeListener;
import filou.observe.ChangeEvent;
import filou.observe.ChangeSupport;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 *
 * @author dark
 */
public final class ArrayEntry implements Iterable<Entry>, Entry<ArrayEntry> {

  private ChangeSupport<ArrayEntry> changeSupport;
  private Register register;
  private final Descriptor descriptor;
  private final ArrayList<Entry> list = new ArrayList<>();

  public ArrayEntry(Descriptor descriptor) {
    descriptor.checkType(Type.Array);
    if (descriptor.size() != 1) {
      throw new IllegalArgumentException();
    }
    this.descriptor = descriptor;
  }

  public ArrayEntry(ArrayEntry array) {
    this.descriptor = array.getDescriptor();
    array.content().map(Entry::copy).forEach(this::add);
  }

  @Override
  public <T extends ChangeEvent<ArrayEntry>> void addListener(ChangeListener<ArrayEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<ArrayEntry>> void removeListener(ChangeListener<ArrayEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public boolean canEdit(Type type) {
    return descriptor.get(0).getType() == type || type == Type.Void;
  }

  public Descriptor getContentDescriptor() {
    return descriptor.get(0);
  }

  @Override
  public Type getType() {
    return descriptor.get(0).getType();
  }

  private void checkType(Type type) {
    if (!canEdit(type)) {
      throw new IllegalStateException();
    }
  }

  public void addBoolean(boolean value) {
    checkType(Type.Boolean);
    add(new BoolEntry(descriptor.get(0), value));
  }

  public void setBoolean(int index, boolean value) {
    checkType(Type.Boolean);
    ((BoolEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public boolean getBoolean(int index) {
    checkType(Type.Boolean);
    return ((BoolEntry) list.get(index)).get();
  }

  public void addByte(byte value) {
    checkType(Type.Byte);
    add(new ByteEntry(descriptor.get(0), value));
  }

  public void setByte(int index, byte value) {
    checkType(Type.Byte);
    ((ByteEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte getByte(int index) {
    checkType(Type.Byte);
    return ((ByteEntry) list.get(index)).get();
  }

  public void addChar(char value) {
    checkType(Type.Char);
    add(new CharEntry(descriptor.get(0), value));
  }

  public void setChar(int index, char value) {
    checkType(Type.Char);
    ((CharEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public char getChar(int index) {
    checkType(Type.Char);
    return ((CharEntry) list.get(index)).get();
  }

  public void addString(String value) {
    checkType(Type.String);
    add(new StringEntry(descriptor.get(0), value));
  }

  public void setString(int index, String value) {
    checkType(Type.String);
    ((StringEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public String getString(int index) {
    checkType(Type.String);
    return ((StringEntry) list.get(index)).get();
  }

  public void addShort(short value) {
    checkType(Type.Short);
    add(new ShortEntry(descriptor.get(0), value));
  }

  public void setShort(int index, short value) {
    checkType(Type.Short);
    ((ShortEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public short getShort(int index) {
    checkType(Type.Short);
    return ((ShortEntry) list.get(index)).get();
  }

  public void addInteger(int value) {
    checkType(Type.Integer);
    add(new IntegerEntry(descriptor.get(0), value));
  }

  public void setInteger(int index, int value) {
    checkType(Type.Integer);
    ((IntegerEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public int getInteger(int index) {
    checkType(Type.Integer);
    return ((IntegerEntry) list.get(index)).get();
  }

  public void addLong(long value) {
    checkType(Type.Long);
    add(new LongEntry(descriptor.get(0), value));
  }

  public void setLong(int index, long value) {
    checkType(Type.Long);
    ((LongEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public long getLong(int index) {
    checkType(Type.Long);
    return ((LongEntry) list.get(index)).get();
  }

  public void addFloat(float value) {
    checkType(Type.Float);
    add(new FloatEntry(descriptor.get(0), value));
  }

  public void setFloat(int index, float value) {
    checkType(Type.Float);
    ((FloatEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public float getFloat(int index) {
    checkType(Type.Float);
    return ((FloatEntry) list.get(index)).get();
  }

  public void addDouble(double value) {
    checkType(Type.Double);
    add(new DoubleEntry(descriptor.get(0), value));
  }

  public void setDouble(int index, double value) {
    checkType(Type.Double);
    ((DoubleEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public double getDouble(int index) {
    checkType(Type.Double);
    return ((DoubleEntry) list.get(index)).get();
  }

  public void addTimestamp(Instant value) {
    checkType(Type.Timestamp);
    add(new TimestampEntry(descriptor.get(0), value));
  }

  public void setTimestamp(int index, Instant value) {
    checkType(Type.Timestamp);
    ((TimestampEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Instant getTimestamp(int index) {
    checkType(Type.Timestamp);
    return ((TimestampEntry) list.get(index)).get();
  }

  public void addUUID(UUID value) {
    checkType(Type.UUID);
    add(new UUIDEntry(descriptor.get(0), value));
  }

  public void setUUID(int index, UUID value) {
    checkType(Type.UUID);
    ((UUIDEntry) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public UUID getUUID(int index) {
    checkType(Type.UUID);
    return ((UUIDEntry) list.get(index)).get();
  }

  public <V> void addData(V value) {
    checkType(Type.Data);
    add(new DataEntry<>(descriptor.get(0), value));
  }

  public <V> void setData(int index, V value) {
    checkType(Type.Data);
    ((DataEntry<V>) list.get(index)).set(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public <V> V getData(int index) {
    checkType(Type.Data);
    return ((DataEntry<V>) list.get(index)).get();
  }

  public ArrayEntry addArray() {
    checkType(Type.Array);
    ArrayEntry result = new ArrayEntry(getContentDescriptor());
    add(result);
    return result;
  }

  public void addArray(ArrayEntry value) {
    checkType(Type.Array);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    add((Entry) value.copy());
  }

  public void setArray(int index, ArrayEntry value) {
    checkType(Type.Array);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.set(index, value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public ArrayEntry getArray(int index) {
    checkType(Type.Array);
    return (ArrayEntry) list.get(index);
  }

  public StructEntry addStruct() {
    checkType(Type.Struct);
    StructEntry result = new StructEntry(getContentDescriptor());
    add(result);
    return result;
  }

  public void addStruct(StructEntry value) {
    checkType(Type.Struct);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    add(value.copy());
  }

  public void setStruct(int index, StructEntry value) {
    checkType(Type.Struct);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.set(index, value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public StructEntry getStruct(int index) {
    checkType(Type.Struct);
    return (StructEntry) list.get(index);
  }

  public void addBlob(byte[] value) {
    checkType(Type.Blob);
    add(new BlobEntry(getContentDescriptor(), value));
  }

  public void setBlob(int index, byte[] value) {
    checkType(Type.Blob);
    list.set(index, new BlobEntry(getContentDescriptor(), value));
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte[] getBlob(int index) {
    checkType(Type.Blob);
    return ((BlobEntry) list.get(index)).getData();
  }

  public void addEntry(Entry value) {
    checkType(value.getDescriptor().getType());
    add(value);
  }

  public void setEntry(int index, Entry value) {
    checkType(value.getDescriptor().getType());
    list.set(index, value);
    if (register != null) {
      value.init(register);
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  private void add(Entry entry) {
    list.add(entry);
    if (register != null) {
      entry.init(register);
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Entry getEntry(int index) {
    return list.get(index);
  }

  public void remove(int index) {
    list.remove(index);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public int size() {
    return list.size();
  }

  @Override
  public Iterator<Entry> iterator() {
    return list.iterator();
  }

  public Stream<Entry> stream() {
    return list.stream();
  }

  public void set(ArrayEntry array) {
    if (!this.descriptor.equals(array.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    this.list.clear();
    array.content().map(Entry::copy).forEach(this::add);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public Stream<Entry> content() {
    return list.stream();
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public ArrayEntry copy() {
    return new ArrayEntry(this);
  }

  public void clear() {
    if (register != null) {
      for (Entry entry : list) {
        entry.uninit(register);
      }
    }
    list.clear();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeInt(list.size());
    for (Entry entry : list) {
      entry.out(register, output);
    }
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    list.clear();
    int size = input.readInt();
    for (int i = 0; i < size; i++) {
      Entry entry = descriptor.get(0).buildEntry();
      entry.in(register, input);
      list.add(entry);
      if (register != null) {
        entry.init(register);
      }
    }
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return list.toString();
  }

  @Override
  public ArrayChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new ArrayChangeEvent();
    }
    return changeEvent;
  }
  private ArrayChangeEvent changeEvent;

  public class ArrayChangeEvent implements ChangeEvent<ArrayEntry> {

    public ArrayChangeEvent() {
    }

    @Override
    public ArrayEntry getObservable() {
      return ArrayEntry.this;
    }

  }

  @Override
  public void init(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    } else {
      this.register = register;
      for (Entry entry : list) {
        entry.init(register);
      }
    }
  }

  @Override
  public void uninit(Register register) {
    if (this.register != null && this.register != register) {
      throw new IllegalStateException("Wrong register");
    } else {
      this.register = register;
      for (Entry entry : list) {
        entry.uninit(register);
      }
    }
  }

}
