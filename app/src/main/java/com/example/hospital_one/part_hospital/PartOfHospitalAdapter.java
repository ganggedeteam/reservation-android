package com.example.hospital_one.part_hospital;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hospital_one.R;

import java.util.List;

public class PartOfHospitalAdapter extends RecyclerView.Adapter<PartOfHospitalAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        View partView;
        public ViewHolder(View view){
            super(view);
            partView = view;
            textView = (TextView)view.findViewById(R.id.keshiCellName);
        }
    }

    private List<String> keshiList;
    public PartOfHospitalAdapter( List<String> keshiList){
        this.keshiList = keshiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.keshi_name,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        String name = this.keshiList.get(position);
        holder.textView.setText(name);
        holder.partView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return this.keshiList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
