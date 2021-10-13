package my.bop.finalassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterNotes extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    List<DataNotes> data = Collections.emptyList();
    DataMed current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterNotes(Context context, List<DataNotes> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_notes, parent,false);
        AdapterNotes.MyHolder holder=new AdapterNotes.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterNotes.MyHolder myHolder= (AdapterNotes.MyHolder) holder;
        DataNotes current=data.get(position);
        myHolder.notes_title.setText("Time: "+ current.notetime);
        myHolder.notes_des.setText(current.thenotes);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView notes_title;
        TextView notes_des;
        ImageView viewimage;
        Button button_des;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            notes_title = (TextView) itemView.findViewById(R.id.notes_title);
            notes_des = (TextView) itemView.findViewById(R.id.notes_des);
            button_des = (Button) itemView.findViewById(R.id.button_des);


            button_des.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteNote();
                }
            });

            notes_title.setOnClickListener(this);

        }

        public void deleteNote()
        {
            int position = this.getAdapterPosition();
            DataNotes notes = data.get(position);
            String patientnotesid = notes.getPatientnotesid();
            String mrnget = notes.getMrn();
            String admissionget = notes.getAdmissionid();


            String[] field = new String[1];
            field[0] = "patientnotesid";

            //Creating array for data
            String[] data = new String[1];
            data[0] = patientnotesid;

            PutData putData = new PutData("http://bopps2130.net/deletenotespatient.php", "POST", field, data);

            if (putData.startPut())
            {
                if (putData.onComplete())
                {
                    String result = putData.getResult();
                    if(result.contains("Deleted"))
                    {

                    }

                }
            }

            Intent intent = new Intent(context, ClientAdmissionNotesPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("admissionid", admissionget);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataNotes notes = data.get(position);
            String patientnotesid = notes.getPatientnotesid();
            String mrnget = notes.getMrn();
            String admissionget = notes.getAdmissionid();

            Intent intent = new Intent(context, ClientViewNotesClientPage.class);
            intent.putExtra("mrn", mrnget);
            intent.putExtra("admissionid", admissionget);
            intent.putExtra("patientNotesID", patientnotesid);
            context.startActivity(intent);
        }
    }

}
