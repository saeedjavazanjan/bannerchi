package com.burhanrashid52.photoediting.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DownloadedSamplesAdapter extends RecyclerView.Adapter<DownloadedSamplesAdapter.SamplesViewHolder> {
    List<String> itemURIs=new ArrayList<>();
    OnEditClicked onEditClicked;

    public void setOnEditClicked(OnEditClicked onEditClicked) {
        this.onEditClicked = onEditClicked;
    }

    public DownloadedSamplesAdapter(List<String> itemURLs) {
        this.itemURIs = itemURLs;
    }

    @NonNull
    @Override
    public SamplesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.downloade_samples_item,parent,false);

        return new SamplesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SamplesViewHolder holder, int position) {
        holder.binder(itemURIs.get(position));

    }

    @Override
    public int getItemCount() {
        return itemURIs.size();
    }

    public class SamplesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MaterialButton edit;
        public SamplesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgSample);
            edit=itemView.findViewById(R.id.btnEdit);

        }

        public void binder(String uri){
            Uri URI=Uri.fromFile(new File(uri));

            Glide.with(itemView.getContext())
                    .load(URI)
                    //.thumbnail(0.1f)
                    .into(imageView);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked.edit(uri);

                }
            });

        }
    }
   public interface OnEditClicked{
        public void edit(String uri);

    }

}
