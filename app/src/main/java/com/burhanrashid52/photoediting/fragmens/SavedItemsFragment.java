package com.burhanrashid52.photoediting.fragmens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.burhanrashid52.photoediting.Adapters.SavedPackagesAdapter;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.activitys.PreviewActivity;
import com.burhanrashid52.photoediting.api.ApiService;
import com.burhanrashid52.photoediting.database.AppDataBase;
import com.burhanrashid52.photoediting.database.SavedModel;
import com.burhanrashid52.photoediting.database.TaskDao;

import java.util.ArrayList;
import java.util.List;

public class SavedItemsFragment extends Fragment {
    RecyclerView mainRecycler;
    List<SavedModel> packages=new ArrayList<>();
    ApiService apiService;
    LinearLayout linearLayout;
    public static SavedPackagesAdapter packagesAdapter;
    LinearLayout progress;
    TaskDao taskDao;
    View emptyLayout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bookmarks_fragment,container,false);
        taskDao=AppDataBase.getAppDataBase(getContext()).getTaskDao();
        packages=taskDao.getSavedPackages();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        if(packages.size()==0){
            emptyLayout.setVisibility(View.VISIBLE);

        }


        linearLayout=view.findViewById(R.id.homeLayout);
        mainRecycler=view.findViewById(R.id.mainRecycler);
      //  mainRecycler.setLayoutManager( new GridLayoutManager(getContext(), 2));
        mainRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        packagesAdapter=new SavedPackagesAdapter(packages);
        mainRecycler.setAdapter(packagesAdapter);

        packagesAdapter.setOnClickItem(new SavedPackagesAdapter.OnClickItem() {
            @Override
            public void onClick(SavedModel savedModel) {
                Intent intent=new Intent(getContext(), PreviewActivity.class);
                intent.putExtra("name",savedModel.getName());
                intent.putExtra("packageUrl",savedModel.getPackageURL());
                intent.putExtra("designer",savedModel.getDesigner());
                intent.putExtra("id",savedModel.getId());
                intent.putExtra("occasion",savedModel.getOcassion());
                intent.putExtra("downloadCount",savedModel.getDowmloadCount());
                intent.putExtra("headerUrl",savedModel.getHeaderURL());
                intent.putExtra("price",savedModel.getPrice());
                intent.putExtra("samples",savedModel.getSamples());
                intent.putExtra("type",savedModel.getType());
              //  intent.putExtra("model", (Serializable) savedModel);
                startActivity(intent);
            }

            @Override
            public void delete(SavedModel savedModel) {

            }
        });





        return view;
    }


}
