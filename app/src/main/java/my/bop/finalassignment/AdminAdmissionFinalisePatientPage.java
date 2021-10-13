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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminAdmissionFinalisePatientPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    String username;
    String role;
    String admissionid;
    String mrn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView heading;
    TextView heading1;
    Switch switch1;
    Switch switch2;
    Switch switch3;
    Switch switch4;
    Button deletebutton;
    Button addbutton;
    Button modifybutton;
    JSONArray feedbackarray;
    String q1;
    String q2;
    String q3;
    String q4;
    TextView up1;
    TextView up2;
    TextView up3;
    TextView up4;
    boolean feedbackcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type", null);
        username = pref.getString("Username", null);

        String s1 = "Role: '1'";
        if(role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_admission_finalise_patient_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            addbutton = (Button) findViewById(R.id.addfeedback);
            modifybutton = (Button) findViewById(R.id.modifyfeedback);
            deletebutton = (Button) findViewById(R.id.deletefeedback);
            heading = (TextView) findViewById(R.id.textView);
            heading1 = (TextView) findViewById(R.id.textView2);
            switch1 = (Switch) findViewById(R.id.question1);
            switch2 = (Switch) findViewById(R.id.question2);
            switch3 = (Switch) findViewById(R.id.question3);
            switch4 = (Switch) findViewById(R.id.question4);
            up1 = (TextView) findViewById(R.id.switchTitle);
            up2 = (TextView) findViewById(R.id.switchTitle2);
            up3 = (TextView) findViewById(R.id.switchTitle3);
            up4 = (TextView) findViewById(R.id.switchTitle4);

            if(getFeedBackValues())
            {
                if(feedbackcheck)
                {
                    heading.setText("Feedback: Entered");
                    addbutton.setVisibility(View.INVISIBLE);
                    addbutton.getLayoutParams().height = 0;
                    if(q1.equals("1"))
                    {
                        switch1.setChecked(true);
                    }

                    if(q2.equals("1"))
                    {
                        switch2.setChecked(true);
                    }

                    if(q3.equals("1"))
                    {
                        switch3.setChecked(true);
                    }

                    if(q4.equals("1"))
                    {
                        switch4.setChecked(true);
                    }
                }
                else
                {
                    heading.setText("Feedback: Not Entered");
                    modifybutton.setVisibility(View.INVISIBLE);
                    deletebutton.setVisibility(View.INVISIBLE);
                    modifybutton.getLayoutParams().height = 0;
                    deletebutton.getLayoutParams().height = 0;
                }
            }
        }
        else
        {
            Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAdmissionFinalisePatientPage.this,  MainActivity.class));
        }
    }

    public boolean getFeedBackValues()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getadmissionvalues.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                    {
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Feedback not entered.", Toast.LENGTH_SHORT).show();
                        q1 = "0";
                        q2 = "0";
                        q3 = "0";
                        q4 = "0";
                        feedbackcheck = false;
                        return true;
                    }

                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        feedbackcheck = false;
                        return false;
                    default:
                        try
                        {
                            feedbackarray = new JSONArray(result);
                            for(int i = 0; i < feedbackarray.length();i++)
                            {

                                JSONObject names =  feedbackarray.getJSONObject(i);
                                q1 = names.getString("Question1");
                                q2 = names.getString("Question2");
                                q3 = names.getString("Question3");
                                q4 = names.getString("Question4");
                                feedbackcheck = true;
                            }
                            return true;
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                }
            }
        }

        feedbackcheck = false;
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_patient_admin:

                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionFinalisePatientPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    public void onClickGraph(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);

    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionPatientNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);


    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void addfeedBack (View view)
    {
        String q1 = "0", q2 = "0", q3 = "0", q4 = "0";

        if (switch1.isChecked())
        {
            q1 = "1";
        }

        if (switch2.isChecked())
        {
            q2 = "1";
        }
        if (switch3.isChecked())
        {
            q3 = "1";
        }

        if (switch4.isChecked())
        {
            q4 = "1";
        }

        if (addFeedbackToDatabase(q1, q2, q3, q4))
        {
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    public boolean addFeedbackToDatabase(String q1, String q2, String q3, String q4)
    {
        String[] field = new String[5];
        field[0] = "admissionid";
        field[1] = "q1";
        field[2] = "q2";
        field[3] = "q3";
        field[4] = "q4";

        //Creating array for data
        String[] data = new String[5];
        data[0] = admissionid;
        data[1] = q1;
        data[2] = q2;
        data[3] = q3;
        data[4] = q4;

        PutData putData = new PutData("http://bopps2130.net/addfeedbacktoadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Feedback already added.":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Feedback already added.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Could not add feedback.":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Could not add feedback.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Added":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Feedback added successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }

        return false;
    }

    public void modifyFeedBack(View view)
    {
        String q1 = "0", q2 = "0", q3 = "0", q4 = "0";

        if (switch1.isChecked())
        {
            q1 = "1";
        }

        if (switch2.isChecked())
        {
            q2 = "1";
        }
        if (switch3.isChecked())
        {
            q3 = "1";
        }

        if (switch4.isChecked())
        {
            q4 = "1";
        }

        if (modifyFeedbackToDatabase(q1, q2, q3, q4))
        {
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }


    public boolean modifyFeedbackToDatabase(String q1, String q2, String q3, String q4)
    {
        String[] field = new String[5];
        field[0] = "admissionid";
        field[1] = "q1";
        field[2] = "q2";
        field[3] = "q3";
        field[4] = "q4";

        //Creating array for data
        String[] data = new String[5];
        data[0] = admissionid;
        data[1] = q1;
        data[2] = q2;
        data[3] = q3;
        data[4] = q4;

        PutData putData = new PutData("http://bopps2130.net/modifyfeedbackadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Could not modify feedback.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                    {
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Modified feedback successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void deleteFeedBack(View view)
    {
        deletefeedback();
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);


    }

    public boolean deletefeedback()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/deletefeedback.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Could not delete feedback.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Feedback deleted successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }

        return false;
    }


    public void deleteAdmission(View view)
    {
        if(deleteAdmissionDatabase())
        {
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionPage.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionFinalisePatientPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    private boolean deleteAdmissionDatabase()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/deleteadmissionadmin.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Could not delete admission.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Deleted":
                        Toast.makeText(AdminAdmissionFinalisePatientPage.this, "Admission deleted successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        }

        return false;

    }

    public void Back(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }
}