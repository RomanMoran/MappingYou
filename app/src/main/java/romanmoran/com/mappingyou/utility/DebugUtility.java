package romanmoran.com.mappingyou.utility;

import android.util.Log;

/**
 * Created by roman on 02.08.2017.
 */

public class DebugUtility {
    private static final String PRE_TAG = "TEST";

    public static void logTest(String tag, String msg) {
        tag = PRE_TAG + " " + tag;
        Log.e(tag, msg);
    }

}
