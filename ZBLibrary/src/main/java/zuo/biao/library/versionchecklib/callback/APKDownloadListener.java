package zuo.biao.library.versionchecklib.callback;

import java.io.File;

/**
 * Created by allenliu on 2017/8/16.
 * xx
 */

public interface APKDownloadListener {
     void onDownloading(int progress);
    void onDownloadSuccess(File file);
    void onDownloadFail();
}
