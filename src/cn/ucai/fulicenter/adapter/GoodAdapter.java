package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.View.FooterViewHolder;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;

import static android.view.View.*;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mcontext;
    List<NewGoodBean> mGoodlist;
    GoodViewHolder mGoodViewHolder;
    FooterViewHolder mFooterViewHolder;
    String footstring;

    public void setFootstring(String footstring) {
        this.footstring = footstring;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    boolean isMore;

    public GoodAdapter(Context context, List<NewGoodBean> list) {
        mcontext = context;
        mGoodlist = new ArrayList<NewGoodBean>();
        mGoodlist.addAll(list);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {

            return  I.TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       ViewHolder holder = null;
        LayoutInflater infla = LayoutInflater.from(mcontext);

        switch (viewType) {
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(infla.inflate(R.layout.item_footer, parent, false));
            break;
            case I.TYPE_ITEM:
                holder = new GoodViewHolder(infla.inflate(R.layout.item_good, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodViewHolder) {
            mGoodViewHolder = (GoodViewHolder) holder;
            NewGoodBean good = mGoodlist.get(position);
         //   mGoodViewHolder.imageThumb.setImageURI();
            ImageUtils.setGoodthumb(mcontext,mGoodViewHolder.imageThumb,good.getGoodsThumb());
            mGoodViewHolder.tvGoodname.setText(good.getGoodsName());
            mGoodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvfooter.setText(footstring);
        }
    }

    @Override
    public int getItemCount() {
        return mGoodlist!=null?mGoodlist.size()+1:1;
    }

    public void initDate(ArrayList<NewGoodBean> list) {
        if (mGoodlist != null) {
            mGoodlist.clear();
        }
        mGoodlist.addAll(list);
        soryByAddTime();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<NewGoodBean> goodBeanArrList) {
        mGoodlist.addAll(goodBeanArrList);
        soryByAddTime();
        notifyDataSetChanged();
    }

    class GoodViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView imageThumb;
        TextView tvGoodname,
                tvGoodPrice;

        public GoodViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            imageThumb = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodname = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }
    private void soryByAddTime(){
        Collections.sort(mGoodlist, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodlefe, NewGoodBean goodRight) {
                return (int) (Long.valueOf(goodRight.getAddTime()) - Long.valueOf(goodlefe.getAddTime()));

            }
        });
    }
}
