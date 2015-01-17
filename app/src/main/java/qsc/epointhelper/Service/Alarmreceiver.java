package qsc.epointhelper.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by 士川 on 2014-11-25.
 */
public class Alarmreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("qsc/epointhelper/Service/myService.java")) {
            Time t = new Time();
            t.setToNow();
            if (t.hour > 16 && t.hour < 23) //下午5点到晚上10点期间进行服务的操作
            {
                final Intent i = new Intent();
                final Context mcontext = context;
                i.setClass(context, myService.class);

                // 启动service
                // 多次调用startService并不会启动多个service 而是会多次调用onStart
                int second = new Random().nextInt(300);
                Log.d("Alarmreceiver", "**********收到广播消息，延迟" + second + "秒后执行***********");
                new Handler().postDelayed(new Runnable() { //5分钟随机执行
                    public void run() {
                        //execute the task
                        mcontext.startService(i);
                        Log.d("Alarmreceiver", "**********启动myService服务完成***********");
                    }
                }, second * 1000);
            } else {
                //否则停止服务
                Log.v("=========", "*****收到广播消息 当前时间 " + t.hour + ":" + t.minute + ":" + t.second + " *****" + "时间不符，停止服务");

            }

        }
    }
}
