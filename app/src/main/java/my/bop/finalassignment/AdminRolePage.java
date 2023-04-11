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

public class AdminRolePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    JSONArray roles;
    EditText rolename;
    EditText rolepower;
    String rolenamestring;
    String rolepowerstring;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    JSONArray rolesarray;
    List<DataRole> data1;
    private RecyclerView mRVAdmission;
    private AdapterRole mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        String s1 = "Role: '1'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_role_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            intializeRoleList();
        }
        else
        {
            Toast.makeText(AdminRolePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminRolePage.this,  MainActivity.class));
        }

    }

    public void fetchRoleList()
    {
        data1 = new ArrayList<>();

        FetchData fetchData = new FetchData("http://uphill-leaper.000webhostapp.com/fetchrolelist.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();

                switch (result)
                {
                    case "No roles.":
                        Toast.makeText(AdminRolePage.this, "Could not retrieve roles.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminRolePage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            rolesarray = new JSONArray(result);
                            data1.clear();

                            for(int i = 0; i < rolesarray.length();i++)
                            {
                                JSONObject names = rolesarray.getJSONObject(i);
                                DataRole roledata = new DataRole();
                                roledata.roleID = names.getString("RoleID");
                                roledata.rolename = names.getString("RoleName");
                                roledata.rolepower = names.getString("RolePower");
                                data1.add(roledata);

                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView)findViewById(R.id.admissionList1);
                            mAdapter = new AdapterRole(AdminRolePage.this, data1);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(AdminRolePage.this));
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

   public void intializeRoleList()
   {
       fetchRoleList();
   }


    public void onClickNavigation(MenuItem item)
    {
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminRolePage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminRolePage.this,  MainActivity.class));
                }
                break;
        }
    }

    public boolean addRoleToDatabase()
    {
        String[] field = new String[2];
        field[0] = "rolename";
        field[1] = "rolepower";

        //Creating array for data
        String[] data = new String[2];
        data[0] = rolenamestring;
        data[1] = rolepowerstring;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/addroletodatabase.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {

                String result = putData.getResult();

                switch (result)
                {
                    case "Could not add role.":
                        Toast.makeText(AdminRolePage.this, "Could not add role to database.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminRolePage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                        Toast.makeText(AdminRolePage.this, "Role added successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }

        return false;

    }
    public void addRoleButton(View view)
    {
        rolename = (EditText) findViewById(R.id.role_name);
        rolepower = (EditText) findViewById(R.id.role_power);
        rolenamestring = rolename.getText().toString();
        rolepowerstring = rolepower.getText().toString();

        boolean validaterolename = validateRoleName();
        boolean validaterolepower = validateRolePower();

        if(validaterolename && validaterolepower)
        {
            if(addRoleToDatabase())
            {
                startActivity(new Intent(AdminRolePage.this, AdminRolePage.class));
            }
        }
    }

    private boolean validateRoleName()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(rolenamestring.isEmpty())
        {
            rolename.setError("Field cannot be empty");
            return false;
        }
        else if(!rolenamestring.matches(regex ))
        {
            rolename.requestFocus();
            rolename.setError("Role name must be of length 1-50");
            return false;
        }
        else
        {
            rolename.setError(null);
            return true;
        }
    }

    private boolean validateRolePower()
    {
        String regex = "[0-9]{1,10}+";
        if(rolepowerstring.isEmpty())
        {
            rolepower.setError("Field cannot be empty");
            return false;
        }
        else if(!rolepowerstring.matches(regex ))
        {
            rolepower.requestFocus();
            rolepower.setError("Role power must be numeric and of length 1-10");
            return false;
        }
        else
        {
            rolepower.setError(null);
            return true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:

                startActivity(new Intent(AdminRolePage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminRolePage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminRolePage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminRolePage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminRolePage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminRolePage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminRolePage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void onClickSearch(View view)
    {
        startActivity(new Intent(AdminRolePage.this, AdminRolePage.class));
    }
}