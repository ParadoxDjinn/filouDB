/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filou.media.types;

import filou.media.Register;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dark
 */
public class TextType extends DataType<String> {

  public static final TextType TYPE = new TextType();

  private final Set<String> fileSuffixes = new HashSet<>();

  public TextType() {
    fileSuffixes.add("txt");
    fileSuffixes.add("htm");
    fileSuffixes.add("html");
  }

  public TextType(String... suffixes) {
    fileSuffixes.addAll(Arrays.asList(suffixes));
  }

  @Override
  public String in(Register register, String key, String fileSuffix,
          DataInputStream stream) throws IOException {
    return stream.readUTF();
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          String value, DataOutputStream stream) throws IOException {
    stream.writeUTF(value);
  }

  @Override
  public Class<String> getKey() {
    return String.class;
  }

  @Override
  public String getDefaultFileSuffix() {
    return "txt";
  }

  @Override
  public Set<String> getFileSuffixes() {
    return Collections.unmodifiableSet(fileSuffixes);
  }

}
