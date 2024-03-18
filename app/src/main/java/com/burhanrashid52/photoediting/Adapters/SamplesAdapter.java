package com.burhanrashid52.photoediting.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.R;

import java.util.ArrayList;
import java.util.List;

public class SamplesAdapter extends RecyclerView.Adapter<SamplesAdapter.SamplesViewHolder> {
    List<String> itemURLs=new ArrayList<>();

    public SamplesAdapter(List<String> itemURLs) {
        this.itemURLs = itemURLs;
    }

    @NonNull
    @Override
    public SamplesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.samples_item,parent,false);

        return new SamplesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SamplesViewHolder holder, int position) {
        holder.binder(itemURLs.get(position));

    }

    @Override
    public int getItemCount() {
        return itemURLs.size();
    }

    public class SamplesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public SamplesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgSample);
        }

        public void binder(String url){
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.place_holder)
                    .into(imageView);


        }
    }
}
