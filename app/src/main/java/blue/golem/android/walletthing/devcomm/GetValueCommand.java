package blue.golem.android.walletthing.devcomm;

import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public class GetValueCommand extends Command {
    public GetValueCommand() {
        id = 3;
    }

    @Override
    protected byte[] serializeParams() throws IOException {
        return new byte[0];
    }
}
