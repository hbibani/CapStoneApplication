package my.bop.finalassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterPatientClient extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataPatient> data = Collections.emptyList();
    DataAdmission current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterPatientClient(Context context, List<DataPatient> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_patient, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataPatient current=data.get(position);
        myHolder.mrn_title.setText("MRN: " + current.patientmrn);
        myHolder.gender_des.setText("Gender: " + current.patientGender);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mrn_title;
        TextView gender_des;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mrn_title = (TextView) itemView.findViewById(R.id.mrn_title);
            gender_des = (TextView) itemView.findViewById(R.id.gender_des);
            mrn_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataPatient patientdetails = data.get(position);
            String mrnget = patientdetails.getMrn();
            String patientid = patientdetails.getPatientID();
            String patientweight = patientdetails.getPatientWeight();
            String patientage = patientdetails.getPatientAge();
            String gender = patientdetails.getPatientGender();
            String userid = patientdetails.getuserid();
            Intent intent = new Intent(context, ClientModifyPatientPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("patientgender", gender);
            intent.putExtra("age", patientage);
            intent.putExtra("weight", patientweight);
            intent.putExtra("gender", gender);
            intent.putExtra("userid", userid);
            intent.putExtra("patientid", patientid);
            intent.putExtra("userid", userid);
            context.startActivity(intent);
        }
    }





}
