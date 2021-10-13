package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientSearchAdmissionPage extends AppCompatActivity
{
    SharedPreferences pref;
    String mrn;
    EditText searchmrn;
    JSONArray mrnarray;
    List<DataAdmission> data1;
    private RecyclerView mRVAdmission;
    private AdapterAdmission mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_search_admission_page);
        }
        else
        {
            Toast.makeText(ClientSearchAdmissionPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientSearchAdmissionPage.this,  MainActivity.class));
        }

    }


    public void searchAdmissionButton(View view)
    {
        searchmrn = (EditText) findViewById(R.id.mrn_number_search);
        mrn = searchmrn.getText().toString();
        fetchPatientAdmissionMrn();
    }

    private void fetchPatientAdmissionMrn()
    {
        data1 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "mrn";

        //Creating array for data
        String[] data = new String[1];
        data[0] = mrn;

        PutData putData = new PutData("http://bopps2130.net/searchforpatientadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "No admissions for mrn.":
                    {
                        int size = data1.size();
                        data1.clear();
                        mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                        mAdapter = new AdapterAdmission(ClientSearchAdmissionPage.this, data1);
                        mRVAdmission.setAdapter(mAdapter);
                        mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientSearchAdmissionPage.this));
                        Toast.makeText(ClientSearchAdmissionPage.this, "No admission listings for that mrn.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "Error: Database connection":
                        Toast.makeText(ClientSearchAdmissionPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            //Toast.makeText(AdminAdmissionPage.this, result, Toast.LENGTH_SHORT).show();
                            mrnarray = new JSONArray(result);
                            data1.clear();

                            for(int i = 0; i < mrnarray.length();i++)
                            {
                                JSONObject names =  mrnarray.getJSONObject(i);
                                DataAdmission admissionData = new DataAdmission();
                                admissionData.patientmrn = names.getString("MRN");
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
                                data1.add(admissionData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                            mAdapter = new AdapterAdmission(ClientSearchAdmissionPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientSearchAdmissionPage.this));
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

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientSearchAdmissionPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientSearchAdmissionPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientSearchAdmissionPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientSearchAdmissionPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void onClickStaffSearch(View view)
    {
        startActivity(new Intent(ClientSearchAdmissionPage.this, ClientSearchStaffAdmissionPage.class));
    }

    public void onClickPatientSearch(View view)
    {
        startActivity(new Intent(ClientSearchAdmissionPage.this, ClientSearchAdmissionPage.class));
    }

    public void onClickPatientActualSearch(View view)
    {
        startActivity(new Intent(ClientSearchAdmissionPage.this, ClientSearchPatientPage.class));
    }
}