package com.fahmisbas.indonesiacovid19tracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fahmisbas.indonesiacovid19tracker.R;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbar_title_about);
        btnBack.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
        return view;
    }
}
