package com.burhanrashid52.photoediting.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.models.ResponseModel;

import java.util.ArrayList;
import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.PackageViewHolder> {
    List<ResponseModel> responseModels=new ArrayList<>();

    OnClickItem onClickItem;


    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public void setResponseModels(List<ResponseModel> responseModels) {

        this.responseModels = responseModels;
    }

    @Override
    public int getItemViewType(int position) {

        Log.d("MODELVIEW",responseModels.get(position).getType());
        return Integer.parseInt(responseModels.get(position).getType());

    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==1){
            View viewStory = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_items_storys,null);

            return new PackageViewHolder(viewStory);


        }else{
            View viewPosts = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_items_posts,null);

            return new PackageViewHolder(viewPosts);

        }



    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.packageBinder(responseModels.get(position));

    }


    @Override
    public int getItemCount() {
        return responseModels.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView downLoadCount;
      //  TextView price;
        TextView title;
        ImageView free;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgItemShow);
           // downLoadCount=itemView.findViewById(R.id.txtCountDown);
          //  price=itemView.findViewById(R.id.txtPrice);
            title=itemView.findViewById(R.id.txtTitle);
            free=itemView.findViewById(R.id.imgFree);


        }
        public void packageBinder(ResponseModel responseModel){
            Glide.with(itemView.getContext())
                    .load(responseModel.getHeaderURL())
                    //.thumbnail(0.1f)
                    .into(imageView);
           // downLoadCount.setText(responseModel.getDownloadCount());
            title.setText(responseModel.getName());
            if(responseModel.getPrice()!=0){
                free.setVisibility(View.VISIBLE);

            }else if(responseModel.getPrice()==0){
                free.setVisibility(View.GONE);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.onClick(responseModel);

                }
            });


        }
    }

    public interface OnClickItem {
        void onClick(ResponseModel responseModel);
    }
}
