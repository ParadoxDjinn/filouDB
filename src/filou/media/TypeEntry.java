package filou.media;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author dark
 * @param <V> value
 */
public class TypeEntry<V> {

  final Type<V> type;
  private final HashMap<String, Entry<V>> data = new HashMap<>();

  TypeEntry(Type<V> type) {
    this.type = type;
  }

  Type<V> getType() {
    return type;
  }

  Entry<V> set(String key, V value) {
    return set(key, type.getDefaultFileSuffix(), value);
  }

  Entry<V> set(String key, String fileSuffix, V value) {
    if (!type.getFileSuffixes().contains(fileSuffix)) {
      throw new IllegalArgumentException(
              "FileSuffix not supported:" + fileSuffix);
    }
    if (data.containsKey(key)) {
      final Entry<V> entry = data.get(key);
      if (entry.getSuffix().equals(fileSuffix)) {
        entry.setValue(value);
        return entry;
      } else {
        throw new IllegalArgumentException(
                "FileSuffix changed:" + fileSuffix
                + " old:" + entry.getSuffix());
      }
    } else {
      final Entry<V> entry = new Entry<>(key, fileSuffix, value, type);
      data.put(key, entry);
      return entry;
    }
  }

  void remove(String key) {
    data.remove(key);
  }

  boolean contains(String key) {
    return data.containsKey(key);
  }

  V get(String key) {
    return data.get(key).getValue();
  }

  Entry<V> getEntry(String key) {
    return data.get(key);
  }

  V get(String key, V defaultValue) {
    if (data.containsKey(key)) {
      return data.get(key).getValue();
    } else {
      return defaultValue;
    }
  }

  void clear() {
    data.clear();
  }

  Stream<Entry<V>> stream() {
    return data.entrySet().stream().map(entry -> entry.getValue());
  }

  void in(Register register, SourceEntry entry) throws IOException {
    data.put(entry.key, new Entry<>(entry, register, type));
  }

  void outAll(Register register, Set<SourceEntry> entrys) throws IOException {
    for (Map.Entry<String, Entry<V>> dat : data.entrySet()) {
      final Entry<V> p = dat.getValue();
      final SourceEntry entry = p.toSourceEntry(register);
      entrys.add(entry);
    }
  }

}
