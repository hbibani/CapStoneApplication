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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminPatientSearchPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{



    SharedPreferences pref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    JSONArray patients;
    List<DataPatient> data1;
    private RecyclerView mRVAdmission;
    private AdapterPatient mAdapter;
    Toolbar toolbar;
    EditText searchpatient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_patient_search_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
        else
        {
            Toast.makeText(AdminPatientSearchPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminPatientSearchPage.this,  MainActivity.class));
        }

    }


    public void searchPatientButton(View view)
    {
        data1 = new ArrayList<>();
        searchpatient = (EditText) findViewById(R.id.search_client);
        String mrnnumber = searchpatient.getText().toString();
        Log.d("Results1", "in here");
        searchpatient(mrnnumber);
    }


    public void searchpatient(String mrnnumber)
    {
        String[] field = new String[1];
        field[0] = "mrn";

        //Creating array for data
        String[] data = new String[1];
        data[0] = mrnnumber;

        PutData putData = new PutData("http://bopps2130.net/adminsearchpatient.php", "POST", field, data);
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
                        mAdapter = new AdapterPatient(AdminPatientSearchPage.this, data1);
                        mRVAdmission.setAdapter(mAdapter);
                        mRVAdmission.setLayoutManager(new LinearLayoutManager(AdminPatientSearchPage.this));
                        Toast.makeText(AdminPatientSearchPage.this, "No patient with that mrn.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "Error: Database connection":
                        Toast.makeText(AdminPatientSearchPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
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
                            mAdapter = new AdapterPatient(AdminPatientSearchPage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(AdminPatientSearchPage.this));

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminPatientSearchPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminPatientSearchPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }

    public void onClickNavigationAdminPatient(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_search_patient:
                startActivity(new Intent(AdminPatientSearchPage.this, AdminHomePage.class));
                break;
        }
    }

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminPatientSearchPage.this, AdminPatientSearchPage.class));
    }

    public void onClickPatient(View view)
    {
        startActivity(new Intent(AdminPatientSearchPage.this, AdminPatientPage.class));
    }
}