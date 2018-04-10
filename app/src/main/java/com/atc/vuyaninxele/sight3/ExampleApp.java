package com.atc.vuyaninxele.sight3;

/**
 * Created by vuyani.nxele on 2018/03/19.
 */

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class ExampleApp extends Application {


    @Override public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

}