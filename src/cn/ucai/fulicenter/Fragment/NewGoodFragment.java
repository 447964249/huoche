package cn.ucai.fulicenter.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodFragment extends Fragment {
    private static final String TAG ="NewGoodFragment" ;
    fulicenterMainActivity mcontext;
    SwipeRefreshLayout mswipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;

    GoodAdapter mAdapter;
    List<NewGoodBean> mGoodList;
    int pageId=1;
    TextView tvHint;
    public NewGoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_now_good, container, false);
        mcontext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mcontext, R.layout.fragment_now_good, null);

        mGoodList=new ArrayList<NewGoodBean>();

        initView(layout);
        indate();
        setListener();
        return layout;
    }

    private void setListener() {
        //下拉刷新
        setPullDownRefreshListener();
    }

    private void setPullDownRefreshListener() {
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvHint.setVisibility(View.VISIBLE);
                pageId = 1;
                indate();
            }
        });
    }

    private void indate() {
        finNewGoodList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
            @Override
            public void onSuccess(NewGoodBean[] result) {
                Log.e(TAG, "result: "+result);
                tvHint.setVisibility(View.GONE);
                mswipeRefreshLayout.setRefreshing(false);
                if (result != null) {
                    Log.e(TAG, "result:length "+result.length);
                    ArrayList<NewGoodBean> goodBeanArrList = Utils.array2List(result);
                    mAdapter.initDate(goodBeanArrList);

                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "eeeeeeeeeeeeeeeeeeeee ");

                tvHint.setVisibility(View.GONE);
                mswipeRefreshLayout.setRefreshing(false);
            }
        });
    }
private void finNewGoodList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]>listener){
    OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
    utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS).addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
            .addParam(I.PAGE_ID,String.valueOf(pageId))
            .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
            .targetClass(NewGoodBean[].class)
            .execute(listener);

}
    private void initView(View layout) {
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);

        mswipeRefreshLayout =(SwipeRefreshLayout) layout.findViewById(R.id.srl_new_good);
        mswipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_new_good);
        //每行2个。垂直方向
        mGridLayoutManager = new GridLayoutManager(mcontext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodAdapter(mcontext, mGoodList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
