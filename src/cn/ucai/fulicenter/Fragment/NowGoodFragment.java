package cn.ucai.fulicenter.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowGoodFragment extends Fragment {
    fulicenterMainActivity mcontext;
    SwipeRefreshLayout mswipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;

    GoodAdapter mAdapter;
    List<NewGoodBean> mGoodList;

    public NowGoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_now_good, container, false);
        mcontext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mcontext, R.layout.fragment_now_good, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        mswipeRefreshLayout =(SwipeRefreshLayout) layout.findViewById(R.id.srl_new_good);
        mswipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_new_good);
        mGridLayoutManager = new GridLayoutManager(mcontext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodAdapter(mcontext, mGoodList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
