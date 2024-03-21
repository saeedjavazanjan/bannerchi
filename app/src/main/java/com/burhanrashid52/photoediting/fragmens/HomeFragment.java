package com.burhanrashid52.photoediting.fragmens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.burhanrashid52.photoediting.Adapters.PackagesAdapter;
import com.burhanrashid52.photoediting.Adapters.SortTitlesAdapter;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.activitys.PreviewActivity;
import com.burhanrashid52.photoediting.api.ApiService;
import com.burhanrashid52.photoediting.models.ResponseModel;
import com.burhanrashid52.photoediting.models.TitlesModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    RecyclerView sortTitles;
    RecyclerView mainRecycler;
    SortTitlesAdapter sortTitlesAdapter;
    List<TitlesModel> titles = new ArrayList<>();
    TextInputEditText search;
    ApiService apiService;
    List<ResponseModel> packages = new ArrayList<>();
    PackagesAdapter packagesAdapter;
    List<ResponseModel> searchedPackages = new ArrayList<>();
    public String selectedTab = "همه";
    LinearLayout progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        search = view.findViewById(R.id.edtSearch);
        tabLayout = view.findViewById(R.id.tab_layout);

        mainRecycler = view.findViewById(R.id.mainRecycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(layoutManager);

        apiService = new ApiService(getContext());
        packagesAdapter = new PackagesAdapter();

        titlesManager(view,"occasion");

        progress = view.findViewById(R.id.linProgress);
        progress.setVisibility(View.VISIBLE);

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


        if (packages.isEmpty()) {
            getPostersRESPONSE("همه");
        } else {
            getPostersRESPONSE(search.getText().toString());

        }

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


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mainRecycler.setAdapter(packagesAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("مناسبتی"));
        tabLayout.addTab(tabLayout.newTab().setText("قالب پیج"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    getPostersRESPONSE("همه");
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    progress.setVisibility(View.VISIBLE);
                    getTemplatesRESPONSE("یلدا");
                    titlesManager(view,"category");


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    progress.setVisibility(View.VISIBLE);
                    getPostersRESPONSE("همه");
                    titlesManager(view,"occasion");

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    progress.setVisibility(View.VISIBLE);
                    getTemplatesRESPONSE("یلدا");
                    titlesManager(view,"category");


                }


            }
        });

        return view;
    }

    public void titlesManager(View view,String type) {
        sortTitlesAdapter = new SortTitlesAdapter(titles, getContext());
        sortTitles = view.findViewById(R.id.sortTitles);
        // sortTitles.setVisibility(View.GONE);

        sortTitles.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        sortTitles.setAdapter(sortTitlesAdapter);

        if (type.equals("occasion")){
            apiService.getOccasion(new Response.Listener<List<TitlesModel>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(List<TitlesModel> response) {

                    titles.addAll(response);
                    sortTitlesAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "مشکلی رخ داده است", Toast.LENGTH_SHORT).show();


                }
            });
        }
        if(type.equals("category")){
            apiService.getTemplatesCategory(new Response.Listener<List<TitlesModel>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(List<TitlesModel> response) {

                    titles.addAll(response);
                    sortTitlesAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "مشکلی رخ داده است", Toast.LENGTH_SHORT).show();


                }
            });
        }



        sortTitlesAdapter.setOnclickTitle(new SortTitlesAdapter.OnclickTitle() {
            @Override
            public void onItemClick(TitlesModel title) {
                selectedTab = title.getOccasion();

                getPostersRESPONSE(selectedTab);


            }
        });


    }

    public void getSearchedResponse(String search) {
        apiService.getSearchedResponse(search, new Response.Listener<List<ResponseModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(List<ResponseModel> response) {
                packagesAdapter.setResponseModels(response);
                packagesAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    public void getPostersRESPONSE(String request) {

        apiService.getPostersResponse(request, new Response.Listener<List<ResponseModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(List<ResponseModel> response) {
                if (response.size() > 0) {
                    packages.clear();
                    packages.addAll(response);
                    packagesAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "موردی پیدا نشد.", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Snackbar.make(linearLayout,"خطا در ارتباط...",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "خطا در ارتباط...", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });


    }
    public void getTemplatesRESPONSE(String request) {

        apiService.getTemplatesResponse(request, new Response.Listener<List<ResponseModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(List<ResponseModel> response) {
                if (response.size() > 0) {
                    packages.clear();
                    packages.addAll(response);
                    packagesAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "موردی پیدا نشد.", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Snackbar.make(linearLayout,"خطا در ارتباط...",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "خطا در ارتباط...", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });


    }
}