package qsc.epointhelper.comm;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringHelp {

	public static final String HR_style1 = "<hr style=\"height:1px\" color=Silver>";
	public static final String HR_style2 = "<hr style=\"border-top: 2px dashed; border-bottom: 2px dashed; height: 2px\" color=black>";
	public static final String HTMLSTYLE1 = "<style>#oschina_title {color: #000000; margin-bottom: 6px;font-weight:bold;}#oschina_title img{vertical-align:middle;margin-right:6px;}#oschina_title a{color:#0D6DA8;}#oschina_outline {color: #707070; font-size:12px;}#oschina_outline a{color:#0D6DA8;}#epoint_sender{color:#000033; font-size:12px;}#oschina_software{color:#808080;font-size:12px}#oschina_body img {max-width: 300px;}#oschina_body{font-size:16px;max-width:300px;line-height:24px;} #oschina_body table{max-width:300px;}#oschina_body pre {font-size:9pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}.hr0{ height:1px;border:none;border-top:1px dashed #0066CC;}.hr1{ height:1px;border:none;border-top:1px solid #555555;}.hr2{ height:3px;border:none;border-top:3px double red;}.hr3{ height:5px;border:none;border-top:5px ridge green;}.hr4{ height:10px;border:none;border-top:10px groove skyblue;}</style>";
	public static final String OschinaStyle = "<style>#oschina_title {color: #000000; margin-bottom: 6px;font-weight:bold;font-size:18px;}#oschina_title img{vertical-align:middle;margin-right:6px;}#oschina_title a{color:#0D6DA8;}#oschina_outline {color: #707070; font-size:12px;}#oschina_outline a{color:#0D6DA8;}#epoint_sender{color:#000033; font-size:12px;}#oschina_software{color:#808080;font-size:12px}#oschina_body img {max-width: 300px;}#oschina_body{font-size:16px;max-width:300px;line-height:24px;} #oschina_body table{max-width:300px;}#oschina_body pre {font-size:9pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}</style>";

	public static byte[] String2Byte(String paramString) {
		byte[] arrayOfByte;
		if (paramString.length() % 2 == 1)
			arrayOfByte = null;
		while (true) {

			arrayOfByte = new byte[paramString.length() / 2];
			for (int i = 0; i < paramString.length(); i += 2)
				arrayOfByte[(i / 2)] = Integer.decode(
						"0x" + paramString.substring(i, i + 2)).byteValue();
			return arrayOfByte;
		}
	}

	public static List<Map<String, String>> String2List(String paramString1,
			String paramString2) {
		ArrayList localArrayList = new ArrayList();
		if ((paramString1 == null) || (paramString2 == null)
				|| ("".equals(paramString1)) || ("".equals(paramString2)))
			localArrayList = new ArrayList();
		while (true) {

			String[] arrayOfString1 = paramString1.split(";");
			String[] arrayOfString2 = paramString2.split(";");
			for (int i = 0; i < arrayOfString1.length; i++) {
				HashMap localHashMap = new HashMap();
				localHashMap.put("name", arrayOfString1[i]);
				localHashMap.put("guid", arrayOfString2[i]);
				localArrayList.add(localHashMap);
			}
			return localArrayList;
		}
	}

	public static String XMLSwitched(String paramString) {
		return paramString.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("'", "&apos;")
				.replace("\"", "&quot;");
	}

	public static String XMLSwitchedOrigion(String paramString) {
		return paramString.replace("&amp;", "&").replace("&lt;", "<")
				.replace("&gt;", ">").replace("&apos;", "'")
				.replace("&quot;", "\"");
	}

	public static String byte2HexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			a = a + hex;
		}

		return a;
	}

	public static String cutLastSymbol(String paramString1, String paramString2) {
		if (paramString1.endsWith(paramString2))
			paramString1 = paramString1
					.substring(0, -1 + paramString1.length());
		return paramString1;
	}

	
	

	public static String filterCDATA(String paramString) {
		return paramString.replace("<![CDATA[", "").replace("]]>", "");
	}

	public static String filterXmlString(String paramString) {
		return paramString.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("'", "&apos;")
				.replace("\"", "&quot;");
	}

	public static String get2AttMid(String paramString1, String paramString2,
			String paramString3) {
		try {
			String str = paramString1.substring(
					paramString1.indexOf(paramString2) + paramString2.length(),
					paramString1.indexOf(paramString3));
			return str;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static String getAttOut(String paramString1, String paramString2) {
		try {
			String str = paramString1.substring(
					paramString1.indexOf("<" + paramString2 + ">"),
					paramString1.indexOf("</" + paramString2 + ">")
							+ ("</" + paramString2 + ">").length());
			return str;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}
/**
 * 提取XML内信息
 * @param paramString1 XML字符串
 * @param paramString2 需要提取的属性
 * @return xml相应标签内的信息
 */
	public static String getXMLAtt(String paramString1, String paramString2) {
		try {
			String str1 = "<" + paramString2 + ">";
			String str2 = "</" + paramString2 + ">";
			String str3 = paramString1.substring(paramString1.indexOf(str1)
					+ str1.length(), paramString1.indexOf(str2));
			return str3;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static String getXMLAttOut(String paramString1, String paramString2) {
		try {
			String str1 = getXMLAtt(paramString1, paramString2);
			String str2 = "<" + paramString2 + ">" + str1 + "</" + paramString2
					+ ">";
			return str2;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static String getXMLInFilterCData(String paramString1,
			String paramString2) {
		return filterCDATA(getXMLAtt(paramString1, paramString2));
	}

	public static boolean isEditTextBlank(EditText paramEditText) {
		return paramEditText.getText().toString().trim().length() == 0;
	}

	public static boolean isEqualBlank(String paramString) {
		if (paramString == null)
			paramString = "";
		return paramString.equals("");
	}

	public static boolean isLength0(String paramString) {
		return paramString.trim().length() == 0;
	}

	public static int versionToInteger(String paramString) {
		try {
			String[] arrayOfString = paramString.split("\\.");
			Object localObject = "";
			int i = arrayOfString.length;
			for (int j = 0;; j++) {
				if (j >= i)
					return Integer.parseInt((String) localObject);
				String str1 = arrayOfString[j];
				String str2 = localObject + str1;
				localObject = str2;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return 0;
	}

}
