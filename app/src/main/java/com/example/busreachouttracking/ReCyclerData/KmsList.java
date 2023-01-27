package com.example.busreachouttracking.ReCyclerData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busreachouttracking.R;

import java.util.ArrayList;
import java.util.Arrays;

public class KmsList extends RecyclerView.Adapter<KmsList.MyViewHolder> {

    Context context;
    String[] hours;
    String[] minutes;
    ArrayList<Double> time = new ArrayList<>();
    ArrayList<Double> listKmUpdate = new ArrayList<>();
    public KmsList(Context context, ArrayList<Double> time, ArrayList<Double> listKmUpdate)
    {
        this.context = context;
        this.time = time;
        this.listKmUpdate=listKmUpdate;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_textdesign,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtKM.setText(String.valueOf(listKmUpdate.get(position)).substring(0,4)+" KM");
        hours = String.valueOf(time.get(position)*60/60).split("[\\.]",0);
        minutes =String.valueOf(time.get(position)*60%60).split("[\\.]",0);
        holder.txtTime.setText(hours[0]+" hr "+minutes[0]+" min");
    }

    @Override
    public int getItemCount() {
        return listKmUpdate.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime,txtKM;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtKM = itemView.findViewById(R.id.txtKM);
        }
    }
}
