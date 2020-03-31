package com.fahmisbas.indonesiacovid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.fahmisbas.indonesiacovid19tracker.fragments.AboutFragment;
import com.fahmisbas.indonesiacovid19tracker.fragments.CasesFragment;
import com.fahmisbas.indonesiacovid19tracker.infosection.fragments.InfoFragment;
import com.fahmisbas.indonesiacovid19tracker.fragments.MapsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.buttom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        toFragment(new CasesFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_cases:
                            toFragment(new CasesFragment());
                            break;
                        case R.id.nav_map:
                            toFragment(new MapsFragment());
                            break;
                        case R.id.nav_info:
                            toFragment(new InfoFragment());
                            break;
                        case R.id.nav_about:
                            toFragment(new AboutFragment());
                            break;
                    }
                    return true;
                }
            };

    private void toFragment(Fragment selectedFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                .commit();
    }
}
