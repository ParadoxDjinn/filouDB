package filou.media;

import filou.log.Level;
import filou.log.Observer;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author dark
 */
public final class Register {

  public Register() {
  }

  public <T> void register(Type<T> type) {
    if (isBlocked(type)) {
      throw new IllegalStateException(
              "Type is blocked: " + type.getKey().getName());
    }
    Set<Class> collect = byClass.values().stream()
            .map(holder -> holder.type.getClass())
            .collect(Collectors.toSet());
    for (Class<? extends Type> requiredType : type.getRequiredTypes()) {
      if (!collect.contains(requiredType)) {
        throw new IllegalStateException(
                "Required Type not registed:" + requiredType.getName());
      }
    }
    final TypeEntry holder = new TypeEntry(type);
    for (Iterator<SourceEntry> iterator = unknownEntries.iterator(); iterator.hasNext();) {
      SourceEntry next = iterator.next();
      if (type.getFileSuffixes().contains(next.fileSuffix)) {
        try {
          holder.in(this, next);
          iterator.remove();
        } catch (IOException ex) {
          Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          throw new IllegalStateException(ex);
        }
      }
    }
    byClass.put(type.getKey(), holder);
    type.getFileSuffixes().forEach(fileSuffix -> byFileSuffix.put(fileSuffix, holder));
  }

  public <T> void unregister(Type<T> type) {
    try {
      if (!isRegisted(type)) {
        throw new IllegalStateException("is not registed");
      } else if (byClass.values().stream().anyMatch(te
              -> te.type.getRequiredTypes().stream().anyMatch(type::equals))) {
        throw new IllegalStateException("is a required type");
      }
      TypeEntry remove = byClass.remove(type.getKey());
      remove.outAll(this, unknownEntries);
      type.getFileSuffixes().stream().forEach(byFileSuffix::remove);
    } catch (IOException ex) {
      Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      throw new IllegalStateException(ex);
    }
  }

  public <T> boolean isSupported(Class<T> c) {
    return byClass.containsKey(c);
  }

  public <T> boolean isRegisted(Type<T> type) {
    return byClass.containsKey(type.getKey())
            && byClass.get(type.getKey()).type.equals(type);
  }

  public Stream<Type> types() {
    return byClass.values().stream().map(TypeEntry::getType);
  }

  public <T> boolean isBlocked(Type<T> type) {
    return byClass.containsKey(type.getKey())
            || type.getFileSuffixes().stream().anyMatch(byFileSuffix::containsKey);
  }

  public <T> Entry<T> set(String key, Class<T> type, T value) {
    return byClass.get(type).set(key, value);
  }

  public <T> Entry<T> set(String key, String fileSuffix, Class<T> type, T value) {
    return byClass.get(type).set(key, fileSuffix, value);
  }

  public <T> boolean contains(String key, Class<T> type) {
    return byClass.get(type).contains(key);
  }

  public <T> boolean contains(Entry<T> entry) {
    final TypeEntry typeEntry = byClass.get(entry.getType().getKey());
    return typeEntry.contains(entry.getKey())
            && typeEntry.get(entry.getKey()) == entry;
  }

  public <T> void remove(String key, Class<T> type) {
    byClass.get(type).remove(key);
  }

  public <T> void remove(Entry<T> entry) {
    if (contains(entry)) {
      byClass.get(entry.getType().getKey()).remove(entry.getKey());
    }
  }

  public <T> T get(String key, Class<T> type) {
    return (T) byClass.get(type).get(key);
  }

  public <T> Entry<T> getEntry(String key, Class<T> type) {
    return (Entry<T>) byClass.get(type).getEntry(key);
  }

  public <T> T get(String key, Class<T> type, T defaultValue) {
    return (T) byClass.get(type).get(key, defaultValue);
  }

  public <T> TypeEntry<T> get(Class<T> type) {
    return byClass.get(type);
  }

  public <T> void clear(Class<T> type) {
    byClass.get(type).clear();
  }

  public <T> Stream<Entry<T>> stream(Class<T> type) {
    return byClass.get(type).stream();
  }

  public <T> void forEach(Class<T> type, Consumer<Entry<T>> consumer) {
    stream(type).forEach(consumer);
  }

  public Stream<Entry<?>> stream() {
    return byClass.values().stream().flatMap(TypeEntry::stream);
  }

  public void forEach(Consumer<Entry<?>> consumer) {
    stream().forEach(consumer);
  }

  public void load(Source source) throws IOException {
    messageInfo(" --- begin load --- ");
    Set<Class> todo = byClass.values().stream()
            .map((TypeEntry holder) -> holder.type.getClass())
            .collect(Collectors.toSet());

    Map<Type, Set<SourceEntry>> map = new HashMap<>();
    unknownEntries.clear();

    source.getContent().forEach(entry -> {
      final Set<SourceEntry> es;
      if (byFileSuffix.containsKey(entry.fileSuffix)) {
        Type type = byFileSuffix.get(entry.fileSuffix).type;
        if (!map.containsKey(type)) {
          map.put(type, new HashSet<>());
        }
        es = map.get(type);
      } else {
        es = unknownEntries;
        unknownEntryInfo(entry);
      }
      es.add(entry);
    });

    while (!todo.isEmpty()) {
      for (TypeEntry holder : byClass.values()) {

        final Class<? extends Type> typeClass = holder.type.getClass();
        if (todo.contains(typeClass)
                && holder.type.getRequiredTypes()
                        .stream().noneMatch(todo::contains)) {
          if (map.containsKey(holder.type)) {
            for (SourceEntry entry : map.get(holder.type)) {
              holder.in(this, entry);
            }
          }
          todo.remove(typeClass);
        }
      }
    }
    messageInfo(" --- end   load --- ");
  }

  public void save(Source source) throws IOException {
    messageInfo(" --- begin save --- ");
    Set<Class> todo = byClass.values().stream()
            .map((TypeEntry holder) -> holder.type.getClass())
            .collect(Collectors.toSet());

    Map<Class, Set<Class>> hooks = new HashMap<>();

    byClass.values().stream().forEach(value -> {
      final Set<Class> requiredTypes = value.type.getRequiredTypes();
      if (requiredTypes != null && !requiredTypes.isEmpty()) {
        requiredTypes.stream().map(requiredType -> {
          final Set<Class> c;
          if (!hooks.containsKey(requiredType)) {
            c = new HashSet<>();
            hooks.put(requiredType, c);
          } else {
            c = hooks.get(requiredType);
          }
          return c;
        }).forEach(c -> c.add(value.type.getClass()));
      }
    });

    Set<SourceEntry> entrys = new HashSet<>();

    while (!todo.isEmpty()) {
      for (TypeEntry holder : byClass.values()) {
        final Class<? extends Type> typeClass = holder.type.getClass();
        if (todo.contains(typeClass) && !hooks.containsKey(typeClass)) {
          holder.outAll(this, entrys);
          todo.remove(typeClass);
          final Set<Class> requiredTypes = holder.type.getRequiredTypes();
          requiredTypes.stream().filter(hooks::containsKey).forEach(requiredType -> {
            Set<Class> cs = hooks.get(requiredType);
            cs.remove(typeClass);
            if (cs.isEmpty()) {
              hooks.remove(requiredType);
            }
          });
        }
      }
    }

    entrys.addAll(unknownEntries);
    source.setContent(entrys);

    messageInfo(" --- end   save --- ");
  }

  public boolean containsFileSuffix(String fileSuffix) {
    return byFileSuffix.containsKey(fileSuffix);
  }

  public Type getByFileSuffix(String fileSuffix) {
    return byFileSuffix.get(fileSuffix).type;
  }

  public void clear() {
    byClass.values().forEach(TypeEntry::clear);
  }

  public Set<SourceEntry> getUnknownEntries() {
    return unknownEntries;
  }

  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  public Observer getObserver() {
    return observer;
  }

  void messageInfo(String msg) {
    try {
      if (observer != null) {
        observer.occurred(Level.INFO, msg);
      }
    } catch (Exception e) {
      Thread.getDefaultUncaughtExceptionHandler()
              .uncaughtException(Thread.currentThread(), e);
    }
  }

  void unknownEntryInfo(SourceEntry entry) {
    try {
      if (observer != null) {
        observer.occurred(Level.INFO, "Unknown Entry found", entry);
      }
    } catch (Exception e) {
      Thread.getDefaultUncaughtExceptionHandler()
              .uncaughtException(Thread.currentThread(), e);
    }
  }

  final Map<Class, TypeEntry> byClass = new HashMap<>();
  final Map<String, TypeEntry> byFileSuffix = new HashMap<>();
  final Set<SourceEntry> unknownEntries = new HashSet<>();
  Observer observer;
}
