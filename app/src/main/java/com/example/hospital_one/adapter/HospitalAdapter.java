package com.example.hospital_one.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hospital_one.R;
import com.example.hospital_one.connection.HospitalConnection;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalName;
        TextView hospitalGrade;
        View partView;

        public ViewHolder(View view) {
            super(view);
            partView = view;
            hospitalName = (TextView) view.findViewById(R.id.HospitalItemName);
            hospitalGrade = (TextView) view.findViewById(R.id.HospitalItemGrade);
        }
    }

    private List<HospitalConnection.HospitalMes> hospitalList;

    public HospitalAdapter(List<HospitalConnection.HospitalMes> hospitalList) {
        this.hospitalList = hospitalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_item ,parent, false);
        HospitalAdapter.ViewHolder holder = new HospitalAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position>this.hospitalList.size())return;
        HospitalConnection.HospitalMes name = this.hospitalList.get(position);

        holder.hospitalName.setText(name.hospitalName);
        holder.hospitalGrade.setText(name.hospitalGrade);

//        holder.hospitalTelephone.setText(name.hospitalPhone==null?"暂无":name.hospitalPhone);
//        holder.hospitalIntroduction.setText(name.introduction == null?"暂无":name.introduction);
//        holder.hospitalAddress.setText(addr.equals("")?"暂无":addr);
        holder.partView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(hospitalList == null)return 0;
        return this.hospitalList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}