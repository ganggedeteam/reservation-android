package com.example.hospital_one.part_hospital;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hospital_one.R;
import com.example.hospital_one.intenet_connection.HospitalConnection;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalName;
        TextView hospitalGrade;
        TextView hospitalAddress;
        TextView hospitalTelephone;
        TextView hospitalIntroduction;
        View partView;

        public ViewHolder(View view) {
            super(view);
            partView = view;
            hospitalName = (TextView) view.findViewById(R.id.HospitalItemName);
            hospitalGrade = (TextView) view.findViewById(R.id.HospitalItemGrade);
            hospitalAddress = (TextView) view.findViewById(R.id.HospitalItemAddress);
            hospitalIntroduction = (TextView)view.findViewById(R.id.HospitalIntroduction);
            hospitalTelephone = (TextView)view.findViewById(R.id.HospitalTelephone);
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
        String province = name.provinceName;
        String city = name.cityName;
        String county = name.countyName;
        String detail = name.detailAddr;
        holder.hospitalName.setText(name.hospitalName);
        holder.hospitalGrade.setText(name.hospitalGrade);
        String addr = (province == null?"":province)
                +(city == null?"":city)+(county == null?"":county)+(detail == null?"":detail);
        holder.hospitalTelephone.setText(name.hospitalPhone==null?"暂无":name.hospitalPhone);
        holder.hospitalIntroduction.setText(name.introduction == null?"暂无":name.introduction);
        holder.hospitalAddress.setText(addr.equals("")?"暂无":addr);
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