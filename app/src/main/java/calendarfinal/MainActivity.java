package calendarfinal;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends BaseActivity {
    private Calendarfinal calendar;
    private com.example.coursetable.DatabaseHelper dbhelp;
    private List<Event> mEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmain);
        Button logout = (Button)findViewById(R.id.logout);
        calendar = (Calendarfinal)findViewById(R.id.cal);
        mEventList = getEvents();

        HashMap<Date, Integer> map = new HashMap();
        //把事件数量存入特定的某一天
        for(int i = 0 ; i < mEventList.size() ; i++) {
            Date tempdate;
            try {
                tempdate = StrToDate((mEventList.get(i).getDeadline()));
                //Log.d("MainActivity_testday", "5555555555555 "+tempdate+" i "+i);
                if(map.get(tempdate) == null){
                    map.put(tempdate, 1 );
                }
                else{
                    map.put(tempdate, map.get(tempdate)+1 );
                }

                //Log.d("MainActivity_testday", "5555555555555 "+map.get(tempdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //传入CalendarFinal类
        calendar.StoreEvents(map);


        //登出
        calendar.setOnClickListener(new Calendarfinal.onClickListener() {

            public void DesMonth() {
                Toast.makeText(MainActivity.this, "DES", Toast.LENGTH_SHORT).show();
                calendar.monthChange(-1);
            }

            public void AddMonth() {
                Toast.makeText(MainActivity.this, "131313ADD", Toast.LENGTH_SHORT).show();
                calendar.monthChange(1);
            }

            public void AddYear(){
                calendar.yearChange(1);
            }

            public void DesYear(){
                calendar.yearChange(-1);
            }

            public void Title(String monthStr, Date month) {
                Toast.makeText(MainActivity.this, "MONTH："+monthStr, Toast.LENGTH_SHORT).show();
            }

            public void Week(int weekIndex, String weekStr) {
                Toast.makeText(MainActivity.this, "星期："+weekStr, Toast.LENGTH_SHORT).show();
            }

            public void Day(int day, String dayStr/* , DayFinish finish*/ ) {
                Toast.makeText(MainActivity.this, "DATE："+dayStr, Toast.LENGTH_SHORT).show();
                Log.w("", "点击了日期:"+dayStr);

            }

            @Override
            public void Syllabus(Date begindate, Date selectdate, int todayweekindex, String dayStr) throws ParseException {
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //String day = "2019-02-02";
                //Date date1 = format.parse(day);
                int a = (int) ((selectdate.getTime() - begindate.getTime()) / (1000*3600*24)/7)+1;


                Intent intent = new Intent();
                //intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
                intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
                //intent = new Intent(this, com.example.coursetable.MainActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "WEEK："+a, Toast.LENGTH_SHORT).show();


                String dayString = mEventList.get(0).getDeadline();
                TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                Log.d("MainActivity", "event_code "+mEventList.get(0).getDeadline());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date thatday;
                thatday = sdf.parse(dayString);
                long day = 24 * 3600 * 1000;
                thatday.setTime(thatday.getTime() / day * day);//这个相当于取整，去掉小时，分钟和秒
                System.out.println(thatday);

                Log.d("MainActivity_testday", "event_code "+thatday);

            }
//
//            @Override
//            public void Reminder(int month, int day, int todayweekindex, String dayStr) {
//                Toast.makeText(MainActivity.this, "点击了日期："+dayStr, Toast.LENGTH_SHORT).show();
//            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcastbestpractice.LOGOUT");
                sendBroadcast(intent);
            }
        });


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

        //Log.d("MainActivity_testday", "event_code "+thatday);
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

    //在日历里要画的
    class DayFinish{
        int day;
        int all;
        int finish;
        public DayFinish(int day, int finish, int all) {
            this.day = day;
            this.all = all;
            this.finish = finish;
        }

    }

}
