package cn.ucai.fulicenter.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.activity.SettingsActivity;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.fulicenter;
import cn.ucai.fulicenter.utils.UserUtils;

/**
 * Created by clawpo on 16/8/8.
 */
public class PersonalCenterFragment extends Fragment {
    private final static String TAG = PersonalCenterFragment.class.getSimpleName();
    fulicenterMainActivity mContext;

    ImageView mivUserAvatar;
    TextView mtvUserName;
    TextView mtvSettings;
    ImageView mivMSG;
    TextView mtvCollectCount;
    RelativeLayout layoutUserCenter;
    LinearLayout layoutCollect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (fulicenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_personal_center, null);
        initView(layout);
        intData();
        setListener();
        return layout;
    }

    private void intData() {
        if(DemoHXSDKHelper.getInstance().isLogined()){
            UserAvatar user = fulicenter.getInstance().getUser();
            Log.e(TAG,"user="+user);
            UserUtils.setAppCurrentUserNick(mtvUserName);
            UserUtils.setAppCurrentUserAvatar(mContext,mivUserAvatar);
        }
    }

    private void setListener() {
        MyClickListener listener = new MyClickListener();
        mtvSettings.setOnClickListener(listener);
        layoutUserCenter.setOnClickListener(listener);
        layoutCollect.setOnClickListener(listener);
        upateCollectCountListener();
    }

    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(DemoHXSDKHelper.getInstance().isLogined()){
                switch (v.getId()){
                    case R.id.tv_center_settings:
                    case R.id.center_user_info:
                        startActivity(new Intent(mContext,SettingsActivity.class));
                        break;
                    case R.id.layout_center_collect:
                        startActivity(new Intent(mContext,CollectActivity.class));
                }
            }else{
                Log.e(TAG,"not logined...");
            }
        }
    }

    private void initView(View layout) {
        mivUserAvatar = (ImageView) layout.findViewById(R.id.iv_user_avatar);
        mtvUserName = (TextView) layout.findViewById(R.id.tv_user_name);
        mtvSettings = (TextView) layout.findViewById(R.id.tv_center_settings);
        mivMSG = (ImageView) layout.findViewById(R.id.iv_persona_center_msg);
        mtvCollectCount = (TextView) layout.findViewById(R.id.tv_collect_count);
        layoutUserCenter = (RelativeLayout) layout.findViewById(R.id.center_user_info);
        layoutCollect = (LinearLayout) layout.findViewById(R.id.layout_center_collect);
        initOrderList(layout);
    }

    private void initOrderList(View layout) {
        GridView gvOrderList = (GridView) layout.findViewById(R.id.center_user_order_lis);
        ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> order1 = new HashMap<String, Object>();
        order1.put("order",R.drawable.order_list1);
        data.add(order1);
        HashMap<String,Object> order2 = new HashMap<String, Object>();
        order2.put("order",R.drawable.order_list2);
        data.add(order2);
        HashMap<String,Object> order3 = new HashMap<String, Object>();
        order3.put("order",R.drawable.order_list3);
        data.add(order3);
        HashMap<String,Object> order4 = new HashMap<String, Object>();
        order4.put("order",R.drawable.order_list4);
        data.add(order4);
        HashMap<String,Object> order5 = new HashMap<String, Object>();
        order5.put("order",R.drawable.order_list5);
        data.add(order5);
        SimpleAdapter adapter = new SimpleAdapter(mContext,data,R.layout.fragment_emojicon,
                new String[]{"order"},new int[]{R.id.center_user_order_lis});
        gvOrderList.setAdapter(adapter);
    }

    class UpdateCollectCount extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int count = fulicenter.getInstance().getCollectCount();
            Log.e(TAG,"count = "+count);
            mtvCollectCount.setText(String.valueOf(count));
        }
    }
    UpdateCollectCount mReceiver;
    private void upateCollectCountListener(){
        mReceiver = new UpdateCollectCount();
        IntentFilter filter = new IntentFilter("update_collect");
        mContext.registerReceiver(mReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }
}