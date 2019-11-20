package com.haohua.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.sdk.TT;
import com.heytap.openid.sdk.OpenIDSDK;
import com.mrocker.push.PushManager;
import com.pinan.microsoft.androidframe.R;
import com.pinan.microsoft.widget.TestJava;

import net.yeah.liliLearne.BaseActivity;
import net.yeah.liliLearne.SwitchLanguageActivity;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    //权限
    String[] mPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CALL_PHONE
    };

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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * EasyPermissions拒绝回调
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
//                getString(R.string.rationale_ask_again),
//                R.string.setting, R.string.cancel, perms);
    }

    @Override
    public void onPermissionCancel() {

    }

    @Override
    public void onPermissionSetting() {

    }


    /**
     * 继承自Activity的请求权限处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 初始化软键盘管理默认为SOFT_INPUT_ADJUST_RESIZE
     */
    private void initSoftInputMethod() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        receiver = new SwitchInputMethodReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SOFT_INPUT_FLAG);
//        registerReceiver(receiver, filter);
//
//        SystemConfigSp.instance().init(this);
//        currentInputMethod = Settings.Secure.getString(
//                MessageActivity.this.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
//        keyboardHeight = SystemConfigSp.instance().getIntConfig(currentInputMethod);
    }

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(this, mPermissions)) {
            EasyPermissions.requestPermissions(this, "权限",
                    129, mPermissions);
        }

        initSoftInputMethod();
//        findViewById(R.id.editText)

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, SwitchLanguageActivity.class), 2);
            }
        });

        TestJava.test();

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, MainActivity2.class), 2);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TT.tt(MainActivity.this);
                String devId = PushManager.getPushId(MainActivity.this);
                Log.d("hh-tag", "devId = " + devId);
                Log.d("hh-tag", "devId = " + a.h(MainActivity.this));
            }
        });
        textView = findViewById(R.id.testView);


        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TT.tt(MainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 102);
                    } catch (Exception e) {
                        Log.d("hh-tag", "系统代码崩溃: " + e.toString());
                    }
                    return;
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
//            SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
//            String selectedLanguage = preferences.getString("language", "");
//            Log.e("selectedLanguage", "onActivityResult: " + selectedLanguage);
//            LanguageUtil.applyLanguage(MainActivity.this, selectedLanguage);
            recreate();
        }
    }
}
