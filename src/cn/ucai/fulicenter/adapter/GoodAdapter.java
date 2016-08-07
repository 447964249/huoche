package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
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
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.widget.D;

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
    int sortBy;

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
        sortBy = I.SORT_BY_ADDTIME_DESC;
        soryBy();
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        soryBy();
        notifyDataSetChanged();
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
            final NewGoodBean good = mGoodlist.get(position);
         //   mGoodViewHolder.imageThumb.setImageURI();
            ImageUtils.setGoodImage(mcontext,mGoodViewHolder.imageThumb,good.getGoodsThumb());
            mGoodViewHolder.tvGoodname.setText(good.getGoodsName());
            mGoodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
            mGoodViewHolder.layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcontext.startActivity(new Intent(mcontext, GoodDetailsActivity.class)
                            .putExtra(D.GoodDetails.KEY_GOODS_ID, good.getGoodsId()));
                }
            });
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
        soryBy();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<NewGoodBean> goodBeanArrList) {
        mGoodlist.addAll(goodBeanArrList);
        soryBy();
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
    private int getSubStr(String price) {
        int i = Integer.parseInt(price.substring(1, price.length()));
        Log.i("main", "getSubStr=" + i);
        return i;}
    private void soryBy(){
        Collections.sort(mGoodlist, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodlefe, NewGoodBean goodRight) {
                int result = 4;
                switch (sortBy) {
                    case I.SORT_BY_PRICE_ASC:
                        result = (int) (Long.valueOf(getSubStr(goodlefe.getCurrencyPrice())) - (Long.valueOf(getSubStr(goodRight.getCurrencyPrice()))));
                        break;
                    case I.SORT_BY_PRICE_DESC:
                      result  = (int) (int) ((Long.valueOf(getSubStr(goodRight.getCurrencyPrice()))) - Long.valueOf(getSubStr(goodlefe.getCurrencyPrice())));
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (Long.valueOf(goodlefe.getAddTime()) - (Long.valueOf(goodRight.getAddTime())));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result=   (int) (Long.valueOf(goodRight.getAddTime()) - Long.valueOf(goodlefe.getAddTime()));
                        break;
                }
                return result;

            }
        });
    }
}
