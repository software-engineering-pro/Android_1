package calendarfinal;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

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
    private float TextSizePre;
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
    private String Mmonth;
    //private String Month;
    private Date year;
    private boolean isCurrentMonth;       //展示的月份是否是当前月
    private int currentDay, selectDay, lastSelectDay;    //当前日期 、 选中的日期 、上一次选中的日期（避免造成重复回调请求）

    private int dayOfMonth;    //月份天数
    private int firstIndex;    //当月第一天位置索引
    private int todayWeekIndex;//今天是星期几
    private int firstLineNum, lastLineNum; //第一行、最后一行能展示多少日期
    private int lineNum;      //日期行数
    private String[] WEEK_STR = new String[]{"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", };
    private HashMap<Date, Integer> Syllabusmap;

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
        TextSizePre = a.getDimension(R.styleable.Calendar_TextSizePre, 10);
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
        LineSpac = a.getDimension(R.styleable.Calendar_LineSpac, 100);
        a.recycle();  //注意回收
        initCompute();

    }
    /**计算相关常量，构造方法中调用*/
    private void initCompute(){
        Paint = new Paint();
        bgPaint = new Paint();
        Paint.setAntiAlias(true);
        bgPaint.setAntiAlias(true);

        //标题高度
        Paint.setTextSize(SizeMonth);
        titleHeight = FontUtil.getFontHeight(Paint) + 2 * MonthSpac;
        //星期高度
        Paint.setTextSize(SizeWeek);
        weekHeight = FontUtil.getFontHeight(Paint);
        //日期高度
        Paint.setTextSize(SizeDay);
        dayHeight = FontUtil.getFontHeight(Paint);
        oneHeight = LineSpac + dayHeight + TextSpac + preHeight;

        //默认当前月份
        String cDateStr = MonthtoString(new Date());
        //Month = cDateStr;

        setMonth(cDateStr);
    }


    private void setMonth(String Month) {
        //设置的月份（2017年01月）

        month = str2Date(Month);
        Mmonth = Month;
        //Date dddd = str2Date()
        Log.d(TAG, "000000?>?>?>设置月份："+Mmonth);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //获取今天是多少号
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

        Date cM = str2Date(MonthtoString(new Date()));
        Log.d(TAG, "!!!!!!!0"+month);
        Log.d(TAG, "!!!!!!!1"+cM);
        //判断是否为当月
        if(cM.getTime() == month.getTime()){
            isCurrentMonth = true;
            selectDay = currentDay;//当月默认选中当前日
        }else{
            isCurrentMonth = false;
            selectDay = 0;
        }
        Log.d(TAG, "000000设置月份："+month+"   今天"+currentDay+"号, 是否为当前月："+isCurrentMonth);
        calendar.setTime(month);
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置（星期几）
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;
        //第一行能展示的天数
        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth - firstLineNum;
        while (shengyu>7){
            lineNum ++;
            shengyu-=7;
        }
        if(shengyu>0){
            lineNum ++;
            lastLineNum = shengyu;
        }
//        Log.i(TAG, getMonthStr(month)+"111111一共有"+dayOfMonth+"天,第一天的索引是："+firstIndex+"   有"+lineNum+
//                "行，第一行"+firstLineNum+"个，最后一行"+lastLineNum+"个");
    }

    private String getDateStr(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(month);
    }
    private Date str2Date(String str){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    private void setYear(String Year){
        year = str2Date(Year);
        Mmonth = Year;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //获取今天是多少号
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

        Date cM = str2Date(MonthtoString(new Date()));
        //判断是否为当月
        if(cM.getTime() == year.getTime()){
            isCurrentMonth = true;
            selectDay = currentDay;//当月默认选中当前日
        }else{
            isCurrentMonth = false;
            selectDay = 0;
        }
        Log.d(TAG, "2222222设置月份："+month+"   今天"+currentDay+"号, 是否为当前月："+isCurrentMonth);
        calendar.setTime(year);
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置（星期几）
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;
        //第一行能展示的天数
        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int left = dayOfMonth - firstLineNum;
        while (left>7){
            lineNum ++;
            left=left - 7;
        }
        if(left>0){
            lineNum ++;
            lastLineNum = left;
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

        //Log.d("MainActivity_testday", "event_code "+thatday);
        return thatday;
    }

//    private Date StringDate(String str){
//        try {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//            return df.parse(str);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    private String MonthtoString(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(month);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度 = 填充父窗体
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        columnWidth = widthSize / 7;
        int height =  MeasureSpec.getSize(heightMeasureSpec);
        //高度 = 标题高度 + 星期高度 + 日期行数*每行高度
        //float height = titleHeight + weekHeight + (lineNum * oneHeight);
        Log.v(TAG, "444444标题高度："+titleHeight+" 星期高度："+weekHeight+" 每行高度："+oneHeight+
                " 行数："+ lineNum + "  \n控件高度："+height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                (int)height);

    }



    Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.bgw);
    int mBitWidth = temp.getWidth();
    int mBitHeight = temp.getHeight();
    //Rect mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
    //int left = mBitWidth / 2;

    //int top = mBitHeight / 2;
    Rect mDestRect = new Rect(0, 0, mBitWidth, mBitHeight);
    Paint bggpaint;


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(temp, null, mDestRect, bggpaint);

        drawMonth(canvas);
        drawWeek(canvas);
        try {
            drawDayAndPre(canvas);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        drawSyllabus(canvas);
        drawReminder(canvas);
    }


    private void drawSyllabus(Canvas canvas){
        Paint.setTextSize(SizeMonth);
        Paint.setColor(TextColorMonth);
        float textLen = FontUtil.getFontlength(Paint, MonthtoString(month));
        float textStart = (getWidth() - textLen)/ 2;
//        canvas.drawText("Syllabus", textStart,
//                MonthSpac+FontUtil.getFontLeading(Paint)+100, Paint);
        int len = (int)FontUtil.getFontlength(Paint, WEEK_STR[0]);
        canvas.drawText("Syllabus", (columnWidth - len)/2, titleHeight + FontUtil.getFontLeading(Paint)+1000, Paint);

    }

    private void drawReminder(Canvas canvas){
        Paint.setTextSize(SizeMonth);
        Paint.setColor(TextColorMonth);
        float textLen = FontUtil.getFontlength(Paint, MonthtoString(month));

        int len = (int)FontUtil.getFontlength(Paint, WEEK_STR[5]);
        canvas.drawText("Reminder", 5 * columnWidth+(columnWidth - len)/2, titleHeight + FontUtil.getFontLeading(Paint)+1000, Paint);

    }





    private int rowLStart, rowRStart, rowWidth;
    private void drawMonth(Canvas canvas){
        //背景
        bgPaint.setColor(BgMonth);
        RectF rect = new RectF(0, 0, getWidth(), titleHeight);
        canvas.drawRect(rect, bgPaint);
        //绘制月份
        Paint.setTextSize(SizeMonth);
        Paint.setColor(TextColorMonth);
        float textLen = FontUtil.getFontlength(Paint, MonthtoString(month));
        float textStart = (getWidth() - textLen)/ 2;
        canvas.drawText(MonthtoString(month), textStart,
                MonthSpac+FontUtil.getFontLeading(Paint), Paint);
        /*绘制左右箭头*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), RowL);
        int h = bitmap.getHeight();
        rowWidth = bitmap.getWidth();
        //float left, float top
        rowLStart = (int)(textStart-2*MonthRowSpac-rowWidth);
        canvas.drawBitmap(bitmap, rowLStart+MonthRowSpac , (titleHeight - h)/2, new Paint());

        canvas.drawBitmap(bitmap, rowLStart-3*MonthRowSpac , (titleHeight - h)/2, new Paint());
        canvas.drawBitmap(bitmap, rowLStart-3*MonthRowSpac-10 , (titleHeight - h)/2, new Paint());
        bitmap = BitmapFactory.decodeResource(getResources(), RowR);
        rowRStart = (int)(textStart+textLen);
        canvas.drawBitmap(bitmap, rowRStart+MonthRowSpac, (titleHeight - h)/2, new Paint());

        canvas.drawBitmap(bitmap, rowRStart+5*MonthRowSpac, (titleHeight - h)/2, new Paint());
        canvas.drawBitmap(bitmap, rowRStart+5*MonthRowSpac+10, (titleHeight - h)/2, new Paint());
    }
    /**绘制绘制星期*/
    private void drawWeek(Canvas canvas){
        //背景
        bgPaint.setColor(BgWeek);
        RectF rect = new RectF(0, titleHeight, getWidth(), titleHeight + weekHeight);
        canvas.drawRect(rect, bgPaint);
        //绘制星期：七天
        Paint.setTextSize(SizeWeek);

        for(int i = 0; i < WEEK_STR.length; i++){
            if(todayWeekIndex == i && isCurrentMonth){
                Paint.setColor(SelectWeekTextColor);
            }else{
                Paint.setColor(TextColorWeek);
            }
            int len = (int)FontUtil.getFontlength(Paint, WEEK_STR[i]);
            int x = i * columnWidth + (columnWidth - len)/2;
            canvas.drawText(WEEK_STR[i], x, titleHeight + FontUtil.getFontLeading(Paint), Paint);
        }
    }

    private void drawDayAndPre(Canvas canvas) throws ParseException {
        //Y坐标，第一行开始的坐标为标题高度+星期部分高度
        float top = titleHeight+weekHeight;
        //行
        for(int line = 0; line < lineNum; line++){
            if(line == 0){
                //第一行
                drawDayAndPre2(canvas, top, firstLineNum, 0, firstIndex);
            }else if(line == lineNum-1){
                //最后一行
                top = top+oneHeight;
                drawDayAndPre2(canvas, top, lastLineNum, firstLineNum+(line-1)*7, 0);
            }else{
                //满行
                top = top+oneHeight;
                drawDayAndPre2(canvas, top, 7, firstLineNum+(line-1)*7, 0);
            }
        }
    }


    /**具体某一行*/
    private void drawDayAndPre2(Canvas canvas, float top,
                                int count, int overDay, int startIndex) throws ParseException {
//        Log.e(TAG, "总共"+dayOfMonth+"天  有"+lineNum+"行"+ "  已经画了"+overDay+"天,下面绘制："+count+"天");
        //背景
        float topPre = top + LineSpac + dayHeight;
        bgPaint.setColor(BgDay);
        RectF rect = new RectF(0, top, getWidth(), topPre);
        canvas.drawRect(rect, bgPaint);

        bgPaint.setColor(BgPre);
        rect = new RectF(0, topPre, getWidth(), topPre + TextSpac + dayHeight);
        canvas.drawRect(rect, bgPaint);

        Paint.setTextSize(SizeDay);
        float dayTextLeading = FontUtil.getFontLeading(Paint);


        for(int i = 0; i<count; i++){
            int left = (startIndex + i)*columnWidth;
            int day = (overDay+i+1);

            Paint.setTextSize(SizeDay);


            //绘制完后将画笔还原，避免脏笔

            //选中的日期，如果是本月，选中日期正好是当天日期，下面的背景会覆盖上面绘制的虚线背景
            if(selectDay == day){
                //选中的日期字体白色，橙色背景
                Paint.setColor(SelectTextColor);
                bgPaint.setColor(SelectDateBg);
                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle(left+columnWidth/2, top + LineSpac +dayHeight/2, SelectRadius, bgPaint);
//                float textLen = FontUtil.getFontlength(Paint, MonthtoString(month));
//                float textStart = (getWidth() - textLen)/ 2;



            }else{
                Paint.setColor(TextColorDay);
            }

            int len = (int)FontUtil.getFontlength(Paint, day+"");
            int x = left + (columnWidth - len)/2;
            if(isCurrentMonth && currentDay == day){
                Paint.setColor(Today);
                canvas.drawText(day+"", x, top + LineSpac + dayTextLeading, Paint);
            }else {
                canvas.drawText(day+"", x, top + LineSpac + dayTextLeading, Paint);
            }

            //画事件数目
            Paint.setTextSize(SizeDay);
            Paint.setColor(TextColorMonth);
            Date thatday;
            int Events;
            thatday = StrToDate(Mmonth+"-"+day+" 00:00:00");
            if(Syllabusmap.get(thatday)!= null){

                Events = Syllabusmap.get(thatday);
                Log.w(TAG, "23333333333 "+thatday+" "+Events);
                x = left + (columnWidth - len)/2;
                canvas.drawText(String.valueOf(Events), x-10, topPre + TextSpac+30, Paint);
            }


        }

    }






    //焦点坐标
    private PointF focusPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                focusPoint.set(event.getX(), event.getY());
                try {
                    touchFocusMove(focusPoint, false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                focusPoint.set(event.getX(), event.getY());
                try {
                    touchFocusMove(focusPoint, false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                focusPoint.set(event.getX(), event.getY());
                try {
                    touchFocusMove(focusPoint, true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    private onClickListener listener;

    /**焦点滑动*/
    public void touchFocusMove(final PointF point, boolean eventEnd) throws ParseException {
        //Log.e(TAG, "555555点击坐标：("+point.x+" ，"+point.y+"),事件是否结束："+eventEnd);
        /**标题和星期只有在事件结束后才响应*/
        if(point.y<=titleHeight){
            //事件在标题上
            if(eventEnd && listener!=null){
                if(point.x>=rowLStart && point.x<(rowLStart+2*MonthRowSpac+rowWidth)){
//                    Log.w(TAG, "666666点击左箭头("+point.x+" ，"+point.y+")");
//                    Log.w(TAG,"rowLStart: "+rowLStart+" rowLStart+2*mMonthRowSpac+rowWidth: "+(rowLStart+2*mMonthRowSpac+rowWidth));
                    listener.DesMonth();
                }else if(point.x>rowRStart && point.x<(rowRStart + 2*MonthRowSpac+rowWidth)){
//                    Log.w(TAG, "777777点击右箭头("+point.x+" ，"+point.y+")");
//                    Log.w(TAG,"rowRStart: "+rowRStart+" rowRStart+2*mMonthRowSpac+rowWidth: "+(rowRStart+2*mMonthRowSpac+rowWidth));
                    listener.AddMonth();
                }else if(point.x>(rowRStart + 2*MonthRowSpac+rowWidth)){
                    listener.AddYear();
                }else if(point.x<rowLStart){
                    listener.DesYear();
                }
                else if(point.x>rowLStart && point.x <rowRStart){
                    listener.Title(MonthtoString(month), month);
                }
            }
        }else if(point.y<=(titleHeight+weekHeight)){
            //事件在星期部分
            if(eventEnd && listener!=null){
                //根据X坐标找到具体的焦点日期
                int xIndex = (int)point.x / columnWidth;
                //Log.e(TAG, "888888列宽："+columnWidth+"  999999x坐标余数："+(point.x / columnWidth));
                if((point.x / columnWidth-xIndex)>0){
                    xIndex += 1;
                }
                if(listener!=null){
                    listener.Week(xIndex-1, WEEK_STR[xIndex-1]);
                }
            }
        }else if(point.y<=titleHeight + FontUtil.getFontLeading(Paint)+800){
            touchDay(point, eventEnd);
        }
        else{
            int len = (int)FontUtil.getFontlength(Paint, WEEK_STR[5]);
            if(eventEnd && listener!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

                if(point.x>0 && point.x<200+(columnWidth - len)/2){
                    //calendar.setTimeInMillis( System.currentTimeMillis());
                    //String selectdate = MonthtoString(month)+"-"+selectDay;

                    //String begindate = "2019-02-18";
                    Date begindate = StrToDate("2019-02-18 00:00:00");
                    //Mmonth = Mmonth+"-"+selectDay;
                    Date selectdate = StrToDate(Mmonth+"-"+selectDay+" "+"00:00:00");
                    String shows = getDateStr(begindate);


                        Log.e(TAG, "888888888  "+shows+" "+Mmonth +" "+selectDay);
                        listener.Syllabus(begindate,selectdate,todayWeekIndex,  Mmonth);

//                    Date selectdate = StringDate(MonthtoString(month) +"-"+selectDay);
//                    Log.e(TAG, "888888  "+begindate+" "+getMonthStr(month) +"-"+selectDay);
//                    listener.Syllabus(begindate,selectdate,todayWeekIndex,  MonthtoString(month) +"-"+selectDay);
                }

                if(point.x>4 * columnWidth+(columnWidth - len)/2 && point.x<200+(4 * columnWidth+(columnWidth - len)/2)){

                    //listener.Reminder(calendar.get(Calendar.YEAR), selectDay,todayWeekIndex, MonthtoString(month) +"-"+selectDay);
                }
            }

        }
    }

    //控制事件是否响应
    private boolean responseWhenEnd = false;
    /**事件点在 日期区域 范围内*/
    private void touchDay(final PointF point, boolean eventEnd){
        //根据Y坐标找到焦点行
        boolean availability = false;  //事件是否有效
        //日期部分
        float top = titleHeight+weekHeight+oneHeight;
        int foucsLine = 1;
        while(foucsLine<=lineNum){
            if(top>=point.y){
                availability = true;
                break;
            }
            top += oneHeight;
            foucsLine ++;
        }
        if(availability){
            //根据X坐标找到具体的焦点日期
            int xIndex = (int)point.x / columnWidth;
            if((point.x / columnWidth-xIndex)>0){
                xIndex += 1;
            }
//            Log.e(TAG, "列宽："+columnWidth+"  x坐标余数："+(point.x / columnWidth));
            if(xIndex<=0)
                xIndex = 1;   //避免调到上一行最后一个日期
            if(xIndex>7)
                xIndex = 7;   //避免调到下一行第一个日期
//            Log.e(TAG, "事件在日期部分，第"+foucsLine+"/"+lineNum+"行, "+xIndex+"列");
            if(foucsLine == 1){
                //第一行
                if(xIndex<=firstIndex){
                    //Log.e(TAG, "101010点到开始空位了");
                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(xIndex-firstIndex, eventEnd);
                }
            }else if(foucsLine == lineNum){
                //最后一行
                if(xIndex>lastLineNum){
                    //Log.e(TAG, "11111111111点到结尾空位了");
                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
                }
            }else{
                setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
            }
        }else{
            //超出日期区域后，视为事件结束，响应最后一个选择日期的回调
            setSelectedDay(selectDay, true);
        }
    }
    /**设置选中的日期*/
    private void setSelectedDay(int day, boolean eventEnd){
        //Log.w(TAG, "121212选中："+day+"  事件是否结束"+eventEnd);
        selectDay = day;
        invalidate();
        if(listener!=null && eventEnd && responseWhenEnd && lastSelectDay!=selectDay) {
            lastSelectDay = selectDay;
            listener.Day(selectDay, Mmonth);
        }
        responseWhenEnd = !eventEnd;
    }

    /****************************事件处理↑↑↑↑↑↑↑****************************/


    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
    }



    public void monthChange(int change){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        calendar.add(Calendar.MONTH, change);
        setMonth(MonthtoString(calendar.getTime()));

        invalidate();
    }

    public void yearChange(int change){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        //Log.w(TAG, "?????????????????????点击右箭头");
        calendar.add(Calendar.YEAR, change);
        setMonth(MonthtoString(calendar.getTime()));

        invalidate();
    }


    public void setOnClickListener(onClickListener listener){
        this.listener = listener;
    }
    interface onClickListener{
        public abstract void DesMonth();
        public abstract void AddMonth();
        public abstract void AddYear();
        public abstract void DesYear();
        public abstract void Title(String monthStr, Date month);
        public abstract void Week(int weekIndex, String weekStr);
        public abstract void Day(int day, String dayStr/*,MainActivity.DayFinish finish*/);
        public abstract void Syllabus(Date begindate, Date selectdate, int todayweekindex, String dayStr) throws ParseException;
 //       public abstract void Reminder(int month, int day,int todayweekindex, String dayStr);

    }

    //将主活动存下的map放到这里
    public void StoreEvents (HashMap<Date, Integer> FromMain){
        Syllabusmap = FromMain;
        //Date tempdate= "2019-05-17 00:00:00";
        //Log.d("MainActivity_testday", "5555555555555 "+tempdate+" i "+i);
        invalidate();
    }




}
