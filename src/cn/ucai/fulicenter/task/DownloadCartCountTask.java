package cn.ucai.fulicenter.task;

/**
 * Created by Administrator on 2016/8/9.
 */
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.fulicenter;
import cn.ucai.fulicenter.utils.OkHttpUtils2;

/**
 * Created by clawpo on 16/7/20.
 */
public class DownloadCartCountTask {
    private final static String TAG = DownloadCartCountTask.class.getSimpleName();
    String username;
    Context mContext;

    public DownloadCartCountTask(Context context, String username) {
        mContext = context;
        this.username = username;
    }

    public void execute(){
        final OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        Log.e(TAG,"s="+msg);
                        if(msg!=null) {
                            if(msg.isSuccess()){
                                fulicenter.getInstance().setCollectCount(Integer.valueOf(msg.getMsg()));
                            }else{
                                fulicenter.getInstance().setCollectCount(0);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_collect"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);
                    }
                });
    }
}