package qsc.epointhelper.comm;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlHelper {
	/**
	 * 解析XML
	 * @param inputStream
	 * @param encoding
	 * @param startNode
	 * @param nodes
	 * @return
	 */
	public static List<HashMap<String,String>> parserXml(InputStream inputStream,String encoding,String startNode,String... nodes)
    throws Exception{
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		
		try {
			XmlPullParser  parser = XmlPullParserFactory.newInstance().newPullParser();
			
			parser.setInput(inputStream,encoding);
			
			int evenType = parser.getEventType();
			
			while (evenType!=XmlPullParser.END_DOCUMENT) {
				
				String nodeName = parser.getName();
				
				switch (evenType) {
				case XmlPullParser.START_TAG:
					if(startNode.equals(nodeName)){
						map = new HashMap<String, String>();
					}
					for(int i=0;i<nodes.length;i++){
						if(nodes[i].equalsIgnoreCase(nodeName)){
							String temp = parser.nextText();
							map.put(nodes[i],temp);
							//LogUtils.logI("节点："+nodes[i]+"\t----------->\t"+temp);
						}
					}
					break;

				case XmlPullParser.END_TAG:
					if(startNode.equals(nodeName)&&map!=null){
						list.add(map);
					}
					break;
				}
				
				evenType = parser.next();
				
			}
			
		} catch (XmlPullParserException e) {
            throw new Exception("解析出错："+e.getMessage());
			//Log.e();
		} catch (IOException e) {
            throw new Exception("IO异常："+e.getMessage());
		}
		
		return list;
	}


	/**
	 * 解析XML字符串
	 * @param str
	 * @param startNode
	 * @param nodes
	 * @return
	 */
public static List<HashMap<String,String>> parserXml(String str,String startNode,String... nodes)
throws Exception{
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		
		try {
			XmlPullParser  parser = XmlPullParserFactory.newInstance().newPullParser();
			
			parser.setInput(new StringReader(str));
			
			int evenType = parser.getEventType();
			
			while (evenType!=XmlPullParser.END_DOCUMENT) {
				
				String nodeName = parser.getName();
				
				switch (evenType) {
				case XmlPullParser.START_TAG:
					if(startNode.equals(nodeName)){
						map = new HashMap<String, String>();
					}
					for(int i=0;i<nodes.length;i++){
						if(nodes[i].equalsIgnoreCase(nodeName)){
							String temp = parser.nextText();
							map.put(nodes[i],temp);
							//LogUtils.logI("节点："+nodes[i]+"\t----------->\t"+temp);
							Log.i("EpointHelper", "节点"+nodes[i]+"\t--------\t"+temp);						}
					}
					break;

				case XmlPullParser.END_TAG:
					if(startNode.equals(nodeName)&&map!=null){
						list.add(map);
					}
					break;
				}
				
				evenType = parser.next();
				
			}
			
		} catch (XmlPullParserException e) {
            throw new Exception("解析出错："+e.getMessage());
            //Log.e();
        } catch (IOException e) {
            throw new Exception("IO异常："+e.getMessage());
        }


    return list;
	}
	
}
