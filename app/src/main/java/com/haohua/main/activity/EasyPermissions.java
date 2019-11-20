package com.haohua.main.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 在Android M(API >= 23)时请求和检测权限
 */
public class EasyPermissions {

    public static final int SETTINGS_REQ_CODE = 16061;

    public static final String TAG = "EasyPermissions";

    public interface PermissionCallbacks extends
            ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(int requestCode, List<String> perms);

        void onPermissionsDenied(int requestCode, List<String> perms);

        void onPermissionCancel();

        void onPermissionSetting();

    }

    /**
     * 检测是否有权限
     *
     * @param context
     * @param perms
     * @return
     */
    public static boolean hasPermissions(Context context, String... perms) {
        //小于23版本的是不可以请求权限的
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    /**
     * 请求权限
     *
     * @param object
     * @param rationale
     * @param requestCode
     * @param perms
     */
    public static void requestPermissions(final Object object, String rationale,
                                          final int requestCode, final String... perms) {
        requestPermissions(object, rationale,
                android.R.string.ok,
                android.R.string.cancel,
                requestCode, perms);
    }

    /**
     * 请求权限
     *
     * @param object
     * @param rationale
     * @param positiveButton
     * @param negativeButton
     * @param requestCode
     * @param perms
     */
    public static void requestPermissions(final Object object, String rationale,
                                          @StringRes int positiveButton,
                                          @StringRes int negativeButton,
                                          final int requestCode, final String... perms) {

        checkCallingObjectSuitability(object);

        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale =
                    shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
        }

        if (shouldShowRationale) {
            Activity activity = getActivity(object);
            if (null == activity) {
                return;
            }

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage(rationale)
                    .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executePermissionsRequest(object, perms, requestCode);
                        }
                    })
                    .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //如果请求权限被拒绝
                            if (object instanceof PermissionCallbacks) {
                                ((PermissionCallbacks) object).onPermissionsDenied(requestCode, Arrays.asList(perms));
                            }
                        }
                    }).create();
            dialog.show();
        } else {
            executePermissionsRequest(object, perms, requestCode);
        }
    }

    /**
     * 请求权限（授权了或者拒绝授权）回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param object
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, Object object) {

        checkCallingObjectSuitability(object);

        //获取授权与否集合
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        //授权的权限回调
        if (!granted.isEmpty()) {
            if (object instanceof PermissionCallbacks) {
                ((PermissionCallbacks) object).onPermissionsGranted(requestCode, granted);
            }
        }

        //拒绝授权的权限回调
        if (!denied.isEmpty()) {
            if (object instanceof PermissionCallbacks) {
                ((PermissionCallbacks) object).onPermissionsDenied(requestCode, denied);
            }
        }

        //如果都成功
        if (!granted.isEmpty() && denied.isEmpty()) {
            runAnnotatedMethods(object, requestCode);
        }
    }

    /**
     * 检测拒绝权限是否永不通知
     *
     * @param object
     * @param rationale
     * @param positiveButton
     * @param negativeButton
     * @param deniedPerms
     * @return
     */
    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object,
                                                              String rationale,
                                                              @StringRes int positiveButton,
                                                              @StringRes int negativeButton,
                                                              List<String> deniedPerms) {
        return checkDeniedPermissionsNeverAskAgain(object, rationale,
                positiveButton, negativeButton, null, deniedPerms);
    }

    /**
     * 检测拒绝权限永久不提示
     *
     * @param object
     * @param rationale
     * @param positiveButton
     * @param negativeButton
     * @param negativeButtonOnClickListener
     * @param deniedPerms
     * @return
     */
    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object,
                                                              String rationale,
                                                              @StringRes int positiveButton,
                                                              @StringRes int negativeButton,
                                                              @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener,
                                                              List<String> deniedPerms) {
        boolean shouldShowRationale;
        for (String perm : deniedPerms) {
            shouldShowRationale = shouldShowRequestPermissionRationale(object, perm);
            if (!shouldShowRationale) {
                final Activity activity = getActivity(object);
                if (null == activity) {
                    return true;
                }

                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage(rationale)
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        })
                        .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((PermissionCallbacks) object).onPermissionCancel();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        }
        return false;
    }

    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    @TargetApi(23)
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);

        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    @TargetApi(11)
    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    @TargetApi(11)
    private static void startAppSettingsScreen(Object object,
                                               Intent intent) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        }
    }

    /**
     * 通过反射注解式回调请求
     *
     * @param object
     * @param requestCode
     */
    private static void runAnnotatedMethods(Object object, int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)) {
            clazz = clazz.getSuperclass();
        }
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (method.isAnnotationPresent(AfterPermissionGranted.class)) {
//                //检测是否是当前请求
//                AfterPermissionGranted ann = method.getAnnotation(AfterPermissionGranted.class);
//                if (ann.value() == requestCode) {
//                    if (method.getParameterTypes().length > 0) {
//                        throw new RuntimeException(
//                                "Cannot execute non-void method " + method.getName());
//                    }
//
//                    try {
//                        //如果是private方法必须设置accessible为true
//                        if (!method.isAccessible()) {
//                            method.setAccessible(true);
//                        }
//                        method.invoke(object);
//                    } catch (IllegalAccessException e) {
//                        Log.e(TAG, "runDefaultMethod:IllegalAccessException", e);
//                    } catch (InvocationTargetException e) {
//                        Log.e(TAG, "runDefaultMethod:InvocationTargetException", e);
//                    }
//                }
//            }
//        }
    }

    /**
     * 检测请求权限的对象是否是Activity或者Fragment
     * 1.因为请求权限必须是Activity、Fragment对象
     *
     * @param object
     */
    private static void checkCallingObjectSuitability(Object object) {
        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (!(isSupportFragment || isActivity || (isAppFragment && isMinSdkM))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }

    /**
     * 是否使用了注解式
     *
     * @param object
     * @return
     */
    private static boolean isUsingAndroidAnnotations(Object object) {
        if (!object.getClass().getSimpleName().endsWith("_")) {
            return false;
        }

        try {
            Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
            return clazz.isInstance(object);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
