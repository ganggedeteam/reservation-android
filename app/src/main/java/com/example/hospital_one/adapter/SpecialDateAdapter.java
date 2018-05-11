package com.example.hospital_one.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hospital_one.R;

import java.util.List;

public class SpecialDateAdapter extends RecyclerView.Adapter<SpecialDateAdapter.ViewHolder>{

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            this.date = (TextView)view.findViewById(R.id.DateNumber);
        }
    }
    List<String> date;
    public SpecialDateAdapter(List<String> date){
        this.date = date;
    }

    @Override
    public SpecialDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_layout,parent,false);
        SpecialDateAdapter.ViewHolder holder = new SpecialDateAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SpecialDateAdapter.ViewHolder holder, final int position){
        holder.date.setText(date.get(position).substring(5));
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.date.size();
    }
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
