package com.example.administrator.Jsoupread;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.Jsoupread.bean.MySQLiteHelper;
import com.example.administrator.Jsoupread.bean.historyBean;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdActivity extends AppCompatActivity {
    private List<historyBean> list = new ArrayList<>();
    private SideslipListView slideCutListView ;
    private HistoryAdapter adapter=new HistoryAdapter(list,ThirdActivity.this);
    private MySQLiteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
    private SQLiteDatabase database;// 申明一个数据库对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);
        slideCutListView = (SideslipListView) findViewById(R.id.info_list_history);
        list.clear();

        // 构造一个数据库管理助手对象
        mySQLiteHelper=new MySQLiteHelper(this,"yuedu.db",null,1);
        //该方法创建一个数据库，可以读写，磁盘满了会自动更改模式为只读模式，getWritableDatabase()盘满报错
        database=mySQLiteHelper.getReadableDatabase();
        database.execSQL("create table if not exists mytable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
        Cursor cursor=database .query("mytable",new String[]{"id","name","targeturl","source","time"},null,null,null,null,"time desc");
        while (cursor.moveToNext()){
            int nameindex=cursor.getColumnIndex("name");
            String name=cursor.getString(nameindex);
            int urlindex=cursor.getColumnIndex("targeturl");
            String url=cursor.getString(urlindex);
            int sourceindex=cursor.getColumnIndex("source");
            String source=cursor.getString(sourceindex);
            int timeindex=cursor.getColumnIndex("time");
            String time=cursor.getString(timeindex);
            historyBean History=new historyBean();
            History.setName(name);
            History.setTargeturl(url);
            History.setSource(source);
            History.setTime(time);
            list.add(History);
            if (!list.isEmpty()) {
                slideCutListView.setAdapter(adapter);
            }
        }
        cursor.close();

        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                intent.putExtra("url", list.get(position).getTargeturl());
                startActivity(intent);
            }
        });
    }
    class HistoryAdapter extends BaseAdapter {
        List<historyBean> mhistoryList;
        Context mContext;
        public HistoryAdapter(List<historyBean> historyList, Context context) {
            mhistoryList= historyList;
            mContext = context;
        }
        @Override
        public int getCount() {
            return mhistoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return mhistoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HistoryAdapter.ViewHolder viewHolder = null;
            if (view==null){
                view = LayoutInflater.from(mContext).inflate(R.layout.adapter_history,viewGroup,false);
                viewHolder = new HistoryAdapter.ViewHolder(view);
                view.setTag(viewHolder);
            }else {
                viewHolder = (HistoryAdapter.ViewHolder) view.getTag();
            }
            historyBean History = mhistoryList.get(i);
            final int pos = i;
            viewHolder.tvhistoryName.setText(History.getName());
            viewHolder.tvhistorysourse.setText(History.getSource());
            viewHolder.tvhistorytime.setText(History.getTime());
            final String delete= History.getTime();
            viewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ThirdActivity.this, "删除成功",
                            Toast.LENGTH_SHORT).show();
                    mhistoryList.remove(pos);
                    notifyDataSetChanged();
                    slideCutListView.turnNormal();
                    MySQLiteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
                    final SQLiteDatabase database;// 申明一个数据库对象
                    // 构造一个数据库管理助手对象
                    mySQLiteHelper=new MySQLiteHelper(ThirdActivity.this,"yuedu.db",null,1);
                    //该方法创建一个数据库，可以读写，磁盘满了会自动更改模式为只读模式，getWritableDatabase()盘满报错
                    database=mySQLiteHelper.getReadableDatabase();
                    database.delete("mytable","time=?",new String[]{delete});
                }
            });
            return view;
        }

        class ViewHolder {

            @BindView(R.id.tvhistoryName)
            TextView tvhistoryName;
            @BindView(R.id.tvhistorysource)
            TextView tvhistorysourse;
            @BindView(R.id.tvhistorytime)
            TextView tvhistorytime;
            @BindView(R.id.delete_button)
            View delete_button;

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
