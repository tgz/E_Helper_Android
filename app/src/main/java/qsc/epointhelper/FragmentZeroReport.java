package qsc.epointhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import qsc.epointhelper.comm.XmlHelper;
import qsc.epointhelper.mywebservice.wsZeroReport;

/**
 * Created by 士川 on 2014-11-16.
 */
public class FragmentZeroReport extends Fragment {


    private Button btnGetZReportHistory;
    private Button btnRepareZReport;
    private EditText tvDateSelected;

    private TextView tvZReportResult;

    int year;
    int month;
    int day;
    String operate;

    Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
//            mainActivity.onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_zeroreport,container,false);

        tvDateSelected = (EditText)view.findViewById(R.id.dateSelect);
        tvDateSelected.setInputType(InputType.TYPE_NULL);


        tvDateSelected.setOnClickListener(myListener);

        btnGetZReportHistory=(Button)view.findViewById(R.id.getZReportHistory);
        btnGetZReportHistory.setOnClickListener(myListener);

        btnRepareZReport=(Button)view.findViewById(R.id.btn_AddMissZReport);
        btnRepareZReport.setOnClickListener(myListener);

        tvZReportResult=(TextView)view.findViewById(R.id.tv_ZReportResult);
        tvZReportResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        initDate();

        return view;
    }



    /**
     * 监听器
     */
    public View.OnClickListener myListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.dateSelect:
                    DatePickerDialog dpd=new DatePickerDialog(mActivity,Datelistener,year,month,day);
                    dpd.show();//显示DatePickerDialog组件
                    break;
                case R.id.getZReportHistory:
                    operate="getZReportHistory";
                    getZeroReportHistory();
                    break;
                case R.id.btn_AddMissZReport:
//                    operate="insertZero";
//                    insertZeroReport();
                    addZeroReport();
                    break;
            }
        }
    };

    public void initDate()
    {
        //初始化日期
        Calendar mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        day =mCalendar.get(Calendar.DAY_OF_MONTH);
        tvDateSelected.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

    }

    /**
     * 弹出框事件监听
     */
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */

        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear,
                              int dayOfMonth) {
            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            // 更新日期
            updateDate();
        }
    };
    // 当DatePickerDialog关闭时，更新日期显示
    private void updateDate()
    {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month, day);
        tvDateSelected.setText(new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime()));

    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = msg.getData().getString("value");
                if (operate == "getZReportHistory") {
                    getZReportRecList(result);
                } else {
                    operate="getZReportHistory";
                    getZeroReportHistory();
               }
        }
    };
    private void getZeroReportHistory()
    {
        String startDate = tvDateSelected.getText().toString();
        if(startDate.length()<10) {
            Toast.makeText(getActivity().getBaseContext(), "请输入正确的日期！", Toast.LENGTH_SHORT).show();
            return;
        }else {
            String EndDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            wsZeroReport ws = new wsZeroReport(getActivity().getBaseContext(), mHandler, mActivity);
            ws.getZeroReport(startDate, EndDate);
            tvZReportResult.setText("正在加载中，请稍后……");
        }
    }

    private void insertZeroReport()
    {
        wsZeroReport ws = new wsZeroReport(getActivity().getBaseContext(),mHandler,mActivity);
        String startDate = tvDateSelected.getText().toString();
        ws.insertZeroReport(startDate);
    }

    //弹出二次确认框
    protected void addZeroReport()
    {
        new AlertDialog.Builder(mActivity)
                .setTitle("请确认")
                .setMessage("确认补签选定日期的零报告？")
                .setPositiveButton("确定", confirmClick)
                .setNegativeButton("取消", null)
                .show();
    }

    //二次确认监听
    DialogInterface.OnClickListener confirmClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            operate="insertZeroReport";
            insertZeroReport();
            tvZReportResult.setText("正在加载中，请稍后……");
            //Toast.makeText(getBaseContext(), "点击了确认！", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 解析结果
     * @param str
     */
    protected void getZReportRecList(String str) {
        try {


            List<HashMap<String, String>> list = XmlHelper.parserXml(str, "Report",
                    "RowGuid", "RecordData", "IsNullProblem", "Status", "Content");
            //Log.i("EpointHelper", list.toString());
            tvZReportResult.setText("");

            for (HashMap<String, String> map : list) {

                tvZReportResult.append(map.get("RecordData").substring(0, 10)
                        + "：");
                // 判断今日零报告是否填写
                if (map.get("RowGuid") != "") {

                    if (map.get("IsNullProblem").equals("1")) {
                        String green = "<font color='#00CC00'> 零报告 </font>";
                        tvZReportResult.append(Html.fromHtml(green));
                    } else {
                        String have = "<font color='#CC6600'> 已填 </font>";
                        tvZReportResult.append(Html.fromHtml(have));
                    }
                } else {
                    String red = "<font color='red'> 未填 </font>";
                    tvZReportResult.append(Html.fromHtml(red));
                }
                tvZReportResult.append("\n");
            }
        }catch (Exception e){
            Toast.makeText(mActivity.getBaseContext(), "发生错误！"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
