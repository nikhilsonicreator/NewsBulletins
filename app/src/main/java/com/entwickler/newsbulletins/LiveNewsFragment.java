package com.entwickler.newsbulletins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entwickler.newsbulletins.Model.NewsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveNewsFragment extends Fragment {

    private static String url_api;
    private RecyclerView live_news_recyclerView;
    public static RelativeLayout live_news_top_relativeLayout;
    public static TextView live_news_date_txt;
    private static ImageButton live_news_minus_btn;
    private static List<NewsClass> newsList = new ArrayList<>();
    private static LiveNewsAdapter liveNewsAdapter;
    static ArrayMap<String, List<String>> applied_filters;
    final static Map<String,String> map_country = new HashMap<>();
    final static Map<String,String> map_topic = new HashMap<>();
    private static String topic,country,From,To,Language="";
    private static Context context_of_search;
    private static TextView line_news_noResult_txt;

    public LiveNewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_live_news, container, false);

        topic="";country="";From="";To="";

        live_news_recyclerView=view.findViewById(R.id.live_news_recyclerView);
        live_news_top_relativeLayout=view.findViewById(R.id.live_news_top_relativeLayout);
        live_news_date_txt=view.findViewById(R.id.live_news_date_txt);
        live_news_minus_btn=view.findViewById(R.id.live_news_minus_btn);
        line_news_noResult_txt=view.findViewById(R.id.live_news_noResult_txt);

        context_of_search=getContext();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);
        live_news_recyclerView.setLayoutManager(linearLayoutManager);
        liveNewsAdapter = new LiveNewsAdapter(getContext(), newsList);
        live_news_recyclerView.setAdapter(liveNewsAdapter);

        map_country.clear();
        String[] country_keys = getResources().getStringArray(R.array.arrays_country_keys);
        String[] country_values = getResources().getStringArray(R.array.arrays_country_values);

        for (int j=0 ; j < country_keys.length ; j++){
            map_country.put(country_keys[j],country_values[j]);
        }

        map_topic.clear();
        String[] topic_keys = getResources().getStringArray(R.array.arrays_category_keys);
        String[] topic_values = getResources().getStringArray(R.array.arrays_category_values);

        for (int j=0 ; j < topic_keys.length ; j++){
            map_topic.put(topic_keys[j],topic_values[j]);
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("language_save",Context.MODE_PRIVATE);
        Language = sharedPreferences.getString("language_value","");

        if (!Language.equals("") || !Language.equals("all")){
            Language="&lang="+Language;
        }
        else{
            Language="";
        }

        live_news_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                From="";
                To="";

                MySampleFabFragment.applied_filters_bullets.remove("fromBullets");
                MySampleFabFragment.applied_filters_bullets.remove("toBullets");

                news_feeds(getContext());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        news_feeds(getContext());

        return view;
    }


    public static void apply_filter_news_feed(ArrayMap<String, List<String>> filters_applied, Context context){
         applied_filters=filters_applied;
         country="";
         topic="";
         From="";
         To="";
        String coun="",top="";
        newsList.clear();
        liveNewsAdapter.notifyDataSetChanged();
        if (applied_filters.get("country_bullets")!=null || applied_filters.get("topic")!=null || applied_filters.get("fromBullets")!=null || applied_filters.get("toBullets")!=null){

            if (applied_filters.get("country_bullets")!=null) {
                for (int y = 0; y < applied_filters.get("country_bullets").size(); y++) {
                    coun += map_country.get(applied_filters.get("country_bullets").get(y)) + ",";
                }
                coun=coun.substring(0,coun.lastIndexOf(","));
            }

            if (applied_filters.get("topic")!=null) {
                for (int y1 = 0; y1 < applied_filters.get("topic").size(); y1++) {
                    top += map_topic.get(applied_filters.get("topic").get(y1)) + ",";
                }
                top = top.substring(0, top.lastIndexOf(","));
            }

            if (applied_filters.get("fromBullets")!=null){
                From=applied_filters.get("fromBullets").get(0);
                From="&from="+From;
            }

            if (applied_filters.get("toBullets")!=null){
                To=applied_filters.get("toBullets").get(0);
                To="&to="+To;
            }

            if (!TextUtils.isEmpty(coun)){
                country="&country="+coun;
            }

            if (!TextUtils.isEmpty(top)){
                topic="&topic="+top;
            }


        }
        news_feeds(context);

    }


    private static void news_feeds(Context context) {
       DownloadTask dt =new DownloadTask();
        try {
            url_api="https://gnews.io/api/v4/top-headlines?token="+context.getResources().getString(R.string.api_key);
            url_api+=topic+country+From+To+Language;

            newsList.clear();
            line_news_noResult_txt.setVisibility(View.GONE);
            liveNewsAdapter.notifyDataSetChanged();

            if (!From.equals("")){
                live_news_top_relativeLayout.setVisibility(View.VISIBLE);

                String time1 = From.substring(6,From.lastIndexOf('Z'));

                String time2 ="";
                time2 += time1.charAt(8);
                time2 += time1.charAt(9);
                time2 += time1.charAt(7);
                time2 += time1.charAt(5);
                time2 += time1.charAt(6);
                time2 += time1.charAt(4);
                time2 += time1.charAt(0);
                time2 += time1.charAt(1);
                time2 += time1.charAt(2);
                time2 += time1.charAt(3);
                time2 += "   ";
                time2 += time1.charAt(11);
                time2 += time1.charAt(12);
                time2 += time1.charAt(13);
                time2 += time1.charAt(14);
                time2 += time1.charAt(15);
                time2 +=" ";

                live_news_date_txt.setText(time2);

            }
            if(!To.equals("")){
                live_news_top_relativeLayout.setVisibility(View.VISIBLE);

                String time1 = To.substring(4,To.lastIndexOf('Z'));

                String time2 ="";
                time2 += time1.charAt(8);
                time2 += time1.charAt(9);
                time2 += time1.charAt(7);
                time2 += time1.charAt(5);
                time2 += time1.charAt(6);
                time2 += time1.charAt(4);
                time2 += time1.charAt(0);
                time2 += time1.charAt(1);
                time2 += time1.charAt(2);
                time2 += time1.charAt(3);
                time2 += "   ";
                time2 += time1.charAt(11);
                time2 += time1.charAt(12);
                time2 += time1.charAt(13);
                time2 += time1.charAt(14);
                time2 += time1.charAt(15);
                time2 +=" ";

                live_news_date_txt.setText(time2);
            }
            else if(From.equals("") && To.equals("")){
                live_news_top_relativeLayout.setVisibility(View.GONE);
                live_news_date_txt.setText("");
            }

            newsList.clear();
            dt.execute(url_api);
        }
        catch (Exception e){

            Toast.makeText(context, "Unable to Load Results", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... Params) {
            URL url;
            HttpURLConnection urlConnection;
            String result="";
            try {
                url=new URL(Params[0]);
                urlConnection=(HttpURLConnection)url.openConnection();

                InputStream inputStream=urlConnection.getInputStream();

                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while(data!=-1){
                    char c=(char)data;
                    result+=c;
                    data=reader.read();
                }
                JSONObject jsonObject=new JSONObject(result);
                String n=jsonObject.getString("articles");
                JSONArray jsonArray=new JSONArray(n);
                if(jsonArray.length()==0){
                    return "size_zero";
                }
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String content = object.getString("content");
                    String title = object.getString("title");
                    String description = object.getString("description");
                    String timestamp = object.getString("publishedAt");
                    String url_of_news = object.getString("url");
                    String image = object.getString("image");

                    JSONObject obj = object.getJSONObject("source");
                    String source_name = obj.getString("name");
                    String url_of_source = obj.getString("url");

                    NewsClass newsClass=new NewsClass(title,description,content,timestamp,image,url_of_news,source_name,url_of_source);
                    newsList.add(newsClass);

                }
                return " ";
            }  catch (IOException e) {
                Log.i("error",e.getMessage()+"");
                e.printStackTrace();
                return "size_zero";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("size_zero")) {
                Toast.makeText(context_of_search, "No results for bullets", Toast.LENGTH_SHORT).show();
                line_news_noResult_txt.setVisibility(View.VISIBLE);
            }
            else{
                line_news_noResult_txt.setVisibility(View.GONE);
            }
            liveNewsAdapter.notifyDataSetChanged();

        }
    }



    public static void get_news_feeds(){

        SharedPreferences sharedPreferences = context_of_search.getSharedPreferences("language_save",Context.MODE_PRIVATE);
        Language = sharedPreferences.getString("language_value","");

        if (!Language.equals("") || !Language.equals("all")){
            Language="&lang="+Language;
        }
        else{
            Language="";
        }

        apply_filter_news_feed(MySampleFabFragment.applied_filters_bullets,context_of_search);
    }


}