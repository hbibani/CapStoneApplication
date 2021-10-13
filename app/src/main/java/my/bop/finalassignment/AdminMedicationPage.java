package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class AdminMedicationPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    EditText medbrand1;
    EditText chemname1;
    EditText dosage1;
    EditText doseform1;
    String medbrand;
    String chemname;
    String dosage;
    String doseform;
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
            setContentView(R.layout.activity_admin_medication_page);
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
            Toast.makeText(AdminMedicationPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminMedicationPage.this,  MainActivity.class));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminMedicationPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminMedicationPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void addMedicationButton(View view)
    {
        medbrand1 = (EditText) findViewById(R.id.medication_brand);
        chemname1 = (EditText) findViewById(R.id.medication_chemical_name);
        dosage1 = (EditText) findViewById(R.id.medication_dosage);
        doseform1 = (EditText) findViewById(R.id.medication_dose_form);
        medbrand = medbrand1.getText().toString();
        chemname = chemname1.getText().toString();
        dosage = dosage1.getText().toString();
        doseform =  doseform1.getText().toString();

        boolean validatemedbrand = validateMedBrand();
        boolean validatechemname = validateChemName();
        boolean validatedosage = validateDosage();
        boolean validatedoseform = validateDoseForm();

        if(validatemedbrand && validatechemname && validatedosage && validatedoseform)
        {
            if(addMedicationToDatabase())
            {
                startActivity(new Intent(AdminMedicationPage.this, AdminMedicationPage.class));
            }

        }
    }

    private boolean validateDoseForm()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(doseform.isEmpty())
        {
            doseform1.setError("Field cannot be empty");
            return false;
        }
        else if(!doseform.matches(regex ))
        {
            doseform1.requestFocus();
            doseform1.setError("Dose Form must be of length 1-50");
            return false;
        }
        else
        {
            doseform1.setError(null);
            return true;
        }
    }

    private boolean validateDosage()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(dosage.isEmpty())
        {
            dosage1.setError("Field cannot be empty");
            return false;
        }
        else if(!dosage.matches(regex ))
        {
            dosage1.requestFocus();
            dosage1.setError("Dosage must be of length 1-100");
            return false;
        }
        else
        {
            dosage1.setError(null);
            return true;
        }
    }

    private boolean validateChemName()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(chemname.isEmpty())
        {
            chemname1.setError("Field cannot be empty");
            return false;
        }
        else if(!chemname.matches(regex ))
        {
            chemname1.requestFocus();
            chemname1.setError("Chemical name must be of length 1-100");
            return false;
        }
        else
        {
            chemname1.setError(null);
            return true;
        }
    }

    private boolean validateMedBrand()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,100}+";
        if(medbrand.isEmpty())
        {
            medbrand1.setError("Field cannot be empty");
            return false;
        }
        else if(!medbrand.matches(regex ))
        {
            medbrand1.requestFocus();
            medbrand1.setError("Medical brand must be of length 1-100");
            return false;
        }
        else
        {
            medbrand1.setError(null);
            return true;
        }
    }

    private boolean addMedicationToDatabase()
    {
        String[] field = new String[4];
        field[0] = "medbrand";
        field[1] = "medchem";
        field[2] = "dosage";
        field[3] = "doseform";

        //Creating array for data
        String[] data = new String[4];
        data[0] = medbrand;
        data[1] = chemname;
        data[2] = dosage;
        data[3] = doseform;

        PutData putData = new PutData("http://bopps2130.net/addnewMedication.php", "POST", field, data);
        Log.i("Check", "Before Start put.");
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not add medication.":
                        Toast.makeText(AdminMedicationPage.this, "Could not add medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminMedicationPage.this, "Medication added successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        }
        return false;
    }

    public void ViewMedicationModifyPage(View view)
    {
        startActivity(new Intent(AdminMedicationPage.this, AdminMedicationModifyPage.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminMedicationPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminMedicationPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminMedicationPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminMedicationPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminMedicationPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminMedicationPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminMedicationPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminMedicationPage.this,  AdminMedicationSearchPage.class) );
    }

    public void onClickMedication(View view)
    {
        startActivity(new Intent(AdminMedicationPage.this,  AdminMedicationPage.class) );
    }

    public void onClickSearchKey(View view)
    {
        startActivity(new Intent(AdminMedicationPage.this, AdminMedicationSearchKeyPage.class) );
    }
}