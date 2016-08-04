package panda.li.leavemanage.activity.Base;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by xueli on 2016/7/19.
 */
public class BaseApp extends Application {

    private static BaseApp instance;
    public static synchronized BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
