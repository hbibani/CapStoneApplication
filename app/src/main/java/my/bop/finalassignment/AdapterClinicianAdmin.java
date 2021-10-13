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

public class AdapterClinicianAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataClinicianAdmin> data = Collections.emptyList();
    DataClinician current;
    int currentPos=0;


    // create constructor to innitilize context and data sent from MainActivity
    public AdapterClinicianAdmin(Context context, List<DataClinicianAdmin> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_clinician_admin, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataClinicianAdmin current=data.get(position);
        myHolder.staffnumber_title.setText("Staff Number: " +  current.StaffNumber);
        myHolder.type_des.setText("Type:" + current.Type);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView staffnumber_title;
        TextView type_des;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            staffnumber_title = (TextView) itemView.findViewById(R.id.staffnumber_title);
            type_des = (TextView) itemView.findViewById(R.id.type_des);
            staffnumber_title .setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataClinicianAdmin clinician = data.get(position);
            String clinicianid = clinician.getclinicianid();
            String staffid = clinician.getStaffNumber();
            String userid = clinician.getUserid();
            String cliniciantype = clinician.getType();
            Intent intent = new Intent(context, AdminClinicianModifyPage.class);
            intent.putExtra("clinicianid", clinicianid);
            intent.putExtra("staffnumber", staffid);
            intent.putExtra("userid", userid);
            intent.putExtra("cliniciantype", cliniciantype);
            context.startActivity(intent);
        }
    }





}
