package com.burhanrashid52.photoediting.fragmens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.burhanrashid52.photoediting.Adapters.PackagesAdapter;
import com.burhanrashid52.photoediting.Adapters.SortTitlesAdapter;
import com.burhanrashid52.photoediting.activitys.PreviewActivity;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.api.ApiService;
import com.burhanrashid52.photoediting.models.ResponseModel;
import com.burhanrashid52.photoediting.models.TitlesModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OccasionFragment extends Fragment {
    RecyclerView sortTitles;
    RecyclerView mainRecycler;
    SortTitlesAdapter sortTitlesAdapter;
    List<TitlesModel> titles = new ArrayList<>();
    List<ResponseModel> packages = new ArrayList<>();
    List<ResponseModel> searchedPackages = new ArrayList<>();
    ApiService apiService;
    public String selectedTab = "همه";
    //LinearLayout linearLayout;
    PackagesAdapter packagesAdapter;
    LinearLayout progress;
  //  TextInputEditText search;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasion, container, false);
        //   linearLayout=view.findViewById(R.id.homeLayout);
        mainRecycler = view.findViewById(R.id.mainRecycler);
        //   mainRecycler.setLayoutManager( new GridLayoutManager(getContext(), 2));
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(layoutManager);
        apiService = new ApiService(getContext());
        packagesAdapter = new PackagesAdapter();
        titlesManager(view);

        packagesAdapter.setResponseModels(packages);
        packagesAdapter.setOnClickItem(new PackagesAdapter.OnClickItem() {
            @Override
            public void onClick(ResponseModel responseModel) {
                Intent intent = new Intent(getContext(), PreviewActivity.class);
                intent.putExtra("name", responseModel.getName());
                intent.putExtra("packageUrl", responseModel.getPackageURL());
                intent.putExtra("designer", responseModel.getDesigner());
                intent.putExtra("id", responseModel.getId());
                intent.putExtra("occasion", responseModel.getOcassion());
                intent.putExtra("downloadCount", responseModel.getDownloadCount());
                intent.putExtra("headerUrl", responseModel.getHeaderURL());
                intent.putExtra("price", responseModel.getPrice());
                intent.putExtra("samples", responseModel.getSamples());
                intent.putExtra("type", responseModel.getType());
                //intent.putExtra("model", (Serializable) responseModel);
                startActivity(intent);


            }
        });

        progress = view.findViewById(R.id.linProgress);
       // search = view.findViewById(R.id.edtSearch);

      /*  search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sortTitles.setVisibility(View.VISIBLE);

            }
        });*/
/*
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                 getSearchedResponse(s.toString());

                } else {
                    packagesAdapter.setResponseModels(packages);
                    packagesAdapter.notifyDataSetChanged();
                    searchedPackages.clear();

                    mainRecycler.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        progress.setVisibility(View.VISIBLE);

        if (packages.isEmpty()){
            getRESPONSE();


        }
        mainRecycler.setAdapter(packagesAdapter);



        return view;
    }


    public void titlesManager(View view) {

                apiService.getOccasion(new Response.Listener<List<TitlesModel>>() {
                    @Override
                    public void onResponse(List<TitlesModel> response) {

                        titles.addAll(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "مشکلی رخ داده است", Toast.LENGTH_SHORT).show();

    /*titles.add("همه");
                titles.add("عید نوروز");
                titles.add("یلدا");
                titles.add("روز مادر");
                titles.add("روز پدر");
                titles.add("مناسبات مذهبی");
                titles.add("مناسبات ملی");
                titles.add("تبریک و تسلیت");
                titles.add("ولنتاین");
                titles.add("چهارشنبه سوری");
                titles.add("Black Friday");*/

                    }
                });



        sortTitlesAdapter = new SortTitlesAdapter(titles, getContext());
        sortTitles = view.findViewById(R.id.sortTitles);
       // sortTitles.setVisibility(View.GONE);

        sortTitles.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        sortTitles.setAdapter(sortTitlesAdapter);
        sortTitlesAdapter.setOnclickTitle(new SortTitlesAdapter.OnclickTitle() {
            @Override
            public void onItemClick(TitlesModel title) {
                selectedTab = title.getOccasion();

                    getRESPONSE();


                           }
        });




    }


    public void getRESPONSE() {

        apiService.getResponse(selectedTab, new Response.Listener<List<ResponseModel>>() {
            @Override
            public void onResponse(List<ResponseModel> response) {
                if (response.size()>0) {
                    packages.clear();
                    packages.addAll(response);
                    packagesAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "موردی پیدا نشد.", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Snackbar.make(linearLayout,"خطا در ارتباط...",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "خطا در ارتباط...", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                // Log.e("volly", "onErrorResponse: "+error);
            }
        });


    }

    public void getSearchedResponse(String search) {
        apiService.getSearchedResponse(search, new Response.Listener<List<ResponseModel>>() {
            @Override
            public void onResponse(List<ResponseModel> response) {
                    packagesAdapter.setResponseModels(response);
                    packagesAdapter.notifyDataSetChanged();

                mainRecycler.setVisibility(View.VISIBLE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainRecycler.setVisibility(View.GONE);
            }
        });

    }

}
