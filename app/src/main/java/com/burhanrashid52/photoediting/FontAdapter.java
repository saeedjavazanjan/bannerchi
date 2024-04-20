package com.burhanrashid52.photoediting;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {

    List<String> farsiFonts=new ArrayList<>();
    OnFontSelect onFontSelect;
    String fontpath="fontper/";

    public void setFarsiFonts(String fontpath) throws IOException {
        this.farsiFonts = Arrays.asList(getAssetsFont(fontpath));;
    }

    public void setFontpath(String fontpath) {
        this.fontpath = fontpath;
    }

    public void setOnFontSelect(OnFontSelect onFontSelect) {
        this.onFontSelect = onFontSelect;
    }

    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FontAdapter(Context context) throws IOException {
        this.context = context;
        farsiFonts= Arrays.asList(getAssetsFont(fontpath));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.font_picker,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBinder(farsiFonts.get(position));

    }

    @Override
    public int getItemCount() {
        return farsiFonts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fontsample;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fontsample=itemView.findViewById(R.id.fontSample);

        }
        public void onBinder(String fontFamily){
            if (fontpath.equals("fontper/")){

                fontsample.setText(context.getString(R.string.label_fontF));
            }else if (fontpath.equals("fonteng/")) {
                fontsample.setText(context.getString(R.string.label_fontE));
            }


            fontsample.setTypeface(Typeface.createFromAsset(context.getAssets(),fontpath+fontFamily));


            fontsample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFontSelect.onFontChanged(fontFamily);

                }
            });

        }
    }
    public String[] getAssetsFont(String fontpath) throws IOException {
        String[] font =context.getAssets().list(fontpath);
        return font;

    }
    interface OnFontSelect{

        public void onFontChanged(String fontFamily);
    }
}
