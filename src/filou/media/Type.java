package filou.media;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author dark
 * @param <V> value
 */
public abstract class Type<V> {

  public abstract Class<V> getKey();

  public Set<String> getFileSuffixes() {
    return Collections.singleton(getDefaultFileSuffix());
  }

  public abstract String getDefaultFileSuffix();

  public Set<Class<? extends Type>> getRequiredTypes() {
    return Collections.emptySet();
  }

  public abstract V in(Register register, String key, String fileSuffix, InputStream stream) throws IOException;

  public abstract void out(Register register, String key, String fileSuffix, V value, OutputStream stream) throws IOException;

  @Override
  public String toString() {
    return getClass().getName() + " ( " + getKey().getName() + " )";
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.getKey());
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Type other = (Type) obj;
    return Objects.equals(this.getKey(), other.getKey());
  }
}
