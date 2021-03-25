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
import android.widget.EditText;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    private RecyclerView search_recyclerView;
    public static RelativeLayout search_middle_relativeLayout;
    private static ImageButton search_image_btn, search_minus_btn;
    private static String url_api;
    private static List<NewsClass> newsList = new ArrayList<>();
    private static LiveNewsAdapter liveNewsAdapter;
    private static EditText search_query_edt;
    public static TextView search_date_txt, search_noResult_txt;
    private static String country,sortBY,From,To,Language="";
    final static Map<String,String> map_country = new HashMap<>();
    final static Map<String,String> map_sort = new HashMap<>();
    private static Context context_of_search;
    private static ArrayMap<String, List<String>> applied_filters;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        country="";
        sortBY="";
        From="";
        To="";

        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        search_image_btn = view.findViewById(R.id.search_image_btn);
        search_query_edt = view.findViewById(R.id.search_query_edt);
        search_middle_relativeLayout= view.findViewById(R.id.search_middle_relativeLayout);
        search_minus_btn=view.findViewById(R.id.search_minus_btn);
        search_date_txt=view.findViewById(R.id.search_date_txt);
        search_noResult_txt=view.findViewById(R.id.search_noResult_txt);

        context_of_search=getContext();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);
        search_recyclerView.setLayoutManager(linearLayoutManager);
        liveNewsAdapter = new LiveNewsAdapter(getContext(), newsList);
        search_recyclerView.setAdapter(liveNewsAdapter);

        map_country.clear();
        String[] country_keys = getResources().getStringArray(R.array.arrays_country_keys);
        String[] country_values = getResources().getStringArray(R.array.arrays_country_values);

        for (int j=0 ; j < country_keys.length ; j++){
            map_country.put(country_keys[j],country_values[j]);
        }

        map_sort.clear();
        String[] sort_values = getResources().getStringArray(R.array.arrays_sort_values);
        String[] sort_keys = getResources().getStringArray(R.array.arrays_sort_keys);

        for (int k=0; k < sort_values.length ; k++){
            map_sort.put(sort_keys[k],sort_values[k]);
        }


        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("language_save",Context.MODE_PRIVATE);
                Language = sharedPreferences.getString("language_value","");

                if (!Language.equals("") || !Language.equals("all")){
                    Language="&lang="+Language;
                }
                else{
                    Language="";
                }

                news_feeds(getContext());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        search_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                From="";
                To="";

                MySampleFabFragment.applied_filters_search.remove("fromSearch");
                MySampleFabFragment.applied_filters_search.remove("toSearch");

                if (!TextUtils.isEmpty(search_query_edt.getText().toString().trim())) {
                    news_feeds(getContext());
                }
                else{
                    search_query_edt.setText("");
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });

        newsList.clear();
        return view;
    }

    public static void apply_filter_news_feed(ArrayMap<String, List<String>> filters_applied, Context context) {
        applied_filters = filters_applied;
        sortBY = "";
        country = "";
        From="";
        To="";
        String sort = "", coun = "";
        newsList.clear();
        liveNewsAdapter.notifyDataSetChanged();

        if (applied_filters.get("country_search") != null || applied_filters.get("sortby") != null
                || applied_filters.get("fromSearch")!=null || applied_filters.get("toSearch")!=null) {

            if (applied_filters.get("country_search") != null) {
                for (int y = 0; y < applied_filters.get("country_search").size(); y++) {
                    coun += map_country.get(applied_filters.get("country_search").get(y)) + ",";
                }
                coun = coun.substring(0, coun.lastIndexOf(","));
            }

            if (applied_filters.get("sortby") != null) {
                for (int y1 = 0; y1 < applied_filters.get("sortby").size(); y1++) {
                    sort += map_sort.get(applied_filters.get("sortby").get(y1)) + ",";
                }
                sort = sort.substring(0, sort.lastIndexOf(","));
            }

            if (applied_filters.get("fromSearch")!=null){
                From=applied_filters.get("fromSearch").get(0);
                From="&from="+From;
            }

            if (applied_filters.get("toSearch")!=null){
                To=applied_filters.get("toSearch").get(0);
                To="&to="+To;
            }

            if (!TextUtils.isEmpty(coun)) {
                country = "&country=" + coun;
            }

            if (!TextUtils.isEmpty(sort)) {
                sortBY = "&sort=" + sort;
            }
        }
        news_feeds(context);

    }

    private static void news_feeds(Context context) {
        DownloadTask dt =new DownloadTask();
        try {
            newsList.clear();
            search_noResult_txt.setVisibility(View.GONE);
            liveNewsAdapter.notifyDataSetChanged();
            String query = search_query_edt.getText().toString().trim();
            if (TextUtils.isEmpty(query)) {
                Toast.makeText(context, "Enter Something First", Toast.LENGTH_SHORT).show();
                search_query_edt.setText("");
            } else {
                url_api = "https://gnews.io/api/v4/search?q="+query+"&token="+context.getResources().getString(R.string.api_key);
                url_api+=country+sortBY+From+To+Language;
                if (!From.equals("")){

                    search_middle_relativeLayout.setVisibility(View.VISIBLE);

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

                    search_date_txt.setText(time2);
                }
                if(!To.equals("")){
                    search_middle_relativeLayout.setVisibility(View.VISIBLE);

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

                    search_date_txt.setText(time2);
                }

                else if (From.equals("") && To.equals("")){
                    search_middle_relativeLayout.setVisibility(View.GONE);
                    search_date_txt.setText("");
                }

                dt.execute(url_api);
            }
        }
        catch (Exception e){
            Log.i("error",e.getMessage()+"");
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

            liveNewsAdapter.notifyDataSetChanged();
            if (s.equals("size_zero")) {
                Toast.makeText(context_of_search, "No results for search", Toast.LENGTH_SHORT).show();
                search_noResult_txt.setVisibility(View.VISIBLE);
            }
            else{
                search_noResult_txt.setVisibility(View.GONE);
            }
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

        if (!TextUtils.isEmpty(search_query_edt.getText().toString().trim())){

            apply_filter_news_feed(MySampleFabFragment.applied_filters_search,context_of_search);
        }

    }

}