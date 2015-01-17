package qsc.epointhelper.mywebservice;

import android.content.Context;

import qsc.epointhelper.R;
import qsc.epointhelper.comm.EncryptUtil;


/**
 * Created by 士川 on 2014-11-19.
 */
public class TokenHelper {
    public    Context mcontext;
    public TokenHelper(Context context)
    {
        this.mcontext = context;
    }
    public  String NewToken()
    {
        String appKey =mcontext.getResources().getString(R.string.appKey);
        String appSecret =mcontext.getResources().getString(R.string.appSecret);
        return EncryptUtil.getToken(appKey, appSecret);
    }
}
