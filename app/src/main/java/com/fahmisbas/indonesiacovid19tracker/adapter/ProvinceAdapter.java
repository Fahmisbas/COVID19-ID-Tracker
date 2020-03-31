package com.fahmisbas.indonesiacovid19tracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmisbas.indonesiacovid19tracker.R;
import com.fahmisbas.indonesiacovid19tracker.model.Province;

import java.util.ArrayList;
import java.util.Collection;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {


    private ArrayList<Province> provincesData;
    private ArrayList<Province> provincesDataFull;

    public ProvinceAdapter(ArrayList<Province> provincesData){
        this.provincesData = provincesData;
        provincesDataFull = new ArrayList<>(provincesData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_provinces,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Province province = provincesData.get(position);

        holder.tvRecovered.setText(province.getRecovered());
        holder.tvPositive.setText(province.getPositive());
        holder.tvDeath.setText(province.getDeath());
        holder.tvProvinceName.setText(province.getProvinceName());

    }

    @Override
    public int getItemCount() {
        return provincesData.size();
    }

    public Filter getFilter(){
        return provinceFilter;
    }

    private Filter provinceFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Province> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(provincesDataFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Province item : provincesDataFull) {
                    if (item.getProvinceName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            provincesData.clear();
            provincesData.addAll((Collection<? extends Province>) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvProvinceName,tvPositive,tvRecovered,tvDeath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProvinceName = itemView.findViewById(R.id.tv_provinceName);
            tvDeath = itemView.findViewById(R.id.tv_death);
            tvPositive = itemView.findViewById(R.id.tv_positive);
            tvRecovered = itemView.findViewById(R.id.tv_recovered);

        }
    }
}
