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

public class ClientHomePage extends AppCompatActivity
{
    //ArrayList<String> patientList;
    SharedPreferences pref;
    List<DataAdmission> data;
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
            setContentView(R.layout.activity_client_home_page);
            initializePatientList();
        }
        else
        {
            Toast.makeText(ClientHomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientHomePage.this,  MainActivity.class));

        }
    }

    private void initializePatientList()
    {
          fetchActivePatientList();
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientHomePage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientHomePage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientHomePage.this, ClientAddAdmissionPage.class));
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    public void fetchActivePatientList()
    {
        data = new ArrayList<>();
        FetchData fetchData = new FetchData("http://bopps2130.net/fetchActivePatients.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();

                switch(result)
                {
                    case "No active patients.":
                        Toast.makeText(ClientHomePage.this, "No active patients.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientHomePage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            patients = new JSONArray(result);
                            data.clear();
                            for(int i = 0; i < patients.length();i++)
                            {
                                JSONObject names = patients.getJSONObject(i);
                                DataAdmission admissionData = new DataAdmission();
                                admissionData.patientmrn = names.getString("mrn");
                                admissionData.bed = names.getString("Bed");
                                admissionData.patientGender = names.getString("PatientGender");
                                admissionData.admissionid = names.getString("AdmissionID");
                                admissionData.concerned = names.getString("Concerned");
                                admissionData.status = names.getString("Status");
                                admissionData.graphid = names.getString("GraphID");
                                admissionData.starttime = names.getString("StartTime");
                                admissionData.endtime = names.getString("EndTime");
                                admissionData.paintype = names.getString("PainType");
                                admissionData.painregion = names.getString("PainRegion");
                                data.add(admissionData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.admissionList);
                            mAdapter = new AdapterAdmission(ClientHomePage.this, data);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientHomePage.this));

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