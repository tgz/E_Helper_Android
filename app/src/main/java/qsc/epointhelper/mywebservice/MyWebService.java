package qsc.epointhelper.mywebservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import qsc.epointhelper.comm.DateHelper;

public class MyWebService extends Thread {
	
	
	private Handler handle = null;
	public String url = null;
    public	String namespace = null;
    public	String methodnames = null;
    public	String validate = null;
    public String parasXML=null;
    public String operation = null;
    public String userguid = null;
    public String Address = null;
	
	public MyWebService(Handler hander,String operate)
	{
		handle = hander;
		operation = operate;
	}
	
	public void doStart()
	{
		this.start();
	}
	
	public void getKaoQinRec(String UserGuid,String Validate)
	{
		this.url = "http://oa.epoint.com.cn/WebServiceManage/EMWebService.asmx";
		this.namespace="http://tempuri.org/";
		this.methodnames="GetWorkAttendanceRecord";
		this.parasXML = "";
		this.validate = Validate;
		this.userguid = UserGuid;
		this.start();
	}
	@Override
	public void run()
	{
		try{
			String result="";
			if (operation == "login") {
				 result = EpointLogin(validate, parasXML);
			}else if(operation=="getKaoQinRec")
			{
				result = getKaoQinRecord(validate,userguid);
			}else if(operation=="doKaoQin")
			{
				result = doKaoQin(validate, userguid, Address);
			}else if(operation=="ValidateAndXML"||operation=="ZReportRec"||operation=="InsertZReport")
			{
				result = validate_And_Parasxml();
			}
			Message message=handle.obtainMessage();  
			Bundle b = new Bundle();
			b.putString("value", result);
            message.setData(b);   
            handle.sendMessage(message);   
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.getMessage();
		}
	}

	public String EpointLogin(String validateDate ,String parasXml)
	{
		SoapObject request = new SoapObject(namespace,methodnames);
		request.addProperty("ValidateData", validateDate);
		request.addProperty("ParasXml", parasXml);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		HttpTransportSE ht = new HttpTransportSE(url);
		try
		{
		ht.call(namespace+methodnames, envelope);
		Object soapobj = (Object)envelope.getResponse();
		return soapobj.toString();
		}
		catch (Exception e)
		{
			return "调用WebService出错！"+e.getMessage();
		}
		
	}

	/**
	 * 获取考勤记录
	 * @param validateDate
	 * @param UserGuid
	 * @return
	 */
	public String getKaoQinRecord(String validateDate,String UserGuid )
	{
		
		
		new DateHelper();
		String dt =DateHelper.convertDate(new Date(), "yyyy-MM-dd");
		SoapObject request = new SoapObject(namespace,methodnames);
		request.addProperty("Token", validateDate);
		request.addProperty("UserGuid", UserGuid);
		request.addProperty("dt",dt);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		HttpTransportSE ht = new HttpTransportSE(url);
		try
		{
		ht.call(namespace+methodnames, envelope);
		Object soapobj = (Object)envelope.getResponse();
		return soapobj.toString();
		}
		catch (Exception e)
		{
			return "调用WebService出错！"+e.getMessage();
		}
	}
	
	public String doKaoQin(String validateDate,String UserGuid,String Address)
	{
		

		new DateHelper();
		SoapObject request = new SoapObject(namespace,methodnames);
		request.addProperty("Token", validateDate);
		request.addProperty("UserGuid", UserGuid);
		request.addProperty("AttendLocation",Address);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		HttpTransportSE ht = new HttpTransportSE(url);
		try
		{
		ht.call(namespace+methodnames, envelope);
		Object soapobj = (Object)envelope.getResponse();
		return soapobj.toString();
		}
		catch (Exception e)
		{
			return "调用WebService出错！"+e.getMessage();
		}
	}

	public String validate_And_Parasxml()
	{
        SoapObject request = new SoapObject(namespace, methodnames);
        request.addProperty("ValidateData", validate);
        request.addProperty("ParasXml", parasXML);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.bodyOut = request;
        HttpTransportSE ht = new HttpTransportSE(url, 30000);
        try {
            ht.call(namespace + methodnames, envelope);
            Object soapobj = (Object) envelope.getResponse();
            return soapobj.toString();
        } catch (Exception e) {
            return "调用WebService出错！" + e.getMessage();
        }

    }



}
