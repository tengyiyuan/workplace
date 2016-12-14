package com.toplion.cplusschool.Utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.ab.util.AbDateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by wang
 * on 2016/9/8.
 * 日历提醒
 */
public class CalendarUtils {

    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";

    //为了兼容不同版本的日历,2.2以后url发生改变
    static {
        if (Build.VERSION.SDK_INT >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";

        } else {
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
    }

    /**
     * @param context
     * @param title
     * @param des
     * @param cId
     * @author wangshengbo
     * @createTime 2016年9月8日 下午5:30:11
     */
    public static long addCalendar(Context context, String cId, String title, String date, String address, String des) {
        //查询将返回所有日历，包括那些平时不会用到的
        long id = -1;
        try {
            String[] projection = new String[]{"_id", "name"};
            Uri calendars = Uri.parse(calanderURL);
            Cursor managedCursor = context.getContentResolver().query(calendars, null, null, null, CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " ASC ");

            String calId = null;
            //现在检索的日历列表,可以遍历的结果如下
            if (managedCursor.moveToFirst()) {
                int nameColumn = managedCursor.getColumnIndex("name");
                int idColumn = managedCursor.getColumnIndex("_id");
                do {
                    String calName = managedCursor.getString(nameColumn);
                    calId = managedCursor.getString(idColumn);
                } while (managedCursor.moveToNext());
            }
            Log.e("calId",calId);
            if (TextUtils.isEmpty(calId)) {
                return -1;
            }
            //添加一个条目到特定的日历，我们需要配置一个日历项插入使用与ContentValues如下：
            ContentValues event = new ContentValues();
            //事件插入日历标识符
            event.put("calendar_id", calId);

            //活动的标题，描述和位置领域的一些基本信息。
            event.put(CalendarContract.Events.TITLE, title);
            event.put(CalendarContract.Events.DESCRIPTION, des);
            event.put(CalendarContract.Events.EVENT_LOCATION, address);

            //设置事件的开始和结束的信息如下
            Calendar mCalendar = Calendar.getInstance();
            try {
                mCalendar.setTime(AbDateUtil.getDateByFormat(date, "yyyy-MM-dd HH:mm"));
            } catch (Exception e) {
                mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            }
            long start = mCalendar.getTime().getTime();
            mCalendar.add(Calendar.HOUR_OF_DAY, 1);
            long end = mCalendar.getTime().getTime();
            event.put("dtstart", start);
            event.put("dtend", end);
            //设置一个全天事件的条目
            //event.put("allDay", 1); // 0 for false, 1 for true

            //事件状态暂定(0)，确认(1)或取消(2)
            event.put("eventStatus", 1);

            //控制是否事件触发报警，提醒如下
            event.put("hasAlarm", 1); // 0 for false, 1 for true

            //设置时区,否则会报错
            event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            //一旦日历事件配置正确，我们已经准备好使用ContentResolver插入到相应的开放新日历的日历事件项：
            Uri eventsUri = Uri.parse(calanderEventURL);
            Uri newEvent = context.getContentResolver().insert(eventsUri, event);
            //设置什么时候提醒
            id = Long.parseLong(newEvent.getLastPathSegment());
            ContentValues values = new ContentValues();
            values.put("event_id", id);
            values.put("method", 1);
            //提前10分钟有提醒
            values.put("minutes", 15);
            context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    /**
     * 查询日历事件：
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean queryCalendarById(Context context, String eventID) {
        try{
            ContentResolver cr = context.getContentResolver();
            Uri eventsUri = Uri.parse(calanderEventURL);
            Cursor cur = cr.query(eventsUri, null, null, null, null);
            while (cur.moveToNext()) {
                String title = null;
                String id = cur.getString(cur.getColumnIndex(CalendarContract.Events._ID));
                title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
                String description = cur.getString(cur.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                Log.i("", "Event:  " + title + "     des :   " + description);
                if (eventID.equals(id)) {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 删除提醒事件
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static int deleteCalendar(Context context, String eventID) {
        int rows = -1;
        try{
            Uri deleteUri = ContentUris.withAppendedId(Uri.parse(calanderEventURL), Long.parseLong(eventID));
            rows = context.getContentResolver().delete(deleteUri, null, null);
        }catch (Exception e){
            e.printStackTrace();
            rows = -1;
        }
        return rows;
    }
}
