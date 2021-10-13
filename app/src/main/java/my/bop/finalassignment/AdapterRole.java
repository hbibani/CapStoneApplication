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

public class AdapterRole extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    List<DataRole> data = Collections.emptyList();

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRole(Context context, List<DataRole> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_role, parent,false);
        AdapterRole.MyHolder holder=new AdapterRole.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterRole.MyHolder myHolder= (AdapterRole.MyHolder) holder;
        DataRole current=data.get(position);
        myHolder.role_title.setText("Role Name: "+ current.rolename);
        myHolder.role_power.setText("Role Power: " + current.rolepower);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView role_title;
        TextView role_power;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            role_title = (TextView) itemView.findViewById(R.id.role_title);
            role_power = (TextView) itemView.findViewById(R.id.role_power);
            role_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataRole role = data.get(position);
            String rolename = role.getRolename();
            String rolepower = role.getRolepower();
            String roleid = role.getRoleID();
            Intent intent = new Intent(context, AdminRoleModifyPage.class);
            intent.putExtra("rolename", rolename);
            intent.putExtra("rolepower", rolepower);
            intent.putExtra("roleid", roleid);
            context.startActivity(intent);
        }
    }
}
