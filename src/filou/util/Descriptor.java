package filou.util;

import filou.entries.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author dark
 */
public final class Descriptor implements Comparable<Descriptor>, Iterable<Descriptor> {

  private final String key;
  private final Type type;
  private final Descriptor[] descriptors;
  private final Supplier<Entry> buildEntry;

  public static StructBuilder buildStruct(String key) {
    return new StructBuilder(key);
  }

  public static ArrayBuilder buildArray(String key) {
    return new ArrayBuilder(key);
  }

  public static Descriptor buildStruct(String key, Consumer<StructBuilder> consumer) {
    StructBuilder builder = new StructBuilder(key);
    consumer.accept(builder);
    return builder.build();
  }

  public static Descriptor buildArray(String key, Consumer<ArrayBuilder> consumer) {
    ArrayBuilder builder = new ArrayBuilder(key);
    consumer.accept(builder);
    return builder.build();
  }

  public static Descriptor buildArray(String key, Descriptor type) {
    return new Descriptor(key, Type.Array, ArrayEntry::new, new Descriptor[]{
      Objects.requireNonNull(type)
    });
  }

  Descriptor(String key, Type type, Function<Descriptor, Entry> buildEntry, Descriptor[] descriptors) {
    if (key == null || key.isEmpty() || key.contains(".")) {
      throw new IllegalArgumentException(
              "Wrong key: \"" + Objects.toString(key) + "\"");
    }
    this.key = key;
    this.type = type;
    this.descriptors = descriptors;
    this.buildEntry = () -> buildEntry.apply(this);
  }

  public static Descriptor createBoolean(String key) {
    return new Descriptor(key, Type.Boolean, BoolEntry::new, null);
  }

  public static Descriptor createByte(String key) {
    return new Descriptor(key, Type.Byte, ByteEntry::new, null);
  }

  public static Descriptor createChar(String key) {
    return new Descriptor(key, Type.Char, CharEntry::new, null);
  }

  public static Descriptor createString(String key) {
    return new Descriptor(key, Type.String, StringEntry::new, null);
  }

  public static Descriptor createShort(String key) {
    return new Descriptor(key, Type.Short, ShortEntry::new, null);
  }

  public static Descriptor createInteger(String key) {
    return new Descriptor(key, Type.Integer, IntegerEntry::new, null);
  }

  public static Descriptor createLong(String key) {
    return new Descriptor(key, Type.Long, LongEntry::new, null);
  }

  public static Descriptor createFloat(String key) {
    return new Descriptor(key, Type.Float, FloatEntry::new, null);
  }

  public static Descriptor createDouble(String key) {
    return new Descriptor(key, Type.Double, DoubleEntry::new, null);
  }

  public static Descriptor createTimestamp(String key) {
    return new Descriptor(key, Type.Timestamp, TimestampEntry::new, null);
  }

  public static Descriptor createUUID(String key) {
    return new Descriptor(key, Type.UUID, UUIDEntry::new, null);
  }

  public static Descriptor createBlob(String key) {
    return new Descriptor(key, Type.Blob, BlobEntry::new, null);
  }

  public static Descriptor createVoid(String key) {
    return new Descriptor(key, Type.Void, VoidEntry::new, null);
  }

  public static Descriptor createData(String key) {
    return new Descriptor(key, Type.Data, DataEntry::new, null);
  }

  public static <T> Descriptor createData(String key, Class<T> type) {
    return new Descriptor(key, Type.Data,
            descriptor -> new DataEntry<>(descriptor, type), null);
  }

  public static Entry createEntry(Descriptor descriptor) {
    switch (descriptor.getType()) {
      case Blob:
        return new BlobEntry(descriptor);
      case Boolean:
        return new BoolEntry(descriptor);
      case Byte:
        return new ByteEntry(descriptor);
      case Char:
        return new CharEntry(descriptor);
      case Double:
        return new DoubleEntry(descriptor);
      case Float:
        return new FloatEntry(descriptor);
      case Integer:
        return new IntegerEntry(descriptor);
      case Long:
        return new LongEntry(descriptor);
      case Short:
        return new ShortEntry(descriptor);
      case String:
        return new StringEntry(descriptor);
      case Timestamp:
        return new TimestampEntry(descriptor);
      case UUID:
        return new UUIDEntry(descriptor);
      case Array:
        return new ArrayEntry(descriptor);
      case Struct:
        return new StructEntry(descriptor);
      case Void:
        return new VoidEntry(descriptor);
      case Data:
        return new DataEntry(descriptor);
      default:
        throw new UnsupportedOperationException("Not supported");
    }
  }

  public static class StructBuilder {

    private final String key;
    private final HashMap<String, Descriptor> ds = new HashMap<>();

    private StructBuilder(String key) {
      this.key = key;
    }

    public void addBoolean(String key) {
      ds.put(key, createBoolean(key));
    }

    public void addByte(String key) {
      ds.put(key, createByte(key));
    }

    public void addChar(String key) {
      ds.put(key, createChar(key));
    }

    public void addString(String key) {
      ds.put(key, createString(key));
    }

    public void addShort(String key) {
      ds.put(key, createShort(key));
    }

    public void addInteger(String key) {
      ds.put(key, createInteger(key));
    }

    public void addLong(String key) {
      ds.put(key, createLong(key));
    }

    public void addFloat(String key) {
      ds.put(key, createFloat(key));
    }

    public void addDouble(String key) {
      ds.put(key, createDouble(key));
    }

    public void addTimestamp(String key) {
      ds.put(key, createTimestamp(key));
    }

    public void addUUID(String key) {
      ds.put(key, createUUID(key));
    }

    public void addArray(String key, Consumer<ArrayBuilder> consumer) {
      ds.put(key, buildArray(key, consumer));
    }

    public void addStruct(String key, Consumer<StructBuilder> consumer) {
      ds.put(key, buildStruct(key, consumer));
    }

    public void addBlob(String key) {
      ds.put(key, createBlob(key));
    }

    public void addVoid(String key) {
      ds.put(key, createVoid(key));
    }

    public void addData(String key) {
      ds.put(key, createData(key));
    }

    public <T> void addData(String key, Class<T> type) {
      ds.put(key, createData(key, type));
    }

    public void add(Descriptor descriptor) {
      if (ds.containsKey(descriptor.key)) {
        throw new IllegalArgumentException(
                "Key already set:" + descriptor.key);
      }
      ds.put(descriptor.key, descriptor);
    }

    public boolean contains(Descriptor descriptor) {
      return ds.containsKey(descriptor.key)
              ? ds.get(descriptor.key).equals(descriptor) : false;
    }

    public boolean blocked(Descriptor descriptor) {
      return ds.containsKey(descriptor.key);
    }

    public Descriptor build() {
      Descriptor[] childs = ds.isEmpty() ? null
              : ds.values().toArray(new Descriptor[ds.size()]);
      return new Descriptor(key, Type.Struct, StructEntry::new, childs);
    }

  }

  public static class ArrayBuilder {

    private static final String ARRAY_TYPE = "type";

    private final String key;
    private Descriptor type = null;

    private ArrayBuilder(String key) {
      this.key = key;
    }

    public void setBoolean() {
      type = createBoolean(ARRAY_TYPE);
    }

    public void setByte() {
      type = createByte(ARRAY_TYPE);
    }

    public void setChar() {
      type = createChar(ARRAY_TYPE);
    }

    public void setString() {
      type = createString(ARRAY_TYPE);
    }

    public void setShort() {
      type = createShort(ARRAY_TYPE);
    }

    public void setInteger() {
      type = createInteger(ARRAY_TYPE);
    }

    public void setLong() {
      type = createLong(ARRAY_TYPE);
    }

    public void setFloat() {
      type = createFloat(ARRAY_TYPE);
    }

    public void setDouble() {
      type = createDouble(ARRAY_TYPE);
    }

    public void setTimestamp() {
      type = createTimestamp(ARRAY_TYPE);
    }

    public void setUUID() {
      type = createUUID(ARRAY_TYPE);
    }

    public void setArray(Consumer<ArrayBuilder> consumer) {
      type = buildArray(ARRAY_TYPE, consumer);
    }

    public void setStruct(Consumer<StructBuilder> consumer) {
      type = buildStruct(ARRAY_TYPE, consumer);
    }

    public void setBlob() {
      type = createBlob(ARRAY_TYPE);
    }

    public void setVoid() {
      type = createVoid(ARRAY_TYPE);
    }

    public void setData() {
      type = createData(ARRAY_TYPE);
    }

    public <T> void setData(Class<T> type) {
      this.type = createData(ARRAY_TYPE, type);
    }

    public void set(Descriptor descriptor) {
      type = descriptor;
    }

    public Descriptor build() {
      if (type == null) {
        throw new IllegalStateException("array type not set");
      }
      return new Descriptor(key, Type.Array, ArrayEntry::new, new Descriptor[]{
        type
      });
    }
  }

  public String getKey() {
    return key;
  }

  public Type getType() {
    return type;
  }

  public boolean equalsType(Type type) {
    return this.type == type;
  }

  public Descriptor checkType(Type type) {
    if (this.type != type) {
      throw new IllegalArgumentException(
              "descriptor is not a type of " + type.name()
              + " ( " + toString() + " )");
    }
    return this;
  }

  public Entry buildEntry() {
    return buildEntry.get();
  }

  public Descriptor get(int index) {
    return descriptors[index];
  }

  public int size() {
    return descriptors != null ? descriptors.length : 0;
  }

  public boolean contains(String key, Type type) {
    return find(key, type) != null;
  }

  public Descriptor find(String key, Type type) {
    for (Descriptor child : descriptors) {
      if (child.key.equals(key) && child.type == type) {
        return child;
      }
    }
    return null;
  }

  @Override
  public Iterator<Descriptor> iterator() {
    return IteratorTools.fromArray(descriptors);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(key);
    builder.append('&');
    builder.append(type);
    if (descriptors != null) {
      builder.append('[');
      for (Iterator<Descriptor> it = this.iterator(); it.hasNext();) {
        Descriptor next = it.next();
        builder.append(next.toString());
        if (it.hasNext()) {
          builder.append(',');
        }
      }
      builder.append(']');
    }
    return builder.toString();
  }

  @Override
  public int compareTo(Descriptor descriptor) {
    return this.key.compareTo(descriptor.key);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.key);
    hash = 23 * hash + Objects.hashCode(this.type);
    hash = 23 * hash + Arrays.deepHashCode(this.descriptors);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Descriptor other = (Descriptor) obj;
    return Objects.equals(this.key, other.key) && this.type == other.type
            && Arrays.deepEquals(this.descriptors, other.descriptors);
  }

  public static void outDescriptor(Descriptor descriptor,
          DataOutput output) throws IOException {
    output.writeUTF(descriptor.key);
    output.writeChar(descriptor.type.code);
    outDescriptors(descriptor, output);
  }

  private static void outDescriptors(Descriptor descriptor,
          DataOutput output) throws IOException {
    switch (descriptor.type) {
      case Array:
        outDescriptor(descriptor.descriptors[0], output);
        return;
      case Struct:
        output.writeInt(descriptor.size());
        for (Descriptor descriptor2 : descriptor) {
          outDescriptor(descriptor2, output);
        }
      default:
    }
  }

  public static Descriptor inDescriptor(DataInput input)
          throws IOException {
    final String key = input.readUTF();
    final Type type = Type.valueOf(input.readChar());
    final Descriptor[] descriptors = inDescriptors(type, input);
    return new Descriptor(key, type, Descriptor::createEntry, descriptors);
  }

  private static Descriptor[] inDescriptors(Type type,
          DataInput input) throws IOException {
    switch (type) {
      case Array:
        return new Descriptor[]{inDescriptor(input)};
      case Struct:
        int size = input.readInt();
        Descriptor[] result = new Descriptor[size];
        for (int i = 0; i < size; i++) {
          result[i] = inDescriptor(input);
        }
        return result;
      default:
        return null;
    }
  }

}
