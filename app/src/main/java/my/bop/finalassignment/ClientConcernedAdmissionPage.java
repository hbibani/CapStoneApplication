package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientConcernedAdmissionPage extends AppCompatActivity {

    SharedPreferences pref;
    List<DataAdmission> data1;
    JSONArray patients;
    String username;
    String type;
    private RecyclerView mRVAdmission;
    private AdapterAdmission mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        type = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";
        if(type.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_concerned_admission_page);
            initializePatientList();
        }
        else
        {
            Toast.makeText(ClientConcernedAdmissionPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientConcernedAdmissionPage.this,  MainActivity.class));
        }
    }

    private void initializePatientList()
    {
        fetchActivePatientList();
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientConcernedAdmissionPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientConcernedAdmissionPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientConcernedAdmissionPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientConcernedAdmissionPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void fetchActivePatientList()
    {
        data1 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "username";
        //Creating array for data
        String[] data = new String[1];
        data[0] = username;

        PutData putData = new PutData("http://bopps2130.net/getconcernedpatientclinican.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();

                switch(result)
                {
                    case "No active patients.":
                        Toast.makeText(ClientConcernedAdmissionPage.this, "No active patients.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientConcernedAdmissionPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            patients = new JSONArray(result);
                            data1.clear();
                            for(int i = 0; i < patients.length();i++)
                            {

                                JSONObject names = patients.getJSONObject(i);
                                DataAdmission admissionData = new DataAdmission();
                                admissionData.patientmrn = names.getString("mrn");
                                admissionData.bed = names.getString("Bed");
                                admissionData.patientGender = names.getString("PatientGender");
                                admissionData.admissionid = names.getString("AdmissionID");
                                admissionData.concerned = names.getString("Concerned");
                                admissionData.starttime = names.getString("StartTime");
                                admissionData.endtime = names.getString("EndTime");
                                data1.add(admissionData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter = new AdapterAdmission(ClientConcernedAdmissionPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientConcernedAdmissionPage.this));

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}