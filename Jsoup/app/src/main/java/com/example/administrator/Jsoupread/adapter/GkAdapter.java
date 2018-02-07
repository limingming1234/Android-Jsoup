package com.example.administrator.Jsoupread.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.Jsoupread.R;
import com.example.administrator.Jsoupread.bean.gkBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/3.
 */

public class GkAdapter extends BaseAdapter {
    List<gkBean> mgkBeanList;
    Context mContext;

    public GkAdapter(List<gkBean> gkBeanList, Context context) {
        mgkBeanList = gkBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mgkBeanList.size();
}

    @Override
    public Object getItem(int i) {
        return mgkBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.gkadapter_layout,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        gkBean GkBean = mgkBeanList.get(i);
        Picasso.with(mContext)
                .load(GkBean.getImg())
                .into(viewHolder.ivgkLogo);
        viewHolder.tvgkName.setText(GkBean.getName());
        return view;
    }

    class ViewHolder {

        @BindView(R.id.ivgkLogo)
        ImageView ivgkLogo;
        @BindView(R.id.tvgkName)
        TextView tvgkName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
