package com.memo.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class noteUtils {
    public static String dateFromLong(long time){
        DateFormat format = new SimpleDateFormat("EEE, dd MM yyyy 'at' hh:mm aaa", Locale.CHINA);
        return format.format(new Date(time));
    }
}
