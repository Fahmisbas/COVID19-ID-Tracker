package com.fahmisbas.indonesiacovid19tracker.infosection.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmisbas.indonesiacovid19tracker.adapter.AdviceAdapter;
import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.model.Advice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private ArrayList<Advice> adviceArrayList = new ArrayList<>();

    private TextView tvBasicProTitle, tvBasicProSub;
    private ProgressBar progressBar;
    private View view;
    private CardView cardViewMeas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        initViews();

        toMythBusterFragment(new MythBusterFragment());

        toWearMaskFragment(new WearMaskFragment());

        toQAFragment(new QandAFragment());

        new GetInfoJson().start();


        return view;
    }


    private void initViews() {
        progressBar = view.findViewById(R.id.progressBar);
        tvBasicProTitle = view.findViewById(R.id.tv_basicProTitle);
        tvBasicProSub = view.findViewById(R.id.tv_basicProSubtitle);
        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Button back = view.findViewById(R.id.btn_back);
        back.setVisibility(View.GONE);
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbar_title_information);
        cardViewMeas = view.findViewById(R.id.cardview_basicmea);
        cardViewMeas.setVisibility(View.GONE);
    }

    private void toWearMaskFragment(final Fragment fragment) {
        CardView mask = view.findViewById(R.id.cardview_howtomask);
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            }
        });
    }

    private void toMythBusterFragment(final Fragment fragment) {
        CardView mythBusta = view.findViewById(R.id.cardview_mythBusta);
        mythBusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            }
        });
    }

    private void toQAFragment(final Fragment fragment) {
        CardView qa = view.findViewById(R.id.cardview_qa);
        qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            }
        });
    }


    public class GetInfoJson extends Thread {
        String result = "";

        @Override
        public void run() {
            super.run();
            whoJson();
        }

        private void whoJson() {
            InputStream in = getResources().openRawResource(R.raw.who_corona_advice);
            InputStreamReader reader = new InputStreamReader(in);
            try {
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
                            processingJson(result);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processingJson(String json) {
        try {
            if (!json.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                cardViewMeas.setVisibility(View.VISIBLE);

                mythBuster(json);
                wearMask(json);
                QandA(json);

                JSONObject jsonObject = new JSONObject(json);

                String basics = jsonObject.getString("basics");
                String basicProTitle = jsonObject.getString("title");
                String basicProSub = jsonObject.getString("subtitle");

                tvBasicProTitle.setText(basicProTitle);
                tvBasicProSub.setText(basicProSub);

                JSONArray array = new JSONArray(basics);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);
                    Advice advice = new Advice();

                    String title = jsonPart.getString("title");
                    String subtitle = jsonPart.getString("subtitle");

                    advice.setTitle(title);
                    advice.setSubtitle(subtitle);

                    adviceArrayList.add(advice);
                }
                initRecyclerview();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                cardViewMeas.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void QandA(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String topics = jsonObject.getString("topics");
            JSONArray array = new JSONArray(topics);
            JSONObject first = array.getJSONObject(2);
            String question = first.getString("questions");

            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("qa", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("QandA", question).apply();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void wearMask(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String topics = jsonObject.getString("topics");
            JSONArray array = new JSONArray(topics);
            JSONObject first = array.getJSONObject(1);
            String question = first.getString("questions");

            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mask", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("wearMask", question).apply();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mythBuster(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String topics = jsonObject.getString("topics");
            JSONArray array = new JSONArray(topics);
            JSONObject first = array.getJSONObject(0);
            String question = first.getString("questions");

            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myth", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("mythBuster", question).apply();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerview() {
        RecyclerView rvAdvice = view.findViewById(R.id.rv_advice);
        AdviceAdapter adapter = new AdviceAdapter(adviceArrayList);
        rvAdvice.setAdapter(adapter);
    }
}
