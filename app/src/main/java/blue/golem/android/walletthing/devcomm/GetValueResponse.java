package blue.golem.android.walletthing.devcomm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public class GetValueResponse extends Response {
    public static final byte ID = 3;

    private int val;

    public double getValue() {
        return val / 100.0;
    }

    public GetValueResponse() {
        id = ID;
    }

    @Override
    public boolean deserialize(byte[] data) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        DataInputStream br = new DataInputStream(is);
        val = read7BitEncodedInt(br);
        br.close();
        is.close();
        return true;
    }
}
