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
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicationSearchPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    JSONArray medications;
    List<DataMedAdmin> data1;
    private RecyclerView mRVAdmission;
    private AdapterMedicationAdmin mAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_medication_search_page);
            initialMedicationList();
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
            Toast.makeText(AdminMedicationSearchPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminMedicationSearchPage.this,  MainActivity.class));
        }

    }

    private void initialMedicationList()
    {
        if(getAllMedList())
        {

        }
        else
        {
            Toast.makeText(AdminMedicationSearchPage.this, "Could not get medication list.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getAllMedList()
    {
        data1 = new ArrayList<>();

        FetchData fetchData = new FetchData("http://bopps2130.net/getallmeds.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();
                if(!result.equals("No medications.") && !result.equals("Error: Database connection") && !result.equals("All fields are required"))
                {
                    try
                    {
                        medications = new JSONArray(result);
                        data1.clear();
                        for(int i = 0; i < medications .length();i++)
                        {
                            JSONObject names = medications.getJSONObject(i);
                            DataMedAdmin medData = new DataMedAdmin();
                            medData.medID = names.getString("MedicationID");
                            medData.brandName = names.getString("MedBrand");
                            medData.chemName = names.getString("MedChemName");
                            medData.dosageName = names.getString("Dosage");
                            medData.doseForm = names.getString("DoseForm");
                            data1.add(medData);
                        }

                        // Setup and Handover data to recyclerview
                        mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                        mAdapter = new AdapterMedicationAdmin(AdminMedicationSearchPage.this, data1);
                        mRVAdmission.setAdapter(mAdapter);
                        mRVAdmission.setLayoutManager(new LinearLayoutManager(AdminMedicationSearchPage.this));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:

                startActivity(new Intent(AdminMedicationSearchPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminMedicationSearchPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminMedicationSearchPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminMedicationSearchPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminMedicationSearchPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminMedicationSearchPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminMedicationSearchPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminMedicationSearchPage.this,  AdminMedicationSearchPage.class) );
    }

    public void onClickMedication(View view)
    {
        startActivity(new Intent(AdminMedicationSearchPage.this,  AdminMedicationPage.class) );
    }

    public void onClickSearchKey(View view)
    {
        startActivity(new Intent(AdminMedicationSearchPage.this, AdminMedicationSearchKeyPage.class) );
    }
}