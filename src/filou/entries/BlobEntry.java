package filou.entries;

import filou.observe.ChangeListener;
import filou.observe.ChangeEvent;
import filou.observe.ChangeSupport;
import filou.io.DataUtil;
import filou.media.Register;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author dark
 */
public final class BlobEntry extends Entry<BlobEntry> {

  private ChangeSupport<BlobEntry> changeSupport;
  private byte[] data;
  private final Descriptor descriptor;

  public BlobEntry(Descriptor descriptor) {
    descriptor.checkType(Type.Blob);
    this.descriptor = descriptor;
    this.data = new byte[0];
  }

  public BlobEntry(Descriptor descriptor, byte[] data) {
    descriptor.checkType(Type.Blob);
    this.descriptor = descriptor;
    this.data = DataUtil.copyBlob(data);
  }

  @Override
  public <T extends ChangeEvent<BlobEntry>> void addListener(ChangeListener<BlobEntry, T> listener) {
    changeSupport = ChangeSupport.addListener(changeSupport, this, listener);
    changeEvent();
  }

  @Override
  public <T extends ChangeEvent<BlobEntry>> void removeListener(ChangeListener<BlobEntry, T> listener) {
    changeSupport = ChangeSupport.removeListener(changeSupport, listener);
    if (changeSupport == null) {
      changeEvent = null;
    }
  }

  public void setData(byte[] data) {
    this.data = DataUtil.copyBlob(data);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  public byte[] getData() {
    return DataUtil.copyBlob(data);
  }

  @Override
  public Descriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public BlobEntry copy() {
    return new BlobEntry(descriptor, data);
  }

  @Override
  public void out(Register register, DataOutput output) throws IOException {
    output.writeInt(data.length);
    output.write(data);
  }

  @Override
  public void in(Register register, DataInput input) throws IOException {
    data = new byte[input.readInt()];
    input.readFully(data);
    ChangeSupport.fireChangedEvent(changeSupport);
  }

  @Override
  public String toString() {
    return Arrays.toString(data);
  }

  @Override
  public BlobChangeEvent changeEvent() {
    if (changeEvent == null) {
      changeEvent = new BlobChangeEvent();
    }
    return changeEvent;
  }
  private BlobChangeEvent changeEvent;

  public class BlobChangeEvent implements ChangeEvent<BlobEntry> {

    public BlobChangeEvent() {
    }

    @Override
    public BlobEntry getObservable() {
      return BlobEntry.this;
    }

  }

}
