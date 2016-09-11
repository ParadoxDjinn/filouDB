package filou.media.types;

import filou.media.Register;
import filou.media.Type;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author dark
 */
public class ImageType extends Type<Image> {

  public static final ImageType TYPE = new ImageType();
  private final Set<String> fileSuffixes;

  public ImageType() {
    HashSet<String> fileSuffixesTemp = new HashSet<>();
    fileSuffixesTemp.addAll(Arrays.asList(ImageIO.getReaderFileSuffixes()));
    fileSuffixesTemp.retainAll(Arrays.asList(ImageIO.getWriterFileSuffixes()));
    this.fileSuffixes = Collections.unmodifiableSet(fileSuffixesTemp);
  }

  @Override
  public Class<Image> getKey() {
    return Image.class;
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
  public Image in(Register register, String key, String fileSuffix,
          InputStream stream) throws IOException {
    return SwingFXUtils.toFXImage(ImageIO.read(stream), null);
  }

  @Override
  public void out(Register register, String key, String fileSuffix,
          Image value, OutputStream stream) throws IOException {
    ImageIO.write(SwingFXUtils.fromFXImage(value, null), fileSuffix, stream);
  }

}
