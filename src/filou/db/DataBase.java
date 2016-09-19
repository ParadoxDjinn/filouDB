package filou.db;

import filou.entries.ArrayEntry;
import filou.entries.StructEntry;
import filou.log.Observer;
import filou.media.REG;
import filou.media.Register;
import filou.media.Source;
import filou.media.types.*;
import filou.util.*;
import filou.util.Descriptor;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import javafx.util.Pair;

/**
 *
 * @author dark
 */
public abstract class DataBase {

  protected final Register register = new Register();
  protected final Config config = new Config(register);

  public DataBase() {
    register.register(PropertiesType.TYPE);
    register.register(DescriptorType.TYPE);
    register.register(EntryType.TYPE);
  }

  public final void add(Storable storable) {
    REG.setEntry(register, UUID.randomUUID().toString(),
            (Entry) storable.save(register));
  }

  public final void set(String key, Storable storable) {
    REG.setEntry(register, key, (Entry) storable.save(register));
  }

  public final void get(String key, Loadable loadable) {
    if (REG.containsEntry(register, key)) {
      loadable.load(register, REG.getEntry(register, key));
    }
  }

  public boolean contains(String key) {
    return REG.containsEntry(register, key);
  }

  public abstract void load() throws IOException;

  public abstract void save() throws IOException;

  public final Stream<Pair<String, Entry>> stream(Descriptor descriptor) {
    return REG.entryStream(register)
            .filter(pair -> pair.getValue().getDescriptor().equals(descriptor));
  }

  public final Stream<Pair<String, ArrayEntry>> streamArray(Descriptor descriptor) {
    descriptor.checkType(Type.Array);
    return REG.entryStream(register)
            .filter(pair -> pair.getValue().getDescriptor().equals(descriptor))
            .map(pair -> new Pair<>(pair.getKey(), (ArrayEntry) pair.getValue()));
  }

  public final Stream<Pair<String, StructEntry>> streamStruct(Descriptor descriptor) {
    descriptor.checkType(Type.Struct);
    return REG.entryStream(register)
            .filter(pair -> pair.getValue().getDescriptor().equals(descriptor))
            .map(pair -> new Pair<>(pair.getKey(), (StructEntry) pair.getValue()));
  }

  public final void setObserver(Observer observer) {
    register.setObserver(observer);
  }

  public final Observer getObserver() {
    return register.getObserver();
  }

  public final Register getRegister() {
    return register;
  }

  public final Config getConfig() {
    return config;
  }

  public final void clear() {
    register.clear();
  }

  public final void load(Source source) throws IOException {
    do {
      register.load(source);
    } while (config.load());
  }

  public final void save(Source source) throws IOException {
    config.save();
    register.save(source);
  }

}
