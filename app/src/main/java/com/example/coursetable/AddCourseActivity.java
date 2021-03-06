package com.example.coursetable;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new DBHelper(this).getTheme("theme"));
        Log.d("Test", "AddCourseActivity is started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false); //点击外部窗口时不销毁
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText inputCourseCode = (EditText) findViewById(R.id.course_code);
        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.week);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);

        Button okButton = (Button) findViewById(R.id.button1);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = inputCourseCode.getText().toString();
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
                Log.d("MainActivity_testday", "event_code "+day);
                Toast.makeText(AddCourseActivity.this, day, Toast.LENGTH_SHORT).show();


                if(courseCode.equals("")||courseName.equals("") || day.equals("") || start.equals("") || end.equals("")){
                    Toast.makeText(AddCourseActivity.this, "基本信息没有填写", Toast.LENGTH_SHORT).show();
                }else if(day.equals("1") || day.equals("2") || day.equals("3") || day.equals("4") || day.equals("5")){

                    //这里class_version随意，所以设为1
                    CourseEdition courseEdition = new CourseEdition(courseCode,-1,courseName, teacher, classRoom, Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end));
                    Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                    intent.putExtra("course", courseEdition);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(AddCourseActivity.this, "星期1-5", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button cancelButton = (Button) findViewById(R.id.button2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
