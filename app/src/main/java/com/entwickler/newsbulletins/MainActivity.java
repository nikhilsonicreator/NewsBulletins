package com.entwickler.newsbulletins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageButton;

import com.entwickler.newsbulletins.Model.NewsClass;
import com.entwickler.newsbulletins.Model.SliderItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton filter_fab;
    public static ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private List<NewsClass> newsList = new ArrayList<>();
    private ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
    private CollapsingToolbarLayout main_activity_collapsing_toolbar;
    private SliderView imageSlider;
    private List<SliderItem> sliderItemList = new ArrayList<>();
    private SliderAdapterExample sliderAdapterExample;
    private ImageButton more_vert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter_fab=findViewById(R.id.filter_float);
        main_activity_collapsing_toolbar=findViewById(R.id.main_activity_collapsing_toolbar);
        viewPager=findViewById(R.id.main_viewPager);
        more_vert=findViewById(R.id.more_vert);

        tabAdapter=new TabAdapter(getSupportFragmentManager());

        imageSlider=findViewById(R.id.imageSlider);

        viewPager.setAdapter(tabAdapter);

        tabLayout=findViewById(R.id.main_tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);

        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int u=viewPager.getCurrentItem();
                String send="";
                if (u==0){
                    send="Search";
                }
                else if (u==1){
                    send="Bullets";
                }
                MySampleFabFragment dialogFrag = MySampleFabFragment.newInstance(send);
                dialogFrag.setParentFab(filter_fab);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });

        viewPager.setCurrentItem(1);


        sliderAdapterExample=new SliderAdapterExample(this,sliderItemList);
        imageSlider.setSliderAdapter(sliderAdapterExample);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(5);
        imageSlider.setAutoCycle(true);
        imageSlider.startAutoCycle();

        sliderItemList.clear();
        for (int i=0;i<5;i++) {
            SliderItem sliderItem = new SliderItem();
            if (i == 0){
                sliderItem.setDescription("Get daily news on phone");
                sliderItem.setImageUrl(R.drawable.news_on_phone);
            }

            if (i==1) {
                sliderItem.setDescription("News from sources all over the world");
                sliderItem.setImageUrl(R.drawable.multi_news);
            }

            if (i==2){
                sliderItem.setDescription("Fast and live news to stay updated");
                sliderItem.setImageUrl(R.drawable.news_update);
            }

            if (i==3){
                sliderItem.setDescription("News of various categories, countries and languages");
                sliderItem.setImageUrl(R.drawable.news_all_around);
            }

            if (i == 4) {
                sliderItem.setDescription("News to make you social");
                sliderItem.setImageUrl(R.drawable.social);
            }

            sliderItemList.add(sliderItem);
            sliderAdapterExample.notifyDataSetChanged();
        }


        more_vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

    }


    public ArrayMap<String, List<String>> getApplied_filters() {
        return applied_filters;
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}