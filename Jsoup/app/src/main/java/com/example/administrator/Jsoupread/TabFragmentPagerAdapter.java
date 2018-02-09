package com.example.administrator.Jsoupread;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mlist;
    //添加标题的集合
    private List<String> mTilteLis;
    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String>titleLis) {
        super(fm);
        this.mlist = list;
        this.mTilteLis=titleLis;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTilteLis.get(position);
    }
}
