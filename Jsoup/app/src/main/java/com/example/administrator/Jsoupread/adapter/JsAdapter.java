package com.example.administrator.Jsoupread.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.Jsoupread.R;
import com.example.administrator.Jsoupread.bean.jianshuBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/4.
 */

public class JsAdapter extends BaseAdapter {
    List<jianshuBean> mjsBeanList;
    Context mContext;

    public JsAdapter(List<jianshuBean> jianshuBeanList, Context context) {
        mjsBeanList = jianshuBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mjsBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mjsBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        JsAdapter.ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.jsadapter_layout,viewGroup,false);
            viewHolder = new JsAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (JsAdapter.ViewHolder) view.getTag();
        }
        jianshuBean JianshuBean = mjsBeanList.get(i);
        Picasso.with(mContext)
                .load(JianshuBean.getImg())
                .into(viewHolder.ivjsLogo);
        viewHolder.tvjsName.setText(JianshuBean.getName());
        viewHolder.tvjsIntroduction.setText(JianshuBean.getIntroduction());
        return view;
    }

    class ViewHolder {

        @BindView(R.id.ivjsLogo)
        ImageView ivjsLogo;
        @BindView(R.id.tvjsName)
        TextView tvjsName;
        @BindView(R.id.tvjsIntroduction)
        TextView tvjsIntroduction;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
