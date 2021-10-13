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
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminAdmissionStaffPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    JSONArray staffarray;
    List<DataAdmissionAdmin> data2;
    private RecyclerView mRVAdmission2;
    private AdapterAdmissionAdmin mAdapter2;
    EditText searchstaffnumber;
    String staffnumber;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_admission_staff_page);
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
            Toast.makeText(AdminAdmissionStaffPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAdmissionStaffPage.this,  MainActivity.class));
        }
    }

    public void searchAdmissionButton2(View view)
    {
        searchstaffnumber = (EditText) findViewById(R.id.staff_number_search);
        staffnumber = searchstaffnumber.getText().toString();

        if(fetchPatientAdmissionStaffNumber())
        {
            Toast.makeText(AdminAdmissionStaffPage.this, "Success.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AdminAdmissionStaffPage.this, "Could not fetch list.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean fetchPatientAdmissionStaffNumber()
    {
        data2 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "staffnumber";

        //Creating array for data
        String[] data = new String[1];
        data[0] = staffnumber;

        PutData putData = new PutData("http://bopps2130.net/searchforstaffadmission.php", "POST", field, data);
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
                        mAdapter2 = new AdapterAdmissionAdmin(AdminAdmissionStaffPage.this, data2);
                        mRVAdmission2.setAdapter(mAdapter2);
                        mRVAdmission2.setLayoutManager(new LinearLayoutManager(AdminAdmissionStaffPage.this));
                        Toast.makeText(AdminAdmissionStaffPage.this, "No admission listings for that staff number.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionStaffPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
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
                                DataAdmissionAdmin admissionData = new DataAdmissionAdmin();
                                admissionData.patientmrn = names.getString("MRN");
                                admissionData.bed = names.getString("Bed");
                                admissionData.patientGender = names.getString("PatientGender");
                                admissionData.admissionid = names.getString("AdmissionID");
                                admissionData.concerned = names.getString("Concerned");
                                admissionData.starttime = names.getString("StartTime");
                                admissionData.endtime = names.getString("EndTime");
                                data2.add(admissionData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission2 = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter2 = new AdapterAdmissionAdmin(AdminAdmissionStaffPage.this, data2);
                            mRVAdmission2.setAdapter(mAdapter2);
                            mRVAdmission2.setLayoutManager(new LinearLayoutManager(AdminAdmissionStaffPage.this));

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionStaffPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionStaffPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    public void onClickStaffSearch(View view)
    {
        startActivity(new Intent(AdminAdmissionStaffPage.this, AdminAdmissionStaffPage.class));
    }

    public void onClickPatientSearch(View view)
    {
        startActivity(new Intent(AdminAdmissionStaffPage.this, AdminAdmissionPage.class));
    }
}