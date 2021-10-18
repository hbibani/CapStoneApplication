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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.finalassignment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class AdminAdmissionMedicationPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    SharedPreferences pref;
    String mrn;
    String admissionid;
    String username;
    String role;
    ArrayList<String> medicationSingleList;
    ArrayAdapter<String> medicationAdapater;
    EditText date_time_in;
    String datetime3;
    JSONArray med;
    JSONArray med2;
    private RecyclerView mRVMedication;
    private AdapterAdmissionMedicationAdmin mAdapter;
    List<DataMed> data1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText doseamount;
    String medicationID;
    MaterialAutoCompleteTextView autoedittext;
    String doseAmountString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '1'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_admin_admission_medication_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            doseamount = findViewById(R.id.amount_dose);
            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            datetime3 = "";
            autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
            initializeMedicationList();
            fetchPatientMedList();


            medicationID = " ";

            autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    try
                    {
                        int positionactual = 0;
                        String selected = (String) parent.getItemAtPosition(position);
                        for(int i = 0; i < medicationSingleList.size(); i++)
                        {
                            if(medicationSingleList.get(i).equals(selected))
                            {
                                positionactual = i;
                                break;
                            }
                        }
                        JSONObject names = med2.getJSONObject(positionactual);
                        medicationID = names.getString("MedicationID");
                        Log.d("MedicationID", Integer.toString(positionactual));

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });



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

                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminPatientPage.class));
                break;
            case R.id.action_medication_admin:
                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminMedicationPage.class));
                break;
            case R.id.action_clinician_admin:
                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminClinicianPage.class));
                break;
            case R.id.action_admission_admin:
                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminAdmissionPage.class));
                break;
            case R.id.action_role_admin:
                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminRolePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AdminAdmissionMedicationPage.this, AdminHomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(AdminAdmissionMedicationPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void initializeMedicationList()
    {
        medicationSingleList = new ArrayList<String>();
        fetchSingleMedList();
        medicationAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicationSingleList);
        autoedittext.setAdapter(medicationAdapater);

    }

    private void fetchSingleMedList()
    {
        FetchData fetchData = new FetchData("http://bopps2130.net/fetchAllMeds.php");
        if (fetchData.startFetch())
        {
            if (fetchData.onComplete())
            {
                String result = fetchData.getResult();


                switch (result)
                {
                    case "None":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Could not fetch medication list for search.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            med2 = new JSONArray(result);
                            medicationSingleList.clear();
                            for (int i = 0; i < med2.length(); i++) {
                                JSONObject names = med2.getJSONObject(i);
                                String medbrand = names.getString("MedBrand");
                                medbrand = medbrand + " "  + names.getString("MedChemName");
                                medbrand = medbrand + " " + names.getString("Dosage");
                                medbrand = medbrand + " " + names.getString("DoseForm");
                                medicationSingleList.add(medbrand);
                            }

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


    private void fetchPatientMedList()
    {
        data1 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getpatientmedication.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            med = new JSONArray(result);
                            data1.clear();
                            for(int i = 0; i < med.length();i++)
                            {
                                JSONObject names = med.getJSONObject(i);
                                DataMed medData = new DataMed();
                                medData.patientmrn = mrn;
                                medData.admissionid = admissionid;
                                medData.medicationStayId = names.getString("MedicationStayID");
                                medData.medID = names.getString("MedicationID");
                                medData.brandName = names.getString("MedBrand");
                                medData.chemName = names.getString("MedChemName");
                                medData.doseForm = names.getString("DoseForm");
                                medData.dosageName = names.getString("Dosage");
                                medData.doseAmount = names.getString("Amount");
                                medData.time = names.getString("Time");
                                data1.add(medData);
                            }

                            // Setup and Handover data to recyclerview
                            mRVMedication = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter = new AdapterAdmissionMedicationAdmin(AdminAdmissionMedicationPage.this, data1);
                            mRVMedication.setAdapter(mAdapter);
                            mRVMedication.setLayoutManager(new LinearLayoutManager(AdminAdmissionMedicationPage.this));
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

    public void addMedication(View view) throws JSONException
    {
        doseAmountString = doseamount.getText().toString();
        boolean validatedate = validateDate();
        boolean validatedoseform = validateDoseAmount();
        boolean validatemedication = validateMedication();

        if(validatedate && validatedoseform && validatemedication)
        {
            addMedicationToPatient(medicationID);
            Intent intent = new Intent(getApplicationContext(), AdminAdmissionMedicationPage.class);
            intent.putExtra("mrn", mrn);
            intent.putExtra("admissionid", admissionid);
            startActivity(intent);
        }
    }

    private boolean validateMedication()
    {
        if(autoedittext.getText().toString().isEmpty())
        {
            autoedittext.setError("Field cannot be empty");
            return false;
        }
        else
        {
            autoedittext.requestFocus();
            autoedittext.setError(null);
            return true;
        }
    }

    private boolean validateDoseAmount()
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(doseAmountString.isEmpty())
        {
            doseamount.setError("Field cannot be empty");
            return false;
        }
        else if(!doseAmountString.matches(regex ))
        {
            doseamount.requestFocus();
            doseamount.setError("Dose amount of length 1-50");
            return false;
        }
        else
        {
            doseamount.setError(null);
            return true;
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

    public void addMedicationToPatient(String medicationID)
    {
        String[] field = new String[4];
        field[0] = "admissionid";
        field[1] = "medicationid";
        field[2] = "datetime";
        field[3] = "doseamount";

        //Creating array for data
        String[] data = new String[4];
        data[0] = admissionid;
        data[1] = medicationID;
        data[2] = datetime3;
        data[3] = doseAmountString;

        PutData putData = new PutData("http://bopps2130.net/addmedicationtoadmission.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "Not":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Could not add medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Success":
                        Toast.makeText(AdminAdmissionMedicationPage.this, "Medication added successfully.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

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

                new TimePickerDialog(AdminAdmissionMedicationPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AdminAdmissionMedicationPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}