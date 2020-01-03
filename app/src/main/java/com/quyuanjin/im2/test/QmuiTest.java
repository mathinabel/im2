package com.quyuanjin.im2.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.quyuanjin.im2.R;

import java.util.ArrayList;
import java.util.List;

public class QmuiTest extends AppCompatActivity {
    private View view1, view2, view3;
    private ViewPager viewPager;  //对应的viewPager

    private List<View> viewList;//view数组
    private QMUITabSegment qmuiTabSegment;
    TabLayout mytab;
    String tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qumui_test);

     /*   qmuiTabSegment = findViewById(R.id.QMUITabSegment);
        int normalColor = QMUIResHelper.getAttrColor(this.getApplicationContext(), R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(this.getApplicationContext(), R.attr.qmui_config_color_blue);
        qmuiTabSegment.setDefaultNormalColor(normalColor);//设置tab正常下的颜色  
        qmuiTabSegment.setDefaultSelectedColor(selectColor);
        qmuiTabSegment.setHasIndicator(true);
        qmuiTabSegment.setIndicatorPosition(false);
        qmuiTabSegment.setIndicatorWidthAdjustContent(true);
        qmuiTabSegment.addTab(new QMUITabSegment.Tab("123"));
        qmuiTabSegment.addTab(new QMUITabSegment.Tab("565"));
        qmuiTabSegment.selectTab(0);*/

        mytab = findViewById(R.id.mytab);

        mytab.addTab(mytab.newTab().setText("选项卡一").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡二").setIcon(R.mipmap.ic_launcher));
        mytab.addTab(mytab.newTab().setText("选项卡三").setIcon(R.mipmap.ic_launcher));



        viewPager = findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2, null);
        view3 = inflater.inflate(R.layout.layout3, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);


        PagerAdapter pagerAdapter = new PagerAdapter() {


            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };


        viewPager.setAdapter(pagerAdapter);
        mytab.setupWithViewPager(viewPager);
        mytab.getTabAt(0).setText("title1");
        mytab.getTabAt(1).setText("title2");
        mytab.getTabAt(2).setText("title3");



    }

}
