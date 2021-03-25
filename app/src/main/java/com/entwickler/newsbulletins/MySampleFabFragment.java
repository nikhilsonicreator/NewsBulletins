package com.entwickler.newsbulletins;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class MySampleFabFragment extends AAH_FabulousFragment {

    private static String Received="";
    private String from_or_to="";
    public static ArrayMap<String, List<String>> applied_filters_bullets = new ArrayMap<>();
    public static ArrayMap<String, List<String>> applied_filters_search = new ArrayMap<>();
    List<TextView> textviews = new ArrayList<>();
    List<TextView> textviews_topic = new ArrayList<>();
    List<TextView> textviews_country_bullets = new ArrayList<>();
    List<TextView> textviews_sortby = new ArrayList<>();
    List<TextView> textviews_country_search = new ArrayList<>();
    ImageButton filter_view_search;
    TabLayout tabs_types;
    ImageButton imgbtn_refresh, imgbtn_calender;
    ViewPager vp_types;
    private DisplayMetrics metrics;
    private int Hours=-1,Minutes=-1,Day=-1,Month=-1,Year=-1;

    public static MySampleFabFragment newInstance(String received) {
        MySampleFabFragment f = new MySampleFabFragment();
        Received=received;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applied_filters_bullets = ((MainActivity) getActivity()).getApplied_filters();
        metrics = this.getResources().getDisplayMetrics();

        applied_filters_search = ((MainActivity) getActivity()).getApplied_filters();
        metrics = this.getResources().getDisplayMetrics();

        for (Map.Entry<String, List<String>> entry : applied_filters_search.entrySet()) {
            Log.d("k9res", "from activity: " + entry.getKey());
            for (String s : entry.getValue()) {
                Log.d("k9res", "from activity val: " + s);

            }
        }
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.filter_view, null);

        tabs_types = contentView.findViewById(R.id.tabs_types);
        RelativeLayout rl_content =  contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons =  contentView.findViewById(R.id.ll_buttons);
        imgbtn_refresh = (ImageButton) contentView.findViewById(R.id.imgbtn_refresh);
        imgbtn_calender = (ImageButton) contentView.findViewById(R.id.imgbtn_calender);
        vp_types = contentView.findViewById(R.id.vp_types);
        filter_view_search=contentView.findViewById(R.id.filterview_search);

        filter_view_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Received.equals("Bullets")){
                    LiveNewsFragment.apply_filter_news_feed(applied_filters_bullets,getContext());
                    closeFilter(applied_filters_bullets);
                }
                else if(Received.equals("Search")){
                    SearchFragment.apply_filter_news_feed(applied_filters_search,getContext());
                    closeFilter(applied_filters_search);
                }

            }
        });

        imgbtn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hours = -1;
                Minutes = -1;
                Day = -1;
                Month = -1;
                Year = -1;
                from_or_to="";

            int mYear,mMonth,mDay;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Year = year;
                            Month = monthOfYear+1;
                            Day = dayOfMonth;

                            final Calendar c = Calendar.getInstance();

                            int mHour,mMinute;
                            mHour = c.get(Calendar.HOUR_OF_DAY);
                            mMinute = c.get(Calendar.MINUTE);

                            // Launch Time Picker Dialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                  new TimePickerDialog.OnTimeSetListener() {


                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            Hours = hourOfDay;
                            Minutes = minute;

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("You want results: ");
                            String[] items = {"From this date","To this date"};
                            int checkedItem = 0;
                            if (Received.equals("Bullets")){
                                from_or_to="fromBullets";
                            }
                            else if (Received.equals("Search")){
                                from_or_to="fromSearch";
                            }

                            alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            if (Received.equals("Bullets")){
                                                from_or_to = "fromBullets";}
                                            else if (Received.equals("Search")){
                                            from_or_to = "fromSearch";}

                                            break;
                                        case 1:
                                            if (Received.equals("Bullets")){
                                                from_or_to = "toBullets";
                                            }
                                            else if (Received.equals("Search")){
                                                from_or_to = "toSearch";
                                            }
                                            break;
                                    }
                                }
                            })
                            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (from_or_to.equals("")) {
                                        Toast.makeText(getContext(), "Please Select Something", Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        if (Received.equals("Bullets")) {
                                            applied_filters_bullets.remove("toBullets");
                                            applied_filters_bullets.remove("fromBullets");
                                        }
                                        else if (Received.equals("Search")){
                                            applied_filters_search.remove("toSearch");
                                            applied_filters_search.remove("fromSearch");
                                        }


                                        String Day0=String.valueOf(Day),Month0=String.valueOf(Month);
                                        String Hours0 = String.valueOf(Hours),Minutes0 =String.valueOf(Minutes);
                                        if(Day0.length()==1){
                                            Day0="0"+Day;
                                        }
                                        if (Month0.length()==1){
                                            Month0="0"+Month;
                                        }
                                        if (Hours0.length()==1){
                                            Hours0="0"+Hours;
                                        }
                                        if (Minutes0.length()==1){
                                            Minutes0="0"+Minutes;
                                        }

                                        OffsetDateTime parsed = LocalDateTime.parse(Year+"-"+Month0+"-"+Day0+"T"+Hours0+":"+Minutes0+":00")
                                                // convert to UTC
                                                .atOffset(ZoneOffset.UTC);

                                            List<String> temp = new ArrayList<>();
                                            temp.add(parsed.toString().substring(0,16)+":00Z");


                                            if (Received.equals("Bullets")){
                                                applied_filters_bullets.put(from_or_to,temp);
                                                LiveNewsFragment.apply_filter_news_feed(applied_filters_bullets,getContext());
                                                closeFilter(applied_filters_bullets);
                                            }
                                            else if(Received.equals("Search")){
                                                applied_filters_search.put(from_or_to,temp);
                                                SearchFragment.apply_filter_news_feed(applied_filters_search,getContext());
                                                closeFilter(applied_filters_search);
                                            }


                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = alertDialog.create();
                            alert.setCanceledOnTouchOutside(false);
                            alert.show();
                        }
                    }, mHour, mMinute, true);

                            timePickerDialog.show();

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            }
        });


        imgbtn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv : textviews) {
                    tv.setTag("unselected");
                    tv.setBackgroundResource(R.drawable.chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                }

                if (Received.equals("Bullets")) {
                    applied_filters_bullets.remove("fromBullets");
                    applied_filters_bullets.remove("toBullets");
                    applied_filters_bullets.remove("topic");
                    applied_filters_bullets.remove("country_bullets");
                    LiveNewsFragment.apply_filter_news_feed(applied_filters_bullets, getContext());
                    closeFilter(applied_filters_bullets);
                }

                else if (Received.equals("Search")) {
                    applied_filters_search.remove("fromSearch");
                    applied_filters_search.remove("toSearch");
                    applied_filters_search.remove("sortby");
                    applied_filters_search.remove("country_search");
                    SearchFragment.apply_filter_news_feed(applied_filters_search, getContext());
                    closeFilter(applied_filters_search);
                }

            }
        });


        vp_types.setOffscreenPageLimit(2);
        vp_types.setAdapter(new SectionsPagerAdapter());

        tabs_types.setupWithViewPager(vp_types);

        //params to set

        setAnimationDuration(600); //optional; default 500ms
        setPeekHeight(400); // optional; default 400dp
//        setCallbacks((Callbacks) getActivity()); //optional; to get back result
//        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewPager(vp_types); //optional; if you use viewpager that has scrollview
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last

    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_filters_sorters, collection, false);
            FlexboxLayout fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
//            LinearLayout ll_scroll = (LinearLayout) layout.findViewById(R.id.ll_scroll);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels-(104*metrics.density)));
//            ll_scroll.setLayoutParams(lp);

            switch (position) {
                case 0:
                    if (Received.equals("Bullets")) {
                        inflateLayoutWithFilters("topic", fbl);
                    }
                    else if (Received.equals("Search")) {
                        inflateLayoutWithFilters("sortby", fbl);
                    }
                     break;

                case 1:
                    if (Received.equals("Bullets")) {
                        inflateLayoutWithFilters("country_bullets", fbl);
                    }
                    else if (Received.equals("Search")){
                        inflateLayoutWithFilters("country_search", fbl);
                    }
                    break;
                }

            collection.addView(layout);
            return layout;

        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    if (Received.equals("Search")){
                        return "SORT BY";
                    }
                    else if(Received.equals("Bullets")) {
                        return "CATEGORY";
                    }
                case 1:
                    if (Received.equals("Search")) {
                        return "COUNTRY";
                    }
                    else if(Received.equals("Bullets")){
                        return "COUNTRY";
                    }
            }
            return "";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void inflateLayoutWithFilters(final String filter_category, FlexboxLayout fbl) {
        List<String> keys = new ArrayList<>();
        switch (filter_category) {
            case "topic":
                String[] topic_keys = getResources().getStringArray(R.array.arrays_category_keys);
                keys.addAll(Arrays.asList(topic_keys));
                break;
            case "sortby":
                String[] sortby_keys = getResources().getStringArray(R.array.arrays_sort_keys);
                keys.addAll(Arrays.asList(sortby_keys));
                break;
            case "country_bullets":
            case "country_search":
                String[] country_keys = getResources().getStringArray(R.array.arrays_country_keys);
                keys.addAll(Arrays.asList(country_keys));
                break;

        }

        for (int i = 0; i < keys.size(); i++) {
            View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
            final TextView tv = ((TextView) subchild.findViewById(R.id.txt_title));
            tv.setText(keys.get(i));
            final int finalI = i;
            final List<String> finalKeys = keys;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.getTag() != null && tv.getTag().equals("selected")) {
                        tv.setTag("unselected");
                        tv.setBackgroundResource(R.drawable.chip_unselected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                        removeFromSelectedMap(filter_category, finalKeys.get(finalI));
                    } else {
                        List<TextView> text = new ArrayList<>();
                        if (filter_category.equals("topic")){
                            text=textviews_topic;
                        }
                        else if (filter_category.equals("country_bullets")){
                            text=textviews_country_bullets;
                        }
                        else if(filter_category.equals("sortby")){
                            text=textviews_sortby;
                        }
                        else if (filter_category.equals("country_search")){
                            text = textviews_country_search;
                        }

                        for (TextView tv : text) {
                            tv.setTag("unselected");
                            tv.setBackgroundResource(R.drawable.chip_unselected);
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                        }

                        tv.setTag("selected");
                        tv.setBackgroundResource(R.drawable.chip_selected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        addToSelectedMap(filter_category, finalKeys.get(finalI));
                    }
                }
            });

            if(Received.equals("Bullets")) {
                if (applied_filters_bullets != null && applied_filters_bullets.get(filter_category) != null && applied_filters_bullets.get(filter_category).contains(keys.get(finalI))) {
                    tv.setTag("selected");
                    tv.setBackgroundResource(R.drawable.chip_selected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    tv.setBackgroundResource(R.drawable.chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                }
            }

            else if (Received.equals("Search")) {
                if (applied_filters_search != null && applied_filters_search.get(filter_category) != null && applied_filters_search.get(filter_category).contains(keys.get(finalI))) {
                    tv.setTag("selected");
                    tv.setBackgroundResource(R.drawable.chip_selected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    tv.setBackgroundResource(R.drawable.chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                }
            }


            if (filter_category.equals("topic")){
                textviews_topic.add(tv);
            }
            else if (filter_category.equals("country_bullets")) {
                textviews_country_bullets.add(tv);
            }
            else if (filter_category.equals("country_search")){
                textviews_country_search.add(tv);
            }
            else if(filter_category.equals("sortby")){
                textviews_sortby.add(tv);
            }
            textviews.add(tv);


            fbl.addView(subchild);
        }

    }

    private void addToSelectedMap(String key, String value) {

        if (Received.equals("Bullets")) {

            if (applied_filters_bullets.get(key) != null && !applied_filters_bullets.get(key).contains(value)) {
                applied_filters_bullets.remove(key);
                List<String> temp = new ArrayList<>();
                temp.add(value);
                applied_filters_bullets.put(key, temp);

            } else {
                List<String> temp = new ArrayList<>();
                temp.add(value);
                applied_filters_bullets.put(key, temp);
            }
        }

        else if (Received.equals("Search")){
            if (applied_filters_search.get(key) != null && !applied_filters_search.get(key).contains(value)) {
                applied_filters_search.remove(key);
                List<String> temp = new ArrayList<>();
                temp.add(value);
                applied_filters_search.put(key, temp);

            } else {
                List<String> temp = new ArrayList<>();
                temp.add(value);
                applied_filters_search.put(key, temp);
            }
        }
    }

    private void removeFromSelectedMap(String key, String value) {

        if (Received.equals("Bullets")) {

            if (applied_filters_bullets.get(key).size() == 1) {
                applied_filters_bullets.remove(key);
            } else {
                applied_filters_bullets.get(key).remove(value);
            }
        }

        else if (Received.equals("Search")){
            if (applied_filters_search.get(key).size() == 1) {
                applied_filters_search.remove(key);
            } else {
                applied_filters_search.get(key).remove(value);
            }
        }

    }



}
