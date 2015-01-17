package qsc.epointhelper.mywebservice;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import qsc.epointhelper.DBHelper.UserData;
import qsc.epointhelper.R;
import qsc.epointhelper.comm.EncryptUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by 士川 on 2014-11-19.
 */
public class wsZeroReport {

    public String operate = "ValidateAndXML";
    public Context context;
    public Activity activity;

    //public String UserName;
    public String UserGuid;

    Boolean IsLogin = false;
    public Handler mhander ;
    TokenHelper token = new TokenHelper(context);
    public wsZeroReport(Context context, Handler mHandler,Activity activity )
    {
        this.mhander = mHandler;
        this.context = context;
        this.activity = activity;
        getUserGuid();
    }

    protected void getUserGuid()
    {
        UserData userData = new UserData(activity);
       // UserName = userData.getData("UserName");
        UserGuid = userData.getData("UserGuid");
    }

    /**
     * 添加零报告
     */
    public void insertZeroReport(String date)
    {
        //调用webservice


        MyWebService ws = new MyWebService(mhander, operate);
        ws.methodnames="ZReport_Insert";
        ws.url="http://oa.epoint.com.cn/ZreportServiceV2/ZreportServer.asmx";
        ws.namespace="http://tempuri.org/";

        String rowGuid = UUID.randomUUID().toString();
        //String date =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String isZero = "1";
        String content = "";

        String parasXML = String.format("<?xml version=\"1.0\" encoding=\"gb2312\"?><paras><RowGuid>%s</RowGuid><UserGuid>%s</UserGuid><RecordData>%s</RecordData><Content>%s</Content><IsNullProblem>%s</IsNullProblem><Status>0</Status><OUGuid></OUGuid></paras>"
                , new Object[] { rowGuid, UserGuid, date, content,isZero });

        ws.parasXML =parasXML;
        //ws.validate_And_Parasxml();
        //ws.validate = token.NewToken();
        ws.validate = getNewToken();
        //ws.start();

        //Log.d("epoint_insertZeroReport_parasXML", parasXML);
        ws.doStart();
    }
    /**
     * 获取今日零报告
     */
    public void getTodayZReport()
    {
        //operate="ZReportRec";
        String enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        getZeroReport(enddate,enddate);
    }
    /**
     * 获取零报告历史数据
     */
    public void getZeroReport(String startDate,String endDate)
    {

        MyWebService ws = new MyWebService(mhander, operate);

        ws.methodnames = "ZReport_UserView";
        ws.url = "http://oa.epoint.com.cn/ZreportServiceV2/ZreportServer.asmx";
        ws.namespace = "http://tempuri.org/";
//        Calendar sCalendar = Calendar.getInstance();
//        sCalendar.add(Calendar.DAY_OF_YEAR, -4);
//        Date sDate = sCalendar.getTime();
//        String startdate = new SimpleDateFormat("yyyy-MM-dd").format(sDate);
       // String enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        //ws.validate = token.NewToken();

        ws.validate = getNewToken();
        ws.parasXML = String
                .format("<?xml version=\"1.0\" encoding=\"gb2312\"?><paras><UserGuid>%s</UserGuid><StartDate>%s</StartDate><EndDate>%s</EndDate><IsShowContent>1</IsShowContent></paras>",
                        new Object[] { UserGuid, startDate, endDate });

        //ws.validate_And_Parasxml();
        //ws.start();
        ws.doStart();
    }


    public String getNewToken()
    {
        String appKey =context.getResources().getString(R.string.appKey);
        String appSecret =context.getResources().getString(R.string.appSecret);
        return EncryptUtil.getToken(appKey, appSecret);

    }

}
