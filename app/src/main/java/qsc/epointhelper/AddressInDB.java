package qsc.epointhelper;

import java.util.ArrayList;
import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import qsc.epointhelper.DBHelper.SqlHelper;
import qsc.epointhelper.DBHelper.UserData;

public class AddressInDB extends Activity implements OnItemClickListener {

	//Old
	private ArrayList<String> address = new ArrayList<String>();
	private ListView lv;

    Button btnOpenMap;

	LstAdapter adapter = new LstAdapter();
	
	//New
	List<PoiModel> plist = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_db);
        lv = (ListView) findViewById(R.id.addressDB);
        // listView.setAdapter(new ArrayAdapter<String>(this,
        // android.R.layout.simple_expandable_list_item_1,getData()));

//		lv.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, getAddress()));

       // getAddress();  //此方法已经转移到onResume方法中

        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(this);


        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
                back2Main(plist.get(arg2).ToString());
            }
        });

//        ActionBar actionBar=getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setTitle("地点收藏");
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.show();

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("地点收藏");

        btnOpenMap = (Button)findViewById(R.id.btn_openMap);
        btnOpenMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMap();
            }
        });

    }
    @Override
    public void onResume()
    {
        super.onResume();
        getAddress();
    }

    private void gotoMap() {
        Intent intent = new Intent(AddressInDB.this,MapActivity.class);
        intent.putExtra("method", "Main2Map");
        startActivityForResult(intent,20);
    }

    private void gotoMain(){
        Intent intent = new Intent(AddressInDB.this,MainActivity.class);
        intent.putExtra("method", "AddressInDB2Main");
        startActivityForResult(intent,30);
    }

    public void getAddress()
	{
		SqlHelper mSqlHelper = new SqlHelper(getBaseContext());
		SQLiteDatabase db = mSqlHelper.getWritableDatabase();
		Cursor c = db.query("Address", new String[]{"Name","Street"}, null, null, null, null, null);

		//读取数据库数据
		 c.moveToFirst(); 
		    while (!c.isAfterLast()) { 
		    	//address.add(c.getString(c.getColumnIndex("Name")));
		    	PoiModel mPoimodel = new PoiModel();
		    	mPoimodel.poiName=c.getString(c.getColumnIndex("Name"));
		    	mPoimodel.street = c.getString(c.getColumnIndex("Street"));
		        // do something useful with these 
		    	plist.add(mPoimodel);
		        c.moveToNext(); 
		      } 
		      c.close();
	}
	
	
	/**
	/**
	 * 位置点列表类，供ListView使用
	 * @author 士川
	 *
	 */
	class LstAdapter_Old extends BaseAdapter
	  {
		LstAdapter_Old()
	    {
	    }

	    public int getCount()
	    {
	      return AddressInDB.this.address.size();
	    }

	    public Object getItem(int position) {  
	    // TODO Auto-generated method stub  
           return address.get(position);  
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
			
	    	final String mAddress = (String) AddressInDB.this.address
					.get(paramInt);
	    	if(mAddress==null) return paramView;
	    	LayoutInflater localLayoutInflater = LayoutInflater
					.from(getBaseContext());
			ViewHolder localViewHolder = null;

			if (paramView == null) {

				localViewHolder = new ViewHolder();
				paramView = localLayoutInflater.inflate(
						R.layout.address_db_adaper, paramViewGroup,false);

				localViewHolder.lvAddress= (TextView) paramView.findViewById(R.id.AddressInDbLv);
//				localViewHolder.tvAddress=(TextView) paramView.findViewById(R.id.tvAddress);
//				localViewHolder.addAddressDB=(Button)paramView.findViewById(R.id.address_AddInDb);
			
				paramView.setTag(localViewHolder);
			}else{
				localViewHolder = (ViewHolder)paramView.getTag();
			
			}
			
			localViewHolder.lvAddress.setText(mAddress.toString());

			return paramView;

		}

	    class ViewHolder
	    {
	      public TextView lvAddress;
//	      public TextView tvStreet;
//	      public Button addAddressDB;

	      ViewHolder()
	      {
	      }
	    }
	  }
	
	
	/**
	 * 实现点击ListItem的监听
	 */
	public void onItemClick(AdapterView  paramAdapterView, View paramView, int paramInt, long paramLong)
	  {
	    
	    final String str = this.plist.get(paramInt).ToString();

	    back2Main(str);
	  }
	
	/**
	 * 跳转回主界面
	 * @param
	 */
	 public void back2Main(String addr)
	  {
		  final Intent  data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("Address", addr+"附近");

			data.putExtras(bundle);

          UserData userdata = new UserData(this);
          userdata.SaveData("Location",addr+"附近");

			// 跳转回MainActivity
			// 注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致

			AddressInDB.this.setResult(20, data);
			//关闭当前窗口
			AddressInDB.this.finish();
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
		      return AddressInDB.this.plist.size();
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

		    	final PoiModel localPoiModel = (PoiModel) AddressInDB.this.plist
						.get(paramInt);
		    	if(localPoiModel==null) return paramView;
		    	LayoutInflater localLayoutInflater = LayoutInflater
						.from(getBaseContext());
				ViewHolder localViewHolder = null;

				if (paramView == null) {

					localViewHolder = new ViewHolder();
					paramView = localLayoutInflater.inflate(
							R.layout.map_position_adaper, paramViewGroup,false);

					localViewHolder.tvStreet= (TextView) paramView.findViewById(R.id.tvStreet);
					localViewHolder.tvAddress=(TextView) paramView.findViewById(R.id.tvAddress);
					localViewHolder.addAddressDB=(Button)paramView.findViewById(R.id.address_AddInDb);
				
					localViewHolder.addAddressDB.setBackgroundDrawable(getResources().getDrawable( R.drawable.trash_2));
					
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
						if(msqlhepler.deleteAddress(localPoiModel))
						{
							Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getBaseContext(), "数据错误！", Toast.LENGTH_SHORT).show();
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
			 
			 public Boolean deleteAddress(PoiModel poi)
			 {
				 if( db.delete("Address", "Name=? and Street=?", new String[]{poi.poiName,poi.street})>0)
				 {
					 plist.clear();
					 getAddress();
					 adapter.notifyDataSetChanged();
					 return true;
				 }
				 else return false;
			 }
		}
		
		
		
}
