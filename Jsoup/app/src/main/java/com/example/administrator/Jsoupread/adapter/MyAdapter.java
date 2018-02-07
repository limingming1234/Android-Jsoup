package com.example.administrator.Jsoupread.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.Jsoupread.R;
import com.example.administrator.Jsoupread.bean.zhihuBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyAdapter extends BaseAdapter {

    List<zhihuBean> mzhihuBeanList;
    Context mContext;

    public MyAdapter(List<zhihuBean> zhihuBeanList, Context context) {
        mzhihuBeanList = zhihuBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mzhihuBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mzhihuBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_layout,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        zhihuBean ZhihuBean = mzhihuBeanList.get(i);
        Picasso.with(mContext)
                .load(ZhihuBean.getImg())
                .into(viewHolder.ivzhihuLogo);
        //Picasso.with(mContext).setIndicatorsEnabled(true);
        viewHolder.tvzhihuName.setText(ZhihuBean.getName());
        return view;
    }

    class ViewHolder {

        @BindView(R.id.ivzhihuLogo)
        ImageView ivzhihuLogo;
        @BindView(R.id.tvzhihuName)
        TextView tvzhihuName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

