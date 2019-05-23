package com.example.coursetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //将数据库的初始操作（建表等）都写在这里
    //对数据库的修改写在各自的活动里
    private static final int version = 1;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public static final String CREATE_COURSES = "create table courses("+
            "course_code text,"+
            "class_version integer primary key autoincrement ,"+
            "course_name text,"+
            "teacher text,"+
            "class_room text,"+
            "day integer,"+
            "class_start integer,"+
            "class_end integer)";

    public static final String CREATE_WEEK = "create table week(" +
            "week_no integer," +
            "course_code text," +
            "affair_code text)";

    public static final String CREATE_EVENTS = "create table events("+
            "event_code integer primary key autoincrement,"+
            "record_time timestamp," +
            "topic text," +
            "detail text," +
            "warn_time timestamp," +
            "relevant_course char(10)," +
            "deadline timestamp," +
            "type_code int)";
    public static final String CREATE_TYPES = "create table types(" +
            "type_code integer primary key autoincrement," +
            "type_name text)";
    public static final String CREATE_COURSE_EVENTS = "create table course_events("+
            "course_code text,"+
            "event_code integer,"+
            "primary key (course_code, event_code),"+
            "foreign key (event_code) references  events(event_code) on delete cascade  on update cascade)";

    public static final String CREATE_DAY_EVENTS = "create table day_events("+
            "thisday text,"+
            "event_code integer,"+
            "primary key (thisday, event_code),"+
            "foreign key (event_code) references  events(event_code) on delete cascade  on update cascade)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_WEEK);
        db.execSQL(CREATE_TYPES);
        db.execSQL(CREATE_EVENTS);
        db.execSQL(CREATE_COURSE_EVENTS);
        db.execSQL(CREATE_DAY_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists courses");
//        db.execSQL("drop table if exists week");
//        db.execSQL("drop table if exists types");
//        db.execSQL("drop table if exists events");
//        onCreate(db);
    }
}
