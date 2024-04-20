package com.burhanrashid52.photoediting.fragmens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.burhanrashid52.photoediting.Adapters.DownloadedPackagesAdapter;
import com.burhanrashid52.photoediting.activitys.DownloadedPreview;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.database.AppDataBase;
import com.burhanrashid52.photoediting.database.LocalModel;
import com.burhanrashid52.photoediting.database.TaskDao;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DownLoadsFragment extends Fragment {
    Context context;
    RecyclerView downloadedRecycler;
    List<LocalModel> downloaded=new ArrayList<>();
    TaskDao taskDao;
    DownloadedPackagesAdapter adapter2;
    View emptyLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.downloads_fragment,container,false);
        taskDao= AppDataBase.getAppDataBase(context).getTaskDao();
        downloadedRecycler=view.findViewById(R.id.downloadedRecycler);
        downloadedRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        downloaded=taskDao.getAllPackages();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        if(downloaded.size()==0){
            emptyLayout.setVisibility(View.VISIBLE);
            
        }
        adapter2 =new DownloadedPackagesAdapter(downloaded);
        downloadedRecycler.setAdapter(adapter2);
        adapter2.setOnClickItem(new DownloadedPackagesAdapter.OnClickItem() {
            @Override
            public void onClick(LocalModel localModel) {
                Intent intent=new Intent(context, DownloadedPreview.class);

                    intent.putExtra("model", (Serializable) localModel);
                    startActivity(intent);

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void delete(LocalModel localModel) {
                taskDao.deleteItem(localModel.getId());
                boolean deleted=deleteDirectory(new File(localModel.getPackageURI()));
                if(deleted){
                    Toast.makeText(context, context.getString(R.string.delete_package), Toast.LENGTH_SHORT).show();
                    try {
                        downloaded.remove(localModel);

                    }catch (IndexOutOfBoundsException e){

                    }
                    adapter2.notifyDataSetChanged();


                }else{
                    Toast.makeText(context, context.getString(R.string.public_error), Toast.LENGTH_SHORT).show();

                }




            }
        });

        return view;

    }
    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }


}
