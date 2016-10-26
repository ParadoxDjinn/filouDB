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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author dark
 */
public final class ArrayEntry implements Iterable<Entry>, Entry<ArrayEntry> {

  private ChangeSupport<ArrayEntry> changeSupport;
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
    array.content().forEach(object -> list.add(object.copy()));
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
    list.add(new BoolEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new ByteEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new CharEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new StringEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new ShortEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new IntegerEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new LongEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new FloatEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new DoubleEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new TimestampEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new UUIDEntry(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(new DataEntry<>(descriptor.get(0), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    ArrayEntry result
            = new ArrayEntry(getContentDescriptor());
    list.add(result);
    ChangeSupport.fireChangedEvent(changeSupport);
    return result;
  }

  public void addArray(ArrayEntry value) {
    checkType(Type.Array);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.add((Entry) value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public void setArray(int index, ArrayEntry value) {
    checkType(Type.Array);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.set(index, (Entry) value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public ArrayEntry getArray(int index) {
    checkType(Type.Array);
    return (ArrayEntry) list.get(index);
  }

  public StructEntry addStruct() {
    checkType(Type.Struct);
    StructEntry result = new StructEntry(getContentDescriptor());
    list.add(result);
    ChangeSupport.fireChangedEvent(changeSupport);
    return result;
  }

  public void addStruct(StructEntry value) {
    checkType(Type.Struct);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.add((Entry) value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public void setStruct(int index, StructEntry value) {
    checkType(Type.Struct);
    if (!getContentDescriptor().equals(value.getDescriptor())) {
      throw new IllegalArgumentException();
    }
    list.set(index, (Entry) value.copy());
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public StructEntry getStruct(int index) {
    checkType(Type.Struct);
    return (StructEntry) list.get(index);
  }

  public void addBlob(byte[] value) {
    checkType(Type.Blob);
    list.add(new BlobEntry(getContentDescriptor(), value));
    ChangeSupport.fireChangedEvent(changeSupport);
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
    list.add(value);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public void setEntry(int index, Entry value) {
    checkType(value.getDescriptor().getType());
    list.set(index, value);
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
    array.content().forEach(
            object -> list.add(object.copy()));
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

}
