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
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;

import static android.view.View.*;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mcontext;
    List<NewGoodBean> mGoodlist;
    GoodViewHolder mGoodViewHolder;

    public GoodAdapter(Context context, List<NewGoodBean> list) {
        mcontext = context;
        mGoodlist = new ArrayList<NewGoodBean>();
        mGoodlist .addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       ViewHolder holder = null;
        LayoutInflater infla = LayoutInflater.from(mcontext);
        holder = new GoodViewHolder(infla.inflate(R.layout.item_good, null, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodViewHolder) {
            mGoodViewHolder = (GoodViewHolder) holder;
            NewGoodBean good = mGoodlist.get(position);
         //   mGoodViewHolder.imageThumb.setImageURI();
            mGoodViewHolder.tvGoodname.setText(good.getGoodsName());
            mGoodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mGoodlist.size();
    }
    class GoodViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView imageThumb;
        TextView tvGoodname, tvGoodPrice;

        public GoodViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            imageThumb = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodname = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }
}
