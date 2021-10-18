package my.bop.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminAdmissionPatientNotesPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SharedPreferences pref;
    EditText notes2;
    String mrn;
    String admissionid;
    String username;
    String role;
    String bednumber2;
    String concerned;
    String gender;
    EditText date_time_in;
    String datetime3;
    JSONArray notesarray;
    private RecyclerView mRVNotes;
    private AdapterAdmissionNotes mAdapter;
    List<DataNotes> data1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String graphid;
    String datetime;
    String datetime1;
    String status;
    String painTypeString;
    String painRegionString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";

        if( role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_admin_admission_patient_notes_page);
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
            date_time_in=findViewById(R.id.date_time_input2);
            date_time_in.setInputType(InputType.TYPE_NULL);
            datetime3 = "";
            initializeNotesList();

            date_time_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog(date_time_in);
                }
            });

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_patient_admin:

                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionPatientNotesPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public void AddClientNotes(View view)
    {
        notes2 = (EditText) findViewById(R.id.notestoadd);
        String notesdata = notes2.getText().toString();

        boolean validatedate = validateDate();
        boolean validatenotes = validateNotes(notesdata);

        if(validatedate && validatenotes)
        {
            if(addpatientnotestodatabase(notesdata))
            {
                Intent intent = new Intent(getApplicationContext(), AdminAdmissionPatientNotesPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                startActivity(intent);
            }
        }
    }

    private boolean validateDate()
    {
        if(datetime3.isEmpty())
        {
            date_time_in.setError("Field cannot be empty");
            return false;
        }
        else
        {
            date_time_in.requestFocus();
            date_time_in.setError(null);
            return true;
        }
    }

    private boolean validateNotes(String notesString)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,500}+";
        if(notesString.isEmpty())
        {
            notes2.setError("Field cannot be empty");
            return false;
        }
        else if(!notesString.matches(regex ))
        {
            notes2.requestFocus();
            notes2.setError("Notes of length 1-500");
            return false;
        }
        else
        {
            notes2.setError(null);
            return true;
        }
    }

    public boolean addpatientnotestodatabase(String notesdata) {
        String[] field = new String[3];
        field[0] = "admissionid";
        field[1] = "notes";
        field[2] = "datetime";


        //Creating array for data
        String[] data = new String[3];
        data[0] = admissionid;
        data[1] = notesdata;
        data[2] = datetime3;

        PutData putData = new PutData("http://bopps2130.net/addpatientnotestoadmission.php", "POST", field, data);

        if (putData.startPut()) {

            if (putData.onComplete()) {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionPatientNotesPage.this, "Could not add note.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionPatientNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                        Toast.makeText(AdminAdmissionPatientNotesPage.this, "Added note successfully.", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        }

        return false;
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

                new TimePickerDialog(AdminAdmissionPatientNotesPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AdminAdmissionPatientNotesPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

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

    void initializeNotesList()
    {
        data1 = new ArrayList<>();
        fetchNotesList();
    }

    public void fetchNotesList()
    {
        String[] field = new String[1];
        field[0] = "admissionid";
        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getadmissionnoteslist.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "None":
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionPatientNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            notesarray = new JSONArray(result);
                            data1.clear();
                            Log.d("Result", result);

                            for(int i = 0; i <notesarray.length() ; i++)
                            {
                                JSONObject names = notesarray.getJSONObject(i);
                                DataNotes notesData = new DataNotes();
                                notesData.patientmrn = mrn;
                                notesData.admissionid = admissionid;
                                notesData.patientnotesid = names.getString("PatientNotesID");
                                notesData.thenotes = names.getString("Notes");
                                notesData.notetime = names.getString("Time");
                                data1.add(notesData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVNotes = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter = new AdapterAdmissionNotes(AdminAdmissionPatientNotesPage.this, data1);
                            mRVNotes.setAdapter(mAdapter);
                            mRVNotes.setLayoutManager(new LinearLayoutManager(AdminAdmissionPatientNotesPage.this));

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
}