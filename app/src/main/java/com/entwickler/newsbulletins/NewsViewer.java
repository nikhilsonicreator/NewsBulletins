package com.entwickler.newsbulletins;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class NewsViewer extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView news_viewer_timestamp_txt,news_viewer_title_txt,news_viewer_description_txt,news_viewer_content_txt;
    CollapsingToolbarLayout news_viewer_collapsing_toolbar;
    ImageView news_viewer_imageview;
    String arr[],urlNews;
    Button news_viewer_readArticle_btn;
    private long mLastClickTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewer);

        toolbar=findViewById(R.id.news_viewer_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        news_viewer_collapsing_toolbar=findViewById(R.id.news_viewer_collapsing_toolbar);
        news_viewer_description_txt=findViewById(R.id.news_viewer_description_txt);
        news_viewer_timestamp_txt=findViewById(R.id.news_viewer_timestamp_txt);
        news_viewer_title_txt=findViewById(R.id.news_viewer_title_txt);
        news_viewer_imageview=findViewById(R.id.news_viewer_imageview);
        news_viewer_readArticle_btn=findViewById(R.id.news_viewer_readArticle_btn);
        news_viewer_content_txt=findViewById(R.id.news_viewer_content_txt);

        Intent intent=getIntent();
        arr=intent.getStringArrayExtra("details_of_news");

        final String title = arr[0];
        final String description =arr[1];
        String content = arr[2];
        final String time = arr[3];
        final String image = arr[4];
        final String sourceName = arr[6];
        final String urlSource = arr[7];
        urlNews = arr[5];

        content = content.substring(0,content.lastIndexOf('['));

        news_viewer_collapsing_toolbar.setTitle(sourceName);

        news_viewer_title_txt.setText(title);
        news_viewer_description_txt.setText(description);
        news_viewer_content_txt.setText(content);

        OffsetDateTime parsed = LocalDateTime.parse(time.substring(0,time.lastIndexOf("Z")))
                .atOffset(ZoneOffset.UTC);


        ZoneId zone = ZoneId.of("Asia/Kolkata");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String time_show = outputFormatter.format(parsed.atZoneSameInstant(zone));

        TimeAgo2 timeAgo2=new TimeAgo2();
        String time1 = timeAgo2.covertTimeToText(time_show);

        news_viewer_timestamp_txt.setText(time1);


        Glide.with(this).load(image).placeholder(R.drawable.ic_launcher_background).into(news_viewer_imageview);

        news_viewer_readArticle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsViewer.this.onClick(v);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        pressedOnClick(v);
    }

    public void pressedOnClick(View v) {
        switch (v.getId()){

            case R.id.news_viewer_readArticle_btn:
                Intent intent1=new Intent(NewsViewer.this,WebViewer.class);
                intent1.putExtra("url_of_news",urlNews);
                startActivity(intent1);
                break;
        }
    }
}