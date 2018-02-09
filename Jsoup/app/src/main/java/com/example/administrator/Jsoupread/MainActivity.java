package com.example.administrator.Jsoupread;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.administrator.Jsoupread.Fragment.Blank1Fragment;
import com.example.administrator.Jsoupread.Fragment.Blank2Fragment;
import com.example.administrator.Jsoupread.Fragment.Blank3Fragment;
import com.example.administrator.Jsoupread.Fragment.Blank4Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,Blank1Fragment.OnFragmentInteractionListener,Blank2Fragment.OnFragmentInteractionListener,
        Blank3Fragment.OnFragmentInteractionListener,Blank4Fragment.OnFragmentInteractionListener{
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.mTb)
    TabLayout mTb;
    private List<Fragment> list;
    private List<String> mTitleList;
    private TabFragmentPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTitleList = new ArrayList<>();
        mTitleList.add("知乎日报");
        mTitleList.add("果壳");
        mTitleList.add("简书");
        mTitleList.add("网易CLUB");
        //设置tablayout模式
        mTb.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        mTb.addTab(mTb.newTab().setText(mTitleList.get(0)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(1)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(2)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(3)));

        list = new ArrayList<>();
        list.add(new Blank1Fragment());
        list.add(new Blank2Fragment());
        list.add(new Blank3Fragment());
        list.add(new Blank4Fragment());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list,mTitleList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        mTb.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public void onFragmentInteraction(Uri uri){

    }
}