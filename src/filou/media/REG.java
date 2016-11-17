package filou.media;

import filou.entries.ArrayEntry;
import filou.entries.StructEntry;
import filou.entries.Descriptor;
import filou.entries.Entry;
import filou.util.Loadable;
import filou.util.SelfStorable;
import filou.util.Storable;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;

/**
 *
 * @author dark
 */
public final class REG {

  public static <T> void set(Register register, String key, Class<T> type, T value) {
    register.byClass.get(type).set(key, value);
  }

  public static <T> void set(Register register, String key, String fileSuffix, Class<T> type, T value) {
    register.byClass.get(type).set(key, fileSuffix, value);
  }

  public static <T> boolean contains(Register register, String key, Class<T> type) {
    return register.byClass.get(type).contains(key);
  }

  public static <T> T get(Register register, String key, Class<T> type) {
    return (T) register.byClass.get(type).get(key);
  }

  public static <T extends filou.entries.Entry> void setSingle(Register register, T entry) {
    REG.setEntry(register, entry.getDescriptor().toString(), entry);
  }

  public static <T extends filou.entries.Entry> T getSingle(Register register, Descriptor descriptor) {
    return REG.getEntry(register, descriptor.toString());
  }

  public static boolean containsSingle(Register register, Descriptor descriptor) {
    return REG.containsEntry(register, descriptor.toString());
  }

  public static boolean contains(Register register, String key) {
    return REG.containsEntry(register, key);
  }

  public static void removeAllOf(Register register, Descriptor descriptor) {

    stream(register, descriptor).collect(Collectors.toList()).forEach(register::remove);
  }

  public static final void addRandom(Register register, Storable storable) {
    final String key = UUID.randomUUID().toString();
    REG.setEntry(register, key, (Entry) storable.save(key, register));
  }

  public static void save(Register register, String key, Storable storable) {
    REG.setEntry(register, key, storable.save(key, register));
  }

  public static boolean load(Register register, String key, Loadable loadable) {
    if (REG.containsEntry(register, key)) {
      loadable.load(key, register, REG.getEntry(register, key));
      return true;
    }
    return false;
  }

  public static void save(Register register, SelfStorable restorable) {
    REG.setEntry(register, restorable.key(),
            (Entry) restorable.save(register));
  }

  public static <T extends Loadable> Function<filou.media.Entry<StructEntry>, T> load(Supplier<T> t) {
    return (filou.media.Entry<StructEntry> entry) -> {
      final T result = t.get();
      result.load(entry.getKey(), entry.getRegister(), entry.getValue());
      return result;
    };
  }

  public static <T extends Loadable> T load(filou.media.Entry<StructEntry> entry, T t) {
    t.load(entry.getKey(), entry.getRegister(), entry.getValue());
    return t;
  }

  /**
   *
   * @param <T>
   * @param register
   * @param key
   * @param type
   * @param defaultValue
   * @return
   */
  public static <T> T init(Register register, String key, Class<T> type, T defaultValue) {
    if (contains(register, key, type)) {
      return get(register, key, type);
    } else {
      set(register, key, type, defaultValue);
      return defaultValue;
    }
  }

  /**
   *
   * @param <T>
   * @param register
   * @param key
   * @param fileSuffix
   * @param type
   * @param defaultValue
   * @return
   */
  public static <T> T init(Register register, String key, String fileSuffix, Class<T> type, T defaultValue) {
    if (contains(register, key, type)) {
      return get(register, key, type);
    } else {
      set(register, key, fileSuffix, type, defaultValue);
      return defaultValue;
    }
  }

  public static <T> T get(Register register, String key, Class<T> type, T defaultValue) {
    return (T) register.byClass.get(type).get(key, defaultValue);
  }

  public static <T> Stream<T> stream(Register register, Class<T> type) {
    return register.byClass.get(type).stream();
  }

  public static <T> void forEach(Register register, Class<T> type, Consumer<Pair<String, T>> consumer) {
    register.byClass.get(type).stream().forEach(consumer);
  }

  public static <T extends filou.entries.Entry> void setEntry(Register register, String key, T value) {
    register.byClass.get(filou.entries.Entry.class).set(key, value);
  }

  public static boolean containsEntry(Register register, String key) {
    return register.byClass.get(filou.entries.Entry.class).contains(key);
  }

  public static <T extends filou.entries.Entry> T getEntry(Register register, String key) {
    return (T) register.byClass.get(filou.entries.Entry.class).get(key);
  }

  public static <T extends filou.entries.Entry> T getEntry(Register register, String key, T defaultValue) {
    return (T) register.byClass.get(filou.entries.Entry.class).get(key, defaultValue);
  }

  public static Stream<filou.media.Entry<Entry>> entryStream(Register register) {
    return register.stream(filou.entries.Entry.class);
  }

  public static void forEachEntry(Register register, Consumer<filou.media.Entry<Entry>> consumer) {
    register.byClass.get(filou.entries.Entry.class).stream().forEach(consumer);
  }

  public static Stream<filou.media.Entry<Entry>> stream(Register register, Descriptor descriptor) {
    return REG.entryStream(register).filter(pair -> {
      return pair.getValue().getDescriptor().equals(descriptor);
    });
  }

  public static Stream<filou.media.Entry<ArrayEntry>> streamArray(Register register, Descriptor descriptor) {
    return stream(register, descriptor.checkType(filou.entries.Type.Array))
            .map((filou.media.Entry<Entry> entry) -> (filou.media.Entry) entry);
  }

  public static Stream<filou.media.Entry<StructEntry>> streamStruct(Register register, Descriptor descriptor) {
    return stream(register, descriptor.checkType(filou.entries.Type.Struct))
            .map((filou.media.Entry<Entry> entry) -> (filou.media.Entry) entry);
  }

  private REG() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

}
