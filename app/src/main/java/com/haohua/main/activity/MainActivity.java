package com.haohua.main.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.pinan.microsoft.androidframe.R;

import net.yeah.liliLearne.BaseActivity;
import net.yeah.liliLearne.SwitchLanguageActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, SwitchLanguageActivity.class), 2);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, MainActivity2.class), 2);
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
