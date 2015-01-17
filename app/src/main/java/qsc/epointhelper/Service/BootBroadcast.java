package qsc.epointhelper.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by 士川 on 2014-11-25.
 */
public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent mintent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(mintent.getAction())) {

            Log.d("qsc_EpoihtHelper","*******收到开机消息***********");
            // 启动完成

          /*  PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                    intent, 0);
            long firstime = SystemClock.elapsedRealtime();
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            // 1小时一个周期，不停的发送广播
            am.setRepeating(AlarmManager.RTC_WAKEUP,firstime,
                    3600000, sender);*/

            SharedPreferences sp =context.getSharedPreferences("UserData", 4);

            String KQHelper;
            String ZRHelper;
            try{
                KQHelper =  sp.getString("KQHelper","1");
                ZRHelper = sp.getString("ZRHelper","1");
            }catch (Exception e)
            {
                KQHelper ="1";
                ZRHelper ="1";
            }
            //零报告定时服务
            if(ZRHelper.equals("1")) {
                Intent intent = new Intent(context, Alarmreceiver.class);
                intent.setAction("qsc/epointhelper/Service/myService.java");
                MyAlarmManager myAlarmManager = new MyAlarmManager(context, intent);
                myAlarmManager.setZReportAlarm();
            }
            //考勤定时服务
            if (KQHelper.equals("1")) {
                Intent intentKQ = new Intent(context, KaoQinAlarmReceiver.class);
                intentKQ.setAction("qsc/epointhelper/Service/myKaoQinService.java");
                MyAlarmManager myAlarmManagerKQ = new MyAlarmManager(context, intentKQ);

                myAlarmManagerKQ.setKaoQinAlarm();
            }
        }
    }

}
