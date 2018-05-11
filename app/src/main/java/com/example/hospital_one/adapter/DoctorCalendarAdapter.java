package com.example.hospital_one.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hospital_one.DoctorCalendarActivity;
import com.example.hospital_one.R;
import com.example.hospital_one.connection.PictureTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DoctorCalendarAdapter extends RecyclerView.Adapter<DoctorCalendarAdapter.ViewHolder> {

    public static class DoctorCalendarView{
        public String admissionId;
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
        TextView calendarFill;
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
            this.calendarFill = (TextView)view.findViewById(R.id.CalendarFill);
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
        holder.skill.setText("主治：" + doctor.skill);
//        holder.doctorPhoto.setImageBitmap(decodeBitmap("/storage/emulated/0/hospitalPicture/doctor.png",
//                holder.doctorPhoto.getMaxWidth(),holder.doctorPhoto.getMaxHeight()));
        holder.admissionPeriod.setText(doctor.admissionPeriod.equals("0")?"上午8:00~12：00":"下午2:00~5:30");
        holder.admissionNum.setText("" + doctor.admissionNum);
        holder.remainingNum.setText("" + doctor.remainingNum);
        if(doctor.remainingNum == 0){
            holder.calendarFill.setVisibility(View.VISIBLE);
            holder.register.setVisibility(View.GONE);
        }else{
            holder.calendarFill.setVisibility(View.GONE);
            holder.register.setVisibility(View.VISIBLE);
        }
        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.view,position);
            }
        });
        if(doctor.doctorPhoto == null || doctor.doctorPhoto.equals("") || doctor.doctorPhoto.equals("暂无")){}
        else {
            PictureTask doctorTask = new PictureTask(holder.doctorPhoto, doctor.doctorPhoto);
            doctorTask.execute((Void) null);
        }
    }

    @Override
    public int getItemCount(){
        return this.list.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private Bitmap decodeBitmap(String path,int width,int height){
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int)Math.ceil(op.outWidth/width);
        int hRatio = (int)Math.ceil(op.outHeight/height);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }

}
