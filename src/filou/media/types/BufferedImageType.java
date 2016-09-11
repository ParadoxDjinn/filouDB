package filou.media.types;

import filou.media.Register;
import filou.media.Type;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author dark
 */
public class BufferedImageType extends Type<BufferedImage> {

  public static final BufferedImageType TYPE = new BufferedImageType();
  private final Set<String> fileSuffixes;

  public BufferedImageType() {
    HashSet<String> fileSuffixesTemp = new HashSet<>();
    fileSuffixesTemp.addAll(Arrays.asList(ImageIO.getReaderFileSuffixes()));
    fileSuffixesTemp.retainAll(Arrays.asList(ImageIO.getWriterFileSuffixes()));
    this.fileSuffixes = Collections.unmodifiableSet(fileSuffixesTemp);
  }

  @Override
  public Class<BufferedImage> getKey() {
    return BufferedImage.class;
  }

  @Override
  public String getDefaultFileSuffix() {
    return fileSuffixes.contains("png")//TODO: unschöne lösung !!!
            ? "png" : fileSuffixes.iterator().next();
  }

  @Override
  public Set<String> getFileSuffixes() {
    return fileSuffixes;
  }

  @Override
  public BufferedImage in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    return ImageIO.read(stream);
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          BufferedImage value, OutputStream stream) throws IOException {
    ImageIO.write(value, fileSuffix, stream);
  }

}
