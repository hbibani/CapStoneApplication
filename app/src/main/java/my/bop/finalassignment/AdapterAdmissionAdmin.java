package my.bop.finalassignment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterAdmissionAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataAdmissionAdmin> data = Collections.emptyList();
    DataAdmission current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterAdmissionAdmin(Context context, List<DataAdmissionAdmin> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_admission_admin, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataAdmissionAdmin current=data.get(position);
        myHolder.bednumber.setText("Bed: " + current.bed);
        myHolder.admission_title.setText("Admission ID: " + current.admissionid);
        myHolder.mrn_title.setText("MRN: " + current.patientmrn);
        myHolder.gender_des.setText("Gender: " + current.patientGender);
        myHolder.starttime_des.setText("Start Time: " + current.starttime);
        myHolder.endtime_des.setText("End Time: " + current.endtime);

        if (current.concerned.equals("1"))
        {
            myHolder.viewimage.setImageURI(Uri.parse("android.resource://com.example.finalassignment/drawable/danger_patients2"));
        }
        else
        {
            myHolder.viewimage.setImageURI(Uri.parse("android.resource://com.example.finalassignment/drawable/all_patients"));
        }

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bednumber;
        TextView mrn_title;
        TextView gender_des;
        TextView starttime_des;
        TextView endtime_des;
        TextView admission_title;
        ImageView viewimage;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            bednumber = (TextView) itemView.findViewById(R.id.bednumber);
            admission_title = (TextView) itemView.findViewById(R.id.admission_title);
            mrn_title = (TextView) itemView.findViewById(R.id.mrn_title);
            gender_des = (TextView) itemView.findViewById(R.id.gender_des);
            starttime_des = (TextView) itemView.findViewById(R.id.starttime_des);
            endtime_des = (TextView) itemView.findViewById(R.id.endtime_des);
            viewimage = (ImageView) itemView.findViewById(R.id.image_des);
            mrn_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataAdmissionAdmin admission = data.get(position);
            String mrnget = admission.getMrn();
            String admissionget = admission.getAdmissionid();
            Intent intent = new Intent(context, AdminAdmissionGraphPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("admissionid", admissionget);
            context.startActivity(intent);
        }
    }





}
