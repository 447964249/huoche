package cn.ucai.fulicenter.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ucai.fulicenter.R;



public class fulicenterMainActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout mParentCart;
    //mNewGoodsFragment,mBoutiqueFragment,mCategoryFragment,mCollectFragment,mCartFragment,mContactFragment
   /* NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CollectFragment mCollectFragment;
    CartFragment mCartFragment;
    ContactFragment mContactFragment;*/
    Drawable drawableTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulicenter_main);
        initView();
        setListener();
    }
    Button mtvNewGoods,
            mtvBoutique,
            mtvCategory,
            mtvCart,
            mtvCollect,
            mtvpersonal;

    private void initView() {
        mtvNewGoods = (Button) findViewById(R.id.btn_new_good);
        mtvBoutique = (Button) findViewById(R.id.btn_boutique);
        mtvCategory = (Button) findViewById(R.id.btn_category);
        mtvCart = (Button) findViewById(R.id.btn_cart);
        mtvpersonal = (Button) findViewById(R.id.btn_personal);


        //mvp = (ViewPager) findViewById(R.id.mvp);
    }



    private void setListener() {

        mtvNewGoods.setOnClickListener(this);
        mtvBoutique.setOnClickListener(this);
        mtvCategory.setOnClickListener(this);

      //  mtvCollect.setOnClickListener(this);
        mtvCart.setOnClickListener(this);


        mtvpersonal.setOnClickListener(this);


        FragmentManager manager = getSupportFragmentManager();

       // initFragments();

    }
    private void initFragments(){

       /* mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mCollectFragment = new CollectFragment();
        mCartFragment = new CartFragment();
        mContactFragment = new ContactFragment();
        mFragment = new Fragment[]{mNewGoodsFragment, mBoutiqueFragment, mCategoryFragment, mCollectFragment, mCartFragment, mContactFragment};
*/
    }
    //定义旋转
    private void booleanRotationY(TextView tv){
        ViewPropertyAnimator animate = tv.animate();
        float rotationY = tv.getRotationY();
        if (rotationY < 360) {
            animate.rotationY(360);
        }else {
            animate.rotationY(0);
        }
        animate.setDuration(1000);
        animate.start();
    }
    //点击按钮，被点击按钮会变色
    @Override
    public void onClick(View v) {
        initDrawables();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_new_good:
              //  booleanRotationY(mtvNewGoods);
                setDrawable(mtvNewGoods, Color.rgb(0xff, 0x66, 0xff), R.drawable.menu_item_new_good_selected);
//                mNewGoodsFragment = new NewGoodsFragment();
//                ft.replace(R.id.layoutContent, mNewGoodsFragment).commit();
                break;
            case R.id.btn_boutique:
              //  booleanRotationY(mtvBoutique);
                setDrawable(mtvBoutique, Color.rgb(0xff, 0x66, 0xff), R.drawable.boutique_selected);
//                mBoutiqueFragment = new BoutiqueFragment();
//                ft.replace(R.id.layoutContent, mBoutiqueFragment).commit();
                break;
            case R.id.btn_category:
              //  booleanRotationY(mtvCategory);
                setDrawable(mtvCategory, Color.rgb(0xff, 0x66, 0xff), R.drawable.menu_item_category_selected);
//                mCategoryFragment = new CategoryFragment();
//                ft.replace(R.id.layoutContent, mCategoryFragment).commit();
                break;
            /*case R.id.tvCollect:
                booleanRotationY(mtvCollect);
                setDrawable(mtvCollect, Color.rgb(0xff, 0x66, 0xff), R.drawable.menu_item_collect_selected);
//                mCollectFragment = new CollectFragment();
//                ft.replace(R.id.layoutContent, mCollectFragment).commit();
                break;*/
            case R.id.btn_cart:
               // booleanRotationY(mtvCart);
                setDrawable(mtvCart, Color.rgb(0xff, 0x66, 0xff), R.drawable.menu_item_cart_selected);
//                mCartFragment = new CartFragment();
//                ft.replace(R.id.layoutContent, mCartFragment).commit();
                break;
            case R.id.btn_personal:
              //  booleanRotationY(mtvpersonal);
                setDrawable(mtvpersonal, Color.rgb(0xff, 0x66, 0xff), R.drawable.menu_item_personal_center_selected);
//                mContactFragment = new ContactFragment();
//                ft.replace(R.id.layoutContent, mContactFragment).commit();
                break;
        }
    }

    private void initDrawables() {
        setDrawable(mtvNewGoods, Color.BLACK, R.drawable.menu_item_new_good_normal);
        setDrawable(mtvBoutique, Color.BLACK, R.drawable.boutique_normal);
        setDrawable(mtvCategory, Color.BLACK, R.drawable.menu_item_category_normal);
       // setDrawable(mtvCollect, Color.BLACK, R.drawable.menu_item_collect_normal);
        setDrawable(mtvCart, Color.BLACK, R.drawable.menu_item_cart_normal);
        setDrawable(mtvpersonal, Color.BLACK, R.drawable.menu_item_personal_center_normal);
    }
    //工具：设置view颜色，（ 控件，颜色代码，）
    private void setDrawable(TextView tv, int textColor, int drawableId) {
        tv.setTextColor(textColor);
        drawableTop = ContextCompat.getDrawable(this, drawableId);
        drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        tv.setCompoundDrawables(null, drawableTop, null, null);
    }
    /*class  myViewpager extends FragmentPagerAdapter {
        Fragment[] mFragment;

        public myViewpager(FragmentManager fm, Fragment[] mFragment) {
            super(fm);
            //   mFragment = new Fragment[]{mNewGoodsFragment, mBoutiqueFragment, mCategoryFragment, mCollectFragment, mCartFragment, mContactFragment};
            this.mFragment = mFragment;
        }

        public myViewpager(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            return   mFragment[position];
        }

        @Override
        public int getCount() {
            return mFragment.length;
        }
    }*/
}
