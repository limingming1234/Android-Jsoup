package com.example.administrator.Jsoupread.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.example.administrator.Jsoupread.R;
import com.example.administrator.Jsoupread.SecondActivity;
import com.example.administrator.Jsoupread.adapter.JsAdapter;
import com.example.administrator.Jsoupread.bean.jianshuBean;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Blank3Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Blank3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Blank3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Unbinder unbinder;
    @BindView(R.id.info_list_view3)
    ListView info_list_view3;
    @BindView(R.id.refresh3)
    SwipeRefreshLayout refresh3;
    private List<jianshuBean> list3 = new ArrayList<>();
    private String url3 = "https://www.jianshu.com/";
    private String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Blank3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Blank3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Blank3Fragment newInstance(String param1, String param2) {
        Blank3Fragment fragment = new Blank3Fragment();
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
        View view= inflater.inflate(R.layout.fragment_blank3, container, false);
        unbinder = ButterKnife.bind(this, view);
        switchOver3();
        info_list_view3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time = df.format(new Date());
                String name = list3.get(i).getName();
                String source = "来自简书";
                Intent intent = new Intent(getContext(), SecondActivity.class);
                intent.putExtra("url", list3.get(i).getTargetUrl());
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
        refresh3.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refresh3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // TODO Auto-generated method stub
                switchOver3();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        refresh3.setRefreshing(false);
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
    //新建线程
    Runnable runnable = new Runnable() {
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
                list3.add(JianshuBean);
                if(list3.size()==15)
                    break;
            }
            if(list3.size()<1){
                jianshuBean JianshuBean = new jianshuBean();
                JianshuBean.setName("抓取简书数据失败！");
                JianshuBean.setIntroduction("请等待修复或与我联系。");
                list3.add(JianshuBean);
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
            show3();
        }
    };

    // 将数据填充到ListView中
    private void show3() {
        if (!list3.isEmpty()) {
            JsAdapter adapter = new JsAdapter(list3, getContext());
            info_list_view3.setAdapter(adapter);
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
    public void switchOver3() {
        if (isNetworkAvailable(getActivity())) {
            list3.clear();
            new Thread(runnable).start();  // 子线程

        } else {
            // 弹出提示框
            new AlertDialog.Builder(getContext())
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
}
