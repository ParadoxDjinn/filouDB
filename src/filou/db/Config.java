package filou.db;

import filou.log.Level;
import filou.log.Observer;
import filou.media.Register;
import filou.media.Type;
import java.util.*;

/**
 *
 * @author dark
 */
public final class Config {

  public static final String CONFIG_KEY_FILE = "db";
  public static final String CONFIG_KEY_USED_TYPES = "used.types";

  public Properties properties = new Properties();
  private final Register register;

  Config(Register register) {
    this.register = register;
  }

  /**
   *
   * @return has changed
   */
  boolean load() {
    boolean hasChanged = false;
    if (register.contains(CONFIG_KEY_FILE, Properties.class)) {
      properties = register.get(CONFIG_KEY_FILE, Properties.class);
      String usedTypes = properties.getProperty(CONFIG_KEY_USED_TYPES, "");
      if (!usedTypes.isEmpty()) {
        HashSet<Class> types = new HashSet<>();
        for (String t : usedTypes.split(",")) {
          final String className = t.trim();
          if (!className.isEmpty()) {
            try {
              types.add(Class.forName(className));
            } catch (ClassNotFoundException ex) {
              wrnException("type class not found: " + className, ex);
            }
          }
        }
        while (!types.isEmpty()) {
          for (Iterator<Class> iterator = types.iterator(); iterator.hasNext();) {
            Class next = iterator.next();
            Type type = tryCreateType(next);
            if (type != null && !register.isRegisted(type)) {
              hasChanged = tryRegister(type) || hasChanged;
            }
            iterator.remove();
          }
        }
      }
    } else {
      infoMessage("No DB config found / not a regulary filouDB DataBase");
    }
    return hasChanged;
  }

  public boolean tryCreateAndRegister(Iterable<Class> classes) {
    boolean result = true;
    for (Class next : classes) {
      Type type = tryCreateType(next);
      result = tryRegister(type) && result;
    }
    return result;
  }

  private boolean tryRegister(Type type) {
    if (type == null || register.isRegisted(type)) {
      return true;
    } else if (register.isBlocked(type)) {
      wrnMessage("type is blocked: " + type);
      return false;
    } else {
      Set<Class> requiredTypes = type.getRequiredTypes();
      if (requiredTypes.isEmpty()) {
        register.register(type);
        return true;
      } else if (tryCreateAndRegister(requiredTypes)) {
        register.register(type);
        return true;
      } else {
        wrnMessage("truble with required types: " + type);
        return false;
      }
    }
  }

  private Type tryCreateType(Class c) {
    try {
      Object newInstance = c.newInstance();
      if (newInstance instanceof Type) {
        return (Type) newInstance;
      } else {
        wrnMessage("class is not a instace of type");
      }
    } catch (InstantiationException | IllegalAccessException ex) {
      wrnException("could not create a instance of type: " + c.getName(), ex);
    }
    return null;
  }

  private void infoMessage(String msg) {
    Observer observer = register.getObserver();
    if (observer != null) {
      observer.occurred(Level.INFO, msg);
    }
  }

  private void wrnMessage(String msg) {
    Observer observer = register.getObserver();
    if (observer != null) {
      observer.occurred(Level.WARNING, msg);
    }
  }

  private void wrnException(String msg, Exception exception) {
    Observer observer = register.getObserver();
    if (observer != null) {
      observer.occurred(Level.WARNING, msg, exception);
    }
  }

  void save() {
    properties.setProperty(CONFIG_KEY_USED_TYPES, register.types()
            .collect(StringBuilder::new, (builder, type)
                    -> builder.append(type.getClass().getName()).append(", "),
                    StringBuilder::append).toString());
    register.set(CONFIG_KEY_FILE, Properties.class, properties);
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public Properties properties() {
    return properties;
  }
}
