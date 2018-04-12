package com.example.hospital_one.part_hospital;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hospital_one.PatientManagerActivity;
import com.example.hospital_one.R;
import com.example.hospital_one.UserInformationActivity;
import com.example.hospital_one.intenet_connection.PatientConnection;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder>{

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView patientId;
        TextView patientName;
        TextView patientIdCard;
        TextView patientRelation;
        Button patientDelete;
        View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            patientName = (TextView)view.findViewById(R.id.PatientName);
            patientId = (TextView)view.findViewById(R.id.PatientIDText);
            patientIdCard = (TextView)view.findViewById(R.id.PatientIDCardText);
            patientRelation = (TextView)view.findViewById(R.id.PatientRelationText);
            patientDelete = (Button)view.findViewById(R.id.PatientDelete);
        }
    }

    private List<PatientConnection.PatientMessage> patientMessageList;
    public PatientAdapter(List<PatientConnection.PatientMessage> patientMessageList){
        this.patientMessageList = patientMessageList;
    }

    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_search_result,parent,false);
        PatientAdapter.ViewHolder holder = new PatientAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PatientAdapter.ViewHolder holder, int position){
        if(position>this.patientMessageList.size())return;
        final PatientConnection.PatientMessage patient
                = this.patientMessageList.get(position);
        holder.patientName.setText(patient.getPatientName());
        holder.patientId.setText(patient.getPatientId());
        holder.patientIdCard.setText(patient.getIdCard());
        holder.patientRelation.setText(patient.getRelation());
        holder.patientDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onButtonClick(v,patient.getPatientId());
            }
        });
    }

    @Override
    public int getItemCount(){
        return this.patientMessageList.size();
    }

    private OnButtonClickListener mOnItemClickListener;//声明接口

    public void setOnButtonClickListener(OnButtonClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
