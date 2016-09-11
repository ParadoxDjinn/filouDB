package filou.media;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javafx.util.Pair;

/**
 *
 * @author dark
 * @param <V> value
 */
public class TypeEntry<V> {

  final Type<V> type;
  private final HashMap<String, Pair<String, V>> data = new HashMap<>();

  TypeEntry(Type<V> type) {
    this.type = type;
  }

  Type<V> getType() {
    return type;
  }

  void set(String key, V value) {
    set(key, type.getDefaultFileSuffix(), value);
  }

  void set(String key, String fileSuffix, V value) {
    if (!type.getFileSuffixes().contains(fileSuffix)) {
      throw new IllegalArgumentException("FileSuffix not supported:" + fileSuffix);
    }
    data.put(key, new Pair<>(fileSuffix, value));
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

  Stream<Pair<String, V>> stream() {
    return data.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), entry.getValue().getValue()));
  }

  void in(Register register, SourceEntry entry) throws IOException {
    data.put(entry.key, new Pair<>(entry.fileSuffix, type.in(register,
            entry.key, entry.fileSuffix, entry.buffer.asInputStream())));
  }

  void outAll(Register register, Set<SourceEntry> entrys) throws IOException {
    for (Map.Entry<String, Pair<String, V>> dat : data.entrySet()) {
      final Pair<String, V> p = dat.getValue();
      final SourceEntry entry = new SourceEntry(dat.getKey(), p.getKey());
      type.out(register, entry.key,
              p.getKey(), p.getValue(), entry.buffer.asOutputStream());
      entrys.add(entry);
    }
  }

}
