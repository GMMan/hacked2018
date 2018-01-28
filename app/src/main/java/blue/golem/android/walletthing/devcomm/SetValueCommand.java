package blue.golem.android.walletthing.devcomm;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public class SetValueCommand extends Command {
    int currencyValue;

    public double getCurrencyValue() {
        return currencyValue / 100;
    }

    public void setCurrencyValue(double value) {
        currencyValue = (int)(value * 100);
    }

    public SetValueCommand() {
        id = 2;
    }

    @Override
    protected byte[] serializeParams() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream bw = new DataOutputStream(os);
        write7BitEncodedInt(bw, currencyValue);
        bw.flush();
        return os.toByteArray();
    }
}
