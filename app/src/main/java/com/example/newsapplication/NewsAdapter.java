package com.example.newsapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// Adapter for news api

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<Model.Data> data;


    public NewsAdapter(Context context, List<Model.Data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =  inflater.inflate(R.layout.activity_news_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.time.setText(data.get(position).publishedAt);
        holder.heading.setText(data.get(position).getTitle());
        holder.source.setText(data.get(position).getSource().getName());
        holder.description.setText(data.get(position).getDescription());
        Glide.with(context).load(data.get(position).getUrlToImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(data != null) {
            Log.e("In Adpater",data.toString());
            return data.size();
        }
        else{
            Log.e("Empty data","response is empty");
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView time, source ,heading , description;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            source = itemView.findViewById(R.id.source);
            heading = itemView.findViewById(R.id.heading);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.imageView);

        }

    }

}
