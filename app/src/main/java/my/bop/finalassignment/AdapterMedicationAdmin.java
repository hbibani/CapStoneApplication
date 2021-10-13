package my.bop.finalassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterMedicationAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    List<DataMedAdmin> data = Collections.emptyList();
    DataMed current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterMedicationAdmin(Context context, List<DataMedAdmin> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_medication_admin, parent,false);
        AdapterMedicationAdmin.MyHolder holder=new AdapterMedicationAdmin.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterMedicationAdmin.MyHolder myHolder= (AdapterMedicationAdmin.MyHolder) holder;
        DataMedAdmin current=data.get(position);
        myHolder.brand_name.setText("Brand: " + current.brandName);
        myHolder.med_title.setText("Chem: "+ current.chemName);
        myHolder.dosage_des.setText("Dose: " + current.dosageName);
        myHolder.dosageform_des.setText("Form: " + current.doseForm);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView med_title;
        TextView brand_name;
        TextView dosage_des;
        TextView dosageform_des;
        TextView time_des;
        ImageView viewimage;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            brand_name = (TextView) itemView.findViewById(R.id.brand_name);
            med_title = (TextView) itemView.findViewById(R.id.med_title);
            dosage_des = (TextView) itemView.findViewById(R.id.dosage_des);
            dosageform_des = (TextView) itemView.findViewById(R.id.dosageform_des);
            viewimage = (ImageView) itemView.findViewById(R.id.image_des);
            time_des = (TextView) itemView.findViewById(R.id.time_des);
            med_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataMedAdmin medication = data.get(position);
            String medicationid = medication.getmedID();
            String medbrand = medication.getBrandName();
            String chemname = medication.getChemName();
            String dosage = medication.getDosageName();
            String doseform = medication.getDoseForm();
            Intent intent = new Intent(context, AdminMedicationModifyPage.class);
            intent.putExtra("medicationid", medicationid);
            intent.putExtra("medbrand", medbrand);
            intent.putExtra("chemname", chemname);
            intent.putExtra("dosage", dosage);
            intent.putExtra("doseform", doseform);
            context.startActivity(intent);
        }

    }

}
