package com.example.coursetable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeCourseInfo extends AppCompatActivity {

    protected final static int result_Code = 17372;
    protected final static int delete_Code = 88;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);

    CourseEdition course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new DBHelper(this).getTheme("theme"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_info);
        setFinishOnTouchOutside(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText inputCourseCode = (EditText) findViewById(R.id.course_code);
        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.week);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);

        course = (CourseEdition) getIntent().getSerializableExtra("course");
        final int verNum = course.getVerNum();
        inputCourseCode.setText(course.getCourseCode());
        inputCourseName.setText(course.getCourseName());
        inputTeacher.setText(course.getTeacher());
        inputClassRoom.setText(course.getClassRoom());
        inputDay.setText(String.valueOf(course.getDay()));
        inputStart.setText(String.valueOf(course.getStart()));
        inputEnd.setText(String.valueOf(course.getEnd()));

        Button button_save_info = (Button)findViewById(R.id.button_save_info);
        button_save_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = inputCourseCode.getText().toString();
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();

                if(courseCode.equals("")||courseName.equals("") || day.equals("") || start.equals("") || end.equals("")){
                    Toast.makeText(ChangeCourseInfo.this, "基本信息没有填写", Toast.LENGTH_SHORT).show();
                }else {
                    course = new CourseEdition(courseCode,verNum,courseName, teacher, classRoom, Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end));
                    Intent intent = new Intent(ChangeCourseInfo.this, MainActivity.class);
                    intent.putExtra("course1", course);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        Button button_delete = (Button)findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
