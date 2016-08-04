package cn.ucai.fulicenter.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CategoryFragment extends Fragment {
    fulicenterMainActivity mcontext;
    ExpandableListView mExpandableListView;
    List<CategoryGroupBean> mGoupList;
    List<ArrayList<CategoryChildBean>> mChildList;
    CategoryAdapter mAdapter;
    int gg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mcontext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mcontext, R.layout.fragment_category, null);
        mGoupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mAdapter = new CategoryAdapter(mcontext, mGoupList, mChildList);
        initView(layout);
        indate();
        return layout;
    }

    private void indate() {
        findCategoryGroupList(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    ArrayList<CategoryGroupBean> groupList = Utils.array2List(result);
                    if (groupList != null) {
                        mGoupList = groupList;
                        int i=0;
                        for (CategoryGroupBean g : groupList) {

                            findCategorychildList(g.getId(),i);
                            i++;
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void findCategorychildList(int parentId, final int index ) {
        OkHttpUtils2<CategoryChildBean[]> util = new OkHttpUtils2<CategoryChildBean[]>();
        util.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, String.valueOf(parentId))
                .addParam(I.PAGE_ID, String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] resultt) {
                        gg++;
                        if (resultt != null) {
                            ArrayList<CategoryChildBean> childgroup = Utils.array2List(resultt);
                            mChildList.set(index,childgroup);
                        }
                        if (gg == mGoupList.size()) {
                            mAdapter.addAll(mGoupList,mChildList);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void findCategoryGroupList(OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils2<CategoryGroupBean[]> util = new OkHttpUtils2<CategoryGroupBean[]>();
        util.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    private void initView(View layout) {
        mExpandableListView = (ExpandableListView) layout.findViewById(R.id.elvCategory);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mAdapter);

    }
}
