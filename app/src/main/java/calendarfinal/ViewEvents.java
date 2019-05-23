package calendarfinal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.coursetable.DBHelper;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.MainActivity;
import com.example.coursetable.R;
import com.example.coursetable.course_event.AddEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class ViewEvents extends AppCompatActivity {


    //private Calendarfinal calendar;
    private DatabaseHelper dbhelp;
    private List<Event> mEventList;
    private List<String> DividebyDate = new ArrayList<>();;
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new DBHelper(this).getTheme("theme"));
        Log.d("Test", "CAddCourseEvent is created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取当前课程
        //final CourseEdition course =(CourseEdition) getIntent().getSerializableExtra("this_course");
        final String thatViewday =getIntent().getStringExtra("thisday");
        System.out.println("CCCCCCCCCCheck"+thatViewday);

        android.support.design.widget.FloatingActionButton button_add_event = (android.support.design.widget.FloatingActionButton) findViewById(R.id.button_add_event);
        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEvents.this, com.example.coursetable.course_event.AddEvent.class);
                intent.putExtra("thisday", thatViewday);
                startActivityForResult(intent, 7);
                System.out.println("CCCCCCCCCCheck"+thatViewday);
            }
        });
        Button finishview = (Button)findViewById(R.id.Finishview);
        finishview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewEvents.this, calendarfinal.MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });



//        //把事件分日期存到特定的某一个链表
//        mEventList = getEvents();
//        for(int i = 0 ; i < mEventList.size() ; i++) {
//            String tempday = thatViewday;
//            try {
//                Date s0 =StrToDate(mEventList.get(i).getDeadline());
//                Date s1 = StrToDate(tempday);
//                Log.d("mEventLis.getDeadline()"," "+mEventList.get(i).getDeadline());
//                Log.d("mEventLis.getDeadline()"," "+s0);
//                Log.d("tempday"," "+s1);
//
//                if(s1.toString().equals(s0.toString())){
//                    Log.d("ViewEventssss",mEventList.get(i).getTopic());
//                    //String toadd = mEventList.get(i).getTopic();
//                    DividebyDate.add(mEventList.get(i).getTopic());
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }


        Log.d("cheeeeeeek2"," "+thatViewday);

    }


    //将添加的课程事件保存
    public void saveData(Event event, String thisday){

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

        sqLiteDatabase.execSQL("insert into day_events(thisday, event_code)"+ "values(?, ?)", new String[]{thisday,String.valueOf(eventCode)});
        cursor = sqLiteDatabase.rawQuery("select * from day_events", null);
        cursor.moveToFirst();
        Log.d("Test","存数据: "+ cursor.getString(cursor.getColumnIndex("thisday"))+ cursor.getInt(cursor.getColumnIndex("event_code")));
        sqLiteDatabase.close();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 7 && resultCode == Activity.RESULT_OK && data != null){
            //接受addEvent
            Event event = (Event) data.getSerializableExtra("event");
            String thisday = (String)data.getSerializableExtra("thisday");
            System.out.println("1372377325588"+thisday);

            saveData(event, thisday);
            Intent intent1 = new Intent(ViewEvents.this, calendarfinal.MainActivity.class);
            intent1.putExtra("thisday", thisday);
            setResult(Activity.RESULT_OK, intent1);
            Log.d("Test1", "go back");
            finish();
        }
    }




    private Date StrToDate(String eventday) throws ParseException {
        //String dayString = mEventList.get(0).getDeadline();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        //Log.d("MainActivity", "event_code "+mEventList.get(0).getDeadline());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date thatday;
        thatday = sdf.parse(eventday);
        long day = 24 * 3600 * 1000;
        thatday.setTime(thatday.getTime() / day * day);//这个相当于取整，去掉小时，分钟和秒
        //System.out.println(thatday);

        Log.d("MainActivity_testday", "event_code "+thatday);
        return thatday;
    }

    //读取课程表中的事件
    private List<Event> getEvents(){
        List<Event> eventList = new ArrayList<>();
       DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("events", null,null,null,null,null,null);
        //SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
       // Cursor cursor = sqLiteDatabase.rawQuery("select * from events", null);
        if(cursor.moveToFirst()){
            do{
                eventList.add(new Event(
                        cursor.getInt(cursor.getColumnIndex("event_code")),
                        cursor.getString(cursor.getColumnIndex("topic")),
                        cursor.getString(cursor.getColumnIndex("detail")),
                        cursor.getString(cursor.getColumnIndex("deadline"))
                ));
            }while (cursor.moveToNext());
            cursor.close();
        }
        databaseHelper.close();
        return eventList;
    }



}
