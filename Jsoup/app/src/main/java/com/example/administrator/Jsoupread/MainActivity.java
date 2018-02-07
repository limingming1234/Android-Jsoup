package com.example.administrator.Jsoupread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.administrator.Jsoupread.adapter.CnAdapter;
import com.example.administrator.Jsoupread.adapter.JsAdapter;
import com.example.administrator.Jsoupread.adapter.MyAdapter;
import com.example.administrator.Jsoupread.adapter.GkAdapter;
import com.example.administrator.Jsoupread.bean.MySQLiteHelper;
import com.example.administrator.Jsoupread.bean.cnBean;
import com.example.administrator.Jsoupread.bean.gkBean;
import com.example.administrator.Jsoupread.bean.jianshuBean;
import com.example.administrator.Jsoupread.bean.zhihuBean;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.viewzhihu)
    Button viewzhihu;
    @BindView(R.id.viewgk)
    Button viewgk;
    @BindView(R.id.viewjs)
    Button viewjs;
    @BindView(R.id.viewcn)
    Button viewcn;
    @BindView(R.id.info_list_view)
    ListView info_list_view;
    private List<zhihuBean> list = new ArrayList<>();
    private List<gkBean> list1 = new ArrayList<>();
    private List<jianshuBean> list2 = new ArrayList<>();
    private List<cnBean> list3 = new ArrayList<>();
    private MySQLiteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
    private SQLiteDatabase database;// 申明一个数据库对象
    private ProgressDialog dialog;
    private ProgressDialog dialog1;
    private ProgressDialog dialog2;
    private ProgressDialog dialog3;
    //爬虫资源及浏览器设置
    private String url = "http://daily.zhihu.com/";
    private String url1 = "https://www.guokr.com/";
    private String url2="https://www.jianshu.com/";
    private String url3="http://tech.163.com/chuangclub/";
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 构造一个数据库管理助手对象
        mySQLiteHelper=new MySQLiteHelper(this,"yuedu.db",null,1);
        //该方法创建一个数据库，可以读写，磁盘满了会自动更改模式为只读模式，getWritableDatabase()盘满报错
        database=mySQLiteHelper.getReadableDatabase();
        database.execSQL("create table if not exists recordtable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
        viewzhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchOver();
                info_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String time = df.format(new Date());
                        String name = list.get(i).getName();
                        String source = "来自知乎日报";
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("url", list.get(i).getTargetUrl());
                        intent.putExtra("time", time);
                        intent.putExtra("name", name);
                        intent.putExtra("source", source);
                        startActivity(intent);
                        Cursor cursor = database.query("recordtable", new String[]{"id", "name", "targeturl", "source", "time"}, null, null, null, null, "time");
                        int tag = 0;
                        int tag1=0;
                        while (cursor.moveToNext()) {
                            int urlindex = cursor.getColumnIndex("targeturl");
                            String url = cursor.getString(urlindex);

                            tag++;
                            if (url.equals(list.get(i).getTargetUrl())) {
                                ContentValues values = new ContentValues();
                                values.put("time", time);
                                database.update("recordtable", values, "targeturl=?", new String[]{list.get(i).getTargetUrl()});
                                tag1=1;
                            }
                        }
                        if(tag1==0){
                            if (tag < 15) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                            else if(tag==15){
                                cursor.moveToFirst();
                                int timeindex = cursor.getColumnIndex("time");
                                String time1 = cursor.getString(timeindex);
                                database.delete("recordtable","time=?",new String[]{time1});
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                        }
                        cursor.close();
                    }
                });
            }
        });
        viewgk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchOver1();
                info_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String time=df.format(new Date());
                        String name=list1.get(i).getName();
                        String source="来自果壳网";
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("url", list1.get(i).getTargetUrl());
                        intent.putExtra("time",time);
                        intent.putExtra("name",name);
                        intent.putExtra("source",source);
                        startActivity(intent);
                        Cursor cursor = database.query("recordtable", new String[]{"id", "name", "targeturl", "source", "time"}, null, null, null, null, "time");
                        int tag = 0;
                        int tag1=0;
                        while (cursor.moveToNext()) {
                            int urlindex = cursor.getColumnIndex("targeturl");
                            String url = cursor.getString(urlindex);
                            tag++;
                            if (url.equals(list1.get(i).getTargetUrl())) {
                                ContentValues values = new ContentValues();
                                values.put("time", time);
                                database.update("recordtable", values, "targeturl=?", new String[]{list1.get(i).getTargetUrl()});
                                tag1=1;
                            }
                        }
                        if(tag1==0){
                            if (tag < 15) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list1.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                            else if(tag==15){
                                cursor.moveToFirst();
                                int timeindex = cursor.getColumnIndex("time");
                                String time1 = cursor.getString(timeindex);
                                database.delete("recordtable","time=?",new String[]{time1});
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list1.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                        }
                        cursor.close();
                    }
                });
            }
        });

        viewjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchOver2();
                info_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String time=df.format(new Date());
                        String name=list2.get(i).getName();
                        String source="来自简书";
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("url", list2.get(i).getTargetUrl());
                        intent.putExtra("time",time);
                        intent.putExtra("name",name);
                        intent.putExtra("source",source);
                        startActivity(intent);
                        Cursor cursor = database.query("recordtable", new String[]{"id", "name", "targeturl", "source", "time"}, null, null, null, null, "time");
                        int tag = 0;
                        int tag1=0;
                        while (cursor.moveToNext()) {
                            int urlindex = cursor.getColumnIndex("targeturl");
                            String url = cursor.getString(urlindex);
                            tag++;
                            if (url.equals(list2.get(i).getTargetUrl())) {
                                ContentValues values = new ContentValues();
                                values.put("time", time);
                                database.update("recordtable", values, "targeturl=?", new String[]{list2.get(i).getTargetUrl()});
                                tag1=1;
                            }
                        }
                        if(tag1==0){
                            if (tag < 15) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list2.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                            else if(tag==15){
                                cursor.moveToFirst();
                                int timeindex = cursor.getColumnIndex("time");
                                String time1 = cursor.getString(timeindex);
                                database.delete("recordtable","time=?",new String[]{time1});
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list2.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                        }
                        cursor.close();
                    }
                });
            }
        });

        viewcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchOver3();
                info_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String time=df.format(new Date());
                        String name=list3.get(i).getName();
                        String source="来自网易创业CLUB";
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("url", list3.get(i).getTargetUrl());
                        intent.putExtra("time",time);
                        intent.putExtra("name",name);
                        intent.putExtra("source",source);
                        startActivity(intent);
                        Cursor cursor = database.query("recordtable", new String[]{"id", "name", "targeturl", "source", "time"}, null, null, null, null, "time");
                        int tag = 0;
                        int tag1=0;
                        while (cursor.moveToNext()) {
                            int urlindex = cursor.getColumnIndex("targeturl");
                            String url = cursor.getString(urlindex);
                            tag++;
                            if (url.equals(list3.get(i).getTargetUrl())) {
                                ContentValues values = new ContentValues();
                                values.put("time", time);
                                database.update("recordtable", values, "targeturl=?", new String[]{list3.get(i).getTargetUrl()});
                                tag1=1;
                            }
                        }
                        if(tag1==0){
                            if (tag < 15) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list3.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                            else if(tag==15){
                                cursor.moveToFirst();
                                int timeindex = cursor.getColumnIndex("time");
                                String time1 = cursor.getString(timeindex);
                                database.delete("recordtable","time=?",new String[]{time1});
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("name", name);
                                contentValues.put("targeturl", list3.get(i).getTargetUrl());
                                contentValues.put("source", source);
                                contentValues.put("time", time);
                                database.insert("recordtable", null, contentValues);
                            }
                        }
                        cursor.close();
                    }
                });
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {*/
        if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ducuments) {
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //新建线程
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Connection conn = Jsoup.connect(url);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", userAgent);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取知乎日报的数据并写入到zhihuBean中
            Elements links=doc.select("div.box");
            for (Element e : links) {
                String targetUrl = e.select("a").attr("href");
                String img = e.select("img").attr("src");
                String zhihuName = e.select("span").text();

                zhihuBean ZhihuBean = new zhihuBean();
                ZhihuBean.setTargetUrl("http://daily.zhihu.com" + targetUrl);
                ZhihuBean.setImg(img);
                ZhihuBean.setName(zhihuName);
                list.add(ZhihuBean);
            }
            if(list.size()<1){
                zhihuBean ZhihuBean = new zhihuBean();
                ZhihuBean.setName("抓取知乎日报数据失败！请等待修复或与我联系。");
                list.add(ZhihuBean);
            }
            // 执行完毕后给handler发送一个空消息
            Message message = new Message();
            handler.sendMessage(message);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 收到消息后执行handler
            show();
        }
    };
    // 将数据填充到ListView中
    private void show() {
        if (!list.isEmpty()) {
            MyAdapter adapter = new MyAdapter(list, MainActivity.this);
            info_list_view.setAdapter(adapter);
        }
        dialog.dismiss();
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            Connection conn = Jsoup.connect(url1);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", userAgent);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //获取果壳的数据并写入到gkBean中
            Elements links=doc.select("div.focus-content").select("a");
            for (Element e :links ) {
                String targetUrl = e.select("a").attr("href");
                String img = e.select("img").attr("src");
                String txnewsName =e.select("img").attr("alt");

                gkBean GkBean = new gkBean();
                GkBean.setTargetUrl(targetUrl);
                GkBean.setImg(img);
                GkBean.setName(txnewsName);
                list1.add(GkBean);
            }
            Elements ilinks=doc.select("div.content");
            for (Element e :ilinks ) {
                String targetUrl = e.select("ul").select("li").select("a").attr("href");
                String img = e.select("img").attr("src");
                String txnewsName =e.select("img").attr("alt");

                gkBean gkBean = new gkBean();
                gkBean.setTargetUrl(targetUrl);
                gkBean.setImg(img);
                gkBean.setName(txnewsName);
                list1.add(gkBean);
            }
            if(list1.size()<1){
                gkBean GkBean = new gkBean();
                GkBean.setName("抓取果壳网数据失败！请等待修复或与我联系。");
                list1.add(GkBean);
            }
            // 执行完毕后给handler1发送一个空消息
            Message message = new Message();
            handler1.sendMessage(message);
        }
    };

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 收到消息后执行handler
            show1();
        }
    };
    // 将数据填充到ListView中
    private void show1() {
        if (!list1.isEmpty()) {
           GkAdapter adapter = new GkAdapter(list1, MainActivity.this);
            info_list_view.setAdapter(adapter);
        }
        dialog1.dismiss();
    }
    //新建线程
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Connection conn = Jsoup.connect(url2);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", userAgent);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取简书的数据并写入到jianshuBean中
            Elements links=doc.select("ul.note-list").select("li.have-img");
            for (Element e : links) {
                String targetUrl = e.select("a.title").attr("href");
                String img =e.select("a.wrap-img").select("img").attr("src");
                String jianshuName = e.select("a.title").text();
                String jianshuIntroduction=e.select("p.abstract").text();

                jianshuBean JianshuBean = new jianshuBean();
                JianshuBean.setTargetUrl("https://www.jianshu.com"+targetUrl);
                JianshuBean.setImg("https:"+img);
                JianshuBean.setName(jianshuName);
                JianshuBean.setIntroduction(jianshuIntroduction);
                list2.add(JianshuBean);
                if(list2.size()==15)
                    break;
            }
            if(list2.size()<1){
                jianshuBean JianshuBean = new jianshuBean();
                JianshuBean.setName("抓取简书数据失败！");
                JianshuBean.setIntroduction("请等待修复或与我联系。");
                list2.add(JianshuBean);
            }
            // 执行完毕后给handler发送一个空消息
            Message message = new Message();
            handler2.sendMessage(message);
        }
    };

    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 收到消息后执行handler
            show2();
        }
    };
    // 将数据填充到ListView中
    private void show2() {
        if (!list2.isEmpty()) {
            JsAdapter adapter = new JsAdapter(list2, MainActivity.this);
            info_list_view.setAdapter(adapter);
        }
        dialog2.dismiss();
    }

    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            Connection conn = Jsoup.connect(url3);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", userAgent);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取网易创业CLUB的数据并写入到cnBean中
            Elements links=doc.select("div.item-Text");
            for (Element e :links ) {
                String targetUrl = e.select("a").attr("href");
                String cnName =e.select("h2").select("a").text();

                cnBean CnBean = new cnBean();
                CnBean.setTargetUrl(targetUrl);
                CnBean.setName(cnName);
                list3.add(CnBean);
            }
            if(list3.size()<1){
                cnBean CnBean = new cnBean();
                CnBean.setName("抓取网易创业CLUB数据失败！请等待修复或与我联系。");
                list3.add(CnBean);
            }
            // 执行完毕后给handler1发送一个空消息
            Message message = new Message();
            handler3.sendMessage(message);
        }
    };

    Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 收到消息后执行handler
            show3();
        }
    };
    // 将数据填充到ListView中
    private void show3() {
        if (!list3.isEmpty()) {
            CnAdapter adapter = new CnAdapter(list3, MainActivity.this);
            info_list_view.setAdapter(adapter);
        }
        dialog3.dismiss();
    }
    // 重新抓取
    public void switchOver() {
        if (isNetworkAvailable(MainActivity.this)) {
            // 显示“正在加载”窗口
            dialog = new ProgressDialog(this);
            dialog.setMessage("正在刷新...");
            dialog.setCancelable(false);
            dialog.show();
            list.clear();
            new Thread(runnable).start();// 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前没有网络连接！")
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchOver();
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);  // 退出程序
                }
            }).show();
        }
    }
    // 判断是否有可用的网络连接
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        else {   // 获取所有NetworkInfo对象
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++)
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;  // 存在可用的网络连接
            }
        }
        return false;
    }
    // 重新抓取
    public void switchOver1() {
        if (isNetworkAvailable(MainActivity.this)) {
            // 显示“正在加载”窗口
            dialog1 = new ProgressDialog(this);
            dialog1.setMessage("正在刷新...");
            dialog1.setCancelable(false);
            dialog1.show();
            list1.clear();
            new Thread(runnable1).start();  // 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前没有网络连接！")
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchOver1();
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);  // 退出程序
                }
            }).show();
        }
    }
    // 重新抓取
    public void switchOver2() {
        if (isNetworkAvailable(MainActivity.this)) {
            // 显示“正在加载”窗口
            dialog2 = new ProgressDialog(this);
            dialog2.setMessage("正在刷新...");
            dialog2.setCancelable(false);
            dialog2.show();
            list2.clear();
            new Thread(runnable2).start();  // 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前没有网络连接！")
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchOver2();
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);  // 退出程序
                }
            }).show();
        }
    }
    // 重新抓取
    public void switchOver3() {
        if (isNetworkAvailable(MainActivity.this)) {
            // 显示“正在加载”窗口
            dialog3 = new ProgressDialog(this);
            dialog3.setMessage("正在刷新...");
            dialog3.setCancelable(false);
            dialog3.show();
            list3.clear();
            new Thread(runnable3).start();  // 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前没有网络连接！")
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchOver3();
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);  // 退出程序
                }
            }).show();
        }
    }
    @Override
    protected void onDestroy(){
        database.close();
        super.onDestroy();
    }
}