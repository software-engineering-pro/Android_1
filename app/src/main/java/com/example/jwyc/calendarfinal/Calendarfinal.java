package com.example.jwyc.calendarfinal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class Calendarfinal extends View {
    private int BgMonth, BgWeek, BgDay, BgPre;
    private int TextColorMonth;
    private float SizeMonth;
    private int RowL, RowR;
    private float MonthRowSpac;
    private float MonthSpac;
    private int TextColorWeek, SelectWeekTextColor;
    private int TextColorDay;
    private float SizeDay;
    private float SizeWeek;
    private int Today;
    /** 选中的文本的颜色*/
    private int SelectTextColor;
    /** 选中背景*/
    private int SelectDateBg, CurrentBg;
    private float SelectRadius, CurrentBgStrokeWidth;
    private float[] CurrentBgDashPath;

    /** 行间距*/
    private float LineSpac;
    /** 字体上下间距*/
    private float TextSpac;

    private Paint Paint;
    private Paint bgPaint;

    private float titleHeight, weekHeight, dayHeight, preHeight, oneHeight;
    private int columnWidth;       //每列宽度

    private Date month; //当前的月份
    private Date year;
    private boolean isCurrentMonth;       //展示的月份是否是当前月
    private int currentDay, selectDay, lastSelectDay;    //当前日期 、 选中的日期 、上一次选中的日期（避免造成重复回调请求）

    private int dayOfMonth;    //月份天数
    private int firstIndex;    //当月第一天位置索引
    private int todayWeekIndex;//今天是星期几
    private int firstLineNum, lastLineNum; //第一行、最后一行能展示多少日期
    private int lineNum;      //日期行数
    private String[] WEEK_STR = new String[]{"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", };
    public Calendarfinal(Context context, AttributeSet attrs) {
        super(context,attrs );
        //获取自定义属性的值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Calendar);//, defStyleAttr, 0);

        BgMonth = a.getColor(R.styleable.Calendar_BgMonth, Color.TRANSPARENT);
        BgWeek = a.getColor(R.styleable.Calendar_BgWeek, Color.TRANSPARENT);
        BgDay = a.getColor(R.styleable.Calendar_BgDay, Color.TRANSPARENT);

        RowL = a.getResourceId(R.styleable.Calendar_RowL, R.drawable.row_left);
        RowR = a.getResourceId(R.styleable.Calendar_RowR, R.drawable.row_right);
        MonthRowSpac = a.getDimension(R.styleable.Calendar_MonthRowSpac, 20);
        TextColorMonth = a.getColor(R.styleable.Calendar_TextColorMonth, Color.BLACK);
        SizeMonth = a.getDimension(R.styleable.Calendar_SizeMonth, 100);
        MonthSpac = a.getDimension(R.styleable.Calendar_MonthSpac, 20);
        TextColorWeek = a.getColor(R.styleable.Calendar_TextColorWeek, Color.BLACK);
        SelectWeekTextColor = a.getColor(R.styleable.Calendar_SelectWeekTextColor, Color.BLACK);

        SizeWeek = a.getDimension(R.styleable.Calendar_SizeWeek, 70);
        TextColorDay = a.getColor(R.styleable.Calendar_TextColorDay, Color.GRAY);
        SizeDay = a.getDimension(R.styleable.Calendar_SizeDay, 70);

        SelectTextColor = a.getColor(R.styleable.Calendar_SelectTextColor, Color.YELLOW);
        CurrentBg = a.getColor(R.styleable.Calendar_CurrentBg, Color.RED);
        Today = a.getColor(R.styleable.Calendar_Today, Color.YELLOW);
        SelectDateBg = a.getColor(R.styleable.Calendar_SelectDateBg, Color.LTGRAY);
        SelectRadius = a.getDimension(R.styleable.Calendar_SelectRadius, 20);
        CurrentBgStrokeWidth = a.getDimension(R.styleable.Calendar_CurrentBgStrokeWidth, 5);
        LineSpac = a.getDimension(R.styleable.Calendar_LineSpac, 20);
        a.recycle();  //注意回收
        initCompute();

    }
    /**计算相关常量，构造方法中调用*/
    private void initCompute(){
        Paint = new Paint();
        bgPaint = new Paint();
        Paint.setAntiAlias(true); //抗锯齿
        bgPaint.setAntiAlias(true); //抗锯齿

        //map = new HashMap<>();

        //标题高度
        Paint.setTextSize(SizeMonth);
        titleHeight = FontUtil.getFontHeight(Paint) + 2 * MonthSpac;
        //星期高度
        Paint.setTextSize(SizeWeek);
        weekHeight = FontUtil.getFontHeight(Paint);
        //日期高度
        Paint.setTextSize(SizeDay);
        dayHeight = FontUtil.getFontHeight(Paint);
        //次数字体高度
        //每行高度 = 行间距 + 日期字体高度 + 字间距 + 次数字体高度
        oneHeight = LineSpac + dayHeight + TextSpac + preHeight;

        //默认当前月份
        String cDateStr = getMonthStr(new Date());
//        cDateStr = "2015年08月";
        setMonth(cDateStr);
    }

    public void setMonth(String Month){
        month = str2Date(Month);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

        Date currentMonth = str2Date(getMonthStr(new Date()));
        if(currentMonth.getTime() == month.getTime()){// 当月
            isCurrentMonth = true;
            selectDay = currentDay;
        }
        else{
            isCurrentMonth = false;
            selectDay = 0;
        }
        calendar.setTime(month);
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        lineNum = 1;

        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int left = dayOfMonth - firstLineNum;
        while (left>7){
            lineNum++;
            left = left-7;
        }
        if(left>0){
            lineNum++;
            lastLineNum = left;
        }
    }




}
