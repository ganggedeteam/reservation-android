package com.example.hospital_one.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hospital_one.R;

import java.util.List;

public class DoctorCalendarAdapter extends RecyclerView.Adapter<DoctorCalendarAdapter.ViewHolder> {

    public static class DoctorCalendarView{
        public String doctorName;
        public int admissionNum;
        public int remainingNum;
        public String admissionPeriod;
        public String doctorPhoto;
        public String doctorTitle;
        public String skill;
        public DoctorCalendarView(){}
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView doctorName;
        TextView admissionNum;
        TextView remainingNum;
        TextView admissionPeriod;
        ImageView doctorPhoto;
        TextView doctorTitle;
        TextView skill;
        Button register;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            this.doctorName  = (TextView)view.findViewById(R.id.DoctorCalendarName);
            this.admissionNum = (TextView)view.findViewById(R.id.DoctorCalendarAllNum);
            this.remainingNum = (TextView)view.findViewById(R.id.DoctorCalendarRemainNum);
            this.admissionPeriod = (TextView)view.findViewById(R.id.DoctorCalendarPeriod);
            this.doctorPhoto = (ImageView) view.findViewById(R.id.DoctorCalendarImage);
            this.doctorTitle = (TextView)view.findViewById(R.id.DoctorCalendarTitle);
            this.skill = (TextView)view.findViewById(R.id.DoctorCalendarSkill);
            this.register = (Button)view.findViewById(R.id.DoctorCalendarRegisterButton);
        }
    }

    private List<DoctorCalendarView> list;
    public DoctorCalendarAdapter(List<DoctorCalendarView> list){
        this.list = list;
    }

    @Override
    public DoctorCalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_duty_message_layout,parent,false);
        DoctorCalendarAdapter.ViewHolder holder = new DoctorCalendarAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DoctorCalendarAdapter.ViewHolder holder, final int position){
        if(position>this.list.size())return;
        DoctorCalendarView doctor = this.list.get(position);
        holder.doctorName.setText(doctor.doctorName);
        holder.doctorTitle.setText(doctor.doctorTitle);
        holder.skill.setText(doctor.skill);
        holder.admissionPeriod.setText(doctor.admissionPeriod.equals("0")?"上午8:00~12：00":"下午2:00~5:30");
        holder.admissionNum.setText(doctor.admissionNum);
        holder.remainingNum.setText(doctor.remainingNum);
        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.view,position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return this.list.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


}
