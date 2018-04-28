package zuo.biao.library.versionchecklib.utils;

import android.util.Log;

import zuo.biao.library.versionchecklib.core.AllenChecker;

/**
 * Created by allenliu on 2017/8/16.
 * xx
 */

public class ALog {
    public static void e(String msg) {
        if (AllenChecker.isDebug()) {
            if (msg != null && !msg.isEmpty())
                Log.e("Allen Checker", msg);
        }
    }
}
