package my.bop.finalassignment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterClientPain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataClientPain> data = Collections.emptyList();
    DataAdmission current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterClientPain(Context context, List<DataClientPain> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_graph_pain, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataClientPain current=data.get(position);
        myHolder.painvalue.setText("Score: " + current.painscore);
        myHolder.admission_title.setText("Admission ID: " + current.admissionid);
        myHolder.mrn_title.setText("MRN: " + current.patientmrn);
        myHolder.time_des.setText("Time: " + current.time);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView painvalue;
        TextView admission_title;
        TextView mrn_title;
        TextView time_des;
        Button deletebutton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            painvalue = (TextView) itemView.findViewById(R.id.painvalue);
            admission_title =(TextView) itemView.findViewById(R.id.admission_title);
            mrn_title = (TextView) itemView.findViewById(R.id.mrn_title);
            time_des = (TextView) itemView.findViewById(R.id.time_des);
            deletebutton = (Button) itemView.findViewById(R.id.button3);
            deletebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
                            // set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete")
                            .setIcon(R.drawable.ic_baseline_delete_forever_24)

                            .setPositiveButton("Delete", (dialog, whichButton) -> {

                                deleteGraphItem();
                                if(context instanceof ClientAdmissionModifyGraphPage)
                                {
                                    ((ClientAdmissionModifyGraphPage)context).fetchPainScoreList();
                                }
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
        }

        private void deleteGraphItem()
        {
            int position = this.getAdapterPosition();
            DataClientPain paininput = data.get(position);
            String mrnget = paininput.getMrn();
            String admissionget = paininput.getAdmissionid();
            String graphid = paininput.getGraphID();
            String graphvalueid = paininput.getGraphValueID();

            String[] field = new String[1];
            field[0] = "graphvalueid";

            //Creating array for data
            String[] data = new String[1];
            data[0] = graphvalueid;


            PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/deletegraphvalue.php", "POST", field, data);
            if (putData.startPut())
            {
                if (putData.onComplete())
                {

                    String result = putData.getResult();

                    if((result.equals("Deleted")))
                    {

                    }

                }
            }

        }

        @Override
        public void onClick(View v)
        {


        }
    }
}

