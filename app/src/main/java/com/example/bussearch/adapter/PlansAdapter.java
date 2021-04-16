package com.example.bussearch.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.bussearch.R;

import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    private List<TransitRouteLine> lines;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_plans, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(lines.get(position).
    }


    @Override
    public int getItemCount() {
        return lines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,meter,fromTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            meter = itemView.findViewById(R.id.tv_meter);
            fromTo = itemView.findViewById(R.id.tv_from_to);
        }
    }
}
