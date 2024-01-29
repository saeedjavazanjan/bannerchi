package com.burhanrashid52.photoediting.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.models.TitlesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SortTitlesAdapter extends RecyclerView.Adapter<SortTitlesAdapter.ViewHolder> {
    List<TitlesModel> titles=new ArrayList<>();
   private OnclickTitle onclickTitle;
   Context context;
   private int pos;

    public SortTitlesAdapter(List<TitlesModel> titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    public void setOnclickTitle(OnclickTitle onclickTitle) {
        this.onclickTitle = onclickTitle;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.title_items,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBinder(titles.get(position),position);
        if(pos==position){
            holder.textView.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_background));
            holder.textView.setTextColor(Color.WHITE);
        }else {
            holder.textView.setBackground(ContextCompat.getDrawable(context, R.drawable.gray_background));
            holder. textView.setTextColor(ContextCompat.getColor(context, R.color.tool_bg));

        }

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.txtSortTitles);

        }
        public void onBinder(TitlesModel title,Integer position){
            textView.setText(title.getOccasion());


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onclickTitle.onItemClick(title);
                    pos=position;
                    notifyDataSetChanged();
                }
            });
        }
    }
   public interface OnclickTitle{
        public void onItemClick(TitlesModel title);
    }
}
