package qsc.epointhelper.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

/**
 * Created by 士川 on 2014-11-29.
 */
public class KaoQinAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("qsc/epointhelper/Service/myKaoQinService.java")) {
            final Intent i = new Intent();
            final Context mcontext=context;

            Log.d("KaoQinAlarmReceiver", "**********收到广播消息***********:");

            i.setClass(context, myKaoQinService.class);
            // 启动service
            // 多次调用startService并不会启动多个service 而是会多次调用onStart
            /*
            int second = new Random().nextInt(300);
            //5分钟随机执行
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
                    mcontext.startService(i);
                    Log.d("EpoihtHelper_Handler", "**********启动服务***********");
                }
            }, second*1000);*/
            mcontext.startService(i);
            Log.d("KaoQinAlarmReceiver", "**********启动myKaoQinService服务完成***********");
        }
    }
}
