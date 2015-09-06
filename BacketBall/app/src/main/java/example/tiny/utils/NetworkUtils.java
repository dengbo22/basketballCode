package example.tiny.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tiny on 15-9-6.
 */
public class NetworkUtils {
    //检查当前网络连接状态
    public static boolean  isOnline (Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnectedOrConnecting();
    }
}
