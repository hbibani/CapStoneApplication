package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
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

public class AdminRoleModifyPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    EditText rolename;
    EditText rolepower;
    String rolenamestring;
    String rolepowerstring;
    String roleidstring;

    String rolenamestring2;
    String rolepowerstring2;

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
            setContentView(R.layout.activity_admin_role_modify_page);
            Intent intent = getIntent();
            rolenamestring = intent.getStringExtra("rolename");
            rolepowerstring = intent.getStringExtra("rolepower");
            roleidstring = intent.getStringExtra("roleid");
            rolename = (EditText) findViewById(R.id.role_name);
            rolepower = (EditText) findViewById(R.id.role_power);
            rolename.setText(rolenamestring);
            rolepower.setText(rolepowerstring);
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
            Toast.makeText(AdminRoleModifyPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminRoleModifyPage.this,  MainActivity.class));
        }
    }


    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin:
                {
                    pref=this.getSharedPreferences("NewsTweetSettings", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminRoleModifyPage.this,  MainActivity.class));
                }
                break;
        }
    }

    public boolean modifyRoleDatabase()
    {
        String[] field = new String[3];
        field[0] = "roleid";
        field[1] = "rolename";
        field[2] = "rolepower";

        //Creating array for data
        String[] data = new String[3];
        data[0] = roleidstring;
        data[1] = rolenamestring2;
        data[2] = rolepowerstring2;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/modifyroledetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.d("Test", result);

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminRoleModifyPage.this, "Could not modify role.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminRoleModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                        Toast.makeText(AdminRoleModifyPage.this, "Role modified successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        }
        return false;
    }

    public void modifyRoleButton(View view)
    {
        rolenamestring2 = rolename.getText().toString();
        rolepowerstring2 = rolepower.getText().toString();

        boolean validaterolename = validateRoleName();
        boolean validaterolepower = validateRolePower();

        if(validaterolename && validaterolepower)
        {
            if(modifyRoleDatabase())
            {
                Intent intent = new Intent(getApplicationContext(), AdminRoleModifyPage.class);
                intent.putExtra("roleid", roleidstring);
                intent.putExtra("rolename", rolenamestring2);
                intent.putExtra("rolepower", rolepowerstring2);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), AdminRoleModifyPage.class);
                intent.putExtra("roleid", roleidstring);
                intent.putExtra("rolename", rolenamestring);
                intent.putExtra("rolepower", rolepowerstring);
                startActivity(intent);
            }
        }
    }


    private boolean validateRoleName()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(rolenamestring2.isEmpty())
        {
            rolename.setError("Field cannot be empty");
            return false;
        }
        else if(!rolenamestring2.matches(regex ))
        {
            rolename.requestFocus();
            rolename.setError("Letters and numbers of length 8-15");
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
        if(rolepowerstring2.isEmpty())
        {
            rolepower.setError("Field cannot be empty");
            return false;
        }
        else if(!rolepowerstring2.matches(regex ))
        {
            rolepower.requestFocus();
            rolepower.setError("Letters and numbers of length 8-15");
            return false;
        }
        else
        {
            rolepower.setError(null);
            return true;
        }

    }

    public boolean deleteRoleDatabase()
    {
        String[] field = new String[1];
        field[0] = "roleid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = roleidstring;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/deleterole.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminRoleModifyPage.this, "Could not delete role.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminRoleModifyPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Deleted":
                        Toast.makeText(AdminRoleModifyPage.this, "Role deleted successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        }

        return false;
    }

    public void deleteRoleButton(View view)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    if(deleteRoleDatabase())
                    {
                        startActivity(new Intent(AdminRoleModifyPage.this, AdminRolePage.class));
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        myQuittingDialogBox.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminRoleModifyPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminRoleModifyPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void goBackButton(View view)
    {
        startActivity(new Intent(AdminRoleModifyPage.this, AdminRolePage.class));
    }
}