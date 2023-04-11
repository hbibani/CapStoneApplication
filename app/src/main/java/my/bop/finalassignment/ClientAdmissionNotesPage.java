package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientAdmissionNotesPage extends AppCompatActivity
{
    SharedPreferences pref;
    EditText notes2;
    String mrn;
    String admissionid;
    String username;
    String role;
    EditText date_time_in;
    String datetime3;
    JSONArray notesarray;
    private RecyclerView mRVNotes;
    private AdapterNotes mAdapter;
    List<DataNotes> data1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";

        if( role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_admission_notes_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
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
                initializeNotesList();
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
        else if(!notesString.matches(regex))
        {
            notes2.requestFocus();
            notes2.setError("Notes must be of length 1-500");
            return false;
        }
        else
        {
            notes2.setError(null);
            return true;
        }
    }

    public boolean addpatientnotestodatabase(String notesdata)
    {
        String[] field = new String[3];
        field[0] = "admissionid";
        field[1] = "notes";
        field[2] = "datetime";

        //Creating array for data
        String[] data = new String[3];
        data[0] = admissionid;
        data[1] = notesdata;
        data[2] = datetime3;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/addpatientnotestoadmission.php", "POST", field, data);

        if (putData.startPut())
        {

            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch (result)
                {
                    case "None":
                        Toast.makeText(ClientAdmissionNotesPage.this, "Could not add note.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientAdmissionNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Success":
                        Toast.makeText(ClientAdmissionNotesPage.this, "Added note successfully.", Toast.LENGTH_SHORT).show();
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

                new TimePickerDialog(ClientAdmissionNotesPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ClientAdmissionNotesPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickGraph(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientViewGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickMedication(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionMedicationPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNotes(View view)
    {
        Intent intent = new Intent(getApplicationContext(),  ClientAdmissionNotesPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickClinician(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionClinicianPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickFeedback(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientFinaliseFeedbackPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(getApplicationContext(), ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(getApplicationContext(), ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(getApplicationContext(), ClientAddAdmissionPage.class));
                break;
            case R.id.action_assigned_patient_client:
            {
                Intent intent = new Intent(getApplicationContext(), ClientSearchAdmissionPage.class);
                startActivity(intent);
            }
            break;
            case R.id.action_logout_client:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(),  MainActivity.class));
            }
            break;
        }
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

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/getadmissionnoteslist.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                try
                {
                    data1.clear();

                    switch (result)
                    {
                        case "None":
                        {
                            // Setup and Handover data to recyclerview
                            mRVNotes = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter = new AdapterNotes(ClientAdmissionNotesPage.this, data1);
                            mRVNotes.setAdapter(mAdapter);
                            mRVNotes.setLayoutManager(new LinearLayoutManager(ClientAdmissionNotesPage.this));
                        }
                        break;
                        case "Error: Database connection":
                            Toast.makeText(ClientAdmissionNotesPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                        {
                            notesarray = new JSONArray(result);

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
                            mAdapter = new AdapterNotes(ClientAdmissionNotesPage.this, data1);
                            mRVNotes.setAdapter(mAdapter);
                            mRVNotes.setLayoutManager(new LinearLayoutManager(ClientAdmissionNotesPage.this));
                        }
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