package weatherforcaster.doit.myweatherforcaster.shared.configurations;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Common {
    private Common() {
    }

    public static final String SHARED_PREFERANCE_NAME = "Settings";

    public static boolean checkNetworkConnectionStatus(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else return false;
    }
}
