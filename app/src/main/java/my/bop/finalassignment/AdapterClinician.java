package my.bop.finalassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterClinician extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataClinician> data = Collections.emptyList();
    DataClinician current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterClinician(Context context, List<DataClinician> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_clinician, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataClinician current=data.get(position);
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
        Button deletebutton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            staffnumber_title = (TextView) itemView.findViewById(R.id.staffnumber_title);
            type_des = (TextView) itemView.findViewById(R.id.type_des);
            deletebutton = (Button) itemView.findViewById(R.id.button3);
            deletebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteClinician();
                }
            });

            //deletebutton.setOnClickListener(this);
        }

        private void deleteClinician()
        {
            int position = this.getAdapterPosition();
            DataClinician clinician = data.get(position);
            String mrnget = clinician.getMrn();
            String admissionget = clinician.getAdmissionid();
            String clinicianadmissionid = clinician.getclinicianadmissionid();

            String[] field = new String[1];
            field[0] = "clinicianaddid";

            //Creating array for data
            String[] data = new String[1];
            data[0] = clinicianadmissionid;

            PutData putData = new PutData("http://bopps2130.net/delclinicianadmission.php", "POST", field, data);

            if (putData.startPut())
            {
                if (putData.onComplete())
                {
                    String result = putData.getResult();
                    if(result.equals("Deleted"))
                    {

                    }
                }
            }

            Intent intent = new Intent(context, ClientAdmissionClinicianPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("admissionid", admissionget);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {

        }
    }
}
