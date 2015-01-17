package qsc.epointhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import qsc.epointhelper.DBHelper.UserData;
import qsc.epointhelper.comm.XmlHelper;
import qsc.epointhelper.mywebservice.wsAttendance;
import qsc.epointhelper.mywebservice.wsZeroReport;

/**
 * Created by 士川 on 2014-11-16.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public  class FragmentHome extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    ImageView btnLocationList;
    TextView tvUserName;
    Button btnZeroReport;
    TextView tvLog;
    TextView tvZReportToday;
    Button btnKaoQing;
    EditText editTextLocation;
    Button btnAttendanceLog;

    Activity  mActivity;
    String UserName;
    String UserGuid;
    String OuName;
    String Address;

    String operate;

    UserData userData ;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentHome newInstance(int sectionNumber) {

        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //打开列表
        btnLocationList = (ImageView)rootView.findViewById(R.id.imageLocation);
        btnLocationList.setOnClickListener(myListener);

        //用户名
        tvUserName = (TextView)rootView.findViewById(R.id.textViewUserInfo);
        tvUserName.setOnClickListener(myListener);

        //零报告按钮
        btnZeroReport = (Button)rootView.findViewById(R.id.btnZeroReport);
        btnZeroReport.setOnClickListener(myListener);

        //考勤按钮、考勤记录按钮
        btnKaoQing=(Button)rootView.findViewById(R.id.btnKaoQin);
        btnKaoQing.setOnClickListener(myListener);
        btnAttendanceLog=(Button)rootView.findViewById(R.id.btnAttendanceLog);
        btnAttendanceLog.setOnClickListener(myListener);

        //地址输入
        editTextLocation=(EditText)rootView.findViewById(R.id.editTextLocation);

        //考勤结果输出
        tvLog =(TextView)rootView.findViewById(R.id.textViewLog);
        //tvLog.setOnClickListener(myListener);

        //今日考勤结果输出
        tvZReportToday=(TextView)rootView.findViewById(R.id.zReportStatus);
        tvZReportToday.setOnClickListener(myListener);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        userData= new UserData(mActivity);
//            mainActivity.onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onStart()
    {
        super.onStart();

         UserName = userData.getData("UserName");
         UserGuid = userData.getData("UserGuid");
         OuName = userData.getData("OuName");
        Address = userData.getData("Location");
        String date =new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String ZReportDay = userData.getData("ZReportDay");
        if(UserName.equals("none"))
            tvUserName.setText("点击登录");
        else {
            tvUserName.setText(UserName);
            Log.d("epoint",UserGuid);
            if(date.equals(ZReportDay)){ //读取到今日已是零报告
                //零报告：
                tvZReportToday.setText(date+"：");
                String green = "<font color='#00CC00'> 零报告 </font>";
                tvZReportToday.append(Html.fromHtml(green));

                switchButtonZReport(1);

            }else {
                insertZReportReturn("");//未读取到零报告的结果时，发起查询零报告状态的请求
                switchButtonZReport(0);
            }
        }
        if(!Address.equals("none"))
            editTextLocation.setText(Address);

    }

    private void switchButtonZReport(int state) {
        if(state>0) {
            //已经是零报告的情况下，零报告按钮不能点
            btnZeroReport.setEnabled(false);
            btnZeroReport.setBackgroundColor(Color.GRAY);
        }else
        {
            btnZeroReport.setEnabled(true);
            btnZeroReport.setBackgroundColor(0xff00bbff);
        }
    }


    /**
     * 跳转到登录页
     */
    protected void gotoLogin()
    {
        Intent intent = new Intent(mActivity,ActivityLogin.class);

        intent.putExtra("method", "Main2Login");

        startActivityForResult(intent,1000);
    }



    /**
     * 监听器
     */
    public View.OnClickListener myListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageLocation:
//                    Toast.makeText(getActivity().getBaseContext(), "inageLocation", Toast.LENGTH_SHORT).show();
                   openLocationList();
                    break;
                case R.id.textViewUserInfo:
                    gotoLogin();
                    break;
                case R.id.btnZeroReport:
                    operate="insertZero";
                    Toast.makeText(getActivity().getBaseContext(), "加载中，请稍后……", Toast.LENGTH_SHORT).show();
                    invoke();
                    break;
                case R.id.btnKaoQin:
                    AlertKaoQinConfirm();
                    break;
//                case R.id.textViewLog:
                case R.id.btnAttendanceLog:
                    Toast.makeText(getActivity().getBaseContext(), "加载中，请稍后……", Toast.LENGTH_SHORT).show();
                    operate="getKaoQinRec";
                    invoke();
                    break;
                case R.id.zReportStatus:
                    insertZReportReturn("");
                    break;
//                    case R.id.btnExit:
//                        getActivity().finish();

            }
        }
    };


    //弹出二次确认框
    protected void AlertKaoQinConfirm()
    {
        new AlertDialog.Builder(mActivity)
                .setTitle("请确认")
                .setMessage("确认考勤？")
                .setPositiveButton("确定", confirmClick)
                .setNegativeButton("取消", null)
                .show();
    }

    //二次确认监听
    DialogInterface.OnClickListener confirmClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Toast.makeText(getActivity().getBaseContext(), "加载中，请稍后……", Toast.LENGTH_SHORT).show();
            operate="doKaoQin";
            invoke();
            //Toast.makeText(getBaseContext(), "点击了确认！", Toast.LENGTH_SHORT).show();
        }
    };

    private void openLocationList() {
        Intent intent = new Intent(mActivity,AddressInDB.class);
        intent.putExtra("method", "Main2List");
        startActivityForResult(intent,20);
    }

    //插入零报告之后的操作：重新查询结果
    protected void insertZReportReturn(String resultXML)
    {
        Toast.makeText(getActivity().getBaseContext(), "查询零报告中……", Toast.LENGTH_SHORT).show();
        operate="ZReportLog";
        invoke();
    }

    //获取零报告记录后的操作
    protected void getZeroReportRecToday(String resultXML)
    {
        getZReportRecList(resultXML);

    }
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = msg.getData().getString("value");
            if (operate == "insertZero") {
                insertZReportReturn(result);
            } else if (operate == "ZReportLog") {
                getZeroReportRecToday(result);
               // Log.d("text3_getZeroReportRecToday_Result",result);
            }else if(operate=="doKaoQin"){
            //TODO 处理考勤结果
                getKaoQinList(result);
            }else if(operate=="getKaoQinRec"){
            //TODO 处理考勤记录结果
                getKaoQinList(result);
            }
        }

    };

    //调用webservice
    protected void invoke() {
        wsZeroReport ws = new wsZeroReport(getActivity().getBaseContext(),mHandler,mActivity);
        wsAttendance wsKaoQin = new wsAttendance(mActivity.getBaseContext(),mActivity,mHandler);
        if (operate == "insertZero") {
            String date =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            ws.insertZeroReport(date);
            tvZReportToday.setText("加载中……");
        } else if (operate == "ZReportLog") {
            ws.getTodayZReport();
            tvZReportToday.setText("加载中……");
        }else if(operate=="doKaoQin"){
            wsKaoQin.operate="doKaoQin";
            wsKaoQin.KaoQin(editTextLocation.getText().toString());
        }else if(operate=="getKaoQinRec"){
            wsKaoQin.operate="getKaoQinRec";
            wsKaoQin.getKaoQinRec();
        }
    }

    /**
     * 解析零报告历史记录的返回值，显示在页面上。
     * @param str
     */
    protected void getZReportRecList(String str)
    {
        try {
            List<HashMap<String,String>> list = XmlHelper.parserXml(str, "Report", "RowGuid", "RecordData", "IsNullProblem", "Status", "Content");
            for(HashMap<String,String> map:list){

                tvZReportToday.setText("");
                String RecordDate=map.get("RecordData").substring(0, 10);
                tvZReportToday.append(RecordDate + "：");
                //判断今日零报告是否填写
                if (map.get("RowGuid") != "") {


                    if (map.get("IsNullProblem").equals("1")) {
                        String green = "<font color='#00CC00'> 零报告 </font>";
                        tvZReportToday.append(Html.fromHtml(green));
                        //结果为零报告的情况下，将结果保存到配置文件中
                        userData.SaveData("ZReportDay",RecordDate);

                        //切换按钮状态
                        switchButtonZReport(1);
                    }else{
                        String have = "<font color='#CC6600'> 已填 </font>";
                        tvZReportToday.append(Html.fromHtml(have));
                        //切换按钮状态
                        switchButtonZReport(0);
                    }
                }else {
                    String red = "<font color='red'> 未填 </font>";
                    tvZReportToday.append(Html.fromHtml(red));
                    //切换按钮状态
                    switchButtonZReport(0);
                }
            }
        }
        catch(Exception e)
        {
            tvZReportToday.setText("发生错误！");
            Toast.makeText(mActivity,e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    protected void getKaoQinList(String str) {
        try {
            tvLog.setText("");
            List<HashMap<String, String>> list = XmlHelper.parserXml(str, "Record", "AttendTime", "AttendLocation");
            Log.i("EpointHelper", list.toString());
            for (HashMap<String, String> map : list) {
                tvLog.append("\n" + map.get("AttendTime") + "\n");
                tvLog.append(map.get("AttendLocation") + "\n");
            }
        }catch (Exception e)
        {
            tvLog.setText("发生错误"+e.getMessage());
            Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}

