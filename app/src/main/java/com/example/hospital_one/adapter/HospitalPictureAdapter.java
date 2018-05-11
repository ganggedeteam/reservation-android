package com.example.hospital_one.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.hospital_one.R;
import com.example.hospital_one.connection.PictureTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HospitalPictureAdapter extends RecyclerView.Adapter<HospitalPictureAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView hospitalPicture;

        public ViewHolder(View view){
            super(view);
            hospitalPicture = (ImageView)view.findViewById(R.id.HospitalPicture);
        }
    }

    private List<String> list;
    public HospitalPictureAdapter(List<String> list){
        this.list = list;
    }

    @Override
    public HospitalPictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_picture_layout ,parent, false);
        HospitalPictureAdapter.ViewHolder holder = new HospitalPictureAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.hospitalPicture.setImageBitmap();
        PictureTask hospitalPicTask = new PictureTask(holder.hospitalPicture,list.get(position));
        hospitalPicTask.execute((Void)null);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
