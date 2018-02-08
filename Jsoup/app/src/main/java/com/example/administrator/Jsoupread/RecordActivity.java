package com.example.administrator.Jsoupread;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.Jsoupread.bean.MySQLiteHelper;
import com.example.administrator.Jsoupread.bean.recordBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {
    private List<recordBean> list = new ArrayList<>();
    private RecordAdapter adapter=new RecordAdapter(list, RecordActivity.this);
    @BindView(R.id.info_list_record)
    ListView info_list_record;
    private MySQLiteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
    private SQLiteDatabase database;// 申明一个数据库对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        list.clear();

        // 构造一个数据库管理助手对象
        mySQLiteHelper=new MySQLiteHelper(this,"yuedu.db",null,1);
        //该方法创建一个数据库，可以读写，磁盘满了会自动更改模式为只读模式，getWritableDatabase()盘满报错
        database=mySQLiteHelper.getReadableDatabase();
        database.execSQL("create table if not exists recordtable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
        Cursor cursor=database .query("recordtable",new String[]{"id","name","targeturl","source","time"},null,null,null,null,"time desc");
        while (cursor.moveToNext()){
            int nameindex=cursor.getColumnIndex("name");
            String name=cursor.getString(nameindex);
            int urlindex=cursor.getColumnIndex("targeturl");
            String url=cursor.getString(urlindex);
            int sourceindex=cursor.getColumnIndex("source");
            String source=cursor.getString(sourceindex);
            int timeindex=cursor.getColumnIndex("time");
            String time=cursor.getString(timeindex);
            recordBean Record=new recordBean();
            Record.setName(name);
            Record.setTargeturl(url);
            Record.setSource(source);
            Record.setTime(time);
            list.add(Record);
            if (!list.isEmpty()) {
                info_list_record.setAdapter(adapter);
            }
        }
        cursor.close();

        info_list_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time = df.format(new Date());
                Intent intent = new Intent(RecordActivity.this, SecondActivity.class);
                intent.putExtra("url", list.get(position).getTargeturl());
                intent.putExtra("time", time);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("source", list.get(position).getSource());
                startActivity(intent);
            }

        });
    }
    class RecordAdapter extends BaseAdapter {
        List<recordBean> mrecordList;
        Context mContext;
        public RecordAdapter(List<recordBean> recordList, Context context) {
            mrecordList= recordList;
            mContext = context;
        }
        @Override
        public int getCount() {
            return mrecordList.size();
        }

        @Override
        public Object getItem(int i) {
            return mrecordList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecordAdapter.ViewHolder viewHolder = null;
            if (view==null){
                view = LayoutInflater.from(mContext).inflate(R.layout.adapter_record,viewGroup,false);
                viewHolder = new RecordAdapter.ViewHolder(view);
                view.setTag(viewHolder);
            }else {
                viewHolder = (RecordAdapter.ViewHolder) view.getTag();
            }
            recordBean Record = mrecordList.get(i);
            viewHolder.recordName.setText(Record.getName());
            viewHolder.recordsource.setText(Record.getSource());
            viewHolder.recordtime.setText(Record.getTime());
            return view;
        }

        class ViewHolder {

            @BindView(R.id.recordName)
            TextView recordName;
            @BindView(R.id.recordsource)
            TextView recordsource;
            @BindView(R.id.recordtime)
            TextView recordtime;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
    @Override
    protected void onDestroy(){
        database.close();
        super.onDestroy();
    }
}
