package com.example.coursetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import static com.example.coursetable.SettingActivity.Setting;

public class PrivacyActivity extends AppCompatActivity{
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(new DBHelper(this).getTheme("theme"));
        setContentView(R.layout.activity_privacy);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button reset=findViewById(R.id.reset);
        Switch switch2=findViewById(R.id.switch2);
        db=new DBHelper(this);
        if (db.getLockState("lockState")==0){
            switch2.setChecked(false);
        }else {
            switch2.setChecked(true);
        }


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivacyActivity.this,calendarfinal.ResetActivity.class);
                intent.putExtra("extra_data","privacy");
                startActivity(intent);
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.setLockState("lockState",1);
                }else {
                    db.setLockState("lockState",0);
                }
            }
        });
    }

//    @Override
//    public void onDestroy(){
//        Intent intent = new Intent( ThemeActivity.this,SettingActivity.class);
//        startActivity(intent);
//        finish();
//        super.onDestroy();
//    }
}
