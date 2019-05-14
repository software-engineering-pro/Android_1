package com.example.coursetable.course_event;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.coursetable.R;

public class DatepickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
        setFinishOnTouchOutside(false);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        Button button = (Button) findViewById(R.id.button_timepick);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = 0;
                int minute = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                String datetime = year + "-"+convertdigit(month+1) + "-" + convertdigit(day)+" "
                        + convertdigit(hour)+ ":" + convertdigit(minute)+ ":00";
                Intent intent = new Intent(DatepickerActivity.this, AddEvent.class);
                intent.putExtra("datetime", datetime);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        datePicker.init(2019, 1, 1, new DatePicker.OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                String str = "您选择的日期是："+year+"年"+String.valueOf(monthOfYear+1)+"月"+String.valueOf(dayOfMonth)+"日。";
                Toast.makeText(DatepickerActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(DatepickerActivity.this,"您选择的时间是："+hourOfDay+"时"+minute+"分。",Toast.LENGTH_SHORT);
            }

        });
    }

    public String convertdigit(int digit){
        if(digit < 10){
            return "0"+ String.valueOf(digit);
        }else {
            return  String.valueOf(digit);
        }
    }
}
