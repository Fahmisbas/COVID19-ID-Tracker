package com.fahmisbas.indonesiacovid19tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmisbas.indonesiacovid19tracker.adapter.ProvinceAdapter;
import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.model.Province;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CasesFragment extends Fragment {

    private TextView tvDeath, tvRecovered, tvPositive, tvTotalCases, viewAll;
    private ProgressBar progressBar;
    private ArrayList<Province> provincesArrayList = new ArrayList<>();
    private ProvinceAdapter adapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cases, container, false);
        initViews();

        setTextIndonesiaData();

        viewAllProvice();

        setToolbar();

        new IndonesiaJSONData("https://indonesia-covid-19.mathdro.id/api").start();
        return view;
    }

    private void initViews() {
        tvDeath = view.findViewById(R.id.tv_death);
        tvPositive = view.findViewById(R.id.tv_positive);
        tvRecovered = view.findViewById(R.id.tv_recovered);
        tvTotalCases = view.findViewById(R.id.tv_totalCases);
        viewAll = view.findViewById(R.id.viewAll);
        Button back = view.findViewById(R.id.btn_back);
        back.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBar_cases);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void toFragment(Fragment selectedFragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
    }

    private void setToolbar() {
        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFragment(new CasesFragment());
            }
        });
    }

    private void viewAllProvice() {
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFragment(new ViewAllFragment());
            }
        });
    }

    public class IndonesiaJSONData extends Thread {

        String address;
        String result = "";

        IndonesiaJSONData(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            super.run();
            getJson();
        }

        private void getJson() {
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int reading = reader.read();

                while (reading != -1) {
                    char current = (char) reading;
                    result += current;
                    reading = reader.read();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!result.equals("")) {
                                processingIDJson(result);
                                getProvinceJSON(result);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void processingIDJson(String json) {
        try {
            if (json != null) {
                progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = new JSONObject(json);
                String death = jsonObject.getString("meninggal");
                String positive = jsonObject.getString("perawatan");
                String recovered = jsonObject.getString("sembuh");
                String totalCases = jsonObject.getString("jumlahKasus");

                if (getActivity() != null) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ID-Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id-death", death).apply();
                    editor.putString("id-positive", positive).apply();
                    editor.putString("id-recovered", recovered).apply();
                    editor.putString("id-total", totalCases).apply();
                }
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTextIndonesiaData() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ID-Data", Context.MODE_PRIVATE);
            String death = sharedPreferences.getString("id-death", null);
            tvDeath.setText(death);
            String positive = sharedPreferences.getString("id-positive", null);
            tvPositive.setText(positive);
            String recovered = sharedPreferences.getString("id-recovered", null);
            tvRecovered.setText(recovered);
            String totalCases = sharedPreferences.getString("id-total", null);
            tvTotalCases.setText(totalCases);
        }
    }

    private void getProvinceJSON(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String perProvinsi = jsonObject.getString("perProvinsi");
            JSONObject objectProvince = new JSONObject(perProvinsi);
            String jsonAddress = objectProvince.getString("json");

            new ProvinceJSONData(jsonAddress).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ProvinceJSONData extends Thread {

        String address;
        String result = "";

        public ProvinceJSONData(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            super.run();
            getJson();
        }

        private void getJson() {
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int reading = reader.read();

                while (reading != -1) {
                    char current = (char) reading;

                    result += current;

                    reading = reader.read();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processingProvinceJSON(result);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processingProvinceJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String data = jsonObject.getString("data");

            JSONArray array = new JSONArray(data);

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonPart = array.getJSONObject(i);
                Province province = new Province();

                String provinceName = jsonPart.getString("provinsi");
                String fid = jsonPart.getString("fid");
                String positive = jsonPart.getString("kasusPosi");
                String recovered = jsonPart.getString("kasusSemb");
                String death = jsonPart.getString("kasusMeni");

                province.setProvinceName(provinceName);
                province.setFid(fid);
                province.setDeath(death);
                province.setPositive(positive);
                province.setRecovered(recovered);

                provincesArrayList.add(province);
            }

            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Province-Data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson = new Gson();
                String gsonToJson = gson.toJson(provincesArrayList);
                editor.putString("provinceList", gsonToJson).apply();

                initRecyclerView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerView() {
        RecyclerView rvProvince = view.findViewById(R.id.rv_provinceCases);
        adapter = new ProvinceAdapter(provincesArrayList);
        rvProvince.setAdapter(adapter);
        searchProvinceName();
    }

    private void searchProvinceName() {
        SearchView search = view.findViewById(R.id.search_province);
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
