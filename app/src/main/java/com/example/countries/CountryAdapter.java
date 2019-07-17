package com.example.countries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private Context context;
    private List<Country> countryList;
    private OnItemClickListener onItemClickListener;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    public void changeData() {
        this.countryList.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = viewHolder.getAdapterPosition();
                onItemClickListener.onItemClick(countryList.get(adapterPosition));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.ViewHolder viewHolder, int position) {
        Country country = countryList.get(position);
        viewHolder.tvCountry.setText(country.getName());
        Glide.with(context).load(country.getFlag()).onlyRetrieveFromCache(true).override(App.px, App.px).into(viewHolder.ivCountry);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCountry;
        private ImageView ivCountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            ivCountry = itemView.findViewById(R.id.ivCountry);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Country country);
    }
}
