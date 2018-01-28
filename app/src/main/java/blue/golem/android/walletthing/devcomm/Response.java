package blue.golem.android.walletthing.devcomm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public abstract class Response {
    protected byte id;

    public byte getId() {
        return id;
    }

    public abstract boolean deserialize(byte[] data) throws IOException;

    protected static int read7BitEncodedInt(DataInputStream br) throws IOException {
        int i = 0;
        byte b;
        do {
            i <<= 7;
            b = br.readByte();
            i |= b & 0x7f;
        } while ((b & 0x80) != 0);
        return i;
    }
}
