package com.example.coursetable;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import static com.example.coursetable.global.GloablApplication.sCurrentTheme;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(sCurrentTheme);
        setContentView(R.layout.activity_theme);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button1=findViewById(R.id.btn_switch_theme_blue);
        Button button2=findViewById(R.id.btn_switch_theme_green);
        Button button3=findViewById(R.id.btn_switch_theme_black);
        Button button4=findViewById(R.id.btn_switch_theme_red);
        Button button5=findViewById(R.id.btn_switch_theme_pink);
        Button button6=findViewById(R.id.btn_switch_theme_orange);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }
    public void onClick(View view) {
        Log.v("dty","111"  );
//       View mview = view.findViewById(R.id.nav_theme);
        switch (view.getId()) {
            case R.id.btn_switch_theme_blue:
                changeTheme(R.style.theme_blue);
                break;
            case R.id.btn_switch_theme_green:
                changeTheme(R.style.theme_green);
                break;
            case R.id.btn_switch_theme_black:
                changeTheme(R.style.theme_black);
                break;
            case R.id.btn_switch_theme_red:
                changeTheme(R.style.theme_red);
                break;
            case R.id.btn_switch_theme_pink:
                changeTheme(R.style.theme_pink);
                break;
            case R.id.btn_switch_theme_orange:
                changeTheme(R.style.theme_orange);
                break;
        }
        Intent intent = new Intent( ThemeActivity.this,SettingActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeTheme(int theme) {
        // 改变主题时应该把当前主题设置保存进 SharedPreferences 里去.
        // 比如给这三个主题编号 101, 102, 103, 然后保存该编号, 供下次启动时设置为对应主题.
        // 这里省略了这部分逻辑, 只留主题相关逻辑.
        sCurrentTheme = theme;

        // 调用 Activity.recreate() 方法即可从 Activity.onCreate() 开始重新加载界面.
        // 该方法不会启动界面过场动画, 但重启时会有一下闪烁.
      //  recreate();
    }
}
