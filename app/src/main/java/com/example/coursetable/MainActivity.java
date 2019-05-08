package com.example.coursetable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.coursetable.course_event.AddCourseEvent;

import java.util.ArrayList;
//目前想的是一进来就打开Syllabus，所以目前主活动是它，往后可能会改

public class MainActivity extends AppCompatActivity {

    private RelativeLayout day;

    private DrawerLayout mDrawerLayout;

    private ArrayList<CourseEdition> courseEditions;

    //创建数据库（只在程序安装的时候加载）
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);


    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载顶部菜单栏（别的活动可能也需要）
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //抽屉布局
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView naviView = findViewById(R.id.nav_view);
//        setContentView(R.layout.activity_main);

        //navi栏的默认选择
        naviView.setCheckedItem(R.id.nav_syllabus);
        //对navi栏进行事件监听
        //你们的活动加载写在这个函数里面
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.nav_syllabus){
                    //关闭弹窗即返回syllabus
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }else if(id == R.id.nav_reminder){
                    //你们的Activity
                    Toast.makeText(MainActivity.this, "Here is Syllabus", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.nav_calender) {
                    //你们的Activity
                    Toast.makeText(MainActivity.this, "Here is Calender", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //加载数据Syllabus的数据
        loadData();
    }

    @Override
    public void onBackPressed() {
        //最左上角的按钮设置
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadData(){
        ArrayList<CourseEdition> coursesList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM courses", null);
        if(cursor.moveToFirst()){
            do{
                coursesList.add(new CourseEdition(
                        cursor.getString(cursor.getColumnIndex("course_code")),
                        cursor.getInt(cursor.getColumnIndex("class_version")),
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))
                ));
            }while(cursor.moveToNext());
            cursor.close();
        }
        for(CourseEdition courseEdition: coursesList){
            creatLeftView(courseEdition);
            creatItemCourseView(courseEdition);
        }
    }

    private void saveData(CourseEdition courseEdition){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_code, course_name, teacher, class_room, day, class_start, class_end) " + "values(?,?, ?, ?, ?, ?, ?)",
                        new String[] {
                                courseEdition.getCourseCode(),
                                courseEdition.getCourseName(),
                                courseEdition.getTeacher(),
                                courseEdition.getClassRoom(),
                                courseEdition.getDay()+"",
                                courseEdition.getStart()+"",
                                courseEdition.getEnd()+""}
                );
    }

    private void updateData(CourseEdition courseEdition){
        deleteData(courseEdition);
        saveData(courseEdition);
    }

    public void deleteData(CourseEdition courseEdition){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from courses where course_code = ?", new String[]{courseEdition.getCourseCode()});
    }

    private void creatLeftView(CourseEdition courseEdition){
        int endNumber = courseEdition.getEnd();
        if(endNumber > maxCoursesNumber){
            for(int i = 0; i < endNumber-maxCoursesNumber; i++){
                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110, 180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));
                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    private void creatItemCourseViews(){
        for(CourseEdition courseEdition : courseEditions){
            creatItemCourseView(courseEdition);
        }
    }

    private void creatItemCourseView(final CourseEdition courseEdition){
        int getDay = courseEdition.getDay();
        if((getDay<1 || getDay>7)||courseEdition.getStart() > courseEdition.getEnd()){
            Toast.makeText(this, "Date is invalid", Toast.LENGTH_SHORT);
        }else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = findViewById(dayId);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null);
            v.setY(height * (courseEdition.getStart()-1));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,(courseEdition.getEnd()-courseEdition.getStart()+1)*height - 8
            );
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(courseEdition.getCourseName() + "\n" +courseEdition.getTeacher() + "\n" + courseEdition.getClassRoom()); //显示课程名
            day.addView(v);

            v.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(final View vCard) {
                    final View text_view = vCard.findViewById(R.id.text_view);
                    final View course_option = vCard.findViewById(R.id.course_option);
                    text_view.setVisibility(View.INVISIBLE);
                    course_option.setVisibility(View.VISIBLE);

                    //这里的findViewByid的使用对象是course_option
                    Button button_change_course_info = (Button)course_option.findViewById(R.id.change_course_info);
                    Button button_add_course_event = (Button) course_option.findViewById(R.id.add_course_event);
                    Button button_cancel = (Button) course_option.findViewById(R.id.cancel);
                    Log.d("View", button_add_course_event.toString());

                    button_add_course_event.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, AddCourseEvent.class);
                            intent.putExtra("this_course", courseEdition);
                            startActivity(intent);
                        }
                    });

                    button_change_course_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ChangeCourseInfo.class);
                            intent.putExtra("course", courseEdition);
                            startActivityForResult(intent, 1);
                            deleteData(courseEdition);
                            vCard.setVisibility(View.GONE);
                            course_option.setVisibility(View.GONE);
                            day.removeView(vCard);
                        }
                    });

                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            text_view.setVisibility(View.VISIBLE);
                            course_option.setVisibility(View.GONE);
                        }
                    });
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_courses:
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);  //让滑动菜单显示出来
                break;
            default:

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Syllbus活动接受别的活动的反馈
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            CourseEdition course = (CourseEdition) data.getSerializableExtra("course");
            Log.d("Course Code", course.getCourseCode());
            creatLeftView(course);
            creatItemCourseView(course);
            saveData(course);
        }
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            CourseEdition courseEdition = (CourseEdition) data.getSerializableExtra("course1");
            Log.d("Course Code", courseEdition.getCourseCode());
            creatItemCourseView(courseEdition);
            updateData(courseEdition);
            creatLeftView(courseEdition);
        }
    }
}
