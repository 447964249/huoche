package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.fulicenter;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.widget.I;

/**
 * 下载联系人集合的任务栈
 * Created by Administrator on 2016/7/20.
 */
public class DownloadContactListTask {
    final static String TAG = DownloadContactListTask.class.getSimpleName();
    String username;
    Context context;

    public DownloadContactListTask(Context context,String username) {
        this.username = username;
        this.context = context;
    }

    public void execute() {
        OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME, username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "onSuccess:s= " + s);
                        Result result = Utils.getListResultFromJson(s, UserAvatar.class);
                        Log.e(TAG, "result " + result);
                        List<UserAvatar> list= (List<UserAvatar>) result.getRetData();
                        Map<String, UserAvatar> map = fulicenter.getInstance().getUserMap();
                        for (UserAvatar u:list){
                            map.put(u.getMUserName(),u);
                        }

                        if (list != null && list.size() > 0) {
                            Log.e(TAG, "list.size= " +list.size());
                            fulicenter.getInstance().setUserlist(list);

                            //发广播
                            context.sendStickyBroadcast(new Intent("update_contact_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
