package filou.entries;

import filou.observe.*;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author dark
 */
public final class CharEntry extends ValueEntry<Character, CharEntry> {

  private ChangeSupport<CharEntry> changeSupport;
  private final Descriptor descriptor;
  private char value;

  public CharEntry(Descriptor descriptor) {
    this(descriptor, ' ');
  }

  public CharEntry(Descriptor descriptor, char value) {
    descriptor.checkType(Type.Char);
    this.descriptor = descriptor;
    this.value = value;
  }

  @Override
  public <T extends ChangeEvent<CharEntry>> void addListener(ChangeListener<CharEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<CharEntry>> void removeListener(ChangeListener<CharEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void set(char value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public char get() {
    return value;
  }

  @Override
  public void setValue(Character value) {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = value;
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public Character getValue() {
    return value;
  }

  @Override
  public CharEntry copy() {
    return new CharEntry(descriptor, value);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeChar(value);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    if (changeEvent != null) {
      changeEvent.old = this.value;
    }
    this.value = input.readChar();
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Character.toString(value);
  }

  @Override
  public CharChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new CharChangeEvent();
    }
    return changeEvent;
  }
  private CharChangeEvent changeEvent;

  public class CharChangeEvent implements ValueChangeEvent<Character, CharEntry> {

    private char old;

    public CharChangeEvent() {
    }

    public char getOldChar() {
      return old;
    }

    public char getNewChar() {
      return value;
    }

    @Override
    public Character getOldValue() {
      return old;
    }

    @Override
    public Character getNewValue() {
      return value;
    }

    @Override
    public CharEntry getObservable() {
      return CharEntry.this;
    }

  }
}
