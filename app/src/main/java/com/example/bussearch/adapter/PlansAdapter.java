package com.example.bussearch.adapter;


import android.content.Context;
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
    private Context mContext;

    public PlansAdapter(Context context, List<TransitRouteLine> routeLines) {
        lines = routeLines;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_plans, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransitRouteLine line = lines.get(position);
        holder.title.setText("方案" + (position+1));
        holder.fromTo.setText(line.toString());
        holder.meter.setText("距离：" + line.getDistance()/1000 +
                "公里  时间：" + line.getDuration()/60 + "分钟");
    }


    @Override
    public int getItemCount() {
        return lines.size();
    }


     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,meter,fromTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            meter = itemView.findViewById(R.id.tv_meter);
            fromTo = itemView.findViewById(R.id.tv_from_to);
        }

        @Override
        public void onClick(View v) {
            int k = getAdapterPosition();
            //MapActivity.actionStart(mContext, lines.get(k));
            //RoutesActivity.startRoutesActivity(mContext, lines.get(k));
        }
    }
}
