package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientAdmissionModifyGraphPage extends AppCompatActivity {


    SharedPreferences pref;
    String username;
    String role;
    String mrn;
    String admissionid;
    String graphid;
    List<DataClientPain> data1;
    JSONArray pains;
    private RecyclerView mRVAdmission;
    private AdapterClientPain mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_admission_modify_graph_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            graphid = intent.getStringExtra("graphid");
            fetchPainScoreList();
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_home_client:
                startActivity(new Intent(ClientAdmissionModifyGraphPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientAdmissionModifyGraphPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientAdmissionModifyGraphPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientAdmissionModifyGraphPage.this,  MainActivity.class));
            }
            break;
        }
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

    public void fetchPainScoreList()
    {
        data1 = new ArrayList<>();
        String[] field = new String[1];
        field[0] = "graphid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = graphid;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/getgraphvaluesandtime.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                    {
                        data1.clear();
                        // Setup and Handover data to recyclerview
                        mRVAdmission = (RecyclerView)findViewById(R.id.painlist);
                        mAdapter = new AdapterClientPain(ClientAdmissionModifyGraphPage.this, data1);
                        mRVAdmission.setAdapter(mAdapter);
                        mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientAdmissionModifyGraphPage.this));

                    }
                    break;
                    case "Error: Database connection":
                        Toast.makeText(ClientAdmissionModifyGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            pains = new JSONArray(result);
                            data1.clear();
                            for(int i = 0; i < pains.length();i++)
                            {

                                JSONObject names = pains.getJSONObject(i);
                                DataClientPain painData = new DataClientPain();
                                painData.patientmrn = mrn;
                                painData.admissionid = admissionid;
                                painData.time = names.getString("Time");
                                painData.painscore = names.getString("PainScore");
                                painData.graphvalueid = names.getString("GraphValueID");
                                painData.graphid = graphid;
                                data1.add(painData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.painlist);
                            mAdapter = new AdapterClientPain(ClientAdmissionModifyGraphPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientAdmissionModifyGraphPage.this));

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