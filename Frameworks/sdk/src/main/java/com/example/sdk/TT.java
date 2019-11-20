package com.example.sdk;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.heytap.openid.sdk.OpenIDSDK;

import java.util.logging.Logger;

public class TT {
    public static void tt(final Context context) {
        OpenIDSDK.init(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = SystemClock.currentThreadTimeMillis();
                if (OpenIDSDK.isSupported()) {
                    Log.d("hh-tag", "is Supported");
                    String t = OpenIDSDK.getOAID(context);
//                    String t = OpenIDSDK.getOAID(context);
                    Log.d("hh-tag", "time = " + (SystemClock.currentThreadTimeMillis() - time));
                    Log.d("hh-tag", "is Supported t = : " + t);
                } else {
                    Log.d("hh-tag", "is not Supported");
                }
            }
        }).start();
    }
}
