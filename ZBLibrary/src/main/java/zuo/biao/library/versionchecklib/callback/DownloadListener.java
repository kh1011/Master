package zuo.biao.library.versionchecklib.callback;

import java.io.File;

/**
 * Created by allenliu on 2017/8/16.
 * xx
 */

public interface DownloadListener {
    void onCheckerDownloading(int progress);
    void onCheckerDownloadSuccess(File file);
    void onCheckerDownloadFail();
    void onCheckerStartDownload();
}
