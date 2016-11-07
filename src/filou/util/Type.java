package filou.util;

import filou.entries.*;
import javafx.util.StringConverter;

/**
 *
 * @author dark
 */
public enum Type {
  Boolean('b', BoolEntry.class),
  Byte('y', ByteEntry.class),
  Char('c', CharEntry.class),
  String('C', StringEntry.class),
  Short('s', ShortEntry.class),
  Integer('i', IntegerEntry.class),
  Long('l', LongEntry.class),
  Float('f', FloatEntry.class),
  Double('d', DoubleEntry.class),
  Void('v', VoidEntry.class),
  Array('A', ArrayEntry.class),
  Struct('S', StructEntry.class),
  Blob('O', BlobEntry.class),
  Timestamp('T', TimestampEntry.class),
  UUID('U', UUIDEntry.class),
  Data('D', DataEntry.class);

  private Type(char code, Class<? extends Entry> utilType) {
    this.code = code;
    this.utilType = utilType;
  }

  public final char code;
  public final Class<? extends Entry> utilType;

  public boolean anyMatch(Type... types) {
    for (Type type : types) {
      if (this == type) {
        return true;
      }
    }
    return false;
  }

  public boolean noneMatch(Type... types) {
    for (Type type : types) {
      if (this == type) {
        return false;
      }
    }
    return true;
  }

  public static Type valueOf(Class utilType) {
    for (Type value : values()) {
      if (value.utilType == utilType) {
        return value;
      }
    }
    throw new IllegalArgumentException("Wrong utilType: " + utilType);
  }

  public static Type valueOf(char code) {
    for (Type value : values()) {
      if (value.code == code) {
        return value;
      }
    }
    throw new IllegalArgumentException("Wrong code: " + code);
  }

  public static final StringConverter<Type> CONVERTER = new StringConverter<Type>() {
    @Override
    public java.lang.String toString(Type object) {
      return object.name();
    }

    @Override
    public Type fromString(java.lang.String string) {
      return valueOf(string);
    }
  };

}
