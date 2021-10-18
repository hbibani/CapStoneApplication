package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientAdmissionClinicianPage extends AppCompatActivity {

    SharedPreferences pref;
    String mrn;
    String admissionid;
    String username;
    String role;
    ArrayList<String> clinSingleList;
    ArrayAdapter<String> clinAdapter;
    JSONArray clin;
    private RecyclerView mRVClinician;
    private AdapterClinician mAdapter;
    List<DataClinician> data1;
    JSONArray clinician;
    MaterialAutoCompleteTextView autoedittext;
    String clinicianid2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);

        String s1 = "Role: '2'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_admission_clinician_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            clinicianid2 = " ";
            autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);

            autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    try
                    {
                        int positionactual = 0;
                        String selected = (String) parent.getItemAtPosition(position);
                        for(int i = 0; i < clinSingleList.size(); i++)
                        {
                            if(clinSingleList.get(i).equals(selected))
                            {
                                positionactual = i;
                                break;
                            }
                        }
                        JSONObject names = clin.getJSONObject(positionactual);
                        clinicianid2 = names.getString("ClinicianID");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            initializeClinicianList();
            initializeClinicianListAdmission();
        }
    }

    private void initializeClinicianListAdmission()
    {
        data1 = new ArrayList<>();
        fetchClinsAdmissionList();
    }

    private void fetchClinsAdmissionList()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getclinicianadmissionlist.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            clinician = new JSONArray(result);
                            data1.clear();

                            for(int i = 0; i < clinician.length() ; i++)
                            {

                                JSONObject names = clinician.getJSONObject(i);
                                DataClinician clinData = new DataClinician();
                                clinData.patientmrn = mrn;
                                clinData.admissionid = admissionid;
                                clinData.StaffNumber = names.getString("StaffNumber");
                                clinData.Type = names.getString("ClinicianType");
                                clinData.clinicianid = names.getString("ClinicianID");
                                clinData.clinicianAdmissionid = names.getString("ClinicianAdmissionID");
                                data1.add(clinData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVClinician = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter = new AdapterClinician(ClientAdmissionClinicianPage.this, data1);
                            mRVClinician.setAdapter(mAdapter);
                            mRVClinician.setLayoutManager(new LinearLayoutManager(ClientAdmissionClinicianPage.this));
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


    private void initializeClinicianList()
    {
        clinSingleList = new ArrayList<String>();
        if(fetchSingleClinList())
        {
            clinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clinSingleList);
            autoedittext.setAdapter(clinAdapter);
        }
    }

    private boolean fetchSingleClinList()
    {
        FetchData fetchData = new FetchData("http://bopps2130.net/fetchAllClin.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Could not get clinician list for search.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                    {
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Retrieved clinician list successfully.", Toast.LENGTH_SHORT).show();

                        try
                        {
                            clin = new JSONArray(result);
                            clinSingleList.clear();
                            for(int i = 0; i < clin.length();i++)
                            {
                                JSONObject names = clin.getJSONObject(i);
                                String clinician  = names.getString("StaffNumber");
                                clinician = clinician + " " + names.getString("ClinicianType");
                                clinSingleList.add(clinician);
                            }

                            return true;
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }

    public void onClickGraph(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientViewGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(),  ClientAdmissionNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientFinaliseFeedbackPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_home_client:
                startActivity(new Intent(ClientAdmissionClinicianPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientAdmissionClinicianPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientAdmissionClinicianPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientAdmissionClinicianPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void addClinician(View view) throws JSONException
    {
        boolean validateStaffNumber = validateStaffNumber();

        if(validateStaffNumber)
        {
            if (addClinToPatientdata())
            {
                Intent intent = new Intent(getApplicationContext(),ClientAdmissionClinicianPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                startActivity(intent);
            }
        }
    }

    private boolean validateStaffNumber()
    {
        if(autoedittext.getText().toString().isEmpty())
        {
            autoedittext.setError("Field cannot be empty");
            return false;
        }
        else
        {
            autoedittext.requestFocus();
            autoedittext.setError(null);
            return true;
        }
    }

    private boolean addClinToPatientdata()
    {
        String[] field = new String[2];
        field[0] = "admissionid";
        field[1] =  "clinicianid";

        //Creating array for data
        String[] data = new String[2];
        data[0] = admissionid;
        data[1] = clinicianid2;

        PutData putData = new PutData("http://bopps2130.net/addcliniciantoadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not insert clinician into database.":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Could not assign clinician to admission.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Clinician already in database.":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Clinician already assigned to admission.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                    {
                        Toast.makeText(ClientAdmissionClinicianPage.this, "Added clinician to admission successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }
}