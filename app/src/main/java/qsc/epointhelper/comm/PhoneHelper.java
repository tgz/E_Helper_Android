package qsc.epointhelper.comm;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class PhoneHelper {
		
	  public static void Call(Context paramContext, String paramString)
	  {
	    Intent localIntent = new Intent();
	    localIntent.setAction("android.intent.action.CALL");
	    localIntent.setData(Uri.parse("tel:" + paramString));
	    paramContext.startActivity(localIntent);
	  }

	  public static boolean ExistAppInSystem(Context paramContext, String paramString)
	  {
	    try
	    {
	      PackageInfo localPackageInfo2 = paramContext.getPackageManager().getPackageInfo(paramString, 0);
	      PackageInfo  localPackageInfo1 = localPackageInfo2;
	      if (localPackageInfo1 == null)
	        return false;
	    }
	    catch (NameNotFoundException localNameNotFoundException)
	    {
	      while (true)
	      {
	        localNameNotFoundException.printStackTrace();
	        PackageInfo localPackageInfo1 = null;
	      }
	    }
	    return true;
	  }

	  public static boolean ExistSDCard()
	  {
	    return Environment.getExternalStorageState().equals("mounted");
	  }

	  public static int getCallState(Context paramContext)
	  {
	    return ((TelephonyManager)paramContext.getSystemService("phone")).getCallState();
	  }

	  public static CellLocation getCellLocation(Context paramContext)
	  {
	    return ((TelephonyManager)paramContext.getSystemService("phone")).getCellLocation();
	  }

	  public static String getDeviceId(Context paramContext)
	  {
	    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
	    if ((localTelephonyManager.getDeviceId() == null) || ("".equals(localTelephonyManager.getDeviceId())))
	      return getMacAddress(paramContext);
	    return localTelephonyManager.getDeviceId();
	  }

	  public static String getMacAddress(Context paramContext)
	  {
	    try
	    {
	      String str = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
	      return str;
	    }
	    catch (Exception localException)
	    {
	    }
	    return "";
	  }

	  public static PackageInfo getPackageInfo(Context paramContext)
	  {
	    PackageManager localPackageManager = paramContext.getPackageManager();
	    try
	    {
	      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(paramContext.getPackageName(), 0);
	      return localPackageInfo;
	    }
	    catch (NameNotFoundException localNameNotFoundException)
	    {
	      localNameNotFoundException.printStackTrace();
	    }
	    return null;
	  }

	  public static String getPackageName(Context paramContext)
	  {
	    return getPackageInfo(paramContext).packageName;
	  }

	  public static int getPhoneHeight(Context paramContext)
	  {
	    return ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getHeight();
	  }

	  public static int getPhoneWidth(Context paramContext)
	  {
	    return ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getWidth();
	  }

	  public static String getVersionName(Context paramContext)
	  {
	    PackageManager localPackageManager = paramContext.getPackageManager();
	    try
	    {
	      PackageInfo localPackageInfo2 = localPackageManager.getPackageInfo(paramContext.getPackageName(), 0);
	      PackageInfo localPackageInfo1 = localPackageInfo2;
	      return localPackageInfo1.versionName;
	    }
	    catch (NameNotFoundException localNameNotFoundException)
	    {
	      while (true)
	      {
	        localNameNotFoundException.printStackTrace();
	        PackageInfo localPackageInfo1 = null;
	      }
	    }
	  }

	  public static void invokeSystemBrowser(Context paramContext, String paramString)
	  {
	    Intent localIntent = new Intent();
	    localIntent.setAction("android.intent.action.VIEW");
	    localIntent.setData(Uri.parse(paramString));
	    paramContext.startActivity(localIntent);
	  }

	  public static void sendMsgUsePhoneSelf(Context paramContext, String paramString1, String paramString2)
	  {
	    Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + paramString1));
	    localIntent.putExtra("sms_body", paramString2);
	    paramContext.startActivity(localIntent);
	  }
}
