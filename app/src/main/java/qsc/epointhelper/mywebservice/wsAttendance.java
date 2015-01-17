package qsc.epointhelper.mywebservice;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import qsc.epointhelper.DBHelper.UserData;
import qsc.epointhelper.R;
import qsc.epointhelper.comm.EncryptUtil;

/**
 * Created by 士川 on 2014-11-22.
 */
public class wsAttendance {
    public String operate;
    public String UserGuid;
    public Activity activity;
    public Context context;
    public Handler mhander;

    public wsAttendance(Context context, Activity mActivity, Handler mHandler) {
        this.mhander = mHandler;
        this.context = context;
        this.activity = mActivity;
        getUserGuid();
    }


    public void KaoQin(String Location) {
        MyWebService ws = new MyWebService(mhander, operate);
        ws.methodnames = "MobileWorkAttendanceInsert";
        ws.url = "http://oa.epoint.com.cn/WebServiceManage/EMWebService.asmx";
        ws.namespace = "http://tempuri.org/";
        ws.userguid = UserGuid;
        ws.validate = getNewToken();
        ws.Address = Location;
        ws.doStart();
    }

    public void getKaoQinRec()
    {
        MyWebService ws = new MyWebService(mhander, operate);
        ws.url = "http://oa.epoint.com.cn/WebServiceManage/EMWebService.asmx";
        ws.namespace="http://tempuri.org/";
        ws.methodnames="GetWorkAttendanceRecord";
        ws.validate = getNewToken();
        ws.userguid = UserGuid;
        ws.doStart();
    }

    //获取新的验证码
    public String getNewToken() {
        String appKey = context.getResources().getString(R.string.appKey);
        String appSecret = context.getResources().getString(R.string.appSecret);
        return EncryptUtil.getToken(appKey, appSecret);

    }
    //获取UserGuid
    protected void getUserGuid() {
        UserData userData = new UserData(activity);
        // UserName = userData.getData("UserName");
        UserGuid = userData.getData("UserGuid");
    }

}
