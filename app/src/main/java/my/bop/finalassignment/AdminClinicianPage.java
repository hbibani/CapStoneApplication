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

public class AdminClinicianPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    String staffnumber;
    String cliniciantype;
    String username;
    String password;
    EditText staffnumber1;
    EditText cliniciantype1;
    EditText username1;
    EditText password1;
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
            setContentView(R.layout.activity_admin_clinician_page);
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
            Toast.makeText(AdminClinicianPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminClinicianPage.this,  MainActivity.class));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminClinicianPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public void addClinicianButton(View view)
    {
        staffnumber1 = (EditText) findViewById(R.id.patient_staff_number);
        cliniciantype1 = (EditText) findViewById(R.id.clinician_type);
        username1 = (EditText) findViewById(R.id.username_clinican);
        password1 = (EditText) findViewById(R.id.password_clincian);

        staffnumber = staffnumber1.getText().toString();
        cliniciantype = cliniciantype1.getText().toString();
        username = username1.getText().toString();
        password = password1.getText().toString();

        boolean validatestaffnumber = validateStaffNumber();
        boolean validatecliniciantype = validateClinicianType();
        boolean validateusername = validateUsername();
        boolean validatepassword = validatePassword();

        if( validatestaffnumber && validatecliniciantype && validateusername && validatepassword)
        {
            if(addClintoDatabase())
            {
                startActivity(new Intent(AdminClinicianPage.this, AdminClinicianPage.class));
            }
        }
    }

    private boolean validateStaffNumber()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(staffnumber.isEmpty())
        {
            staffnumber1.setError("Field cannot be empty");
            return false;
        }
        else if(!staffnumber.matches(regex ))
        {
            staffnumber1.requestFocus();
            staffnumber1.setError("Staff number must be of length 1-50");
            return false;
        }
        else
        {
            staffnumber1.setError(null);
            return true;
        }
    }

    private boolean validateClinicianType()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(cliniciantype.isEmpty())
        {
            cliniciantype1.setError("Field cannot be empty");
            return false;
        }
        else if(!cliniciantype.matches(regex ))
        {
            cliniciantype1.requestFocus();
            cliniciantype1.setError("Type must be of length 1-30");
            return false;
        }
        else
        {
            cliniciantype1.setError(null);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(password.isEmpty())
        {
            password1.setError("Field cannot be empty");
            return false;
        }
        else if(!password.matches(regex ))
        {
            password1.requestFocus();
            password1.setError("Password of length 8-50");
            return false;
        }
        else
        {
            password1.setError(null);
            return true;
        }
    }

    private boolean validateUsername()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(username.isEmpty())
        {
            username1.setError("Field cannot be empty");
            return false;
        }
        else if(!username.matches(regex ))
        {
            username1.requestFocus();
            username1.setError("Username of length 8-50");
            return false;
        }
        else
        {
            username1.setError(null);
            return true;
        }

    }

    private boolean addClintoDatabase()
    {
        //Start ProgressBar first (Set visibility VISIBLE)
        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[4];
        field[0] = "username";
        field[1] = "password";
        field[2] = "staffnumber";
        field[3] = "clintype";

        //Creating array for data
        String[] data = new String[4];
        data[0] = username;
        data[1] = password;
        data[2] = staffnumber;
        data[3] = cliniciantype;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/addnewclinician.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Toast.makeText(AdminClinicianPage.this, result, Toast.LENGTH_SHORT).show();

                switch(result)
                {
                    case "Staff number in use.":
                        Toast.makeText(AdminClinicianPage.this, "Staff number in use.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Username in use.":
                        Toast.makeText(AdminClinicianPage.this, "Username in use.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Insert into user failed.":
                        Toast.makeText(AdminClinicianPage.this, "Insert into user failed.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not get user name.":
                        Toast.makeText(AdminClinicianPage.this, "Could not retrieve username.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not insert clinician.":
                        Toast.makeText(AdminClinicianPage.this, "Could not insert clinician.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminClinicianPage.this, "Added clinician successfully.", Toast.LENGTH_SHORT).show();
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

                startActivity(new Intent(AdminClinicianPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminClinicianPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminClinicianPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminClinicianPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickclinician(View view)
    {
        startActivity(new Intent(AdminClinicianPage.this, AdminClinicianPage.class));
    }

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminClinicianPage.this, AdminClinicianSearchPage.class));
    }
}