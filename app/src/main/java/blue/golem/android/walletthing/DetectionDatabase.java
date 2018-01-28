package blue.golem.android.walletthing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyanic on 2018-01-28.
 */

public class DetectionDatabase {
    private static HashMap<String, Map<Integer, int[][]>> database;

    public static Map<String, Map<Integer, int[][]>> getDatabase() {
        if (database == null) {
            database = new HashMap<>();
            HashMap<Integer, int[][]> m = new HashMap<>();
            m.put(2000, new int[][] { { 0, 128, 254, 0 }});
            database.put("CAD", m);
        }
        return database;
    }

    public static Map<Integer, int[][]> getDatabaseForCurrency(String currency) {
        Map<String, Map<Integer, int[][]>> db = getDatabase();
        if (db.containsKey(currency))
            return db.get(currency);
        else
            return null;
    }
}
