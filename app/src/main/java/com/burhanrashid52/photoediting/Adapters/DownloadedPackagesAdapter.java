package com.burhanrashid52.photoediting.Adapters;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.database.LocalModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadedPackagesAdapter extends RecyclerView.Adapter<DownloadedPackagesAdapter.PackageViewHolder> {
    List<LocalModel> localModels=new ArrayList<>();
    OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public DownloadedPackagesAdapter(List<LocalModel> localModels) {
        this.localModels = localModels;
    }

    @Override
    public int getItemViewType(int position) {

        return Integer.parseInt(localModels.get(position).getType());

    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==1){
            View viewStory = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_items_storys,null);

            return new PackageViewHolder(viewStory);


        }else{
            View viewPosts = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_items_posts,null);

            return new PackageViewHolder(viewPosts);

        }



    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.packageBinder(localModels.get(position));

    }

    @Override
    public int getItemCount() {
        return localModels.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView delete;
        TextView downLoadCount;
        TextView price;
        TextView title;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgItemShow);
            delete=itemView.findViewById(R.id.imgDelete);

            title=itemView.findViewById(R.id.txtTitle);


        }
        public void packageBinder(LocalModel localModels){
            try {
                Uri headerUri=Uri.fromFile(new File(localModels.getHeaderURI()));


                Glide.with(itemView.getContext())
                        .load(headerUri)
                        //.thumbnail(0.1f)
                        .into(imageView);
            }catch (NullPointerException e){


            }


            title.setText(localModels.getName());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(v.getContext())
                            .setMessage("در صورت حذف لازم است پکیج را مجددا دانلود یا خرید کنید.آیا مطمئنید؟")
                            .setPositiveButton("حذف ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onClickItem.delete(localModels);

                                }
                            })
                            .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            })
                            .create()
                            .show();

                }
            });



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.onClick(localModels );

                }
            });


        }
    }

    public interface OnClickItem {
        void onClick(LocalModel localModel);
        void delete(LocalModel localModel);
    }
}
