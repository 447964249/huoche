package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<CategoryGroupBean> mGoupList;
    List<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, List<CategoryGroupBean> mGoupList, List<ArrayList<CategoryChildBean>> mChildList) {
        this.mContext = mContext;
        this.mGoupList = new ArrayList<CategoryGroupBean>();
        this.mGoupList.addAll(mGoupList);
        this.mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        this.mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGoupList!=null?mGoupList.size():0;
    }

    @Override
    public int getChildrenCount(int i) {
        return mChildList.get(i).size();
    }

    @Override
    public CategoryGroupBean getGroup(int i) {
        if (mGoupList!=null) return mGoupList.get(i);
        return null;
    }

    @Override
    public CategoryChildBean getChild(int i, int i1) {
        if (mChildList.get(i)!=null
                &&mChildList.get(i).get(i1)!=null)
            return mChildList.get(i).get(i1);
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewholder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewholder();
            holder.tvGroupName = (TextView) view.findViewById(R.id.tv_group_name);
            holder.ivGroupImg = (ImageView) view.findViewById(R.id.iv_group_img);
            holder.iv_indicator = (ImageView) view.findViewById(R.id.iv_indicator);
            view.setTag(holder);

        } else {
            holder = (GroupViewholder) view.getTag();
        }
        CategoryGroupBean group = getGroup(i);
        ImageUtils.setGroupCAtegoryImage(mContext, holder.ivGroupImg, group.getImageUrl());
        holder.tvGroupName.setText(group.getName());
        if (b) {
            holder.iv_indicator.setImageResource(R.drawable.expand_off);
        } else {
            holder.iv_indicator.setImageResource(R.drawable.expand_on);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder();
            holder.layoutCategoryChild = (RelativeLayout) view.findViewById(R.id.layout_category_child);
            holder.ivCategorychildImg = (ImageView) view.findViewById(R.id.iv_category_child_img);
            holder.tvCategorychildName = (TextView) view.findViewById(R.id.tv_category_child_name);
            view.setTag(holder);

        } else {
            holder = (ChildViewHolder) view.getTag();
        }
         CategoryChildBean child = getChild(i, i1);
        if (child != null) {
            ImageUtils.setchildCAtegoryImage(mContext,holder.ivCategorychildImg,child.getImageUrl());
            holder.tvCategorychildName.setText(child.getName());

        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewholder {
        ImageView ivGroupImg,
        iv_indicator;
        TextView tvGroupName;
    }

    class ChildViewHolder {
        RelativeLayout layoutCategoryChild;
        ImageView ivCategorychildImg;
        TextView tvCategorychildName;
    }
}
