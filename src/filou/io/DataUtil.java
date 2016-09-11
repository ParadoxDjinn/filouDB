package filou.io;

import java.io.*;
import java.time.*;

/**
 *
 * @author dark
 */
public final class DataUtil {

  @FunctionalInterface
  public interface Handler<V> {

    public void handle(V value) throws IOException;
  }

  @FunctionalInterface
  public interface BiHandler<V, T> {

    public void handle(V value, T value2) throws IOException;
  }

  @FunctionalInterface
  public interface Actor<V, R> {

    public R act(V value) throws IOException;
  }

  public static void load(Data dio, File file) throws IOException {
    load(dio, new FileInputStream(file));
  }

  public static void save(Data dio, File file) throws IOException {
    save(dio, new FileOutputStream(file));
  }

  public static void load(Data dio, InputStream stream) throws IOException {
    load(dio.io(), stream);
  }

  public static void save(Data dio, OutputStream stream) throws IOException {
    save(dio.io(), stream);
  }

  public static void load(InputData data, File file) throws IOException {
    load(data, new FileInputStream(file));
  }

  public static void save(OutputData data, File file) throws IOException {
    save(data, new FileOutputStream(file));
  }

  public static void load(InputData data, InputStream stream) throws IOException {
    try (DataInputStream is = new DataInputStream(stream)) {
      data.in(is);
    }
  }

  public static void save(OutputData data, OutputStream stream) throws IOException {
    try (DataOutputStream os = new DataOutputStream(stream)) {
      data.out(os);
    }
  }

  public static <V> V load(InputStream stream,
          Actor<DataInput, V> convert) throws IOException {
    try (DataInputStream is = new DataInputStream(stream)) {
      return convert.act(is);
    }
  }

  public static <V> void save(OutputStream stream,
          V value, BiHandler<V, DataOutput> consumer) throws IOException {
    try (DataOutputStream os = new DataOutputStream(stream)) {
      consumer.handle(value, os);
    }
  }

  public static Handler<InputData> load(File file) throws IOException {
    return data -> load(data, new FileInputStream(file));
  }

  public static Handler<OutputData> save(File file) throws IOException {
    return data -> save(data, new FileOutputStream(file));
  }

  public static Handler<InputData> load(InputStream stream) throws IOException {
    return data -> load(data, stream);
  }

  public static Handler<OutputData> save(OutputStream stream) throws IOException {
    return data -> save(data, stream);
  }

  public static void writeDateTime(DataOutput output, LocalDateTime dateTime) throws IOException {
    output.writeLong(dateTime.toLocalDate().toEpochDay());
    output.writeLong(dateTime.toLocalTime().toNanoOfDay());
  }

  public static LocalDateTime readDateTime(DataInput input) throws IOException {
    return LocalDateTime.of(
            LocalDate.ofEpochDay(input.readLong()),
            LocalTime.ofNanoOfDay(input.readLong()));
  }

  public static byte[] copyBlob(byte[] blob) {
    final int size = blob.length;
    byte[] copy = new byte[size];
    System.arraycopy(blob, 0, copy, 0, size);
    return copy;
  }

  /**
   * Returns an array with two strings. The first is the filename.
   * The second is the filesuffix.
   *
   * @param longName
   * @return name and suffix of the file
   */
  public static String[] splitFileName(String longName) {
    int lastIndexOfDot = longName.lastIndexOf('.');
    final String fileSuffix;
    final String name;
    if (lastIndexOfDot == -1 || lastIndexOfDot == 0
            || lastIndexOfDot == longName.length() - 1) {
      name = longName;
      fileSuffix = "";
    } else {
      name = longName.substring(0, lastIndexOfDot);
      fileSuffix = longName.substring(lastIndexOfDot + 1);
    }
    return new String[]{name, fileSuffix};
  }

  private DataUtil() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

}
