package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalassignment.R;

public class ClientViewHomePage extends AppCompatActivity {


    SharedPreferences pref;
    String type;
    String username;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        type = pref.getString("Type",null);
        role = pref.getString("Type", null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_view_home_page);
        }
        else
        {
            Toast.makeText(ClientViewHomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewHomePage.this, MainActivity.class));
        }
    }

    public void assignedPatients(View view)
    {
        startActivity(new Intent(ClientViewHomePage.this, ClientAssignedAdmissionPage.class));
    }

    public void concernedPatients(View view)
    {
        startActivity(new Intent(ClientViewHomePage.this, ClientConcernedAdmissionPage.class));
    }

    public void allPatients(View view)
    {
        startActivity(new Intent(ClientViewHomePage.this, ClientHomePage.class));
    }


    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewHomePage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewHomePage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewHomePage.this, ClientAddAdmissionPage.class));
                break;
            case R.id.action_assigned_patient_client:
            {
                Intent intent = new Intent(getApplicationContext(), ClientSearchAdmissionPage.class);
                startActivity(intent);
            }
            break;
            case R.id.action_logout_client:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(ClientViewHomePage.this,  MainActivity.class));
            }
            break;
        }
    }

}