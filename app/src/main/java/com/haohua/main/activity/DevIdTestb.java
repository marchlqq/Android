package com.haohua.main.activity;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.mrocker.push.c.d;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DevIdTestb {
    private static final String[] a = new String[]{"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO_0", "EVDO_A", "1xRTT", "HSDPA", "HSUPA", "HSPA", "IDEN", "EVDO_B", "LTE", "EHRPD", "HSPAP"};
    private static final String[] b = new String[]{"NONE", "GSM", "CDMA", "SIP"};

    private static String a(int var0) {
        return var0 >= 0 && var0 < a.length ? a[var0] : String.valueOf(var0);
    }

    private static String b(int var0) {
        return var0 >= 0 && var0 < b.length ? b[var0] : String.valueOf(var0);
    }

    public static JSONArray a(Context var0) {
        JSONArray var1 = new JSONArray();

        try {
            TelephonyManager var2 = (TelephonyManager)var0.getSystemService("phone");
            ArrayList var3 = new ArrayList();
            JSONObject var5;
            if (d.a(22)) {
                SubscriptionManager var4 = (SubscriptionManager)var0.getSystemService("telephony_subscription_service");

                try {
                    var5 = a(var2, var4, 0);
                    var5.put("imei", var2.getDeviceId());
                    var1.put(var5);
                } catch (Throwable var12) {
                }

                try {
                    var5 = a(var2, var4, 1);
                    var5.put("imei", d.a(23) && var2.getPhoneCount() == 2 ? var2.getDeviceId(1) : null);
                    if (var5.length() > 0) {
                        var1.put(var5);
                    }
                } catch (Throwable var13) {
                }
            } else {
                String var15 = var2.getDeviceId();
                if (var15 != null && b(var15.trim())) {
                    var3.add(var15.trim());
                    var5 = a(var2, var15);
                    if (var5 != null) {
                        var1.put(var5);
                    }
                }

                String var6;
                JSONObject var7;
                TelephonyManager var16;
                try {
                    var16 = (TelephonyManager)var0.getSystemService("phone1");
                    var6 = var16.getDeviceId();
                    if (var6 != null && b(var6) && !var3.contains(var6)) {
                        var3.add(var6);
                        var7 = a(var16, var6);
                        if (var7 != null) {
                            var1.put(var7);
                        }
                    }
                } catch (Throwable var11) {
                }

                try {
                    var16 = (TelephonyManager)var0.getSystemService("phone2");
                    var6 = var16.getDeviceId();
                    if (var6 != null && b(var6) && !var3.contains(var6)) {
                        var3.add(var6);
                        var7 = a(var16, var6);
                        if (var7 != null) {
                            var1.put(var7);
                        }
                    }
                } catch (Throwable var10) {
                }

                JSONArray var17 = e(var0);
                JSONArray var18 = d(var0);
                if (var18 != null) {
                    var17 = var18;
                }

                var18 = c(var0);
                if (var18 != null) {
                    var17 = var18;
                }

                var18 = b(var0);
                if (var18 != null) {
                    var17 = var18;
                }

                if (var17 != null && var17.length() > 0) {
                    for(int var19 = 0; var19 < var17.length(); ++var19) {
                        JSONObject var8 = var17.getJSONObject(var19);
                        if (var8 != null) {
                            String var9 = var8.getString("imei");
                            if (!var3.contains(var9)) {
                                var3.add(var9);
                                var1.put(var8);
                            }
                        }
                    }
                }
            }
        } catch (Throwable var14) {
        }

        return var1;
    }

    private static JSONObject a(TelephonyManager var0, String var1) {
        if (var0 == null) {
            return null;
        } else {
            try {
                JSONObject var2 = new JSONObject();
                var2.put("imei", var1 != null ? var1.trim() : "");
                var2.put("subscriberId", var0.getSubscriberId() == null ? "" : var0.getSubscriberId());
                var2.put("simSerialNumber", var0.getSimSerialNumber() == null ? "" : var0.getSimSerialNumber());
                var2.put("dataState", var0.getDataState());
                var2.put("networkType", a(var0.getNetworkType()));
                var2.put("networkOperator", var0.getNetworkOperator());
                var2.put("phoneType", b(var0.getPhoneType()));
                var2.put("simOperator", var0.getSimOperator() == null ? "" : var0.getSimOperator());
                var2.put("simOperatorName", var0.getSimOperatorName() == null ? "" : var0.getSimOperatorName());
                var2.put("simCountryIso", var0.getSimCountryIso() == null ? "" : var0.getSimCountryIso());
                return var2;
            } catch (Throwable var3) {
                return null;
            }
        }
    }

    private static JSONObject a(TelephonyManager var0, SubscriptionManager var1, int var2) {
        JSONObject var3 = new JSONObject();

        try {
            if (d.a(22)) {
                SubscriptionInfo var4 = var1.getActiveSubscriptionInfoForSimSlotIndex(var2);
                if (var4 != null) {
                    var3.put("simSerialNumber", var4.getIccId() == null ? "" : var4.getIccId());
                    var3.put("simOperator", var4.getMcc() + "0" + var4.getMnc());
                    var3.put("simOperatorName", var4.getCarrierName() == null ? "" : var4.getCarrierName());
                    var3.put("simCountryIso", var4.getCountryIso() == null ? "" : var4.getCountryIso());
                    int var5 = var4.getSubscriptionId();
                    Method var6 = var0.getClass().getMethod("getSubscriberId", Integer.TYPE);
                    var6.setAccessible(true);
                    Object var7 = var6.invoke(var0, var5);
                    var3.put("subscriberId", var7 == null ? "" : var7);
                }
            }
        } catch (Throwable var8) {
        }

        return var3;
    }

    private static Boolean a(String var0) {
        try {
            char var1 = '0';
            if (var0.length() > 0) {
                var1 = var0.charAt(0);
            }

            Boolean var2 = true;

            for(int var3 = 0; var3 < var0.length(); ++var3) {
                char var4 = var0.charAt(var3);
                if (var1 != var4) {
                    var2 = false;
                    break;
                }
            }

            return var2;
        } catch (Throwable var5) {
            return false;
        }
    }

    private static Boolean b(String var0) {
        try {
            Integer var1 = var0.length();
            if (var1 > 10 && var1 < 20 && !a(var0.trim())) {
                return true;
            }
        } catch (Throwable var2) {
        }

        return false;
    }

    private static JSONArray b(Context var0) {
        try {
            JSONArray var1 = new JSONArray();
            TelephonyManager var2 = (TelephonyManager)var0.getSystemService("phone");
            Class var3 = Class.forName("com.android.internal.telephony.Phone");

            Integer var4;
            Integer var5;
            try {
                Field var6 = var3.getField("GEMINI_SIM_1");
                var6.setAccessible(true);
                var4 = (Integer)var6.get((Object)null);
                Field var7 = var3.getField("GEMINI_SIM_2");
                var7.setAccessible(true);
                var5 = (Integer)var7.get((Object)null);
            } catch (Throwable var10) {
                var4 = 0;
                var5 = 1;
            }

            Method var12 = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", Integer.TYPE);
            if (var2 != null && var12 != null) {
                String var13 = ((String)var12.invoke(var2, var4)).trim();
                String var8 = ((String)var12.invoke(var2, var5)).trim();
                JSONObject var9;
                if (b(var13)) {
                    var9 = a(TelephonyManager.class, var2, var4, var13, "Gemini");
                    var1.put(var9);
                }

                if (b(var8)) {
                    var9 = a(TelephonyManager.class, var2, var5, var8, "Gemini");
                    var1.put(var9);
                }

                return var1;
            } else {
                return null;
            }
        } catch (Throwable var11) {
            return null;
        }
    }

    private static JSONArray c(Context var0) {
        try {
            JSONArray var1 = new JSONArray();
            TelephonyManager var2 = (TelephonyManager)var0.getSystemService("phone");
            Class var3 = Class.forName("com.android.internal.telephony.Phone");

            Integer var4;
            Integer var5;
            try {
                Field var6 = var3.getField("GEMINI_SIM_1");
                var6.setAccessible(true);
                var4 = (Integer)var6.get((Object)null);
                Field var7 = var3.getField("GEMINI_SIM_2");
                var7.setAccessible(true);
                var5 = (Integer)var7.get((Object)null);
            } catch (Throwable var12) {
                var4 = 0;
                var5 = 1;
            }

            Method var14 = TelephonyManager.class.getMethod("getDefault", Integer.TYPE);
            TelephonyManager var15 = (TelephonyManager)var14.invoke(var2, var4);
            TelephonyManager var8 = (TelephonyManager)var14.invoke(var2, var5);
            String var9 = var15.getDeviceId().trim();
            String var10 = var8.getDeviceId().trim();
            JSONObject var11;
            if (b(var9)) {
                var11 = a(var15, var9);
                if (var11 != null) {
                    var1.put(var11);
                }
            }

            if (b(var10)) {
                var11 = a(var8, var10);
                if (var11 != null) {
                    var1.put(var11);
                }
            }

            return var1;
        } catch (Throwable var13) {
            return null;
        }
    }

    private static JSONArray d(Context var0) {
        try {
            JSONArray var1 = new JSONArray();
            Class var2 = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method var3 = var2.getMethod("getServiceName", String.class, Integer.TYPE);
            String var4 = (String)var3.invoke(var2, "phone", 1);
            TelephonyManager var5 = (TelephonyManager)var0.getSystemService("phone");
            String var6 = var5.getDeviceId().trim();
            TelephonyManager var7 = (TelephonyManager)var0.getSystemService(var4);
            String var8 = var7.getDeviceId().trim();
            JSONObject var9;
            if (b(var6)) {
                var9 = a(var5, var6);
                if (var9 != null) {
                    var1.put(var9);
                }
            }

            if (b(var8)) {
                var9 = a(var7, var8);
                if (var9 != null) {
                    var1.put(var9);
                }
            }

            return var1;
        } catch (Throwable var10) {
            return null;
        }
    }

    private static JSONArray e(Context var0) {
        try {
            JSONArray var1 = new JSONArray();
            Class var2 = Class.forName("android.telephony.MSimTelephonyManager");
            Object var3 = var0.getSystemService("phone_msim");
            Integer var4 = 0;
            Integer var5 = 1;
            Method var6 = var2.getMethod("getDeviceId", Integer.TYPE);
            String var7 = ((String)var6.invoke(var3, var4)).trim();
            String var8 = ((String)var6.invoke(var3, var5)).trim();
            JSONObject var9;
            if (b(var7)) {
                var9 = a(var2, var3, var4, var7, "");
                var1.put(var9);
            }

            if (b(var8)) {
                var9 = a(var2, var3, var5, var8, "");
                var1.put(var9);
            }

            return var1;
        } catch (Throwable var10) {
            return null;
        }
    }

    private static JSONObject a(Class<?> var0, Object var1, Integer var2, String var3, String var4) {
        JSONObject var5 = new JSONObject();
        try {
            var5.put("imei", var3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Method var6;
        try {
            var6 = var0.getMethod("getSubscriberId" + var4, Integer.TYPE);
            var5.put("subscriberId", var6.invoke(var1, var2) == null ? "" : ((String)var6.invoke(var1, var2)).trim());
        } catch (Throwable var14) {
        }

        try {
            var6 = var0.getMethod("getSimSerialNumber" + var4, Integer.TYPE);
            var5.put("simSerialNumber", var6.invoke(var1, var2) == null ? "" : ((String)var6.invoke(var1, var2)).trim());
        } catch (Throwable var13) {
        }

        try {
            var6 = var0.getMethod("getDataState" + var4, Integer.TYPE);
            var5.put("dataState", (Integer)var6.invoke(var1, var2));
        } catch (Throwable var12) {
        }

        try {
            var6 = var0.getMethod("getNetworkType" + var4, Integer.TYPE);
            var5.put("networkType", a((Integer)var6.invoke(var1, var2)));
        } catch (Throwable var11) {
        }

        try {
            var6 = var0.getMethod("getNetworkOperator" + var4, Integer.TYPE);
            var5.put("networkOperator", (String)var6.invoke(var1, var2));
        } catch (Throwable var10) {
        }

        try {
            var6 = var0.getMethod("getPhoneType" + var4, Integer.TYPE);
            var5.put("phoneType", b((Integer)var6.invoke(var1, var2)));
        } catch (Throwable var9) {
        }

        try {
            var6 = var0.getMethod("getSimOperator" + var4, Integer.TYPE);
            var5.put("simOperator", var6.invoke(var1, var2) == null ? "" : ((String)var6.invoke(var1, var2)).trim());
        } catch (Throwable var8) {
        }

        try {
            var6 = var0.getMethod("getSimOperatorName" + var4, Integer.TYPE);
            var5.put("simOperatorName", var6.invoke(var1, var2) == null ? "" : ((String)var6.invoke(var1, var2)).trim());
        } catch (Throwable var7) {
        }

        return var5;
    }
}
