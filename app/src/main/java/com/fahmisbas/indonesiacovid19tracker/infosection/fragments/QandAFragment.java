package com.fahmisbas.indonesiacovid19tracker.infosection.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.adapter.AdviceAdapter;
import com.fahmisbas.indonesiacovid19tracker.model.Advice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QandAFragment extends Fragment {

    private View view;
    private ArrayList<Advice> wearMaskList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qa,container,false);

        processingJSON();

        onBackPressed();

        setToolbarTitle();
        return view;
    }

    private void setToolbarTitle() {
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbar_title_QA);

        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setVisibility(View.GONE);
    }

    private void onBackPressed() {
        Button back = view.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new InfoFragment()).commit();
                }
            }
        });
    }

    private void processingJSON() {
        try {
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("qa", Context.MODE_PRIVATE);
                String question = sharedPreferences.getString("QandA", null);

                JSONArray questionArray = new JSONArray(question);

                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject jsonPart = questionArray.getJSONObject(i);
                    Advice advice = new Advice();

                    String title = jsonPart.getString("title");
                    String subtitle = jsonPart.getString("subtitle");

                    advice.setTitle(title);
                    advice.setSubtitle(subtitle);

                    wearMaskList.add(advice);
                }
            }
            initRecyclerview();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initRecyclerview() {
        RecyclerView rvQA = view.findViewById(R.id.rv_qa);
        AdviceAdapter adapter = new AdviceAdapter(wearMaskList);
        rvQA.setAdapter(adapter);
    }
}
