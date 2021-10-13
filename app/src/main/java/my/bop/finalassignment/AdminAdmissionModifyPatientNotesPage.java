package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AdminAdmissionModifyPatientNotesPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    String mrn;
    String patientnotesid;
    String notesdata;
    EditText notesinfo;
    String admissionid;
    String username;
    String role;
    String bednumber2;
    String concerned;
    String gender;
    EditText date_time_in;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";
        if(role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_admission_modify_patient_notes_page);
            Intent intent = getIntent();
            patientnotesid = intent.getStringExtra("patientNotesID");
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            notesinfo = (EditText) findViewById(R.id.patientHistoryModify);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            date_time_in.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showDateTimeDialog(date_time_in);
                }
            });

            fetchNotes();
            notesinfo.setText(notesdata);
            date_time_in.setText(datetime3);
        }
        else
        {
            Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this,  MainActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionModifyPatientNotesPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fetchNotes()
    {
        String[] field = new String[1];
        field[0] = "patientnotesid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = patientnotesid;

        PutData putData = new PutData("http://bopps2130.net/getsinglenotes.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Could not retrieve note.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            JSONArray array = new JSONArray(result);
                            JSONObject names = array.getJSONObject(0);
                            notesdata = names.getString("Notes");
                            datetime3 = names.getString("Time");
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

    public void modifyPatientInformation(String notes)
    {
        String[] field = new String[3];
        field[0] = "patientnotesid";
        field[1] = "notes";
        field[2] = "datetime";

        //Creating array for data
        String[] data = new String[3];
        data[0] = patientnotesid;
        data[1] = notes;
        data[2] = datetime3;

        PutData putData = new PutData("http://bopps2130.net/modifypatientnotes.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Could not modify notes.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Modified":
                    {
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Patient note modified succesfully.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        }

    }

    public void deletePatientInformation()
    {
        String[] field = new String[1];
        field[0] = "patientnotesid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = patientnotesid;

        PutData putData = new PutData("http://bopps2130.net/deletenotespatient.php", "POST", field, data);

        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Patient note deleted successfully.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }

    public void goBackbutton(View view)
    {
        Intent intent = new Intent(this,  AdminAdmissionPatientNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void ModifyButton(View view)
    {
        String notes;
        notes = notesinfo.getText().toString();
        datetime3 = date_time_in.getText().toString();
        boolean validatenotes = validateNotes(notes);

        if(validatenotes)
        {
            modifyPatientInformation(notes);
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionModifyPatientNotesPage.class);
            intent.putExtra("patientNotesID", patientnotesid);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    private boolean validateNotes(String notesString)
    {
        Log.d("NotesString", notesString);
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,500}+";
        if(notesString.isEmpty())
        {
            notesinfo.setError("Field cannot be empty");
            return false;
        }
        else if(!notesString.matches(regex ))
        {
            notesinfo.requestFocus();
            notesinfo.setError("Notes must be of length 1-500");
            return false;
        }
        else
        {
            notesinfo.setError(null);
            return true;
        }
    }

    public void DeleteNotes(View view)
    {
        deletePatientInformation();
        Intent intent = new Intent(getApplicationContext(), AdminAdmissionPatientNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        Toast.makeText(AdminAdmissionModifyPatientNotesPage.this, "Successful.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void showDateTimeDialog(final EditText date_time_in)
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime3 = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(AdminAdmissionModifyPatientNotesPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AdminAdmissionModifyPatientNotesPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

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
        Intent intent = new Intent(getApplicationContext(),  AdminAdmissionPatientNotesPage.class);
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

}