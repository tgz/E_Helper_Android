package qsc.epointhelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import qsc.epointhelper.DBHelper.SqlHelperUserList;
import qsc.epointhelper.DBHelper.UserData;
import qsc.epointhelper.comm.StringHelp;
import qsc.epointhelper.mywebservice.wsDoLogin;


/**
 * Created by 士川 on 2014-11-16.
 */
public class ActivityLogin extends Activity {

    private EditText txtUserName = null;
    private EditText txtPwd = null;
    private Button btn_Login = null;


    public String userName;
    public String password;

    String operate = null;

    UserListAdaper userListAdaper = new UserListAdaper();

    List<User> userList = new ArrayList();

    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle();
        initControl();
    }



    private void setTitle() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("登录");
    }

    private void initControl() {

        getUserList();

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPwd = (EditText) findViewById(R.id.txtPsw);
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(myListener);


        listView = (ListView)findViewById(R.id.lv_UserList);
        listView.setAdapter(userListAdaper);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectUser(userList.get(position));
            }
        });

    }

    private void selectUser(User user) {

        // 保存用户信息
        UserData userdata = new UserData(this);
        userdata.SaveData("UserName", user.UserName);
        userdata.SaveData("UserGuid", user.UserGuid);
        userdata.SaveData("OuName", user.OUName);


        // 跳转回MainActivity
        // 注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致

//        ActivityLogin.this.setResult(1000, data);
        //关闭当前窗口
        gotoMain();
    }


    public View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Login:
                    doLogin();
                    break;

            }
        }
    };

    /**
     * 检查用户输入
     *
     * @return 返回检查结果
     */
    protected Boolean checkInput() {
        //取出用户名密码
        userName = txtUserName.getText().toString().trim();
        password = txtPwd.getText().toString().trim();
        if (userName.length() == 0 || password.length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return false;
        }
        return true;
    }

    protected void invoke() {

        Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("value");
//                if (operate == "login") {
                    getUserInfo(result);
//                } else {
//                    text_Log.append(result);
//                }
            }

        };

        wsDoLogin ws = new wsDoLogin(this, mHandler);
        ws.doLogin(userName, password);
        Toast.makeText(this,"正在加载中，请稍后……",Toast.LENGTH_SHORT).show();
    }

    protected void doLogin() {
        if (checkInput()) //首先检查用户输入
        {
            invoke(); //用户输入通过检查后，调用ws
        }
    }



    //根据返回的结果，进行信息处理。
    public void getUserInfo(String xml) {
        String userguid = StringHelp.getXMLAtt(xml, "UserGuid");
        String userName = StringHelp.getXMLAtt(xml, "UserDisplayName");
        String ouName = StringHelp.getXMLAtt(xml, "OUName");
        //登录状态
        String IsLogin = StringHelp.getXMLAtt(xml, "UserLogin");

        if ("True".equals(IsLogin)) {
            /**
             * 跳转二开始
             */
            final Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("OuName", ouName);
            bundle.putString("UserGuid", userguid);
            bundle.putString("userName", userName);

            data.putExtras(bundle);

            // 保存用户信息
            UserData userdata = new UserData(this);
            userdata.SaveData("UserName", userName);
            userdata.SaveData("UserGuid", userguid);
            userdata.SaveData("OuName", ouName);


            User user = new User();
            user.OUName=ouName;
            user.UserGuid=userguid;
            user.UserName=userName;

            mSqlHelper sqlHelper = new mSqlHelper();
            sqlHelper.saveUserInfo(user);

            // 跳转回MainActivity
            // 注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致

            ActivityLogin.this.setResult(1000, data);
            //关闭当前窗口
           gotoMain();
        } else {
            //String Description = StringHelp.getXMLAtt(xml, "Description");
            Toast.makeText(this,"\n\n登录失败！详细信息：\n\n" + StringHelp.getXMLAtt(StringHelp.getXMLAtt(xml, "UserArea"), "Description") + "\n\n",Toast.LENGTH_SHORT).show();

        }

    }


    public class User{
        public String UserName;
        public String UserGuid;
        public String OUName;

    }

    public class mSqlHelper
    {
        SqlHelperUserList mSqlHelper = new SqlHelperUserList(getBaseContext());
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        public mSqlHelper() {

        }
        public  Boolean saveUserInfo(User user)
        {
            //String[] columns = {"Address"};
            //Cursor c = db.query("Address", columns, "Address=?", new String[]{Address}, null,null, null);
            String  isExist = "SELECT * FROM UserList Where UserGuid =? ";

            Cursor c = db.rawQuery(isExist, new String[]{user.UserGuid});
            if(!c.moveToFirst()) //查询不到结果的情况下插入数据。
            {
//					ContentValues cv =  new ContentValues();
//					cv.put("Address",Address);
//					db.insert("Address", null, cv);

                String strsql = "Insert Into UserList(UserName,UserGuid,OUName) VALUES ('"+user.UserName+"' , '"+user.UserGuid+"' , '"+user.OUName+"')";
                db.execSQL(strsql);

                return true;
            }
            else return false;
        }

        public Boolean deleteUser(User user)
        {
            if( db.delete("UserList", "UserName=? and UserGuid=?", new String[]{user.UserName,user.UserGuid})>0)
            {
                userList.clear();
                getUserList();
                userListAdaper.notifyDataSetChanged();
                return true;
            }
            else return false;
        }
        public void Close()
        {
            this.db.close();
        }
    }

    public void getUserList() {
        SqlHelperUserList mSqlHelper = new SqlHelperUserList(getBaseContext());
        SQLiteDatabase db = mSqlHelper.getReadableDatabase();
        Cursor c = db.query("UserList", new String[]{"UserName", "UserGuid", "OUName"}, null, null, null, null, null);

        //读取数据库数据
        c.moveToFirst();
        while (!c.isAfterLast()) {
            User mUser = new User();
            mUser.UserName = c.getString(c.getColumnIndex("UserName"));
            mUser.UserGuid = c.getString(c.getColumnIndex("UserGuid"));
            mUser.OUName = c.getString(c.getColumnIndex("OUName"));
            userList.add(mUser);
            c.moveToNext();
        }
        c.close();

    }


    class UserListAdaper extends BaseAdapter {
        UserListAdaper() {
        }

        public int getCount(){
            return userList.size();
        }
        public Object getItem(int position) {
            return userList.get(position);
        }
        public long getItemId(int paramInt){
            return paramInt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final User localUser = (User)userList.get(position);
            if(localUser==null) return convertView;
            LayoutInflater localLayoutInflater = LayoutInflater
                    .from(getBaseContext());
            ViewHolder localViewHolder = null;
            if (convertView == null) {

                localViewHolder = new ViewHolder();
                convertView = localLayoutInflater.inflate(
                        R.layout.listadapter, parent,false);

                localViewHolder.tvUserName= (TextView) convertView.findViewById(R.id.listAdapterTitle);
                localViewHolder.tvOUName=(TextView) convertView.findViewById(R.id.listAdapterItem);
                localViewHolder.btn_Delete=(Button) convertView.findViewById(R.id.listAdapter_Button);
              //  localViewHolder.btn_Delete.setBackground(getResources().getDrawable(R.drawable.trash_2));
                localViewHolder.btn_Delete.setBackgroundResource(R.drawable.trash_2);
                convertView.setTag(localViewHolder);
            }else{
                localViewHolder = (ViewHolder)convertView.getTag();
            }
            localViewHolder.tvUserName.setText(localUser.UserName.toString());
            localViewHolder.tvOUName.setText(localUser.OUName.toString());

            localViewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(getBaseContext(), localPoiModel.ToString(), Toast.LENGTH_SHORT).show();
                    mSqlHelper msqlhepler = new mSqlHelper();
                    if(msqlhepler.deleteUser(localUser))
                    {
                        Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "数据错误！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }


        class ViewHolder {
            public TextView tvUserName;
            public TextView tvOUName;
            public Button btn_Delete;
            ViewHolder() {
            }
        }



    }
    public void gotoMain()
    {
        UserData userdata = new UserData(this);
        userdata.SaveData("ZReportDay", "");
        ActivityLogin.this.finish();
    }



}