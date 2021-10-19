package my.bop.finalassignment;

import android.content.Context;
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

public class AdapterGraph extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    private LayoutInflater inflater;
    List<DataAUC> data = Collections.emptyList();
    DataAUC current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterGraph(Context context, List<DataAUC> data){
        this.context= context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_graph, parent,false);
        AdapterGraph.MyHolder holder=new AdapterGraph.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterGraph.MyHolder myHolder= (AdapterGraph.MyHolder) holder;
        DataAUC current = data.get(position);
        myHolder.AUCnumber1.setText("AUC: " +current.AUC);
        myHolder.mrn_title.setText("MRN: " + current.patientmrn);
        myHolder.gender_des.setText("Gender: " + current.patientGender);
        if (current.concerned.equals("1"))
        {
            myHolder.viewimage.setImageURI(Uri.parse("android.resource://my.bop.finalassignment/drawable/danger_patients2"));
        }
        else
        {
            myHolder.viewimage.setImageURI(Uri.parse("android.resource://my.bop.finalassignment/drawable/all_patients"));
        }

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView AUCnumber1;
        TextView mrn_title;
        TextView gender_des;
        ImageView viewimage;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            AUCnumber1 = (TextView) itemView.findViewById(R.id.AUCnumber);
            mrn_title = (TextView) itemView.findViewById(R.id.mrn_title);
            gender_des = (TextView) itemView.findViewById(R.id.gender_des);
            viewimage = (ImageView) itemView.findViewById(R.id.image_des);
            mrn_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

        }
    }

}
