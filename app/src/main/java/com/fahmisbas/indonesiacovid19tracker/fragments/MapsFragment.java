package com.fahmisbas.indonesiacovid19tracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.fahmisbas.indonesiacovid19tracker.HostActivity;
import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.model.Province;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View view;
    private ArrayList<Province> provincesDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps,container,false);

        setToolbar();

        return view;
    }

    private void setToolbar() {
        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setVisibility(View.GONE);

        Button back = view.findViewById(R.id.btn_back);
        back.setVisibility(View.GONE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (getContext() != null) {
            MapsInitializer.initialize(getContext());
            mMap = googleMap;
            mMap.getUiSettings().setMapToolbarEnabled(false);
            LatLng latLng = new LatLng(-0.7387066, 100.7950193);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 3.0f));
            getProvinceDataList();

            for (int i = 0; i < provincesDataList.size(); i++) {
                Province province = provincesDataList.get(i);
                mMap.getUiSettings().setMapToolbarEnabled(false);

                String provinceName = province.getProvinceName().toLowerCase();
                String recovered = province.getRecovered();
                String positive = province.getPositive();
                String death = province.getDeath();

                if (!positive.equals("0") || !death.equals("0")) {  // i know this is stupid but it works
                    if (provinceName.equals("dki jakarta")) {
                        makeMarker(-6.187082, 106.844473, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("banten")) {
                        makeMarker(-6.4695801, 105.9880533, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("jawa barat")) {
                        makeMarker(-6.8960665, 107.4489156, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("jawa timur")) {
                        makeMarker(-7.6850479, 111.7593912, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("jawa tengah")) {
                        makeMarker(-7.1505104, 109.5868991, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("daerah istimewa yogyakarta")) {
                        makeMarker(-7.8383562, 110.2481931, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sulawesi selatan")) {
                        makeMarker(-3.6662356, 119.9702343, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kalimantan timur")) {
                        makeMarker(0.5368844, 116.4157303, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("bali")) {
                        makeMarker(-8.3373257, 115.0892313, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sumatera utara")) {
                        makeMarker(2.1602914, 99.5103623, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kepulauan riau")) {
                        makeMarker(2.8513745, 105.4588846, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kalimantan barat")) {
                        makeMarker(-0.2815676, 111.4556913, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kalimantan tengah")) {
                        makeMarker(-1.6842256, 113.3764323, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sulawesi tenggara")) {
                        makeMarker(-4.1455716, 122.1723673, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("papua")) {
                        makeMarker(-4.2654716, 138.0733643, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("riau")) {
                        makeMarker(0.3075984, 101.6702753, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("nusa tenggara barat")) {
                        makeMarker(-8.6540817, 117.3572623, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kalimantan selatan")) {
                        makeMarker(-3.1099087, 115.2598629, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sulawesi utara")) {
                        makeMarker(0.6290644, 123.9675643, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("jambi")) {
                        makeMarker(-1.6106946, 103.6107633, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sumatera selatan")) {
                        makeMarker(-3.4149891, 103.7336015, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("lampung")) {
                        makeMarker(-4.5608446, 105.4008513, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("maluku")) {
                        makeMarker(-3.2403666, 130.1651423, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("maluku utara")) {
                        makeMarker(1.5653304, 127.8177033, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("aceh")) {
                        makeMarker(4.6898283, 96.7450723, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sumatera barat")) {
                        makeMarker(-0.7387066, 100.7950193, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("bengkulu")) {
                        makeMarker(-3.7930146, 102.2585743, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kepulauan bangka belitung")) {
                        makeMarker(-2.7366636, 106.4333673, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("nusa tenggara timur")) {
                        makeMarker(-8.6544617, 121.1036203, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("kalimantan utara")) {
                        makeMarker(3.0689394, 116.0553403, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sulawesi tengah")) {
                        makeMarker(-1.4851465, 121.0473733, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("gorontalo")) {
                        makeMarker(0.5467333, 123.0412594, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("sulawesi barat")) {
                        makeMarker(-2.8266076, 119.2623433, provinceName.toUpperCase(), positive, recovered, death);
                    }
                    if (provinceName.equals("papua barat")) {
                        makeMarker(-1.3363926, 133.1490723, provinceName.toUpperCase(), positive, recovered, death);
                    }
                }
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }else {
            return null;
        }
    }

    private void makeMarker(double latude, double longit, String orovinceName, String positive, String recovered, String meninggal) {
        LatLng location = new LatLng(latude, longit);
        mMap.addMarker(new MarkerOptions().position(location).title("(" + orovinceName + ")" + " Positive : " + positive+","+
                " Recovered : " + recovered+"," + " Deaths : " + meninggal+",").icon(bitmapDescriptorFromVector(getContext(),
                R.drawable.ic_circle_small)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void getProvinceDataList() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Province-Data", Context.MODE_PRIVATE);
            String json = sharedPreferences.getString("provinceList", null);

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Province>>() {
            }.getType();
            provincesDataList = gson.fromJson(json, type);
        }
    }


}
