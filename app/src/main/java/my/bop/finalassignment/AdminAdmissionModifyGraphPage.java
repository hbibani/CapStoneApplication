package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminAdmissionModifyGraphPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    String mrn;
    String admissionid;
    String username;
    String role;
    String graphid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    List<DataClientPain> data1;
    JSONArray pains;
    private RecyclerView mRVAdmission;
    private AdapterAdminPain mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type", null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_admin_admission_modify_graph_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            graphid = intent.getStringExtra("graphid");
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            fetchPainScoreList();
        }
    }

    public void onClickGraph(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionPatientNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:

                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionModifyGraphPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fetchPainScoreList()
    {
        data1 = new ArrayList<>();
        String[] field = new String[1];
        field[0] = "graphid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = graphid;

        PutData putData = new PutData("http://bopps2130.net/getgraphvaluesandtime.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
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
                            mAdapter = new AdapterAdminPain(AdminAdmissionModifyGraphPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(AdminAdmissionModifyGraphPage.this));

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