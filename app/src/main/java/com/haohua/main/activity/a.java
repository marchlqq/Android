package com.haohua.main.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class a {
    private static final Pattern c = Pattern.compile("^([0-9A-F]{2}:){5}([0-9A-F]{2})$");
    private static final Pattern d = Pattern.compile("[0-3][0-9a-f]{24,32}");
    private static final Pattern e = Pattern.compile("[0-3][0-9a-f]{32}");
    static TelephonyManager a;
    static String b;
    private static String f = null;

    public static void a(Context var0) {
        a = (TelephonyManager)var0.getSystemService("phone");
    }

    public static synchronized String b(Context var0) {
        if (b == null) {
            b = h(var0);
        }

        return b;
    }

    public static String c(Context var0) {
        try {
            return Settings.Secure.getString(var0.getContentResolver(), "android_id");
        } catch (Throwable var2) {
            return null;
        }
    }

    public static String d(Context var0) {
        try {
            if (com.mrocker.push.c.d.a(23) && var0.checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
                return null;
            }

            if (com.mrocker.push.c.d.a(var0, "android.permission.READ_PHONE_STATE")) {
                if (a == null) {
                    a(var0);
                }

                String var1 = null;
                JSONArray var2 = com.mrocker.push.c.b.a(var0);
                if (var2 != null && var2.length() == 2) {
                    try {
                        var1 = var2.getJSONObject(1).getString("imei");
                    } catch (Exception var4) {
                    }
                }

                if (var1 == null) {
                    var1 = a.getDeviceId();
                }

                return var1;
            }
        } catch (Throwable var5) {
        }

        return null;
    }

    public static String e(Context var0) {
        if (!com.mrocker.push.c.d.a) {
            return null;
        } else {
            String var1 = null;

            try {
                if (com.mrocker.push.c.d.a(var0, "android.permission.ACCESS_WIFI_STATE")) {
                    WifiManager var2 = (WifiManager)var0.getSystemService("wifi");
                    if (var2 != null && var2.isWifiEnabled()) {
                        WifiInfo var3 = var2.getConnectionInfo();
                        if (var3 != null) {
                            var1 = var3.getMacAddress();
                            if (var1 != null) {
                                var1 = var1.toUpperCase().trim();
                                if ("00:00:00:00:00:00".equals(var1) || !c.matcher(var1).matches()) {
                                    var1 = null;
                                }
                            }
                        }
                    }
                }
            } catch (Throwable var4) {
            }

            return var1;
        }
    }

    public static String h(Context var0) {
        String var1 = f(var0);
//        {
//            // test
//            var1 = null;
//        }
        String var2 = a();
        boolean var3 = b();
        String var4 = a(var0, var3);
//        {
//            // test
//            var4 = null;
//        }
        String[] var5 = new String[]{var1, var2, var4};
        String var6 = null;
        String[] var7 = var5;
        int var8 = var5.length;



        int var9;
        String var10;
        for(var9 = 0; var9 < var8; ++var9) {
            var10 = var7[var9];
            if (!com.mrocker.push.c.d.a(var10) && e.matcher(var10).matches()) {
                var6 = var10;
                break;
            }
        }

        if (com.mrocker.push.c.d.a(var6) && !com.mrocker.push.c.d.a(var1) && Math.random() < 0.99D) {
            var7 = var5;
            var8 = var5.length;

            for(var9 = 0; var9 < var8; ++var9) {
                var10 = var7[var9];
                if (!com.mrocker.push.c.d.a(var10) && d.matcher(var10).matches()) {
                    var6 = var10;
                    break;
                }
            }
        }

        if (com.mrocker.push.c.d.a(var6)) {
            var6 = i(var0);
        }

        if (!var6.equals(var1)) {
            b(var0, var6);
        }

        if (!var6.equals(var4)) {
            a(var0, var6, var3);
        }

        if (!var6.equals(var2)) {
            a(var0, var6);
        }

        return var6;
    }

    static String a(Context var0, boolean var1) {
        if (com.mrocker.push.c.d.a(23) && var0.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return null;
        } else {
            String var2 = Environment.getExternalStorageState();
            if ("mounted".equals(var2)) {
                String var3 = a(new File(Environment.getExternalStorageDirectory(), var1 ? ".tcookieid" : ".tcookieid" + g(var0)));
                if (com.mrocker.push.c.d.a(var3)) {
                    var3 = a(new File(Environment.getExternalStorageDirectory(), ".tid" + g(var0)));
                }

                return var3;
            } else {
                return "";
            }
        }
    }

    static String a() {
        String var0 = null;
        File[] var1 = (new File("/")).listFiles();
        if (var1 != null && var1.length != 0) {
            File[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                File var5 = var2[var4];
                if (var5.isDirectory() && !"/sdcard".equals(var5.getAbsolutePath())) {
                    if (var5.canWrite()) {
                        var0 = a(new File(var5, ".tcookieid"));
                        if (!com.mrocker.push.c.d.a(var0)) {
                            return var0;
                        }
                    }

                    if (var5.listFiles() != null) {
                        File[] var6 = var5.listFiles();
                        int var7 = var6.length;

                        for(int var8 = 0; var8 < var7; ++var8) {
                            File var9 = var6[var8];
                            if (var9.isDirectory()) {
                                var0 = a(new File(var9, ".tcookieid"));
                                if (!com.mrocker.push.c.d.a(var0)) {
                                    return var0;
                                }
                            }
                        }
                    }
                }
            }

            return var0;
        } else {
            return var0;
        }
    }

    private static String a(File var0) {
        try {
            if (var0.exists() && var0.canRead()) {
                FileInputStream var1 = new FileInputStream(var0);
                byte[] var2 = new byte[128];
                int var3 = var1.read(var2);
                var1.close();
                return new String(var2, 0, var3);
            }
        } catch (Throwable var4) {
        }

        return null;
    }

    static String f(Context var0) {
        String var1 = com.mrocker.push.c.c.a(var0, "tdid", "pref.deviceid.key", (String)null);
        if (com.mrocker.push.c.d.a(var1)) {
            SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var0);
            var1 = var2.getString("pref.deviceid.key", (String)null);
        }

        return var1;
    }

    private static void a(Context var0, String var1, boolean var2) {
        a(new File(Environment.getExternalStorageDirectory(), var2 ? ".tcookieid" : ".tcookieid" + g(var0)), var1);
    }

    private static void a(Context var0, String var1) {
        File[] var2 = (new File("/")).listFiles();
        if (var2 != null && var2.length != 0) {
            File[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File var6 = var3[var5];
                if (var6.isDirectory() && !"/sdcard".equals(var6.getAbsolutePath())) {
                    if (var6.canWrite() && !(new File(var6, ".tcookieid" + g(var0))).exists()) {
                        a(new File(var6, ".tcookieid"), var1);
                    }

                    if (var6.listFiles() != null) {
                        File[] var7 = var6.listFiles();
                        int var8 = var7.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                            File var10 = var7[var9];
                            if (var10.isDirectory() && var10.canWrite() && !(new File(var10, ".tcookieid" + g(var0))).exists()) {
                                a(new File(var10, ".tcookieid"), var1);
                            }
                        }
                    }
                }
            }

        }
    }

    private static void a(File var0, String var1) {
        try {
            FileOutputStream var2 = new FileOutputStream(var0);
            var2.write(var1.getBytes());
            var2.close();
            if (com.mrocker.push.c.d.a(9)) {
                Method var3 = var0.getClass().getMethod("setReadable", Boolean.TYPE, Boolean.TYPE);
                var3.invoke(var0, true, false);
            } else {
                Runtime.getRuntime().exec("chmod 444 " + var0.getAbsolutePath());
            }
        } catch (Throwable var4) {
        }

    }

    private static void b(Context var0, String var1) {
        try {
            SharedPreferences var2 = var0.getSharedPreferences("tdid", 0);
            if (var2 != null) {
                SharedPreferences.Editor var3 = var2.edit();
                var3.putString("pref.deviceid.key", var1);
                var3.commit();
            }
        } catch (Throwable var4) {
        }

    }

    private static String i(Context var0) {
        String var1 = j(var0);
        var1 = "3" + com.mrocker.push.c.d.b(var1);
        return var1;
    }

    private static String j(Context var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append(d(var0)).append('-').append(e(var0)).append('-').append(c(var0));
        return var1.toString();
    }

    static boolean b() {
        boolean var0 = true;

        try {
            if (com.mrocker.push.c.d.a(9)) {
                var0 = (Boolean)Environment.class.getMethod("isExternalStorageRemovable").invoke((Object)null);
            }
        } catch (Throwable var2) {
        }

        return !var0;
    }

    static String g(Context var0) {
        if (f == null) {
            try {
                SensorManager var1 = (SensorManager)var0.getSystemService("sensor");
                List var2 = var1.getSensorList(-1);
                Sensor[] var3 = new Sensor[64];
                Iterator var4 = var2.iterator();

                while(var4.hasNext()) {
                    Sensor var5 = (Sensor)var4.next();
                    if (var5.getType() < var3.length && var5.getType() >= 0) {
                        var3[var5.getType()] = var5;
                    }
                }

                StringBuffer var7 = new StringBuffer();

                for(int var8 = 0; var8 < var3.length; ++var8) {
                    if (var3[var8] != null) {
                        var7.append(var8).append('.').append(var3[var8].getVendor()).append('-').append(var3[var8].getName()).append('-').append(var3[var8].getVersion()).append('\n');
                    }
                }

                f = String.valueOf(var7.toString().hashCode());
            } catch (Throwable var6) {
            }
        }

        return f;
    }
}
