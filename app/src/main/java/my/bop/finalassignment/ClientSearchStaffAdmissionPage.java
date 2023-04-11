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

public class ClientSearchStaffAdmissionPage extends AppCompatActivity
{

    SharedPreferences pref;
    JSONArray staffarray;
    List<DataAdmission> data2;
    private RecyclerView mRVAdmission2;
    private AdapterAdmission mAdapter2;
    EditText searchstaffnumber;
    String staffnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '2'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_search_staff_admission_page);
        }
        else
        {
            Toast.makeText(ClientSearchStaffAdmissionPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientSearchStaffAdmissionPage.this,  MainActivity.class));
        }
    }

    public void searchAdmissionButton2(View view)
    {
        searchstaffnumber = (EditText) findViewById(R.id.staff_number_search);
        staffnumber = searchstaffnumber.getText().toString();
        fetchPatientAdmissionStaffNumber();
    }

    private void fetchPatientAdmissionStaffNumber()
    {
        data2 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "staffnumber";

        //Creating array for data
        String[] data = new String[1];
        data[0] = staffnumber;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/searchforstaffadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "No search results.":
                    {
                        data2.clear();
                        mRVAdmission2 = (RecyclerView)findViewById(R.id.admissionList2);
                        mAdapter2 = new AdapterAdmission(ClientSearchStaffAdmissionPage.this, data2);
                        mRVAdmission2.setAdapter(mAdapter2);
                        mRVAdmission2.setLayoutManager(new LinearLayoutManager(ClientSearchStaffAdmissionPage.this));
                        Toast.makeText(ClientSearchStaffAdmissionPage.this, "No admission listings for that staff number.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "Error: Database connection":
                        Toast.makeText(ClientSearchStaffAdmissionPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            staffarray = new JSONArray(result);
                            data2.clear();

                            for(int i = 0; i < staffarray.length();i++)
                            {
                                JSONObject names = staffarray.getJSONObject(i);
                                DataAdmission admissionData = new DataAdmission();
                                admissionData.patientmrn = names.getString("MRN");
                                admissionData.bed = names.getString("Bed");
                                admissionData.patientGender = names.getString("PatientGender");
                                admissionData.admissionid = names.getString("AdmissionID");
                                admissionData.concerned = names.getString("Concerned");
                                admissionData.starttime = names.getString("StartTime");
                                admissionData.endtime = names.getString("EndTime");
                                admissionData.status = names.getString("Status");
                                admissionData.graphid = names.getString("GraphID");
                                admissionData.paintype = names.getString("PainType");
                                admissionData.painregion = names.getString("PainRegion");
                                data2.add(admissionData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission2 = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter2 = new AdapterAdmission(ClientSearchStaffAdmissionPage.this, data2);
                            mRVAdmission2.setAdapter(mAdapter2);
                            mRVAdmission2.setLayoutManager(new LinearLayoutManager(ClientSearchStaffAdmissionPage.this));
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
                startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientSearchStaffAdmissionPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void onClickStaffSearch(View view)
    {
        startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientSearchStaffAdmissionPage.class));
    }

    public void onClickPatientSearch(View view)
    {
        startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientSearchAdmissionPage.class));
    }

    public void onClickPatientActualSearch(View view)
    {
        startActivity(new Intent(ClientSearchStaffAdmissionPage.this, ClientSearchPatientPage.class));
    }
}