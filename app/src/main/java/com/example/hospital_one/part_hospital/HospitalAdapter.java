package com.example.hospital_one.part_hospital;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hospital_one.R;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    public static class HospitalNativeMes{
        public String hospitalId;
        public String hospitalGrade;
        public String hospitalName;
        public String hospitalAddress;
        public int hospitalPic = R.drawable.account;

        public HospitalNativeMes(String hospitalId,
                           String hospitalGrade,
                           String hospitalName,
                           String hospitalAddress){
            this.hospitalId = hospitalId;
            this.hospitalGrade = hospitalGrade;
            this.hospitalName = hospitalName;
            this.hospitalAddress = hospitalAddress;
        }

        public HospitalNativeMes(String hospitalId,
                                 String hospitalGrade,
                                 String hospitalName,
                                 String hospitalAddress,
                                 int hospitalPic){
            this.hospitalId = hospitalId;
            this.hospitalGrade = hospitalGrade;
            this.hospitalName = hospitalName;
            this.hospitalAddress = hospitalAddress;
            this.hospitalPic = hospitalPic;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hospitalView;
        TextView hospitalName;
        TextView hospitalGrade;
        TextView hospitalAddress;
        View partView;

        public ViewHolder(View view) {
            super(view);
            partView = view;
            hospitalName = (TextView) view.findViewById(R.id.HospitalItemName);
            hospitalGrade = (TextView) view.findViewById(R.id.HospitalItemGrade);
            hospitalAddress = (TextView) view.findViewById(R.id.HospitalItemAddress);
            hospitalView = (ImageView)view.findViewById(R.id.HospitalItemView);
        }
    }

    private List<HospitalNativeMes> hospitalList;

    public HospitalAdapter(List<HospitalNativeMes> hospitalList) {
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
        HospitalNativeMes name = this.hospitalList.get(position);
        holder.hospitalName.setText(name.hospitalName);
        holder.hospitalGrade.setText(name.hospitalGrade);
        holder.hospitalAddress.setText(name.hospitalAddress);
        holder.hospitalView.setImageResource(name.hospitalPic);
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