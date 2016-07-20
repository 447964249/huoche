package cn.ucai.superwechat.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.OkHttpUtils2;
import cn.ucai.superwechat.utils.Utils;
import cn.ucai.superwechat.widget.I;

/**
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
                        if (list != null && list.size() > 0) {
                            Log.e(TAG, "list.size= " +list.size());
                            SuperWeChatApplication.getInstance().setUserlist(list);
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
