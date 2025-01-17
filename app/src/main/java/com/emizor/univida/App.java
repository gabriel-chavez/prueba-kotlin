package com.emizor.univida;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.emizor.univida.utils.AidlUtil;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class App extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);

        //RedEnlaceSDK.init(developerToken, this, true);
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("d29f2d04-c076-44d0-9f2f-fbf97fc08120")
                .withLogs()
                .withCrashReporting(true)
                .build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
//        // Automatic tracking of user activity.
//        YandexMetrica.enableActivityAutoTracking(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
