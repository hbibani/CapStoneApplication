package my.bop.finalassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterAdmissionMedicationAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    List<DataMed> data = Collections.emptyList();
    DataMed current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterAdmissionMedicationAdmin(Context context, List<DataMed> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_medication, parent,false);
        AdapterAdmissionMedicationAdmin.MyHolder holder=new AdapterAdmissionMedicationAdmin.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterAdmissionMedicationAdmin.MyHolder myHolder= (AdapterAdmissionMedicationAdmin.MyHolder) holder;
        DataMed current=data.get(position);
        myHolder.med_title.setText("Chem: "+ current.chemName);
        myHolder.brand_title.setText("Brand: " + current.brandName);
        myHolder.dosage_des.setText("Dose: " + current.dosageName);
        myHolder.dosageform_des.setText("Form: " + current.doseForm);
        myHolder.amount_des.setText("Amount:" + current.doseAmount);
        myHolder.time_des.setText("Time: " + current.time);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView med_title;
        TextView dosage_des;
        TextView dosageform_des;
        TextView time_des;
        ImageView viewimage;
        TextView amount_des;
        TextView brand_title;
        Button button_des;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            med_title = (TextView) itemView.findViewById(R.id.med_title);
            brand_title = (TextView) itemView.findViewById(R.id.brand_title);
            dosage_des = (TextView) itemView.findViewById(R.id.dosage_des);
            dosageform_des = (TextView) itemView.findViewById(R.id.dosageform_des);
            viewimage = (ImageView) itemView.findViewById(R.id.image_des);
            time_des = (TextView) itemView.findViewById(R.id.time_des);
            amount_des = (TextView) itemView.findViewById(R.id.amount_des);
            button_des = (Button) itemView.findViewById(R.id.button_des);

            button_des.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
                            // set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete")
                            .setIcon(R.drawable.ic_baseline_delete_forever_24)

                            .setPositiveButton("Delete", (dialog, whichButton) -> {
                                deleteMed();
                                dialog.dismiss();
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            })
                            .create();

                    myQuittingDialogBox.show();
                }
            });

            med_title.setOnClickListener(this);
        }

        private void deleteMed()
        {
            int position = this.getAdapterPosition();
            DataMed medication = data.get(position);
            String medicationstayid = medication.getMedicationStayId();
            String mrnget = medication.getMrn();
            String admissionget = medication.getAdmissionid();


            String[] field = new String[1];
            field[0] = "medicationstayid";

            //Creating array for data
            String[] data = new String[1];
            data[0] = medicationstayid;

            PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/deletemedfromadmission.php", "POST", field, data);

            if (putData.startPut())
            {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    if (result.equals("Deleted"))
                    {
                        Intent intent = new Intent(context, AdminAdmissionMedicationPage.class);
                        intent.putExtra("mrn", mrnget);
                        intent.putExtra("admissionid", admissionget);
                        context.startActivity(intent);

                    }
                }
            }
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataMed medication = data.get(position);
            String medicationstayid = medication.getMedicationStayId();
            String medicationid = medication.getmedID();
            String mrnget = medication.getMrn();
            String admissionget = medication.getAdmissionid();

            Intent intent = new Intent(context, AdminAdmissionModifyMedicationPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("admissionid", admissionget);
            intent.putExtra("medicationstayid", medicationstayid);
            intent.putExtra("medicationid", medicationid);
            context.startActivity(intent);
        }
    }


}
