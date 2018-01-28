package blue.golem.android.walletthing.devcomm;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by cyanic on 2018-01-28.
 */

public class SetDatabaseCommand extends Command {
    private int[][][] database;
    private int conversionRate;

    public SetDatabaseCommand() {
        id = 1;
    }

    public int[][][] getDatabase() {
        return database;
    }

    public void setDatabase(int[][][] value) {
        database = value;
    }

    public double getConversionRate() {
        return conversionRate / 100.0;
    }

    public void setConversionRate(float value) {
        conversionRate = (int) (value * 100);
    }

    @Override
    protected byte[] serializeParams() throws IllegalStateException, IOException {
        if (database == null) throw new IllegalStateException("No database specified.");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream bw = new DataOutputStream(os);
        write7BitEncodedInt(bw, conversionRate);
        bw.writeByte(database.length);
        bw.writeByte(database[0].length);
        bw.writeByte(database[0][0].length);
        for (int[][] x : database) {
            for (int[] y : x) {
                for (int z : y) {
                    bw.writeByte(z);
                }
            }
        }

        bw.flush();
        return os.toByteArray();
    }
}
