package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.bean.cnBean;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/4.
 */

public class CnAdapter extends BaseAdapter {
    List<cnBean> mcnBeanList;
    Context mContext;

    public CnAdapter(List<cnBean> cnBeanList, Context context) {
        mcnBeanList = cnBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mcnBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mcnBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CnAdapter.ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.cnadapter_layout,viewGroup,false);
            viewHolder = new CnAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (CnAdapter.ViewHolder) view.getTag();
        }
        cnBean CnBean = mcnBeanList.get(i);
        viewHolder.tvcnName.setText(CnBean.getName());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tvcnName)
        TextView tvcnName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
