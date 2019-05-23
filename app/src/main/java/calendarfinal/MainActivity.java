package calendarfinal;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable.DBHelper;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;
import com.example.coursetable.utils.BitmapUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends BaseActivity {
    private calendarfinal.DBOpenHelper dbOpenHelper;
    private Calendarfinal calendar;
    private com.example.coursetable.DatabaseHelper dbhelp;
    private List<Event> mEventList;
    private DrawerLayout mDrawerLayout;
    String thatViewday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(new DBHelper(this).getTheme("theme"));
        setContentView(R.layout.cmain1);
        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH)+1;
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        String defaultday;

        if(month<10 && day>=10){
            defaultday = (year+"-"+"0"+month+"-"+day+" "+"00:00:00");
        }
        else if(month>=10 && day<10){
            defaultday = (year+"-"+month+"-"+"0"+day+" "+"00:00:00");
        }
        else if(month>=10&&day>=10){
            defaultday = (year+"-"+month+"-"+day+" "+"00:00:00");
        }
        else{
            defaultday = (year+"-"+"0"+month+"-"+"0"+day+" "+"00:00:00");
        }

        String testtt = defaultday.substring(0,9);
        testtt = "158 "+testtt+"5";
        System.out.println(testtt);
        thatViewday = defaultday;
        //加载顶部菜单栏（别的活动可能也需要）
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //抽屉布局
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView naviView = findViewById(R.id.nav_view);
//        setContentView(R.layout.rmain);
        View headerView = naviView.getHeaderView(0);

        DBHelper dbHelper = new DBHelper(this);
        de.hdodenhof.circleimageview.CircleImageView  headImage1 = headerView.findViewById(R.id.icon_image);
        //de.hdodenhof.circleimageview.CircleImageView  headtext = headerView.findViewById(R.id.username);

        dbOpenHelper = new DBOpenHelper(this);
        TextView helloText = (TextView) headerView.findViewById(R.id.username);
        String s;
        ArrayList<User> data666 = dbOpenHelper.getAllData();
        s = data666.get(0).getName();
        helloText.setText(s);
        helloText.setTextColor(Color.WHITE);

        byte[] data = dbHelper.getBitmapByName("pic");
        if (data != null)   {
            Bitmap bitmap = BitmapUtils.getImage(data);
            headImage1.setImageBitmap(bitmap);
        }

        //navi栏的默认选择
        naviView.setCheckedItem(R.id.nav_calendar);

        //对navi栏进行事件监听
        //你们的活动加载写在这个函数里面
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.nav_syllabus){
                    //你们的Activity
                    //Toast.makeText(MainActivity.this, "Here is Calender", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    //intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
                    intent.setComponent(new ComponentName(calendarfinal.MainActivity.this, "com.example.coursetable.MainActivity"));
                    //intent = new Intent(this, com.example.coursetable.MainActivity.class);
                    startActivity(intent);
                    finish();


                }else if(id == R.id.nav_reminder){
                    //你们的Activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(calendarfinal.MainActivity.this, "simplenotepad.MainActivity"));
                    startActivity(intent);
                    finish();
                }else if(id == R.id.nav_calendar) {

                    //关闭弹窗即返回syllabus
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                }else if(id == R.id.nav_setting) {
                    //          mDrawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(calendarfinal.MainActivity.this, "com.example.coursetable.SettingActivity"));
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });





        Log.d("qwq", "c");

        Button setting = (Button)findViewById(R.id.SetEvent);
        calendar = (Calendarfinal)findViewById(R.id.cal);
        mEventList = getEvents();
        Log.d("qwq", "d");
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
                if(day<10){
                    String thisday = dayStr+"-0"+day+" "+"00:00:00";
                    thatViewday = thisday;
                }
                else{
                    String thisday = dayStr+"-"+day+" "+"00:00:00";
                    thatViewday = thisday;
                }

            }

//            @Override
//            public void Syllabus(Date begindate, Date selectdate, int todayweekindex, String dayStr) throws ParseException {
//                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                //String day = "2019-02-02";
//                //Date date1 = format.parse(day);
//                int a = (int) ((selectdate.getTime() - begindate.getTime()) / (1000*3600*24)/7)+1;
//
//
//                Intent intent = new Intent();
//                //intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
//                intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
//                //intent = new Intent(this, com.example.coursetable.MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this, "WEEK："+a, Toast.LENGTH_SHORT).show();
//
//                if( !mEventList.isEmpty()){
//                    String dayString = mEventList.get(0).getDeadline();
//                    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//                    Log.d("MainActivity", "event_code "+mEventList.get(0).getDeadline());
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date thatday;
//                    thatday = sdf.parse(dayString);
//                    long day = 24 * 3600 * 1000;
//                    thatday.setTime(thatday.getTime() / day * day);//这个相当于取整，去掉小时，分钟和秒
//                    System.out.println(thatday);
//
//                    Log.d("MainActivity_testday", "event_code "+thatday);
//                }
//
//
//            }
//
//            @Override
//            public void Reminder(int month, int day, int todayweekindex, String dayStr) {
//                Toast.makeText(MainActivity.this, "点击了日期："+dayStr, Toast.LENGTH_SHORT).show();
//            }

        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, CAddCourseEvent.class);
                //startActivity(intent);
//                if(thatViewday != null){
                    Intent intent = new Intent(MainActivity.this, ViewEvents.class);
                    intent.putExtra("thisday", thatViewday);
                    startActivityForResult(intent, 7);
                    Log.d("cheeeeeeek"," "+thatViewday);

//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Please select a day first",Toast.LENGTH_SHORT).show();
//                }


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



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 7 && resultCode == Activity.RESULT_OK && data != null){
            //该部分处理刷新addevent
            //Event event = (Event) data.getSerializableExtra("event");
            String thisday = (String)data.getSerializableExtra("thisday");
            //CourseEdition courseEdition = (CourseEdition) data.getSerializableExtra("course");
            Intent intent = new Intent(MainActivity.this, ViewEvents.class);
            intent.putExtra("thisday", thisday);
            Log.d("chhhhhday",thisday);
            //calendar = (Calendarfinal)findViewById(R.id.cal);

            startActivityForResult(intent, 7);
            Log.d("Test1","Start 666");
        }
    }


}
