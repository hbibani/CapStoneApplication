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

public class ClientSearchPatientPage extends AppCompatActivity
{
    SharedPreferences pref;
    JSONArray patients;
    List<DataPatient> data1;
    private RecyclerView mRVAdmission;
    private AdapterPatientClient mAdapter;
    EditText searchpatient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_search_patient_page);
        }
        else
        {
            Toast.makeText(ClientSearchPatientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientSearchPatientPage.this,  MainActivity.class));
        }
    }


    public void searchPatientButton(View view)
    {
        data1 = new ArrayList<>();
        searchpatient = (EditText) findViewById(R.id.search_client);
        String mrnnumber = searchpatient.getText().toString();

        if(searchpatient(mrnnumber))
        {

        }
        else
        {

        }
    }


    public boolean searchpatient(String mrnnumber)
    {
        String[] field = new String[1];
        field[0] = "mrn";

        //Creating array for data
        String[] data = new String[1];
        data[0] = mrnnumber;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/adminsearchpatient.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "No active patients.":
                    {
                        data1.clear();
                        mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                        mAdapter = new AdapterPatientClient(ClientSearchPatientPage.this, data1);
                        mRVAdmission.setAdapter(mAdapter);
                        mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientSearchPatientPage.this));
                        Toast.makeText(ClientSearchPatientPage.this, "No patient with that mrn.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "Error: Database connection":
                        Toast.makeText(ClientSearchPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
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
                                DataPatient patientData = new DataPatient();
                                patientData.patientmrn = names.getString("MRN");
                                patientData.patientAge = names.getString("PatientAge");
                                patientData.patientGender = names.getString("PatientGender");
                                patientData.patientWeight = names.getString("PatientWeight");
                                patientData.patientID = names.getString("PatientID");
                                patientData.userid = names.getString("UserID");
                                data1.add(patientData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                            mAdapter = new AdapterPatientClient(ClientSearchPatientPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientSearchPatientPage.this));

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

    public void onClickStaffSearch(View view)
    {
        startActivity(new Intent(ClientSearchPatientPage.this, ClientSearchStaffAdmissionPage.class));
    }

    public void onClickPatientSearch(View view)
    {
        startActivity(new Intent(ClientSearchPatientPage.this, ClientSearchAdmissionPage.class));
    }

    public void onClickPatientActualSearch(View view)
    {
        startActivity(new Intent(ClientSearchPatientPage.this, ClientSearchPatientPage.class));
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientSearchPatientPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientSearchPatientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientSearchPatientPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientSearchPatientPage.this,  MainActivity.class));
            }
            break;
        }
    }


}