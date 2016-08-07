package cn.ucai.fulicenter.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.View.DisplayUtils;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.View.CatChildFilterButton;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.widget.D;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CategoryChildActivity extends BaseActivity {
    private static final String TAG = "CategoryChildActivity";
    CategoryChildActivity mcontext;
    SwipeRefreshLayout mswipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    Button btnprice,
            btnaddtime;
    boolean mSortpriceAsc;
    boolean mSortaddtimeAsc;
    int sortBy;

    GoodAdapter mAdapter;
    List<NewGoodBean> mGoodList;
    TextView tvHint;
    int pageId = 0;
    int action = I.ACTION_DOWNLOAD;
    int catId = 0;
    CatChildFilterButton mCatChildFilterButton;
    public CategoryChildActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mcontext = this;
        setContentView(R.layout.activity_categorychild);

        mGoodList = new ArrayList<NewGoodBean>();

        sortBy = I.SORT_BY_ADDTIME_ASC;
        initView();
        indate();
        setListener();
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_now_good, container, false);
        mcontext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mcontext, R.layout.fragment_now_good, null);

        mGoodList = new ArrayList<NewGoodBean>();

        initView(layout);
        indate();
        setListener();
        return layout;
    }*/

    private void setListener() {
        //下拉刷新
        setPullDownRefreshListener();
        //上拉刷新
        setPullUpRefreshListener();
       // mCatChildFilterButton.setOnCatFilterClickListener(name,);
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
                Log.e(TAG, "F" + f + "L" + l);
                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                mswipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstVisibleItemPosition() == 0);
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
        catId = getIntent().getIntExtra(D.Boutique.KEY_GOODS_ID, 0);
        if (catId < 0) {
            finish();
        }
        try {
            finBoutiqueList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
                @Override
                public void onSuccess(NewGoodBean[] result) {
                    Log.e(TAG, "result: " + result);
                    tvHint.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    mAdapter.setFootstring(getResources().getString(R.string.load_more));
                    mswipeRefreshLayout.setRefreshing(false);
                    if (result != null) {
                        Log.e(TAG, "result:length " + result.length);
                        ArrayList<NewGoodBean> goodBeanArrList = Utils.array2List(result);
                        if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {

                            mAdapter.initDate(goodBeanArrList);
                        } else {
                            mAdapter.addItem(goodBeanArrList);
                        }

                        if (goodBeanArrList.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                            mAdapter.setFootstring(getResources().getString(R.string.no_more));
                        }
                    } else {
                        mAdapter.setMore(false);
                        mAdapter.setFootstring(getResources().getString(R.string.no_more));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "eeeeeeeeeeeeeeeeeeeee ");
                    tvHint.setVisibility(View.GONE);
                    mswipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void finBoutiqueList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]> listener) {
        OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS).addParam(I.NewAndBoutiqueGood.CAT_ID, String.valueOf(catId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listener);

    }

    private void initView() {
        DisplayUtils.initBack(mcontext);
        tvHint = (TextView) findViewById(R.id.tv_refresh_hint);

        mswipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_category);
        mswipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_category);
        //每行2个。垂直方向
        mGridLayoutManager = new GridLayoutManager(mcontext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodAdapter(mcontext, mGoodList);
        mRecyclerView.setAdapter(mAdapter);
        SortStatusChangedListener s = new SortStatusChangedListener();
        btnprice = (Button) findViewById(R.id.btn_sort_price);
        btnprice.setOnClickListener(s);
        btnaddtime = (Button) findViewById(R.id.btn_sort_addtime);
        btnaddtime.setOnClickListener(s);
        mCatChildFilterButton = (CatChildFilterButton) findViewById(R.id.btnCatChildFilter);
        name= getIntent().getStringExtra(I.CategoryGroup.NAME);
    }

    String name;
    class SortStatusChangedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Drawable right;
            switch (v.getId()) {
                case R.id.btn_sort_price:
                    if (mSortpriceAsc) {
                        sortBy = I.SORT_BY_PRICE_ASC;
                        right = getResources().getDrawable(R.drawable.arrow_order_up);
                    } else {
                        sortBy = I.SORT_BY_PRICE_DESC;
                        right = getResources().getDrawable(R.drawable.arrow_order_down);
                    }
                    mSortpriceAsc = !mSortpriceAsc;
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    btnprice.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                    break;
                case R.id.btn_sort_addtime:
                    if (mSortaddtimeAsc) {
                        sortBy = I.SORT_BY_ADDTIME_ASC;
                        right = getResources().getDrawable(R.drawable.arrow_order_up);
                    } else {
                        sortBy = I.SORT_BY_ADDTIME_DESC;
                        right = getResources().getDrawable(R.drawable.arrow_order_up);
                    }
                    mSortaddtimeAsc = !mSortaddtimeAsc;
                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                    btnaddtime.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                    break;
            }
            mAdapter.setSortBy(sortBy);
        }
    }
}

