package qsc.epointhelper.Service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by 士川 on 2014-11-29.
 */
public class MyAlarmManager {

    private Context context;
    private Intent intent;

    public MyAlarmManager(Context context,Intent intent){
        this.context=context;
        this.intent=intent;
    }

    /**
     * 开启全部提醒
     */
    public void startAll()
    {
        setKaoQinAlarm();
        setZReportAlarm();
    }

    /**
     * 取消全部提醒
     */
    public void cancleAll()
    {
        cancleKaoQinAlarm();
        cancleZReportAlarm();
    }

    /**
     * 设置零报告提醒
     */
    public void setZReportAlarm(){

        PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        android.app.AlarmManager am = (android.app.AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        //calendar.getTimeInMillis()
        //long firstime = SystemClock.elapsedRealtime();

        // 1小时一个周期，不停的发送广播
        Time firsttime = new Time();
        firsttime.setToNow();
        firsttime.set(16, 00, 17, firsttime.monthDay, firsttime.month, firsttime.year);

        am.setRepeating(android.app.AlarmManager.RTC_WAKEUP, firsttime.toMillis(false),
                3600000, sender);
        Log.d("setZReportAlarm", "*******设置零报告提醒***********");
//        Toast.makeText(context, "已开启服务！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消零报告提醒
     */
    public void cancleZReportAlarm(){
        PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        android.app.AlarmManager am = (android.app.AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        Log.d("cancleZReportAlarm", "*******取消零报告提醒***********");
    }

    /**
     * 设置考勤提醒
     */
    public  void setKaoQinAlarm()
    {
        SharedPreferences sp =context.getSharedPreferences("UserData", 4);

        int AmHour;
        int AmMinute;
        int PmHour;
        int PmMinute;
        try{
            AmHour = Integer.valueOf( sp.getString("AlarmAmHour","8"));
            AmMinute = Integer.valueOf(  sp.getString("AlarmAmMinute","20"));
            PmHour =  Integer.valueOf(  sp.getString("AlarmPmHour","18"));
            PmMinute = Integer.valueOf(  sp.getString("AlarmPmMinute","00"));
        }catch (Exception e)
        {
            AmHour = 8;
            AmMinute = 20;
            PmHour =  18;
            PmMinute = 0;
        }

        Time time = new Time();
        Time now = new Time();
        now.setToNow();
        time.setToNow();
        time.set(15, AmMinute, AmHour, now.monthDay, now.month, now.year);
        if(time.after(now)) {
            //time.set(15, 20, 8, time.monthDay, time.month, time.year);//当天的8点20

        }else {
            time.set(15, AmMinute, AmHour, time.monthDay+1, time.month, time.year);//明天的8点20
        }

        //上午的提醒
        PendingIntent monring = PendingIntent.getBroadcast(context, 8,
                intent,0);

        android.app.AlarmManager am = (android.app.AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 24小时一个周期，不停的发送广播
        am.setRepeating(android.app.AlarmManager.RTC_WAKEUP,time.toMillis(false),
                24*3600000, monring);
        Log.d("setKaoQinAlarm","*******设置考勤提醒(上午)***********"+time.toString());


        //下午的提醒：
        PendingIntent afternoon = PendingIntent.getBroadcast(context, 18,
                intent,0);
        time.set(15, PmMinute, PmHour, now.monthDay, now.month, now.year);//当天的18点
        if(time.after(now)) {
            //time.set(15, 20, 8, time.monthDay, time.month, time.year);//当天的8点20

        }else {
            time.set(15,PmMinute, PmHour, time.monthDay+1, time.month, time.year);//明天的8点20
        }
        am.setRepeating(android.app.AlarmManager.RTC_WAKEUP, time.toMillis(false),
                24 * 3600000, afternoon);
        Log.d("setKaoQinAlarm","*******设置考勤提醒（下午）***********"+time.toString());
    }

    /**
     * 取消考勤提醒
     */
    public void cancleKaoQinAlarm()
    {
        PendingIntent monring = PendingIntent.getBroadcast(context, 8,
                intent,0);
        android.app.AlarmManager am = (android.app.AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.cancel(monring);

        //下午的提醒：
        PendingIntent afternoon = PendingIntent.getBroadcast(context, 18,
                intent,0);
        am.cancel(afternoon);
        Log.d("cancleKaoQinAlarm","*******取消考勤提醒***********");
    }
}
