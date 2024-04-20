package com.burhanrashid52.photoediting.Adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.ImageSaver;
import com.burhanrashid52.photoediting.R;
import com.nambimobile.widgets.efab.FabOption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadedSamplesAdapter extends RecyclerView.Adapter<DownloadedSamplesAdapter.SamplesViewHolder> {
    List<String> itemURIs=new ArrayList<>();
    OnEditClicked onEditClicked;
    OnShareClicked onShareClicked;
    public void setOnEditClicked(OnEditClicked onEditClicked) {
        this.onEditClicked = onEditClicked;
    }
    public void setOnShareClicked(OnShareClicked onShareClicked) {
        this.onShareClicked = onShareClicked;
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
    public void onBindViewHolder(@NonNull SamplesViewHolder holder,  int position) {
        holder.binder(itemURIs.get(position));
    }







    @Override
    public int getItemCount() {
        return itemURIs.size();
    }

    public class SamplesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        FabOption edit;
        FabOption save;
        FabOption share;
        public SamplesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgSample);
            edit=itemView.findViewById(R.id.edit);
            save=itemView.findViewById(R.id.save);
            share=itemView.findViewById(R.id.share);

        }

        public void binder(String uri){
            Uri URI=Uri.fromFile(new File(uri));

            Glide.with(itemView.getContext())
                    .load(URI)
                    .placeholder(R.drawable.place_holder)
                    //.thumbnail(0.1f)
                    .into(imageView);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                       ImageSaver imageSaver= ImageSaver.getInstance();
                        imageSaver.saveToGallery(imageView,itemView.getContext());
                        Toast.makeText(itemView.getContext(), imageView.getContext().getString(R.string.saved_in_gallery),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.buildDrawingCache();
                    Bitmap bmp = imageView.getDrawingCache();
                    onShareClicked.share(bmp);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked.edit(uri);
                   /* try {
                        saveToGallery(imageView,itemView.getContext());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }*/

                }
            });

        }
    }
   public interface OnEditClicked{
        public void edit(String uri);

    }

    public interface OnShareClicked{
        public void share(Bitmap bmp);

    }




}
