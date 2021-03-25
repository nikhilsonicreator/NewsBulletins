package com.entwickler.newsbulletins;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.entwickler.newsbulletins.Model.NewsClass;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LiveNewsAdapter extends RecyclerView.Adapter<LiveNewsAdapter.MyViewHolder> implements View.OnClickListener {

    Context context;
    String arr[];
    private List<NewsClass> newsList;
    private long mLastClickTime = 0;

    public LiveNewsAdapter(Context context, List<NewsClass> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public LiveNewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final LiveNewsAdapter.MyViewHolder holder, int position) {

        NewsClass data= newsList.get(position);

        String time = data.getTime();
        String image = data.getImage();
        String urlNews = data.getUrlNews();
        String urlSource = data.getUrlSource();
        String title = data.getTitle();
        String description = data.getDescription();
        final String content = data.getContent();
        String sourceName = data.getSourceName();

        holder.news_layout_source_txt.setText(sourceName);
        holder.news_layout_title_txt.setText(title);

        OffsetDateTime parsed = LocalDateTime.parse(time.substring(0,time.lastIndexOf("Z")))
                // convert to UTC
                .atOffset(ZoneOffset.UTC);

        // convert to Asia/Kolkata
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String time_show = outputFormatter.format(parsed.atZoneSameInstant(zone));

        TimeAgo2 timeAgo2=new TimeAgo2();
        String time1=timeAgo2.covertTimeToText(time_show);


        holder.news_layout_time_past_txt.setText(time1);

        if (image.equals("null")){
            holder.news_layout_image.setImageResource(R.drawable.ic_launcher_background);
        }
        else {
            Glide.with(context).load(image).placeholder(R.drawable.loading).into(holder.news_layout_image);

        }

        holder.news_layout_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getAdapterPosition();

                String time0 = newsList.get(n).getTime();
                String image0 = newsList.get(n).getImage();
                String urlNews0 = newsList.get(n).getUrlNews();
                String urlSource0 = newsList.get(n).getUrlSource();
                String title0 = newsList.get(n).getTitle();
                String description0 = newsList.get(n).getDescription();
                String content0 = newsList.get(n).getContent();
                String sourceName0 = newsList.get(n).getSourceName();

                arr= new String[]{title0, description0, content0, time0, image0, urlNews0, sourceName0, urlSource0};

                LiveNewsAdapter.this.onClick(v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
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
            case R.id.news_layout_cardview:
                Intent intent1=new Intent(context,NewsViewer.class);
                intent1.putExtra("details_of_news",arr);
                context.startActivity(intent1);
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView news_layout_image;
        CardView news_layout_cardview;
        TextView news_layout_title_txt,news_layout_source_txt,news_layout_time_past_txt;

        public MyViewHolder(View itemView) {
            super(itemView);

            news_layout_time_past_txt=itemView.findViewById(R.id.news_layout_time_past_txt);
            news_layout_image=itemView.findViewById(R.id.news_layout_image);
            news_layout_cardview=itemView.findViewById(R.id.news_layout_cardview);
            news_layout_title_txt=itemView.findViewById(R.id.news_layout_title_txt);
            news_layout_source_txt=itemView.findViewById(R.id.news_layout_source_txt);


        }
    }
}
