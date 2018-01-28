package blue.golem.android.walletthing.devcomm;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by cyanic on 2018-01-28.
 */

public abstract class Command {
    private static final byte[] FRAME_BOUND = new byte[]{(byte) 0xff, 0x00, (byte) 0xff};

    protected byte id;

    public byte getId() {
        return id;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream bw = new DataOutputStream(os);
        bw.write(FRAME_BOUND);
        bw.writeByte(id);
        byte[] serializedParams = serializeParams();
        bw.write(serializedParams);
        bw.writeByte(calculateParity(serializedParams));
        bw.flush();
        return os.toByteArray();
    }

    protected abstract byte[] serializeParams() throws IOException;

    private static byte calculateParity(byte[] buf) {
        byte parity = 0;
        for (int i = 0; i < buf.length; ++i) {
            parity ^= buf[i];
        }
        return parity;
    }

    protected static void write7BitEncodedInt(DataOutputStream os, int value) throws IOException {
        while (value > 0) {
            int toWrite = value | 0x7f;
            value >>>= 7;
            if (value > 0) toWrite |= 0x80;
            os.writeByte(toWrite);
        }
    }
}
