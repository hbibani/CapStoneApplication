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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminClinicianModifyPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    String username;
    String password;
    String username2;
    String password2;
    String userID;
    String clinicianid;
    String staffnumber;
    String cliniciantype;
    String staffnumber2;
    String cliniciantype2;
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
            setContentView(R.layout.activity_admin_clinician_modify_page);
            Intent intent = getIntent();
            clinicianid = intent.getStringExtra("clinicianid");
            userID = intent.getStringExtra("userid");
            staffnumber = intent.getStringExtra("staffnumber");
            cliniciantype = intent.getStringExtra("cliniciantype");

            if(!getUsernameAndPassword())
            {
                Toast.makeText(AdminClinicianModifyPage.this, "Could not get username and password.", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), AdminClinicianPage.class);
                startActivity(intent1);
            }

            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            staffnumber1 = (EditText) findViewById(R.id.patient_staff_number);
            cliniciantype1 = (EditText) findViewById(R.id.clinician_type);
            username1 = (EditText) findViewById(R.id.username_clinican);
            password1 = (EditText) findViewById(R.id.password_clincian);

            staffnumber1.setText(staffnumber);
            cliniciantype1.setText(cliniciantype);
            username1.setText(username);
            password1.setText(password);


        }
        else
        {
            Toast.makeText(AdminClinicianModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminClinicianModifyPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminClinicianModifyPage.this,  MainActivity.class));
                }
                break;
        }
    }


    public boolean getUsernameAndPassword()
    {
        String[] field = new String[1];
        field[0] = "userid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = userID;

        PutData putData = new PutData("http://bopps2130.net/retusernameandpassword.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not retrieve username and password.":
                        Toast.makeText(AdminClinicianModifyPage.this, "No username or password for that account.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            JSONArray array = new JSONArray(result);
                            JSONObject names = array.getJSONObject(0);
                            username = names.getString("Username");
                            password = names.getString("Password");
                            return true;

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    public void ModifyClinicianButton(View view)
    {
        staffnumber2 = staffnumber1.getText().toString();
        cliniciantype2 = cliniciantype1.getText().toString();

        boolean validatestaffnumber = validateStaffNumber();
        boolean validatecliniciantype = validateClinicianType();

        if(validatestaffnumber && validatecliniciantype)
        {
            if(modifyClinDatabase())
            {
                Intent intent = new Intent(getApplicationContext(), AdminClinicianModifyPage.class);
                intent.putExtra("clincianid", clinicianid);
                intent.putExtra("userid", userID);
                intent.putExtra("staffnumber", staffnumber2);
                intent.putExtra("cliniciantype", cliniciantype2);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), AdminClinicianModifyPage.class);
                intent.putExtra("clincianid", clinicianid);
                intent.putExtra("userid", userID);
                intent.putExtra("staffnumber", staffnumber);
                intent.putExtra("cliniciantype", cliniciantype);
                startActivity(intent);
            }

        }
    }

    private boolean validateStaffNumber()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(staffnumber2.isEmpty())
        {
            staffnumber1.setError("Field cannot be empty");
            return false;
        }
        else if(!staffnumber2.matches(regex ))
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
        if(cliniciantype2.isEmpty())
        {
            cliniciantype1.setError("Field cannot be empty");
            return false;
        }
        else if(!cliniciantype2.matches(regex ))
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
        if(password2.isEmpty())
        {
            password1.setError("Field cannot be empty");
            return false;
        }
        else if(!password2.matches(regex ))
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
        if(username2.isEmpty())
        {
            username1.setError("Field cannot be empty");
            return false;
        }
        else if(!username2.matches(regex ))
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

    private void updatePasswordAndUsername()
    {
        String[] field = new String[3];
        field[0] = "username";
        field[1] = "password";
        field[2] = "userid";

        //Creating array for data
        String[] data = new String[3];
        data[0] = username2;
        data[1] = password2;
        data[2] = userID;

        PutData putData = new PutData("http://bopps2130.net/updateclinusernamepassword.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not update username and password.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not update username and password.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                    {
                        Toast.makeText(AdminClinicianModifyPage.this, "Updated username and password successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    private boolean modifyClinDatabase()
    {
        String[] field = new String[3];
        field[0] = "staffnumber";
        field[1] = "cliniciantype";
        field[2] = "userid";
        //Creating array for data

        String[] data = new String[3];
        data[0] = staffnumber2;
        data[1] = cliniciantype2;
        data[2] = userID;

        PutData putData = new PutData("http://bopps2130.net/updatecliniciandetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Could not update clinician information.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not update clinician information.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                    {
                        Toast.makeText(AdminClinicianModifyPage.this, "Clinician details updated successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void DeleteClinicianButton(View view)
    {
        if(deleteClinician())
        {
            Intent intent = new Intent(getApplicationContext(),  AdminClinicianPage.class);
            startActivity(intent);
        }
    }

    private boolean deleteClinician()
    {
        String[] field = new String[1];
        field[0] = "userid";

        //Creating array for data

        String[] data = new String[1];
        data[0] = userID;


        PutData putData = new PutData("http://bopps2130.net/deleteclinician.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();

                switch (result)
                {
                    case "Could not delete from clinician.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not delete from clinician.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete from user.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not delete from user.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(AdminClinicianModifyPage.this, "Clinician deleted successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void GoBackButton(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianSearchPage.class));
    }

    public void DeleteAllClinicianButton(View view)
    {
        if(deleteAllClinician())
        {
            Intent intent = new Intent(getApplicationContext(), AdminClinicianPage.class);
            startActivity(intent);
        }
    }

    private boolean deleteAllClinician()
    {
        String[] field = new String[2];
        field[0] = "userid";
        field[1] = "staffnumber";

        //Creating array for data

        String[] data = new String[2];
        data[0] = userID;
        data[1] = staffnumber;


        PutData putData = new PutData("http://bopps2130.net/deleteAllclinician.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();

                switch (result)
                {
                    case "Could not delete from admission.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not delete from admission.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete from clinician.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not delete from clinician.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Could not delete from user.":
                        Toast.makeText(AdminClinicianModifyPage.this, "Could not delete from user.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminClinicianModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(AdminClinicianModifyPage.this, "Clinician deleted successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }

            }
        }

        return false;

    }

    public void UpdateLoginDetails(View view)
    {
        username2 = username1.getText().toString();
        password2 = password1.getText().toString();
        boolean validateusername = validateUsername();
        boolean validatepassword = validatePassword();

        if(validateusername && validatepassword)
        {
            updatePasswordAndUsername();
            Intent intent = new Intent(getApplicationContext(), AdminClinicianModifyPage.class);
            intent.putExtra("clincianid", clinicianid);
            intent.putExtra("userid", userID);
            intent.putExtra("staffnumber", staffnumber);
            intent.putExtra("cliniciantype", cliniciantype);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminClinicianModifyPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminClinicianModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminClinicianModifyPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void onClickclinician(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianPage.class));
    }

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminClinicianModifyPage.this, AdminClinicianSearchPage.class));
    }
}