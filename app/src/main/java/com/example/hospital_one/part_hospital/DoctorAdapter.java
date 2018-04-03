package com.example.hospital_one.part_hospital;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hospital_one.R;
import com.example.hospital_one.searchresult.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView doctorImage;
        TextView doctorName;
        TextView doctorKeshi;
        TextView doctorZhicheng;
        TextView doctorSpecial;
        public ViewHolder(View view) {
            super(view);
            doctorImage = (ImageView)view.findViewById(R.id.doctorImage);
            doctorName = (TextView)view.findViewById(R.id.doctorName);
            doctorKeshi = (TextView)view.findViewById(R.id.doctor_ke);
            doctorZhicheng = (TextView)view.findViewById(R.id.doctorclass);
            doctorSpecial = (TextView)view.findViewById(R.id.doctor_special);
        }
    }

    private List<Doctor> doctorsList;
    public DoctorAdapter( List<Doctor> doctorsList){
        this.doctorsList = doctorsList;
    }

    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_search_result,parent,false);
        DoctorAdapter.ViewHolder holder = new DoctorAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DoctorAdapter.ViewHolder holder, int position){
        if(position>this.doctorsList.size())return;
        Doctor doctor = this.doctorsList.get(position);
        holder.doctorImage.setImageResource(doctor.getImageid());
        holder.doctorName.setText(doctor.getName());
        holder.doctorKeshi.setText(doctor.getKeshi());
        holder.doctorZhicheng.setText(doctor.getZhicheng());
        holder.doctorSpecial.setText(doctor.getSpecial());
    }

    @Override
    public int getItemCount(){
        return this.doctorsList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
