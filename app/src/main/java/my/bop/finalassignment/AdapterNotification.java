package my.bop.finalassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalassignment.R;

import java.util.Collections;
import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    List<DataNotification> data = Collections.emptyList();
    DataMed current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterNotification(Context context, List<DataNotification> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_notification, parent,false);
        AdapterNotification.MyHolder holder=new AdapterNotification.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterNotification.MyHolder myHolder= (AdapterNotification.MyHolder) holder;
        DataNotification current=data.get(position);
        myHolder.notes_title.setText("Time: ");
        myHolder.notes_des.setText("Notification: Please input pain score.");
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

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            notes_title = (TextView) itemView.findViewById(R.id.notes_title);
            notes_des = (TextView) itemView.findViewById(R.id.notes_des);
            notes_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
        }
    }



}
