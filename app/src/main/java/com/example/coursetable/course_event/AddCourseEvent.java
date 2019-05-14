package com.example.coursetable.course_event;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.coursetable.CourseEdition;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.MainActivity;
import com.example.coursetable.R;

import java.util.List;

public class AddCourseEvent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "AddCourseEvent is created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_event);
        //获取当前课程
        final CourseEdition course =(CourseEdition) getIntent().getSerializableExtra("this_course");
        Button button_add_event = (Button) findViewById(R.id.button_add_event);
        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourseEvent.this, AddEvent.class);
                intent.putExtra("this_course", course);
                startActivityForResult(intent, 3);
            }
        });
    }

    //将添加的课程事件保存
    public void saveData(Event event, CourseEdition courseEdition){
        DatabaseHelper dbHelper = new DatabaseHelper(this, "database.db",null,1);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into events(topic, detail, deadline) " +"values(?,?,?)",
                new String[]{
                        event.getTopic(),
                        event.getDetails(),
                        event.getDeadline()
                });
        //获取刚刚添加的event的值
        Cursor cursor = sqLiteDatabase.rawQuery("select * from events where topic = ? and detail = ? and deadline = ?",
                new String[]{event.getTopic(),
                        event.getDetails(),
                        event.getDeadline()});
        cursor.moveToNext();
        int eventCode = cursor.getInt(cursor.getColumnIndex("event_code"));
        cursor.close();
//        Log.d("Test", "读取eventCode");
        Log.d("EventCode",String.valueOf(eventCode));
//        Log.d("CourseCode", courseEdition.getCourseCode());

        sqLiteDatabase.execSQL("insert into course_events(course_code, event_code)"+ "values(?, ?)", new String[]{courseEdition.getCourseCode(),String.valueOf(eventCode)});
        cursor = sqLiteDatabase.rawQuery("select * from course_events", null);
        cursor.moveToFirst();
        Log.d("Test","存数据: "+ cursor.getString(cursor.getColumnIndex("course_code"))+ cursor.getInt(cursor.getColumnIndex("event_code")));
        sqLiteDatabase.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 3 && resultCode == Activity.RESULT_OK && data != null){
            //接受addEvent
            Event event = (Event) data.getSerializableExtra("event");
            CourseEdition courseEdition = (CourseEdition) data.getSerializableExtra("this_course");
            saveData(event, courseEdition);
            Intent intent = new Intent(AddCourseEvent.this, MainActivity.class);
            intent.putExtra("course", courseEdition);
            setResult(Activity.RESULT_OK, intent);
            Log.d("Test1", "go back");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
