package com.example.hospital_one.adapter;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hospital_one.DoctorCalendarActivity;
import com.example.hospital_one.R;
import com.example.hospital_one.connection.DoctorCalendarConnection;
import com.example.hospital_one.connection.DoctorConnection;
import com.example.hospital_one.connection.InternetConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    private List<DoctorConnection.DoctorMessage> doctorsList;
    public DoctorAdapter( List<DoctorConnection.DoctorMessage> doctorsList){
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
        DoctorConnection.DoctorMessage doctor = this.doctorsList.get(position);
//        holder.doctorImage.setImageResource();
        holder.doctorName.setText(doctor.doctorName);
        holder.doctorKeshi.setText(doctor.typeName);
        holder.doctorZhicheng.setText(doctor.doctorTitle);
        holder.doctorSpecial.setText(doctor.skill);
        getPicture(holder.doctorImage,"doctor.jpg");
    }

    @Override
    public int getItemCount(){
        return this.doctorsList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void getPicture(ImageView picture,String pictureName){
        String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hospitalPicture";
        File fileDir = new File(picturePath);
        if(fileDir.isDirectory() && !fileDir.exists()){
            fileDir.mkdir();
        }

        File filePicture = new File(picturePath + "/" + pictureName);

        if(!filePicture.exists()){
            try{
                filePicture.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        picture.setImageURI(Uri.fromFile(filePicture));

    }

    public class GetPictureTask extends AsyncTask<Void, Void, Boolean> {

        final String url;
        final File pictureFile;

        public GetPictureTask(String url,File pictureFile){
            this.url = url;
            this.pictureFile = pictureFile;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

            }
        }

        @Override
        protected void onCancelled() {
        }

    }


}
