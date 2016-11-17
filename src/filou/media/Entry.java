/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filou.media;

import filou.util.ValueChangeEvent;
import filou.observe.ChangeListener;
import filou.observe.ChangeEvent;
import filou.observe.ChangeSupport;
import filou.observe.Observable;
import filou.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 *
 * @author dark
 * @param <V>
 */
public final class Entry<V> implements Observable<Entry<V>> {

  private final String key;
  private final String suffix;
  private final WeakReference<TypeEntry<V>> type;
  private V value;

  private final LocalDateTime creationTime;
  private LocalDateTime lastModifiedTime;
  private LocalDateTime lastAccessTime;

  private ChangeSupport<Entry<V>> changeSupport;
  private EntryChangeEvent changeEvent;

  Entry(SourceEntry entry, Register register, TypeEntry<V> type) throws IOException {
    this.key = entry.key;
    this.suffix = entry.fileSuffix;
    this.type = new WeakReference<>(type);
    this.value = type.type.in(register, key, suffix, entry.buffer.asInputStream());
    checkInit(value);
    this.creationTime = LocalDateTime.ofInstant(entry.creationTime, ZoneId.systemDefault());
    this.lastModifiedTime = LocalDateTime.ofInstant(entry.lastModifiedTime, ZoneId.systemDefault());
    this.lastAccessTime = LocalDateTime.ofInstant(entry.lastAccessTime, ZoneId.systemDefault());
  }

  Entry(String key, String suffix, V value, TypeEntry<V> type) {
    this.key = key;
    this.suffix = suffix;
    this.value = value;
    this.type = new WeakReference<>(type);
    checkInit(value);
    LocalDateTime now = LocalDateTime.now();
    this.creationTime = now;
    this.lastModifiedTime = now;
    this.lastAccessTime = now;
  }

  public String getKey() {
    return key;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setValue(V value) {
    LocalDateTime now = LocalDateTime.now();
    lastAccessTime = now;
    if (this.value != value) {
      if (changeEvent != null) {
        changeEvent.old = this.value;
      }
      this.value = value;
      lastModifiedTime = now;
      checkUninit(this.value);
      checkInit(this.value);
    }

    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public V getValue() {
    lastAccessTime = LocalDateTime.now();
    return value;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public LocalDateTime getLastModifiedTime() {
    return lastModifiedTime;
  }

  public LocalDateTime getLastAccessTime() {
    return lastAccessTime;
  }

  public Type<V> getType() {
    return type.get().type;
  }

  public Register getRegister() {
    return type.get().getRegister();
  }

  public boolean isDead() {
    TypeEntry<V> typeEntry = type.get();
    return typeEntry == null ? true : typeEntry.isDead();
  }

  public void in(Register register, InputStream stream) throws IOException {
    setValue(type.get().type.in(register, key, suffix, stream));
  }

  public void out(Register register, OutputStream stream) throws IOException {
    type.get().type.out(register, key, suffix, value, stream);
  }

  SourceEntry toSourceEntry(Register register) throws IOException {
    SourceEntry result = new SourceEntry(key, suffix,
            creationTime.toInstant(ZoneOffset.UTC),
            lastModifiedTime.toInstant(ZoneOffset.UTC),
            lastAccessTime.toInstant(ZoneOffset.UTC));
    out(register, result.buffer.asOutputStream());
    return result;
  }

  private void checkInit(V value) {
    if (value instanceof SelfObservable) {
      ((SelfObservable) value).init(this);
    }
  }

  private void checkUninit(V value) {
    if (value instanceof SelfObservable) {
      ((SelfObservable) value).uninit(this);
    }
  }

  @Override
  public <T extends ChangeEvent<Entry<V>>> void addListener(ChangeListener<Entry<V>, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<Entry<V>>> void removeListener(ChangeListener<Entry<V>, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  @Override
  public EntryChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new EntryChangeEvent();
    }
    return changeEvent;
  }

  public class EntryChangeEvent implements ValueChangeEvent<V, Entry<V>> {

    private V old;

    @Override
    public V getOldValue() {
      return old;
    }

    @Override
    public V getNewValue() {
      return value;
    }

    @Override
    public Entry<V> getObservable() {
      return Entry.this;
    }

  }
}
