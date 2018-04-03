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

    public static class PartOfHospitalMes{
        public String departmentTypeId;
        public String departmentName;
        public String remark;

        public PartOfHospitalMes(String departmentName){
            this.departmentName = departmentName;
        }

        public PartOfHospitalMes(String departmentTypeId,
                String departmentName,
                String remark){
            this.departmentTypeId = departmentTypeId;
            this.departmentName = departmentName;
            this.remark = remark;
        }
    }

    private List<PartOfHospitalMes> keshiList;
    public PartOfHospitalAdapter( List<PartOfHospitalMes> keshiList){
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
        PartOfHospitalMes name = this.keshiList.get(position);
        holder.textView.setText(name.departmentName);
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
        if(this.keshiList == null)return 0;
        return this.keshiList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
