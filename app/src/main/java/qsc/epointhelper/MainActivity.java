package qsc.epointhelper;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.ActionBar;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Long keyBackFirstTime= 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        ActionBar actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Fragment();
        switch (position){
            case 0:
                fragment =  FragmentHome.newInstance(position + 1);
                mTitle=getString(R.string.title_section1);
                break;
            case 1:
                fragment = new FragmentZeroReport();
                mTitle=getString(R.string.title_section2);
//                setActionBarTitle("零报告");
                break;
            case 2:
               // mTitle="收藏的地点";
//                fragment=new AddressInDB();
                gotoAddressInDB();
                fragment = FragmentHome.newInstance(1);
                mTitle=getString(R.string.title_section1);
                break;
            case 3:
                //fragment = new FragmentMap();
                gotoMap();
                fragment = FragmentHome.newInstance(1);
                mTitle=getString(R.string.title_section1);
                //mTitle="地图选点";
//                setActionBarTitle("地图选点");
                break;
            case 4:
                gotoLogin();
                fragment = FragmentHome.newInstance(1);
                mTitle=getString(R.string.title_section1);
               // mTitle="登录";
//                setActionBarTitle("登录");
                break;
            case 5:
                fragment =new FragmentService();
                mTitle=getString(R.string.title_section6);
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

    private void gotoAddressInDB() {
        Intent intent = new Intent(MainActivity.this,AddressInDB.class);
        intent.putExtra("method", "Main2Map");
        startActivityForResult(intent,20);
    }

    private void gotoMap() {
        Intent intent = new Intent(MainActivity.this,MapActivity.class);
        intent.putExtra("method", "Main2Map");
        startActivityForResult(intent,20);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 6:
                mTitle=getString(R.string.title_section6);
                break;
            default:
                mTitle=getString(R.string.app_name);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int KeyCode,KeyEvent event)
    {
        if(KeyCode==KeyEvent.KEYCODE_BACK)
        {
            Long keyBackSecondTime = System.currentTimeMillis();
            if(keyBackSecondTime-keyBackFirstTime>2000)//若第二次按键比第一次延迟大于2秒，不退出
            {
                Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
                keyBackFirstTime=keyBackSecondTime;//按按键时间

            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(KeyCode,event);

    }
    /**
     * 跳转到登录页
     */
    protected void gotoLogin()
    {
        Intent intent = new Intent(MainActivity.this,ActivityLogin.class);
        intent.putExtra("method", "Main2Login");
        startActivityForResult(intent,1000);
    }
    /**
     * 跳转方法二：接收返回值
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == 1000) { //获取用户登录的数据
//
//            Bundle bundle = data.getExtras();
//            String OuName = bundle.getString("OuName");
//            String UserGuid = bundle.getString("UserGuid");
//            String userName = bundle.getString("userName");
//
//            // 保存用户信息
//            UserData userdata = new UserData(this);
//            userdata.SaveData("UserName", userName);
//            userdata.SaveData("UserGuid", UserGuid);
//            userdata.SaveData("OuName", OuName);
//
//        }
//        else if(resultCode==20)
//        {
//            Bundle bundle = data.getExtras();
//            String Address = bundle.getString("Address");
//
//        }
    }





}
