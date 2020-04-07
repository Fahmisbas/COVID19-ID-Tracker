package com.fahmisbas.indonesiacovid19tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.fahmisbas.indonesiacovid19tracker.DownloadingTask;
import com.fahmisbas.indonesiacovid19tracker.adapter.ProvinceAdapter;
import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.model.Province;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CasesFragment extends Fragment {

    private TextView tvDeath, tvRecovered, tvPositive, tvTotalCases, viewAll, tvLastUpdate;
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

        DownloadIndonesiaJson();
        DownloadLastUpdateTimeJson();

        return view;
    }

    private void initViews() {
        tvLastUpdate = view.findViewById(R.id.tv_lastUpdate);
        tvDeath = view.findViewById(R.id.tv_death);
        tvPositive = view.findViewById(R.id.tv_positive);
        tvRecovered = view.findViewById(R.id.tv_recovered);
        tvTotalCases = view.findViewById(R.id.tv_totalCases);
        viewAll = view.findViewById(R.id.viewAll);
        Button back = view.findViewById(R.id.btn_back);
        progressBar = view.findViewById(R.id.progressBar_cases);
        progressBar.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
    }

    private void setTextIndonesiaData() {
        if (getActivity() != null) {
            SharedPreferences indonesiaData = getActivity().getSharedPreferences("ID-Data", Context.MODE_PRIVATE);
            String death = indonesiaData.getString("id-death", null);
            tvDeath.setText(death);
            String positive = indonesiaData.getString("id-positive", null);
            tvPositive.setText(positive);
            String recovered = indonesiaData.getString("id-recovered", null);
            tvRecovered.setText(recovered);
            String totalCases = indonesiaData.getString("id-total", null);
            tvTotalCases.setText(totalCases);

            SharedPreferences latestUpdate = getActivity().getSharedPreferences("timestamp", Context.MODE_PRIVATE);
            String latestupdate = latestUpdate.getString("time", "Check your internet connection");
            tvLastUpdate.setText(latestupdate);
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

    private void toFragment(Fragment selectedFragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
    }

    private void DownloadLastUpdateTimeJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DownloadingTask downloadingTask = new DownloadingTask("https://covid19.mathdro.id/api/countries/ID/");
                downloadingTask.start();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            formatDate(getLastUpdateTime(downloadingTask.getResult()));
                        }
                    });
                }
            }
        }).start();
    }

    private void formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMMM yyyy");
            if (date != null && getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("timestamp", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("time", "Last Update, " + fmtOut.format(date)).apply();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getLastUpdateTime(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("lastUpdate");
        } catch (JSONException e) {
            e.printStackTrace();
            return "No Internet Connection";
        }
    }

    private void DownloadIndonesiaJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DownloadingTask downloadingTask = new DownloadingTask("https://indonesia-covid-19.mathdro.id/api");
                downloadingTask.start();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processingIndonesiaJson(downloadingTask.getResult());
                            provinceJsonAddress(downloadingTask.getResult());
                        }
                    });
                }
            }
        }).start();
    }

    private void processingIndonesiaJson(String json) {
        try {
            if (!json.equals("")) {
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
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void provinceJsonAddress(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String perProvinsi = jsonObject.getString("perProvinsi");
            JSONObject objectProvince = new JSONObject(perProvinsi);
            String jsonAddress = objectProvince.getString("json");

            downloadingProvinceJson(jsonAddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadingProvinceJson(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final DownloadingTask downloadingTask = new DownloadingTask(address);
                downloadingTask.start();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processingProvinceJson(downloadingTask.getResult());
                        }
                    });
                }
            }
        }).start();
    }

    private void processingProvinceJson(String json) {
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

            saveProvinceArrayList();

            initRecyclerView();

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveProvinceArrayList() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Province-Data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String gsonToJson = gson.toJson(provincesArrayList);
            editor.putString("provinceList", gsonToJson).apply();
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