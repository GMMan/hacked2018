package blue.golem.android.walletthing.devcomm;

import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public class ResponseParser {
    public static Response parse(byte[] data) throws IOException {
        byte[] frameBound = Command.FRAME_BOUND;
        for (int i = 0; i < data.length - frameBound.length; ++i) {
            boolean validFrame = true;
            for (int j = 0; j < frameBound.length; ++j) {
                if (data[i + j] != frameBound[j]) {
                    validFrame = false;
                    break;
                }
            }
            if (validFrame) {
                byte respId = data[i + frameBound.length];
                Response resp = makeResponse(respId);
                if (resp == null) return null;
                byte lenHi = data[i + frameBound.length + 1];
                byte lenLo = data[i + frameBound.length + 2];
                int len = (lenHi << 8) | lenLo;
                byte[] paramData = new byte[len];
                System.arraycopy(data, i + frameBound.length + 3, paramData, 0, len);
                byte parity = Command.calculateParity(paramData);
                if (parity != data[i + frameBound.length + 3 + len])
                    return null;
                if (!resp.deserialize(paramData))
                    return null;
                return resp;
            }
        }
        return null;
    }

    private static Response makeResponse(byte id) {
        switch (id) {
            case GetValueResponse.ID:
                return new GetValueResponse();
            default:
                return null;
        }
    }
}
