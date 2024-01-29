package com.burhanrashid52.photoediting.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.database.SavedModel;

import java.util.ArrayList;
import java.util.List;

public class SavedPackagesAdapter extends RecyclerView.Adapter<SavedPackagesAdapter.PackageViewHolder> {
    List<SavedModel> savedModels=new ArrayList<>();
    OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public SavedPackagesAdapter(List<SavedModel> savedModels) {
        this.savedModels = savedModels;
    }

    @Override
    public int getItemViewType(int position) {

        return Integer.parseInt(savedModels.get(position).getType());

    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==1){
            View viewStory = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_items_storys,null);

            return new SavedPackagesAdapter.PackageViewHolder(viewStory);


        }else{
            View viewPosts = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_items_posts,null);

            return new SavedPackagesAdapter.PackageViewHolder(viewPosts);

        }




    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.packageBinder(savedModels.get(position));

    }

    @Override
    public int getItemCount() {
        return savedModels.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
       // ImageView delete;
        TextView price;
        TextView title;
        ImageView free;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgItemShow);
         //   delete=itemView.findViewById(R.id.imgDelete);
           // price=itemView.findViewById(R.id.txtPrice);

            title=itemView.findViewById(R.id.txtTitle);
            free=itemView.findViewById(R.id.imgFree);




        }
        public void packageBinder(SavedModel savedModel){
            try {
              //  Uri headerUri=Uri.fromFile(new File(savedModel.getHeaderURI()));


                Glide.with(itemView.getContext())
                        .load(savedModel.getHeaderURL())
                        //.thumbnail(0.1f)
                        .into(imageView);

                if(savedModel.getPrice()!=0){
                    free.setVisibility(View.VISIBLE);

                }else if(savedModel.getPrice()==0){
                    free.setVisibility(View.GONE);
                }
            }catch (NullPointerException e){


            }


            title.setText(savedModel.getName());

         /*   delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.delete(savedModel);

                }
            });*/



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.onClick(savedModel );

                }
            });


        }
    }

    public interface OnClickItem {
        void onClick(SavedModel savedModel);
        void delete(SavedModel savedModel);
    }
}
