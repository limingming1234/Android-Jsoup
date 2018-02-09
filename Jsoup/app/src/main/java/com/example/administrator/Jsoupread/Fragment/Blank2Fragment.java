package com.example.administrator.Jsoupread.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.example.administrator.Jsoupread.R;
import com.example.administrator.Jsoupread.SecondActivity;
import com.example.administrator.Jsoupread.adapter.GkAdapter;
import com.example.administrator.Jsoupread.bean.gkBean;
import android.app.AlertDialog;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Blank2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Blank2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Blank2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Unbinder unbinder;
    @BindView(R.id.info_list_view2)
    ListView info_list_view2;
    @BindView(R.id.refresh2)
    SwipeRefreshLayout refresh2;
    private List<gkBean> list2 = new ArrayList<>();
    private String url2 = "https://www.guokr.com/";
    private String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    private OnFragmentInteractionListener mListener;

    public Blank2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Blank2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Blank2Fragment newInstance(String param1, String param2) {
        Blank2Fragment fragment = new Blank2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank2, container, false);
        unbinder = ButterKnife.bind(this, view);
        switchOver2();
        info_list_view2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time = df.format(new Date());
                String name = list2.get(i).getName();
                String source = "来自果壳网";
                Intent intent = new Intent(getContext(), SecondActivity.class);
                intent.putExtra("url", list2.get(i).getTargetUrl());
                intent.putExtra("time", time);
                intent.putExtra("name", name);
                intent.putExtra("source", source);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        refresh2.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // TODO Auto-generated method stub
                switchOver2();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        refresh2.setRefreshing(false);
                    }
                });
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    Runnable runnable = new Runnable() {
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
                list2.add(GkBean);
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
                list2.add(gkBean);
            }
            if(list2.size()<1){
                gkBean GkBean = new gkBean();
                GkBean.setName("抓取果壳网数据失败！请等待修复或与我联系。");
                list2.add(GkBean);
            }
            // 执行完毕后给handler1发送一个空消息
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
        if (!list2.isEmpty()) {
            GkAdapter adapter = new GkAdapter(list2, getContext());
            info_list_view2.setAdapter(adapter);
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
    public void switchOver2() {
        if (isNetworkAvailable(getActivity())) {

            list2.clear();
            new Thread(runnable).start();  // 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(getContext())
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
}
