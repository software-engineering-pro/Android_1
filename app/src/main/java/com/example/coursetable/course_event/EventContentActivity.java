package com.example.coursetable.course_event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.coursetable.DBHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;


public class EventContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new DBHelper(this).getTheme("theme"));
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_event_content);
        String eventTopic = getIntent().getStringExtra("event_topic");
        String eventContent = getIntent().getStringExtra("event_content");
        String eventDeadline = getIntent().getStringExtra("event_deadline");
        EventContentFragment eventContentFragment = (EventContentFragment)
                getSupportFragmentManager().findFragmentById(R.id.event_content_fragment);
        eventContentFragment.refresh(eventTopic, eventContent, eventDeadline);
    }

    public static void actionStart(Context context, String eventTopic, String eventContent, String eventDeadline){
        Intent intent = new Intent(context, EventContentActivity.class);
        intent.putExtra("event_topic", eventTopic);
        intent.putExtra("event_content", eventContent);
        intent.putExtra("event_deadline", eventDeadline);
        context.startActivity(intent);
    }
}
