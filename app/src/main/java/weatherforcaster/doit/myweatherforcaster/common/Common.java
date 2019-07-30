package weatherforcaster.doit.myweatherforcaster.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Common {
    private Common() {
    }

    public static final String SHARED_PREFERANCE_NAME = "Settings";
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int INTERVAL_ONCE = 36000000;
    public static final int INTERVAL_TWICE = 86400000;
    public static final int INTERVAL_TRICE = 86400000;

    public static boolean checkNetworkConnectionStatus(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else return false;
    }
}
