package com.example.coursetable.course_event;

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
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import com.example.coursetable.AddCourseActivity;
import com.example.coursetable.CourseEdition;
import com.example.coursetable.DBHelper;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.MainActivity;
import com.example.coursetable.R;

import calendarfinal.ViewEvents;


public class AddEvent extends AppCompatActivity {

    private int mode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(new DBHelper(this).getTheme("theme"));
        Log.d("Test", "AddEvent is created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        setFinishOnTouchOutside(false); //点击外部窗口时不销毁
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText inputEventTitle = (EditText) findViewById(R.id.input_event_title);
        final EditText inputEventDetail = (EditText) findViewById(R.id.input_event_detail);
        final EditText inputEventDeadline = (EditText) findViewById(R.id.input_event_deadline);

        //获取点击课程信息
        final CourseEdition courseEdition = (CourseEdition)getIntent().getSerializableExtra("this_course");
        final String thatViewday = (String)getIntent().getSerializableExtra("thisday");
        if(courseEdition==null){
            mode=2;
        }
        else if(thatViewday==null){
            mode=1;
        }
        System.out.println("alaaaaaaa"+courseEdition);
        System.out.println("alaaaaaaa"+thatViewday);

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




                if(mode==1){
                    Intent intent = new Intent(AddEvent.this, AddCourseEvent.class);
                    intent.putExtra("event", event);
                    intent.putExtra("this_course", courseEdition);
                    setResult(Activity.RESULT_OK, intent);
                }
                else if(mode==2){
                    Intent intent1 = new Intent(AddEvent.this, ViewEvents.class);
                    intent1.putExtra("event", event);
                    intent1.putExtra("thisday", thatViewday);
                    //intent1.putExtra("this_course", courseEdition);
                    setResult(Activity.RESULT_OK, intent1);
                }

                Log.d("Test","传递数据");
                finish();

            }
        });

        inputEventDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEvent.this, DatepickerActivity.class);

                startActivityForResult(intent, 5);
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //接受datepicker
        if(requestCode == 5 && resultCode == Activity.RESULT_OK && data != null){
            String datetime = (String)data.getSerializableExtra("datetime");
            EditText inputEventDeadline = (EditText) findViewById(R.id.input_event_deadline);
            inputEventDeadline.setText(datetime);
            Log.d("Test", "结果");
            Log.d("Test", datetime);
        }
    }
}

