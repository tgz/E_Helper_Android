package qsc.epointhelper.comm;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {


	  public static String DateFormat_1 = "yyyy-MM-dd hh:mm:ss";
	  public static String DateFormat_24 = "yyyy-MM-dd HH:mm:ss";

	  public static String AddZero(int paramInt)
	  {
	    if ((paramInt >= 0) && (paramInt <= 9))
	      return "0" + paramInt;
	    return String.valueOf(paramInt);
	  }

	  public static String Num2Haizi_Week(int paramInt)
	  {
	    switch (paramInt)
	    {
	    default:
	      return "";
	    case 1:
	      return "星期一";
	    case 2:
	      return "星期二";
	    case 3:
	      return "星期三";
	    case 4:
	      return "星期四";
	    case 5:
	      return "星期五";
	    case 6:
	      return "星期六";
	    case 0:
	    }
	    return "星期日";
	  }

	  public static String Num2Haizi_Week_HTML_Color(int paramInt)
	  {
	    switch (paramInt)
	    {
	    default:
	      return "";
	    case 1:
	      return "星期一";
	    case 2:
	      return "星期二";
	    case 3:
	      return "星期三";
	    case 4:
	      return "星期四";
	    case 5:
	      return "星期五";
	    case 6:
	      return "<font color=red>星期六</font>";
	    case 0:
	    }
	    return "<font color=red>星期日</font>";
	  }

	  public static String convertDate(Date paramDate, String paramString)
	  {
	    if (paramDate != null)
	      return new SimpleDateFormat(paramString).format(paramDate);
	    return "";
	  }

	  public static Date convertString2Date(String paramString1, String paramString2)
	  {
	    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(paramString2);
	    try
	    {
	      Date localDate = localSimpleDateFormat.parse(paramString1);
	      return localDate;
	    }
	    catch (ParseException localParseException)
	    {
	      localParseException.printStackTrace();
	    }
	    return null;
	  }

	  public static String getCurrentTime()
	  {
	    return convertDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	  }

	  public static String getCurrentTimeYM()
	  {
	    return convertDate(new Date(), "yyyy-MM");
	  }

	  public static int getDayNumsOfCurrentMonth()
	  {
	    return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	  }

	  public static int getDayNumsOfMonth(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.setTime(paramDate);
	    return localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	  }

	  public static int getDaysOfYM(int Year, int Month)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.clear();
	    localCalendar.set(Calendar.YEAR, Year);
	    localCalendar.set(Calendar.MONTH, Month - 1);
	    return localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	  }

	  public static String getFormatedDate(String paramString1, String paramString2)
	  {
	    String[] arrayOfString = paramString1.split(paramString2);
	    String str1 = arrayOfString[0];
	    String str2 = switchDay(Integer.parseInt(arrayOfString[1]));
	    String str3 = switchDay(Integer.parseInt(arrayOfString[2]));
	    return str1 + "-" + str2 + "-" + str3;
	  }

	  public static String getListUpdateStr()
	  {
	    return new SimpleDateFormat("MM-dd HH:mm").format(new Date(System.currentTimeMillis()));
	  }

	  public static int getMonthLastDay(int Year, int Month)
	  {
	    Calendar localCalendar = Calendar.getInstance();
          localCalendar.set(Calendar.YEAR, Year);
          localCalendar.set(Calendar.MONTH, Month - 1);
	    localCalendar.set(Calendar.DAY_OF_MONTH, 1);
	    localCalendar.roll(Calendar.DAY_OF_MONTH, -1);
	    return localCalendar.get(Calendar.DAY_OF_MONTH);
	  }

	  public static String getTimeStrHanzi()
	  {
	    return convertDate(new Date(), "yyyy/MM/dd HH:mm:ss");
	  }

	  public static String getWeekByDate(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.clear();
	    localCalendar.setTime(paramDate);
	    return Num2Haizi_Week(-1 + localCalendar.get(Calendar.DAY_OF_MONTH));
	  }

	  public static String getWeekByDateSingleChar(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.clear();
	    localCalendar.setTime(paramDate);
	    return getWeekNameByNum(-1 + localCalendar.get(Calendar.DAY_OF_MONTH));
	  }

	  public static String getWeekByDateStr(String paramString)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.clear();
	    localCalendar.setTime(convertString2Date(paramString, "yyyy-MM-dd"));
	    return Num2Haizi_Week(-1 + localCalendar.get(Calendar.DAY_OF_MONTH));
	  }

	  public static int getWeekByDateTime(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.setTime(paramDate);
	    return -1 + localCalendar.get(Calendar.DAY_OF_MONTH);
	  }

	  public static String getWeekByDate_HTML_Color(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.clear();
	    localCalendar.setTime(paramDate);
	    return Num2Haizi_Week_HTML_Color(-1 + localCalendar.get(Calendar.DAY_OF_MONTH));
	  }

	  public static String getWeekByFormatedDateStr(String paramString)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    String[] arrayOfString = paramString.split("-");
	    localCalendar.set(Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2]));
	    return getWeekByDate(localCalendar.getTime());
	  }

	  public static String getWeekByFormatedDateStr_HTML_Color(String paramString)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    String[] arrayOfString = paramString.split("-");
	    localCalendar.set(Integer.parseInt(arrayOfString[0]), -1 + Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2]));
	    return getWeekByDate_HTML_Color(localCalendar.getTime());
	  }

	  public static String getWeekNameByDate(Date paramDate)
	  {
	    Calendar localCalendar = Calendar.getInstance();
	    localCalendar.setTime(paramDate);
	    switch (localCalendar.get(Calendar.DAY_OF_MONTH))
	    {
	    default:
	      return "";
	    case 7:
	      return "六";
	    case 1:
	      return "日";
	    case 2:
	      return "一";
	    case 3:
	      return "二";
	    case 4:
	      return "三";
	    case 5:
	      return "四";
	    case 6:
	    }
	    return "五";
	  }

	  public static String getWeekNameByNum(int paramInt)
	  {
	    switch (paramInt)
	    {
	    default:
	      return "";
	    case 2:
	      return "一";
	    case 3:
	      return "二";
	    case 4:
	      return "三";
	    case 5:
	      return "四";
	    case 6:
	      return "五";
	    case 7:
	      return "六";
	    case 1:
	    }
	    return "日";
	  }

	  public static int getWeeksOfYear()
	  {
	    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar localCalendar = Calendar.getInstance();
	    int i = localCalendar.get(Calendar.YEAR);
	    try
	    {
	      localCalendar.setTime(localSimpleDateFormat.parse(i + "-12-31"));
	      int j = localCalendar.get(Calendar.DAY_OF_MONTH);
	      localCalendar.setTime(localSimpleDateFormat.parse(i + "-12-" + (31 - j)));
	      return localCalendar.get(Calendar.WEEK_OF_YEAR);
	    }
	    catch (ParseException localParseException)
	    {
	      while (true)
	        localParseException.printStackTrace();
	    }
	  }

	  public static int getWeeksOfYear(int paramInt)
	  {
	    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar localCalendar = Calendar.getInstance();
	    try
	    {
	      localCalendar.setTime(localSimpleDateFormat.parse(paramInt + "-12-31"));
	      int i = localCalendar.get(Calendar.DAY_OF_MONTH);
	      localCalendar.setTime(localSimpleDateFormat.parse(paramInt + "-12-" + (31 - i)));
	      return localCalendar.get(Calendar.WEEK_OF_YEAR);
	    }
	    catch (ParseException localParseException)
	    {
	      while (true)
	        localParseException.printStackTrace();
	    }
	  }

	  public static String getWeektimeOfCurrentTime()
	  {
	    return Num2Haizi_Week(-1 + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	  }

	  public static void main(String[] paramArrayOfString)
	  {
	    System.out.println(getCurrentTime() + "-01 00:00:00");
	  }

	  /**
	   * 对不足两位数进行补0
	   * @param paramInt
	   * @return
	   */
	  public static String switchDay(int paramInt)
	  {
	    String str =String.valueOf(paramInt);
	    if (str.length() == 2)
	      return str;
	    return "0" + str;
	  }
}
