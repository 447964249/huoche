package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import cn.ucai.fulicenter.fulicenter;
import cn.ucai.fulicenter.bean.GroupAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.widget.I;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DownloadGroupListTask {
    private final static String TAG = "DownloadGroupListTask";
    String username;
    Context context;

    public DownloadGroupListTask( Context context,String username) {
        this.username = username;
        this.context = context;
    }
    public void execute(){
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<String>();
        utils2.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = Utils.getListResultFromJson(s, GroupAvatar.class);
                        List<GroupAvatar> list = (List<GroupAvatar>) result.getRetData();
                        if (list!=null&&list.size()>0){
                            fulicenter.getInstance().setGrouplist(list);
                            for (GroupAvatar g: list) {

                                fulicenter.getInstance().getGroupMap().put(g.getMGroupHxid(),g);
                            }
                            context.sendStickyBroadcast(new Intent("update_group_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "onError:error "+error);
                    }
                });
    }

}
