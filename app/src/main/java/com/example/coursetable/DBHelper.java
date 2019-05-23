package com.example.coursetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "SaveBitmap.db";
    private static final int DB_VER = 1;

    private static final String TBL_NAME = "Gallery";

    private static final String COL_NAME = "Name";
    private static final String COL_DATA = "Data";

    public DBHelper(Context context) {
        super(context, DB_NAME, null,DB_VER);
    }

    public void addBitmap(String name, byte[] image) throws SQLException {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME,name);
        cv.put(COL_DATA,image);
        database.insert(TBL_NAME,null,cv);
    }
    public void changeTheme(String name, int theme) throws SQLException {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,name);
        cv.put(COL_DATA,theme);
        database.insert(TBL_NAME,null,cv);
    }
    public void setLockState(String name, int state) throws SQLException {
        //0为关闭，1为开启
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,name);
        cv.put(COL_DATA,state);
        database.insert(TBL_NAME,null,cv);
    }
    public byte[] getBitmapByName(String name)  {

        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] select = {COL_DATA};

        qb.setTables(TBL_NAME);

        Cursor c = qb.query(database,select,"Name = ?",new String[]{name},null,null,null);

        byte[] result = null;
        if (c.moveToFirst())    {

            do {
                result = c.getBlob(c.getColumnIndex(COL_DATA));
            }while (c.moveToNext());
        }
        return result;
    }
    public int getTheme(String name)  {

        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] select = {COL_DATA};

        qb.setTables(TBL_NAME);

        Cursor c = qb.query(database,select,"Name = ?",new String[]{name},null,null,null);

        int result = R.style.theme_blue;
        if (c.moveToFirst())    {
            do {
                result = c.getInt(c.getColumnIndex(COL_DATA));
            }while (c.moveToNext());
        }
        return result;
    }
    public int getLockState(String name)  {

        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] select = {COL_DATA};

        qb.setTables(TBL_NAME);

        Cursor c = qb.query(database,select,"Name = ?",new String[]{name},null,null,null);

        int result = R.style.theme_blue;
        if (c.moveToFirst())    {
            do {
                result = c.getInt(c.getColumnIndex(COL_DATA));
            }while (c.moveToNext());
        }
        return result;
    }
}
