package cn.ucai.fulicenter.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.fulicenterMainActivity;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PersonalFragment extends Fragment {

    fulicenterMainActivity mContent;
    ImageView mivUserAvatar, mivMSG;
    TextView mtvUserName, mtvSettings, mtvCollectCount;
    RelativeLayout layoutUserCenter;
    LinearLayout layoutCollect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = View.inflate(mContent, R.layout.fragment_personal_center, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        mivUserAvatar = (ImageView) layout.findViewById(R.id.iv_user_avatar);
        mtvUserName = (TextView) layout.findViewById(R.id.tv_user_name);
        mtvSettings = (TextView) layout.findViewById(R.id.tv_center_settings);
        mivMSG = (ImageView) layout.findViewById(R.id.iv_persona_center_msg);
        mtvCollectCount = (TextView) layout.findViewById(R.id.tv_collect_count);
        layoutUserCenter = (RelativeLayout) layout.findViewById(R.id.center_user_info);
        layoutCollect = (LinearLayout) layout.findViewById(R.id.layout_center_collect);
        initDrderList(layout);
    }

    private void initDrderList(View layout) {
        GridView gvOrderList = (GridView) layout.findViewById(R.id.center_user_order_lis);
        ArrayList<HashMap<String, Object>> date = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> order1 = new HashMap<String, Object>();
        order1.put("order", R.drawable.order_list1);
        date.add(order1);
        SimpleAdapter adapter = new SimpleAdapter(mContent,date,R.layout.fragment_personal_center,
                new String[]{"order"},new int[]{R.id.center_user_order_lis});
        gvOrderList.setAdapter(adapter);
    }

}
