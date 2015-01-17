package qsc.epointhelper.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import qsc.epointhelper.DBHelper.UserData;
import qsc.epointhelper.MainActivity;
import qsc.epointhelper.R;
import qsc.epointhelper.comm.EncryptUtil;
import qsc.epointhelper.comm.XmlHelper;
import qsc.epointhelper.mywebservice.MyWebService;
import qsc.epointhelper.mywebservice.wsZeroReport;

/**
 * Created by 士川 on 2014-11-25.
 */
public class myService extends Service {
    String operate;
    String UserGuid;
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
        Log.v("=========", "***** 签到服务 myService *****: onStart");
        ZeroReport();

    }

    public void ZeroReport(){




            //Todo 服务签到
            if (!checkTodayZReport()) //今天零报告还没签
            {
                Log.v("=========", "***** 今天零报告还没签 *****");
                //签到
                SharedPreferences sp = getSharedPreferences("UserData", MODE_MULTI_PROCESS);
                UserGuid = sp.getString("UserGuid", "");
                reportZero();

            } else {
                Log.v("=========", "***** 今天已经是零报告 *****:停止服务");
                this.stopSelf();
            }


    }


    @Override
    public void onDestroy(){
        Log.v("=========", "***** 签到服务 *****: onDestroy");
    }


    /**
     * 判断今日零报告是否已经填写
     * @return
     */
    public boolean checkTodayZReport()
    {
        SharedPreferences sp = getSharedPreferences("UserData",MODE_MULTI_PROCESS);

        String date =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String ZReportDay = sp.getString("ZReportDay","");
        if(date.equals(ZReportDay)){ //读取到今日已是零报告
            //零报告：
            return true;
        }
        else return false;
    }

    public void reportZero() {
        operate = "insertZero";

        Log.v("=========", "***** insertZero *****: insertZero");


        insertZeroReport();

    }
    public void insertZeroReport()
    {
        //调用webservice

        Log.v("=========", "***** 调用webservice *****: 开始");

        MyWebService ws = new MyWebService(mHandler,  "ValidateAndXML");
        ws.methodnames="ZReport_Insert";
        ws.url="http://oa.epoint.com.cn/ZreportServiceV2/ZreportServer.asmx";
        ws.namespace="http://tempuri.org/";

        String rowGuid = UUID.randomUUID().toString();
        String date =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String isZero = "1";
        String content = "";

        String parasXML = String.format("<?xml version=\"1.0\" encoding=\"gb2312\"?><paras><RowGuid>%s</RowGuid><UserGuid>%s</UserGuid><RecordData>%s</RecordData><Content>%s</Content><IsNullProblem>%s</IsNullProblem><Status>0</Status><OUGuid></OUGuid></paras>"
                , new Object[] { rowGuid, UserGuid, date, content,isZero });

        ws.parasXML =parasXML;

        ws.validate = getNewToken();

        ws.doStart();

        Log.v("=========", "***** 调用webservice *****: 开始请求");

    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = msg.getData().getString("value");
            if (operate == "insertZero") {
                Log.v("=========", "***** 调用webservice *****: 零报告返回");
                insertZReportReturn(result);
            } else if (operate == "ZReportLog") {
                getZReportResult(result);
                Log.v("=========", "***** 调用webservice *****: 查询零报告状态");
                // Log.d("text3_getZeroReportRecToday_Result",result);
            }else if(operate=="doKaoQin"){
                //TODO 处理考勤结果
                //getKaoQinList(result);
            }else if(operate=="getKaoQinRec"){
                //TODO 处理考勤记录结果
                //getKaoQinList(result);
            }
        }

    };

    public String getNewToken()
    {
        String appKey =getResources().getString(R.string.appKey);
        String appSecret =getResources().getString(R.string.appSecret);
        return EncryptUtil.getToken(appKey, appSecret);
    }

    //插入零报告之后的操作：重新查询结果
    protected void insertZReportReturn(String resultXML)
    {
        Toast.makeText(getBaseContext(), "查询零报告中……", Toast.LENGTH_SHORT).show();
        operate="ZReportLog";
        getZeroReport();
    }

    public void getZeroReport()
    {

        MyWebService ws = new MyWebService(mHandler,  "ValidateAndXML");

        ws.methodnames = "ZReport_UserView";
        ws.url = "http://oa.epoint.com.cn/ZreportServiceV2/ZreportServer.asmx";
        ws.namespace = "http://tempuri.org/";
//        Calendar sCalendar = Calendar.getInstance();
//        sCalendar.add(Calendar.DAY_OF_YEAR, -4);
//        Date sDate = sCalendar.getTime();
//        String startdate = new SimpleDateFormat("yyyy-MM-dd").format(sDate);
         String enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        ws.validate = getNewToken();
        ws.parasXML = String
                .format("<?xml version=\"1.0\" encoding=\"gb2312\"?><paras><UserGuid>%s</UserGuid><StartDate>%s</StartDate><EndDate>%s</EndDate><IsShowContent>1</IsShowContent></paras>",
                        new Object[] { UserGuid, enddate, enddate });

        ws.doStart();
    }


    /**
     * 解析零报告历史记录的返回值，显示在页面上。
     * @param str
     */
    protected void getZReportResult(String str)
    {
        try {
            List<HashMap<String,String>> list = XmlHelper.parserXml(str, "Report", "RowGuid", "RecordData", "IsNullProblem", "Status", "Content");
            for(HashMap<String,String> map:list){
                String RecordDate=map.get("RecordData").substring(0, 10);
                //判断今日零报告是否填写
                if (map.get("RowGuid") != "") {
                    if (map.get("IsNullProblem").equals("1")) {
                        //零报告
                        sendNotification("今日零报告已提交！",RecordDate);
                        //结果为零报告的情况下，将结果保存到配置文件中
                        SharedPreferences sp = getSharedPreferences("UserData", MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ZReportDay", RecordDate);
                        editor.commit();
                        Log.v("=========", "***** 发送通知结束 *****: 今日零报告");
                    }else{
                       //已填 </font>";
                       // sendNotification("已填非零报告！",RecordDate);
                        Log.v("=========", "***** 发送通知结束 *****: 已填非零报告");

                    }
                }else {
                   // 未填 </font>";
                  //  sendNotification("今日零报告尚未填写！",RecordDate);
                    Log.v("=========", "***** 发送通知结束 *****: 今日零报告尚未填写");

                }
            }
        }
        catch(Exception e)
        {
            //"发生错误！");

            sendNotification("零报告服务发生错误！",e.getMessage());
            Log.v("=========", "***** 发送通知结束 *****: 零报告服务发生错误"+e.getMessage()+str);
        }


        Log.v("=========", "***** 服务停止 *****:");
        this.stopSelf();


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
