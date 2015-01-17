package qsc.epointhelper.mywebservice;

import android.content.Context;
import android.os.Handler;

import qsc.epointhelper.R;
import qsc.epointhelper.comm.EncryptUtil;
import qsc.epointhelper.comm.StringHelp;

/**
 * Created by 士川 on 2014-11-17.
 */
public class wsDoLogin {

    public String operate = "Login";
    public Context context;

    Boolean IsLogin = false;
    public Handler mhander ;
    public wsDoLogin(Context context, Handler mHandler ){
        this.mhander = mHandler;
        this.context = context;
    }



    public void doLogin(String UserName,String Password ) {
        Password = EncryptUtil.authPassword(Password);

//        String url = "http://oa.epoint.com.cn/EpointOAWebservice8V2/OAWebService.asmx";
//        String namespace = "http://tempuri.org/";

        String str4 = StringHelp.filterXmlString(UserName);
        String str5 = StringHelp.filterXmlString(Password);
        String str6 = String.format("<?xml version=\"1.0\" encoding=\"gb2312\"?><paras><LoginID>%s</LoginID><Password>%s</Password></paras>", new Object[]{str4, str5});

        operate="ValidateAndXML";
        MyWebService ws = new MyWebService(mhander, operate);

        ws.url="http://oa.epoint.com.cn/EpointOAWebservice8V2/OAWebService.asmx";
        ws.namespace="http://tempuri.org/";
        ws.methodnames="UserLogin2";
        ws.parasXML=str6;
        ws.validate =getNewToken();
        ws.doStart();
    }

    public String getNewToken()
    {
        String appKey =context.getResources().getString(R.string.appKey);
        String appSecret =context.getResources().getString(R.string.appSecret);
        return EncryptUtil.getToken(appKey, appSecret);

    }


}
