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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

public class AdminPatientPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    EditText mrn;
    EditText age;
    EditText weight;
    EditText username1;
    EditText password1;
    Spinner gender;
    String mrnString;
    String ageString;
    String weightString;
    String usernameString;
    String passwordString;
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
            setContentView(R.layout.activity_admin_patient_page);

            gender = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.Gender_Array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gender.setAdapter(adapter);

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
            Toast.makeText(AdminPatientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminPatientPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminPatientPage.this,  MainActivity.class));
                }
                break;
            }
    }

    public void addPatientButton(View view)
    {
        age = findViewById(R.id.editAge);
        weight = findViewById(R.id.editWeight);
        mrn = findViewById(R.id.patient_mrn_number);
        //gender = findViewById(R.id.editGender);
        username1 = findViewById(R.id.username_client);
        password1 = findViewById(R.id.password_client);
        usernameString = username1.getText().toString();
        passwordString = password1.getText().toString();
        mrnString = mrn.getText().toString();
        ageString = age.getText().toString();
        weightString = weight.getText().toString();

        boolean validatemrn = validateMRN();
        boolean validateusername = validateUserName();
        boolean validatepassword = validatePassword();
        boolean validateage = validateAge();
        boolean validateweight = validateWeight();

        gender.findViewById(R.id.spinner1);
        String genderString = String.valueOf(gender.getSelectedItem());
        if( validatemrn && validateusername && validatepassword && validateage && validateweight)
        {
            signUpPatient(usernameString, passwordString, mrnString, genderString, ageString, weightString);
        }

    }

    private boolean validateWeight()
    {
        String regex = "[0-9]{1,5}+";
        if(weightString.isEmpty())
        {
            weight.setError("Field cannot be empty");
            return false;
        }
        else if(!weightString.matches(regex ))
        {
            weight.requestFocus();
            weight.setError("Weight must be numeric and of length 1-5");
            return false;
        }
        else
        {
            weight.setError(null);
            return true;
        }
    }

    private boolean validateAge()
    {
        String regex = "[0-9]{1,4}+";
        if(ageString.isEmpty())
        {
            age.setError("Field cannot be empty");
            return false;
        }
        else if(!ageString.matches(regex ))
        {
            age.requestFocus();
            age.setError("Age must be numeric and of length 1-4");
            return false;
        }
        else
        {
            age.setError(null);
            return true;
        }
    }

    private boolean validateMRN()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,30}+";
        if(mrnString.isEmpty())
        {
            mrn.setError("Field cannot be empty");
            return false;
        }
        else if(!mrnString.matches(regex ))
        {
            mrn.requestFocus();
            mrn.setError("MRN of length 1-30");
            return false;
        }
        else
        {
            mrn.setError(null);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(passwordString.isEmpty())
        {
            password1.setError("Field cannot be empty");
            return false;
        }
        else if(!passwordString.matches(regex ))
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

    private boolean validateUserName()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{8,50}+";
        if(usernameString.isEmpty())
        {
            username1.setError("Field cannot be empty");
            return false;
        }
        else if(!usernameString.matches(regex ))
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


    public void signUpPatient(String usernameString, String passwordString, String mrnString, String genderString, String ageString, String weightString)
    {
        //Start ProgressBar first (Set visibility VISIBLE)
        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[6];
        field[0] = "username";
        field[1] = "password";
        field[2] = "mrn";
        field[3] = "gender";
        field[4] = "age";
        field[5] = "weight";
        //Creating array for data
        String[] data = new String[6];
        data[0] = usernameString;
        data[1] = passwordString;
        data[2] = mrnString;
        data[3] = genderString;
        data[4] = ageString;
        data[5] = weightString;
        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/signup.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "search user":
                        Toast.makeText(AdminPatientPage.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    case "search mrn":
                        Toast.makeText(AdminPatientPage.this, "MRN already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    case "user database":
                        Toast.makeText(AdminPatientPage.this, "Username could not be entered into database.", Toast.LENGTH_SHORT).show();
                        break;
                    case "get userid":
                        Toast.makeText(AdminPatientPage.this, "Could not retrieve the userid.", Toast.LENGTH_SHORT).show();
                        break;
                    case "insert patient":
                        Toast.makeText(AdminPatientPage.this, "Could not insert into database.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminPatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Sign Up Success":
                        Intent intent = new Intent(getApplicationContext(), AdminPatientPage.class);
                        intent.putExtra("mrn", mrnString);
                        startActivity(intent);
                        break;
                }
            }
        }
    }


    public void onClickNavigationAdminPatient(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_search_patient:
                startActivity(new Intent(AdminPatientPage.this, AdminPatientSearchPage.class));
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminPatientPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminPatientPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminPatientPage.this,  MainActivity.class));
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

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminPatientPage.this, AdminPatientSearchPage.class));
    }

    public void onClickPatient(View view)
    {
        startActivity(new Intent(AdminPatientPage.this, AdminPatientPage.class));
    }
}