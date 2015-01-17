package qsc.epointhelper.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

import qsc.epointhelper.MainActivity;
import qsc.epointhelper.R;

/**
 * Created by 士川 on 2014-11-29.
 */
public class myKaoQinService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onStart(Intent intent, int startId) {

        // 这里可以做Service该做的事

       /* SimpleDateFormat shijian = new SimpleDateFormat("yyyy年MM月dd日"+"hh:mm:ss");
        String date = shijian.format(new Date());
        Log.v("=========", "***** 当前时间 Date（）  *****"+date);
*/

        Time time = new Time();
        time.setToNow();
        if (time.hour < 12) {
            sendNotification("考勤提醒！", "点击打开软件进行签到");
            Log.v("=========", "***** 考勤提醒 *****:"+time.toString());

        } else {
            sendNotification("考勤提醒！", "点击打开软件进行签到");
            Log.v("=========", "***** 考勤提醒 *****:"+time.toString());

        }
        this.stopSelf();
    }

    @Override
    public void onDestroy(){
        Log.v("=========", "***** 考勤提醒 *****: onDestroy");
    }
    protected void sendNotification(String Title,String Detail)
    {
        //定义NotificationManager
        String ns = Context.NOTIFICATION_SERVICE;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

        //定义通知栏展现的内容信息

        int icon = R.drawable.ic_notification;

        CharSequence tickerText = "考勤伴侣"; //"通知栏标题

        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);


        //定义下拉通知栏时要展现的内容信息
        Context context = getApplicationContext();

        CharSequence contentTitle = Title; //"我的通知栏标展开标题";
        CharSequence contentText = Detail;// "我的通知栏展开详细内容";

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);

    }
}
