package com.burhanrashid52.photoediting.fragmens;

import static com.burhanrashid52.photoediting.fragmens.SavedItemsFragment.packagesAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.burhanrashid52.photoediting.Adapters.HomeTabLayoutAdapter;
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
    ViewPager viewPager;
    RecyclerView mainRecycler;

    TextInputEditText search;
    ApiService apiService;
    List<ResponseModel> packages = new ArrayList<>();
    PackagesAdapter packagesAdapter;
    List<ResponseModel> searchedPackages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.view_pager);
        search = view.findViewById(R.id.edtSearch);
        mainRecycler = view.findViewById(R.id.mainRecycler);
        //   mainRecycler.setLayoutManager( new GridLayoutManager(getContext(), 2));
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(layoutManager);

        apiService = new ApiService(getContext());
        packagesAdapter = new PackagesAdapter();

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


        if (packages.isEmpty()){
            getRESPONSE("همه");
        }else {
            getRESPONSE(search.getText().toString());

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


        tabLayout.setupWithViewPager(viewPager);

        prepareViewPager(viewPager);

        return view;
    }
    private void prepareViewPager(ViewPager viewPager) {
        HomeTabLayoutAdapter adapter=new HomeTabLayoutAdapter();
        viewPager.setAdapter(adapter);

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
    public void getRESPONSE(String request) {

        apiService.getResponse(request, new Response.Listener<List<ResponseModel>>() {
            @Override
            public void onResponse(List<ResponseModel> response) {
                if (response.size()>0) {
                    packages.clear();
                    packages.addAll(response);
                    packagesAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "موردی پیدا نشد.", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Snackbar.make(linearLayout,"خطا در ارتباط...",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "خطا در ارتباط...", Toast.LENGTH_SHORT).show();
                // Log.e("volly", "onErrorResponse: "+error);
            }
        });


    }
}