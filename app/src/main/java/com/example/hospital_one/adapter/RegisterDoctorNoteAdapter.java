package com.example.hospital_one.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hospital_one.R;
import com.example.hospital_one.connection.ReservationConnection;

import java.util.List;

public class RegisterDoctorNoteAdapter extends RecyclerView.Adapter<RegisterDoctorNoteAdapter.ViewHolder> {


    public static class RegisterNoteItemDetailMessage{
        public String doctorName;
        public String doctorLevel;
        public String departmentName;
        public String patientName;
        public String patientStatus;
        public String noteNumber;
        public String noteTime;
        public String hospitalName;
        public String admissionId;

        public RegisterNoteItemDetailMessage(){}

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView doctorName;
        public TextView doctorGrade;
        public TextView departmentName;
        public TextView patientName;
        public TextView patientStatus;
        public TextView noteNumber;
        public TextView noteTime;
        public TextView hospitalName;
        public Button cancelButton;
        public Button detailMessage;
        LinearLayout layout;
        public ViewHolder(View view){
            super(view);
            this.doctorName = (TextView)view.findViewById(R.id.RegisterNoteDoctorName);
            this.doctorGrade = (TextView)view.findViewById(R.id.RegisterNoteDoctorGrade);
            this.departmentName = (TextView)view.findViewById(R.id.RegisterNoteDepartment);
            this.patientName = (TextView)view.findViewById(R.id.RegisterNotePatientName);
            this.patientStatus = (TextView)view.findViewById(R.id.RegisterNoteStatus);
            this.noteNumber = (TextView)view.findViewById(R.id.RegisterNoteNumber);
            this.noteTime = (TextView)view.findViewById(R.id.RegisterNoteTime);
            this.hospitalName = (TextView)view.findViewById(R.id.RegisterNoteHospital);
            this.cancelButton = (Button)view.findViewById(R.id.RegisterNoteCancel);
            this.detailMessage = (Button)view.findViewById(R.id.RegisterNoteDetailButton);
            this.layout = (LinearLayout)view.findViewById(R.id.RegisterNoteDetailMessage);
        }
    }

    List<RegisterNoteItemDetailMessage> list;
    public RegisterDoctorNoteAdapter(List<RegisterNoteItemDetailMessage> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.register_doctor_note_item_layout,parent,false);
        RegisterDoctorNoteAdapter.ViewHolder holder = new RegisterDoctorNoteAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RegisterNoteItemDetailMessage message = list.get(position);

        holder.doctorName.setText(message.doctorName);
        holder.doctorGrade.setText(message.doctorLevel);
        holder.departmentName.setText(message.departmentName);
        holder.patientName.setText(message.patientName);
        holder.patientStatus.setText(ReservationConnection
                .reservationStatus[Character.digit(message.patientStatus.charAt(0),10)]);
        holder.noteNumber.setText(message.noteNumber);
        holder.cancelButton.setVisibility(Character.
                digit(message.patientStatus.charAt(0),10) == 0?View.VISIBLE:View.GONE);
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelButton.OnCancelButtonClicked(position);

            }
        });

        holder.detailMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailButton.OnButtonClicked(holder,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    OnRegisterNoteAdaterButtonClicked detailButton;

    public void setOnRegisterNoteAdaterButtonClicked(OnRegisterNoteAdaterButtonClicked detailButton){
        this.detailButton = detailButton;
    }

    OnCancelButton cancelButton;
    public void setOnCancelButton(OnCancelButton cancelButton){
        this.cancelButton = cancelButton;
    }
}
