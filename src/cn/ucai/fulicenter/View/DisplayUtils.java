package cn.ucai.fulicenter.View;

/**
 * Created by Administrator on 2016/8/3.
 */

        import android.app.Activity;
        import android.view.View;
        import android.widget.TextView;

        import cn.ucai.fulicenter.R;

/**
 * Created by clawpo on 16/8/3.
 */
public class DisplayUtils {
    public static void initBack(final Activity activity){
        activity.findViewById(R.id.backClickArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
    public static void initBack(final Activity activity,String Tiltel){
        initBack(activity);
        ((TextView)activity.findViewById(R.id.tiltelArea)).setText(Tiltel);
    }
}