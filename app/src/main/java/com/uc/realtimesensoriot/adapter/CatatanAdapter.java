package com.uc.realtimesensoriot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.uc.realtimesensoriot.R;
import com.uc.realtimesensoriot.model.Catatan;

import java.util.List;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.MyViewHolder>{
    private Context context;
    private List<Catatan> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public CatatanAdapter(Context context, List<Catatan> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.berat.setText(list.get(position).getBerat());
        holder.tanggal.setText(list.get(position).getDatepersubmit());
        holder.biaya.setText(list.get(position).getBiaya());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView berat, tanggal, biaya;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            berat = itemView.findViewById(R.id.berat);
            biaya = itemView.findViewById(R.id.biaya);
            tanggal = itemView.findViewById(R.id.tanggal);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog!=null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
