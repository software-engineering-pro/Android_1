package com.example.coursetable.course_event;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;

import java.util.List;

public class AddCourseEvent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "AddCourseEvent is created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_event);

        Button button_add_event = (Button) findViewById(R.id.button_add_event);
        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourseEvent.this, AddEvent.class);
                startActivityForResult(intent, 3);
            }
        });
    }

    //将添加的课程保存
    public void saveData(Event event){
        DatabaseHelper dbHelper = new DatabaseHelper(this, "database.db",null,1);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL("insert into events(topic, detail, deadline) " +"values(?,?,?)",
                new String[]{
                        event.getTopic(),
                        event.getDetails(),
                        event.getDeadline()
                });
        Log.d("Test","存数据");
        sqLiteDatabase.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 3 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Test", "接受其他反馈");
            Event event = (Event) data.getSerializableExtra("event");
            saveData(event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Test", "Resume");
    }
}