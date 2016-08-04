package cn.ucai.fulicenter.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {

    fulicenterMainActivity mcontext;
    SwipeRefreshLayout mswipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;

    BoutiqueAdapter mAdapter;
    TextView tvHint;
    int pageId = 0;
    int action = I.ACTION_DOWNLOAD;

    private static final String TAG = "BoutiqueFragment";
    List<BoutiqueBean> mBoutiqueList;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
       // fulicenterMainActivity mContext;
        mcontext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mcontext, R.layout.fragment_boutique,null);
        mBoutiqueList = new ArrayList<BoutiqueBean>();
        initView(layout);
        indate();
        setListener();
        return layout;
    }
    private void setListener() {
        //下拉刷新
        setPullDownRefreshListener();
        //上拉刷新
        setPullUpRefreshListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int a1 = RecyclerView.SCROLL_STATE_DRAGGING;
                int b0 = RecyclerView.SCROLL_STATE_IDLE;
                int c2 = RecyclerView.SCROLL_STATE_SETTLING;
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition == mAdapter.getItemCount() - 1) {
                    if (mAdapter.isMore()) {
                        action = I.ACTION_PULL_UP;
                        pageId += I.PAGE_SIZE_DEFAULT;
                        indate();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int f = mGridLayoutManager.findFirstVisibleItemPosition();
                int l = mGridLayoutManager.findLastVisibleItemPosition();
                Log.e(TAG, "F"+f+"L"+l);
                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                mswipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstVisibleItemPosition()==0);
                if (f == -1 || l == -1) {
                    lastItemPosition = mAdapter.getItemCount() - 1;
                }
            }
        });
    }

    private void setPullDownRefreshListener() {
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action = I.ACTION_PULL_DOWN;
                tvHint.setVisibility(View.VISIBLE);
                mswipeRefreshLayout.setRefreshing(true);
                pageId = 0;

                indate();
            }
        });
    }

    private void indate() {
        try {
            finNewGoodList(new OkHttpUtils2.OnCompleteListener<BoutiqueBean[]>() {
                @Override
                public void onSuccess(BoutiqueBean[] result) {
                    Log.e(TAG, "result: " + result);
                    tvHint.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    mAdapter.setFooterString(getResources().getString(R.string.load_more));
                    mswipeRefreshLayout.setRefreshing(false);
                    if (result != null) {
                        Log.e(TAG, "result:length " + result.length);
                        ArrayList<BoutiqueBean> BoutiqueBeanArrList = Utils.array2List(result);
                        if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {

                            mAdapter.initItem(BoutiqueBeanArrList);
                        } else {
                            mAdapter.addItem(BoutiqueBeanArrList);
                        }

                        if (BoutiqueBeanArrList.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                            mAdapter.setFooterString(getResources().getString(R.string.no_more));
                        }
                    } else {
                        mAdapter.setMore(false);
                        mAdapter.setFooterString(getResources().getString(R.string.no_more));
                    }
                }
                @Override
                public void onError(String error) {
                    Log.e(TAG, "eeeeeeeeeeeeeeeeeeeee ");
                    tvHint.setVisibility(View.GONE);
                    mswipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(fulicenterMainActivity.content,"连接服务器失败", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void finNewGoodList(OkHttpUtils2.OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils2<BoutiqueBean[]> utils = new OkHttpUtils2<BoutiqueBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
//                .addParam(I.NewAndBoutiqueGood.CAT_ID, String.valueOf(I.CAT_ID))
//                .addParam(I.PAGE_ID, String.valueOf(pageId))
//                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(BoutiqueBean[].class)
                .execute(listener);

    }

    private void initView(View layout) {
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);

        mswipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_boutique);
        mswipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_boutique);
        //每行2个-1。垂直方向
        mGridLayoutManager = new GridLayoutManager(mcontext, I.COLUM_NUM-1);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new BoutiqueAdapter(mcontext, mBoutiqueList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
