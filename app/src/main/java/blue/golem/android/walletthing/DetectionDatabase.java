package blue.golem.android.walletthing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyanic on 2018-01-28.
 */

public class DetectionDatabase {
    private static HashMap<String, int[][][]> database;

    public static Map<String, int[][][]> getDatabase() {
        if (database == null) {
            database = new HashMap<>();
        }
        return database;
    }
}
