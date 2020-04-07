package com.fahmisbas.indonesiacovid19tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.adapter.ProvinceAdapter;
import com.fahmisbas.indonesiacovid19tracker.model.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewAllFragment extends Fragment {

    private ArrayList<Province> provincesArrayList = new ArrayList<>();
    private View view;
    private ProvinceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewall,container,false);

        initViews();
        provinceData();
        initRecyclerView();

        return view;
    }

    private void initViews() {
        Button back = view.findViewById(R.id.btn_back);
        Button refresh = view.findViewById(R.id.btn_refresh);
        back.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        RecyclerView rvViewAll = view.findViewById(R.id.rv_viewAll);
        adapter = new ProvinceAdapter(provincesArrayList);
        rvViewAll.setAdapter(adapter);
    }

    private void provinceData() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Province-Data", Context.MODE_PRIVATE);
            String json = sharedPreferences.getString("provinceList", null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Province>>() {
            }.getType();
            provincesArrayList = gson.fromJson(json, type);

            searchProvinceName();
        }
    }

    private void searchProvinceName(){
        SearchView search = view.findViewById(R.id.search_province_viewAll);
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
