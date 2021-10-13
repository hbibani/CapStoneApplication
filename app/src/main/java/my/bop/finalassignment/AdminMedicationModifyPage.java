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

public class AdminMedicationModifyPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    String medicationid;
    String medbrand;
    String chemname;
    String dosage;
    String doseform;
    String medbrand2;
    String chemname2;
    String dosage2;
    String doseform2;
    EditText medbrand1;
    EditText chemname1;
    EditText dosage1;
    EditText doseform1;


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
            setContentView(R.layout.activity_admin_medication_modify_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            initializeValues();
        }
        else
        {
            Toast.makeText(AdminMedicationModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminMedicationModifyPage.this,  MainActivity.class));
        }

    }

    private void initializeValues()
    {
        medbrand1 = (EditText) findViewById(R.id.medication_brand);
        chemname1 = (EditText) findViewById(R.id.medication_chemical_name);
        dosage1 = (EditText) findViewById(R.id.medication_dosage);
        doseform1 = (EditText) findViewById(R.id.medication_dose_form);
        Intent intent = getIntent();
        medicationid = intent.getStringExtra("medicationid");
        medbrand = intent.getStringExtra("medbrand");
        chemname = intent.getStringExtra("chemname");
        dosage = intent.getStringExtra("dosage");
        doseform = intent.getStringExtra("doseform");
        medbrand1.setText(medbrand);
        chemname1.setText(chemname);
        dosage1.setText(dosage);
        doseform1.setText(doseform);
    }

    public void ModifyMedicationButton(View view)
    {
        medbrand2 = medbrand1.getText().toString();
        chemname2 = chemname1.getText().toString();
        dosage2 = dosage1.getText().toString();
        doseform2 =  doseform1.getText().toString();

        boolean validatemedbrand = validateMedBrand();
        boolean validatechemname = validateChemName();
        boolean validatedosage = validateDosage();
        boolean validatedoseform = validateDoseForm();

        if(validatemedbrand && validatechemname && validatedosage && validatedoseform)
        {
            if(modifyMedicationDatabase())
            {
                Intent intent = new Intent(getApplicationContext(), AdminMedicationModifyPage.class);
                intent.putExtra("medicationid", medicationid);
                intent.putExtra("medbrand", medbrand2);
                intent.putExtra("chemname", chemname2 );
                intent.putExtra("dosage", dosage2);
                intent.putExtra("doseform", doseform2);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), AdminMedicationModifyPage.class);
                intent.putExtra("medicationid", medicationid);
                intent.putExtra("medbrand", medbrand);
                intent.putExtra("chemname", chemname);
                intent.putExtra("dosage", dosage);
                intent.putExtra("doseform", doseform);
                startActivity(intent);
            }

        }
    }

    private boolean validateDoseForm()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(doseform2.isEmpty())
        {
            doseform1.setError("Field cannot be empty");
            return false;
        }
        else if(!doseform2.matches(regex ))
        {
            doseform1.requestFocus();
            doseform1.setError("Numbers of length 1-5");
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
        if(dosage2.isEmpty())
        {
            dosage1.setError("Field cannot be empty");
            return false;
        }
        else if(!dosage2.matches(regex ))
        {
            dosage1.requestFocus();
            dosage1.setError("Numbers of length 1-5");
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
        if(chemname2.isEmpty())
        {
            chemname1.setError("Field cannot be empty");
            return false;
        }
        else if(!chemname2.matches(regex ))
        {
            chemname1.requestFocus();
            chemname1.setError("Numbers of length 1-5");
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
        if(medbrand2.isEmpty())
        {
            medbrand1.setError("Field cannot be empty");
            return false;
        }
        else if(!medbrand2.matches(regex ))
        {
            medbrand1.requestFocus();
            medbrand1.setError("Numbers of length 1-5");
            return false;
        }
        else
        {
            medbrand1.setError(null);
            return true;
        }
    }

    private boolean modifyMedicationDatabase()
    {
        String[] field = new String[5];
        field[0] = "medicationid";
        field[1] = "medbrand";
        field[2] = "medchem";
        field[3] = "dosage";
        field[4] = "doseform";

        //Creating array for data
        String[] data = new String[5];
        data[0] = medicationid;
        data[1] = medbrand2;
        data[2] = chemname2;
        data[3] = dosage2;
        data[4] = doseform2;

        Log.i("Medicationid", medicationid);
        PutData putData = new PutData("http://bopps2130.net/modifyMedicationAdmin.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminMedicationModifyPage.this, "Could not modify medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminMedicationModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminMedicationModifyPage.this, "Modified medication successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }

        return false;
    }

    public void DeleteMedicationButton(View view)
    {
        if(deleteMedDatabase())
        {
            startActivity(new Intent(AdminMedicationModifyPage.this, AdminMedicationPage.class));
        }
    }

    private boolean deleteMedDatabase()
    {
        String[] field = new String[1];
        field[0] = "medicationid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationid;

        PutData putData = new PutData("http://bopps2130.net/deleteMedicationFromMeds.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminMedicationModifyPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminMedicationModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                        Toast.makeText(AdminMedicationModifyPage.this, "Deleted medication successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }
        return false;
    }
    
    public void DeleteAllMedicationButton(View view)
    {
        if(deleteMedsFromAll())
        {
            startActivity(new Intent(AdminMedicationModifyPage.this, AdminMedicationPage.class));
        }
    }

    private boolean deleteMedsFromAll()
    {
        String[] field = new String[1];
        field[0] = "medicationid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = medicationid;

        PutData putData = new PutData("http://bopps2130.net/clearmedsfromsystem.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();
                Log.d("Result", result);
                switch (result)
                {
                    case "Could not delete medication.":
                        Toast.makeText(AdminMedicationModifyPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete from admissions.":
                        Toast.makeText(AdminMedicationModifyPage.this, "Could not delete medication from admissions.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminMedicationModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(AdminMedicationModifyPage.this, "Deleted medication successfully.", Toast.LENGTH_SHORT).show();
                        return true;
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

                startActivity(new Intent(AdminMedicationModifyPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminMedicationModifyPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminMedicationModifyPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminMedicationModifyPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminMedicationModifyPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminMedicationModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminMedicationModifyPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminMedicationModifyPage.this,  AdminMedicationSearchPage.class) );
    }

    public void onClickMedication(View view)
    {
        startActivity(new Intent(AdminMedicationModifyPage.this,  AdminMedicationPage.class) );
    }

    public void onClickSearchKey(View view)
    {
        startActivity(new Intent(AdminMedicationModifyPage.this,  AdminMedicationSearchKeyPage.class) );
    }
}