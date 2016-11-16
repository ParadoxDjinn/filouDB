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
import java.util.stream.Stream;

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
    register.register(TextType.TYPE);
  }

  public void save(SelfRestorable restorable) {
    REG.save(this.register, restorable);
  }

  public abstract void load() throws IOException;

  public abstract void save() throws IOException;

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
