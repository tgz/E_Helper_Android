package qsc.epointhelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.Switch;

import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;


/**
 * Created by 士川 on 2014-11-26.
 */
public class FragmentService extends Fragment {
    Activity mActivity;

    Switch switchKaoQin;
    Switch switchZeroReport;

    EditText amAlarm;
    EditText pmAlarm;

    int clickID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
//            mainActivity.onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servicefragment, container, false);

        SharedPreferences sp = mActivity.getSharedPreferences("UserData", 4);
        String KQHelper;
        String ZRHelper;

        String AlarmAmHour;
        String AlarmAMMinute;
        String AlarmPMHour;
        String AlarmPMMinute;

        try {
            KQHelper = sp.getString("KQHelper", "1");
            ZRHelper = sp.getString("ZRHelper", "1");
            AlarmAmHour =  sp.getString("AlarmAmHour","8");
            AlarmAMMinute = sp.getString("AlarmAmMinute","20");
            AlarmPMHour =   sp.getString("AlarmPmHour","18");
            AlarmPMMinute =  sp.getString("AlarmPmMinute","00");
        } catch (Exception e) {
            KQHelper = "1";
            ZRHelper = "1";
            AlarmAmHour = "8";
            AlarmAMMinute = "20";
            AlarmPMHour =  "18";
            AlarmPMMinute = "0";
        }



        switchKaoQin = (Switch) view.findViewById(R.id.switchKaoQin);
        switchKaoQin.setChecked(KQHelper.equals("1"));
        switchKaoQin.setOnCheckedChangeListener(KaoQinChangeListener);

        switchZeroReport = (Switch) view.findViewById(R.id.switchZeroReport);
        switchZeroReport.setChecked(ZRHelper.equals("1"));
        switchZeroReport.setOnCheckedChangeListener(ZeroReportChangeListener);

        amAlarm = (EditText) view.findViewById(R.id.amAlarm);
        amAlarm.setInputType(InputType.TYPE_NULL);
        amAlarm.setOnClickListener(alarmClickListener);
        amAlarm.setText(AlarmAmHour+":"+AlarmAMMinute);

        pmAlarm = (EditText) view.findViewById(R.id.pmAlarm);
        pmAlarm.setInputType(InputType.TYPE_NULL);
        pmAlarm.setOnClickListener(alarmClickListener);
        pmAlarm.setText(AlarmPMHour+":"+AlarmPMMinute);




        return view;
    }


    EditText.OnClickListener alarmClickListener = new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {

            clickID = v.getId();
            Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity
                    , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
                    , onTimeSetListener
                    , c.get(Calendar.HOUR_OF_DAY)
                    , c.get(Calendar.MINUTE)
                    , true);
            timePickerDialog.show();
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            try {
                SharedPreferences sp = mActivity.getSharedPreferences("UserData", Context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = sp.edit();
                switch (clickID) {
                    case R.id.amAlarm:
                        amAlarm.setText("" + hourOfDay + ":" + minute);

                        editor.putString("AlarmAmHour", hourOfDay + "");
                        editor.putString("AlarmAmMinute", minute + "");

                        break;
                    case R.id.pmAlarm:
                        pmAlarm.setText("" + hourOfDay + ":" + minute);

                        editor.putString("AlarmPmHour", hourOfDay + "");
                        editor.putString("AlarmPmMinute", minute + "");

                        break;
                    default:
                        Toast.makeText(mActivity, "设置时间失败", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            } catch (Exception e) {
Log.i("**************",e.getMessage());
            }
        }

    };

    CompoundButton.OnCheckedChangeListener ZeroReportChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //  Intent intent = new Intent(mActivity,Alarmreceiver.class);
            // MyAlarmManager myAlarmManager = new MyAlarmManager(mActivity,intent);
            SharedPreferences sp = mActivity.getSharedPreferences("UserData", 4);
            SharedPreferences.Editor editor = sp.edit();
            if (isChecked) {
                //      myAlarmManager.setZReportAlarm();
                editor.putString("ZRHelper", "1"); //保存服务设置
                Toast.makeText(mActivity, "已打开零报告服务", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("ZRHelper", "0");//保存服务设置
                //      myAlarmManager.cancleZReportAlarm();
                Toast.makeText(mActivity, "已关闭零报告服务", Toast.LENGTH_SHORT).show();
            }
            editor.commit();  //提交设置保存
        }
    };
    CompoundButton.OnCheckedChangeListener KaoQinChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // Intent intent = new Intent(mActivity,Alarmreceiver.class);
            // MyAlarmManager myAlarmManager = new MyAlarmManager(mActivity,intent);

            SharedPreferences sp = mActivity.getSharedPreferences("UserData", 4);
            SharedPreferences.Editor editor = sp.edit();

            if (isChecked) {
                //      myAlarmManager.setKaoQinAlarm();
                editor.putString("KQHelper", "1"); //保存服务设置

                Toast.makeText(mActivity, "已打开考勤提醒", Toast.LENGTH_SHORT).show();
            } else {
                //    myAlarmManager.cancleKaoQinAlarm();
                editor.putString("KQHelper", "0"); //保存服务设置
                Toast.makeText(mActivity, "已关闭考勤提醒", Toast.LENGTH_SHORT).show();
            }

            editor.commit();
        }
    };

}
