package com.example.administrator.Jsoupread;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.Jsoupread.bean.MySQLiteHelper;


public class SecondActivity extends Activity {
    WebView mWebview;
    WebSettings mWebSettings;
    TextView beginLoading,endLoading,loading,mTitle;
    Intent mIntent;
    private String url1;
    private String time;
    private String name;
    private String source;
    private int tag;
    private int tag1;
    private MySQLiteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
    private SQLiteDatabase database;// 申明一个数据库对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //获取目标文章的链接
        mIntent = getIntent();
        url1 = mIntent.getStringExtra("url");
        time=mIntent.getStringExtra("time");
        name=mIntent.getStringExtra("name");
        source=mIntent.getStringExtra("source");
        // 构造一个数据库管理助手对象
        mySQLiteHelper=new MySQLiteHelper(this,"yuedu.db",null,1);
        //该方法创建一个数据库，可以读写，磁盘满了会自动更改模式为只读模式，getWritableDatabase()盘满报错
        database=mySQLiteHelper.getReadableDatabase();
        database.execSQL("create table if not exists mytable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
        database.execSQL("create table if not exists recordtable(id integer primary key autoincrement,name text,targeturl text,source text,time text)");
        Cursor cursor = database.query("recordtable", new String[]{"id", "name", "targeturl", "source", "time"}, null, null, null, null, "time");
        tag = 0;
        tag1=0;
        while (cursor.moveToNext()) {
            int urlindex = cursor.getColumnIndex("targeturl");
            String url = cursor.getString(urlindex);
            tag++;
            if (url.equals(url1)) {
                ContentValues values = new ContentValues();
                values.put("time", time);
                database.update("recordtable", values, "targeturl=?", new String[]{url1});
                tag1=1;
            }
        }
        if(tag1==0){
            if (tag < 15) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", name);
                contentValues.put("targeturl", url1);
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
                contentValues.put("targeturl", url1);
                contentValues.put("source", source);
                contentValues.put("time", time);
                database.insert("recordtable", null, contentValues);
            }
        }
        cursor.close();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag=0;
                Cursor cursor=database .query("mytable",new String[]{"id","name","targeturl","source","time"},null,null,null,null,null);
                while (cursor.moveToNext()){
                    int urlindex=cursor.getColumnIndex("targeturl");
                    String url=cursor.getString(urlindex);
                    if(url1.equals(url)){
                        tag=1;
                        Snackbar.make(view, "已收藏", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    }
                }
                if(tag==0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name);
                    contentValues.put("targeturl", url1);
                    contentValues.put("source", source);
                    contentValues.put("time", time);
                    database.insert("mytable", null, contentValues);
                    Snackbar.make(view, "收藏成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                cursor.close();
            }
        });

        mWebview = (WebView) findViewById(R.id.webView1);
        beginLoading = (TextView) findViewById(R.id.text_beginLoading);
        endLoading = (TextView) findViewById(R.id.text_endLoading);
        loading = (TextView) findViewById(R.id.text_Loading);
        mTitle=(TextView) findViewById(R.id.Title);

        mWebSettings = mWebview.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);

        mWebview.loadUrl(url1);


        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                mTitle.setText(title);
            }
            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);
                }
            }
        });

        //设置WebViewClient类
        mWebview.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
                beginLoading.setText("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                endLoading.setText("结束加载了");

            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        database.close();
        super.onDestroy();
    }
}
