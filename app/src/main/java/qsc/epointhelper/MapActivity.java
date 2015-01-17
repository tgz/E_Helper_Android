package qsc.epointhelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import qsc.epointhelper.DBHelper.SqlHelper;
import qsc.epointhelper.DBHelper.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MapActivity extends Activity implements
        OnGetGeoCoderResultListener, AdapterView.OnItemClickListener {

    boolean isFirstLoc = true;
    MapView mMapView = null;
    TextView mTextView = null;
    ListView lv = null;
    private BaiduMap mBaiduMap;
    GeoCoder mSearch = null;
    LocationClient mLocClient;
    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();



    List<PoiModel> plist = new ArrayList();
    LstAdapter adapter = new LstAdapter();

    Button reGPS = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现

//        ActionBar actionBar=getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setTitle("地图选点");
//        //actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.show();

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("地图选点");

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);

        mTextView = (TextView) findViewById(R.id.LoactionTextView);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());


        reGPS = (Button)findViewById(R.id.reGPS);
        reGPS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mLocClient.start();
                mTextView.setText("定位中……");

            }
        });



        initListView();

        BaiDuMapInint();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 百度地图初始化方法
     */
    protected void BaiDuMapInint() {
        this.mMapView = ((com.baidu.mapapi.map.MapView) findViewById(R.id.map));
        // this.lv = ((ListView)findViewById(R.id.lv));
        // this.lv.setOnItemClickListener(this);
        this.mBaiduMap = this.mMapView.getMap();

        this.mBaiduMap.setMaxAndMinZoomLevel(3.0F, 19.0F);
        this.mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
        this.mSearch = GeoCoder.newInstance();
        this.mSearch.setOnGetGeoCodeResultListener(this);
        this.mBaiduMap.setMyLocationEnabled(true);

        this.mBaiduMap.setOnMapClickListener(mapClickListener);

        this.mLocClient = new LocationClient(this);
        this.mLocClient.registerLocationListener(this.myListener);

//		LocationClientOption localLocationClientOption = new LocationClientOption();
//		localLocationClientOption.setOpenGps(true);
//		localLocationClientOption.setCoorType("bd09ll");
//		localLocationClientOption.setScanSpan(15000);
//		this.mLocClient.setLocOption(localLocationClientOption);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
        this.mLocClient.setLocOption(option);

        this.mLocClient.start();

    }

    /**
     * 监听百度地图
     * @author 士川
     *
     */
    public class MyLocationListenner implements BDLocationListener {
        public MyLocationListenner() {
        }

        @Override
        public void onReceiveLocation(BDLocation paramBDLocation) {
            if ((paramBDLocation == null)
                    || (MapActivity.this.mMapView == null)) {
                // MapActivity.this.hideLoadingDialog();
                return;
            }
            MyLocationData localMyLocationData = new MyLocationData.Builder()
                    .latitude(paramBDLocation.getLatitude())
                    .longitude(paramBDLocation.getLongitude()).build();
            MapActivity.this.mBaiduMap.setMyLocationData(localMyLocationData);
            LatLng localLatLng = new LatLng(paramBDLocation.getLatitude(),
                    paramBDLocation.getLongitude());
            if (MapActivity.this.isFirstLoc) {
                MapActivity.this.isFirstLoc = false;
            }
            MapActivity.this.mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .zoomTo(16));
            centerMap(localLatLng);
        }

        /*public void onReceiveLocation(BDLocation location) {
            if (location == null)
                    return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            Log.i("EpointHelper", sb.toString());
        }
        */
        public void onReceivePoi(BDLocation paramBDLocation) {
        }
    }

    /**
     * 监听搜索结果
     */
    public void onGetReverseGeoCodeResult(
            ReverseGeoCodeResult paramReverseGeoCodeResult) {
        // hideLoadingDialog();
        Iterator localIterator ;
        boolean hasresult = true;
        try {
            // this.plist.clear();\

            if (paramReverseGeoCodeResult == null)
                return;
            if ((paramReverseGeoCodeResult.getPoiList() == null)|| (paramReverseGeoCodeResult.getPoiList().size() <= 0))
                hasresult = false;
            localIterator = paramReverseGeoCodeResult.getPoiList().iterator();
            if(hasresult)
            {
                PoiModel localPoiModel1 = new PoiModel();
                localPoiModel1.street = "";
                localPoiModel1.poiName = (getNotNull(paramReverseGeoCodeResult
                        .getAddressDetail().province)
                        + getNotNull(paramReverseGeoCodeResult
                        .getAddressDetail().city)
                        + getNotNull(paramReverseGeoCodeResult
                        .getAddressDetail().district) + getNotNull(paramReverseGeoCodeResult
                        .getAddressDetail().street));

                mTextView.setText( localPoiModel1.ToString());

                this.plist.clear();
            }
            while(true)
            {
                if(!localIterator.hasNext())
                {
                    this.mLocClient.stop();

                    return;
                }
                PoiInfo localPoiInfo = (PoiInfo) localIterator.next();
                PoiModel localPoiModel2 = new PoiModel();
                localPoiModel2.street = localPoiInfo.address;
                localPoiModel2.poiName = getNotNull(localPoiInfo.name);
				/*Log.i("E_H_PoiMode","\nAddress:"+localPoiInfo.address+"\ncity"+localPoiInfo.city
						+"\nname"+localPoiInfo.name+"\nphoneNum"+localPoiInfo.phoneNum
						+"\npostCode"+localPoiInfo.postCode+"\n"+localPoiInfo.uid+"\n"+localPoiInfo.toString());
				*/

                //mTextView.append("\n_附近："+localPoiModel2.street.toString()+localPoiModel2.poiName);


                this.plist.add(localPoiModel2);
                this.adapter.notifyDataSetChanged();

            }


        } catch (Exception localException) {
            // ApplicationUtils.saveErrorLog(localException);
            Log.i("EpointHelper", "onGetReverseGeoCodeResult Exception"
                    + localException.getMessage());
            return;
        }

    }

    public void onGetGeoCodeResult(GeoCodeResult paramGeoCodeResult) {
    }

    /**
     * 位置点类
     * @author 士川
     *
     */
    public class PoiModel {

        public String poiName;
        public String street;

        public String ToString() {
            return street + poiName;
        }
    }

    /**
     * 位置点列表类，供ListView使用
     * @author 士川
     *
     */
    class LstAdapter extends BaseAdapter
    {
        LstAdapter()
        {
        }

        public int getCount()
        {
            return MapActivity.this.plist.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return plist.get(position);
        }


        public long getItemId(int paramInt)
        {
            return paramInt;
        }

        /**
         * ListView渲染
         */
        @Override
        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {



            final PoiModel localPoiModel = (PoiModel) MapActivity.this.plist
                    .get(paramInt);
            if(localPoiModel==null) return paramView;
            LayoutInflater localLayoutInflater = LayoutInflater
                    .from(MapActivity.this.getBaseContext());
            ViewHolder localViewHolder = null;

            if (paramView == null) {

                localViewHolder = new ViewHolder();
                paramView = localLayoutInflater.inflate(
                        R.layout.map_position_adaper, paramViewGroup,false);

                localViewHolder.tvStreet= (TextView) paramView.findViewById(R.id.tvStreet);
                localViewHolder.tvAddress=(TextView) paramView.findViewById(R.id.tvAddress);
                localViewHolder.addAddressDB=(Button)paramView.findViewById(R.id.address_AddInDb);

                paramView.setTag(localViewHolder);
            }else{
                localViewHolder = (ViewHolder)paramView.getTag();

            }

            localViewHolder.tvAddress.setText(localPoiModel.poiName.toString());
            localViewHolder.tvStreet.setText(localPoiModel.street.toString());

            localViewHolder.addAddressDB.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getBaseContext(), localPoiModel.ToString(), Toast.LENGTH_SHORT).show();
                    mSqlHelper msqlhepler = new mSqlHelper();
                    if(msqlhepler.saveAddress(localPoiModel))
                    {
                        Toast.makeText(getBaseContext(), "加入收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "数据已存在，无需重复添加", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return paramView;

        }

        class ViewHolder
        {
            public TextView tvAddress;
            public TextView tvStreet;
            public Button addAddressDB;

            ViewHolder()
            {
            }
        }
    }



    /**
     * String类型取不为NULL的值
     * @param paramString
     * @return
     */
    public String getNotNull(String paramString) {
        if (paramString == null)
            paramString = "";
        return paramString;
    }
    /**
     * 初始化ListView
     */
    public void initListView()
    {
        this.lv=(ListView)findViewById(R.id.lv);
        this.lv.setAdapter(this.adapter);
        //this.lv.setOnItemClickListener(onItemClick);
        this.lv.setOnItemClickListener(this);
        //this.lv.setOnItemClickListener(new OnItemClickListener(AdapterView<>  paramAdapterView, View paramView, int paramInt, long paramLong) {

        //} );
    }

    /**
     * 实现ListView的点击监听事件
     */
    public void onItemClick(AdapterView  paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        final PoiModel localPoiModel = (PoiModel)this.plist.get(paramInt);
        final String str = localPoiModel.street + localPoiModel.poiName + "附近";

        back2Main(localPoiModel);
    }

    /**
     * 跳转回MainActivity
     */
    public void back2Main(PoiModel poiMode)
    {
//        final Intent  data = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("Address", poiMode.ToString()+"附近");
//
//        data.putExtras(bundle);
//        // 跳转回MainActivity
//        // 注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致
//
//        MapActivity.this.setResult(20, data);

        //保存选中的值：
        // 保存用户信息
        UserData userdata = new UserData(this);
        userdata.SaveData("Location",poiMode.ToString());
        //关闭当前窗口
        gotoMain();
        MapActivity.this.finish();
    }
    private void gotoMain(){
        Intent intent = new Intent(MapActivity.this,MainActivity.class);
        intent.putExtra("method", "AddressInDB2Main");
        startActivityForResult(intent,30);
    }
    /**
     * 实现点击事件的监听
     */
    OnMapClickListener mapClickListener = new OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         * @param point 点击的地理坐标
         */
        public void onMapClick(LatLng point){
            MapActivity.this.mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(point));

            centerMap(point);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka);

            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .anchor(10, 20)
                    .icon(bitmap);
            //清除旧Marker
            mBaiduMap.clear();
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);

        }
        /**
         * 地图内 Poi 单击事件回调函数
         * @param poi 点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi){
            MapActivity.this.mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(poi.getPosition()));
            return true;
        }
    };

    /**
     * 移动地图中心点
     *
     */
    public void centerMap(LatLng localLatLng)
    {
        MapActivity.this.mBaiduMap
                .animateMapStatus(MapStatusUpdateFactory
                        .newLatLng(localLatLng));
        MapActivity.this.mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(localLatLng));
    }

    public class mSqlHelper
    {
        SqlHelper mSqlHelper = new SqlHelper(getBaseContext());
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        public mSqlHelper() {
            // TODO Auto-generated constructor stub
        }
        public  Boolean saveAddress(PoiModel poi)
        {
            //String[] columns = {"Address"};
            //Cursor c = db.query("Address", columns, "Address=?", new String[]{Address}, null,null, null);
            String  isExist = "SELECT * FROM Address Where Name =? and  Street=? ";

            Cursor c = db.rawQuery(isExist, new String[]{poi.poiName,poi.street});
            if(!c.moveToFirst()) //查询不到结果的情况下插入数据。
            {
//					ContentValues cv =  new ContentValues();
//					cv.put("Address",Address);
//					db.insert("Address", null, cv);

                String strsql = "Insert Into Address(Name,Street) VALUES ('"+poi.poiName+"' , '"+poi.street+"')";
                db.execSQL(strsql);
                return true;
            }
            else return false;
        }
    }



}
