package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.fulicenter;
import cn.ucai.fulicenter.bean.MemberUserAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.widget.I;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DownloadmemberMapTask {
    private final static String TAG = "DownloadmemberMapTask";
    String hxid;
    Context context;

    public DownloadmemberMapTask(Context context, String username) {
        this.hxid = username;
        this.context = context;
    }
    public void execute(){
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<String>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = Utils.getListResultFromJson(s,MemberUserAvatar.class);
                        List<MemberUserAvatar> list = (List<MemberUserAvatar>) result.getRetData();
                        if (list!=null&&list.size()>0){
                            Log.e(TAG, "onSuccess: list="+list);
                            Map<String, HashMap<String, MemberUserAvatar>> map =
                                    fulicenter.getInstance().getMemberMap();
                            if (!map.containsKey(hxid)) {
                                map.put(hxid, new HashMap<String, MemberUserAvatar>());
                            }
                            HashMap<String, MemberUserAvatar> hxidmembers = map.get(hxid);
                            for (MemberUserAvatar u : list) {
                                hxidmembers.put(u.getMUserName(), u);
                            }
                                context.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "onError:error "+error);
                    }
                });
    }

}
