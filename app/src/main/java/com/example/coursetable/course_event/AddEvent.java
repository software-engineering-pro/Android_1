package com.example.coursetable.course_event;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursetable.AddCourseActivity;
import com.example.coursetable.CourseEdition;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.MainActivity;
import com.example.coursetable.R;

public class AddEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "AddEvent is created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        setFinishOnTouchOutside(false); //点击外部窗口时不销毁

        final EditText inputEventTitle = (EditText) findViewById(R.id.input_event_title);
        final EditText inputEventDetail = (EditText) findViewById(R.id.input_event_detail);
        final EditText inputEventDeadline = (EditText) findViewById(R.id.input_event_deadline);

        //获取点击课程信息
        final CourseEdition courseEdition = (CourseEdition)getIntent().getSerializableExtra("this_course");


        Button okButton = (Button) findViewById(R.id.button1);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里输入的内容三条不能与其他的重复
                String eventTitle = inputEventTitle.getText().toString();
                String eventDetail = inputEventDetail.getText().toString();
                String eventDeadline = inputEventDeadline.getText().toString();
                
                //假设eventCode为-1 （空）
                Event event = new Event(-1,eventTitle, eventDetail, eventDeadline);
                Intent intent = new Intent(AddEvent.this, AddCourseEvent.class);
                intent.putExtra("event", event);
                intent.putExtra("this_course", courseEdition);
                setResult(Activity.RESULT_OK, intent);
                Log.d("Test","传递数据");
                finish();

            }
        });
        Button cancelButton = (Button) findViewById(R.id.button2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEvent.this, AddCourseEvent.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

