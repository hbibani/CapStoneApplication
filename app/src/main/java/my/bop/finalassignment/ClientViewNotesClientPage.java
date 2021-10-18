package my.bop.finalassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ClientViewNotesClientPage extends AppCompatActivity {

    SharedPreferences pref;
    String mrn;
    String patientnotesid;
    String notesdata;
    EditText notesinfo;
    String admissionid;
    String username;
    String role;
    EditText date_time_in;
    String datetime3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";
        if(role.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_client_view_notes_client_page);
            Intent intent = getIntent();
            patientnotesid = intent.getStringExtra("patientNotesID");
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            notesinfo = (EditText) findViewById(R.id.patientHistoryModify);

            date_time_in=findViewById(R.id.date_time_input);
            date_time_in.setInputType(InputType.TYPE_NULL);
            date_time_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimeDialog(date_time_in);
                }
            });

            fetchNotes();
            notesinfo.setText(notesdata);
            date_time_in.setText(datetime3);
        }
        else
        {
            Toast.makeText(ClientViewNotesClientPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewNotesClientPage.this,  MainActivity.class));
        }

    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewNotesClientPage.this, ClientAddAdmissionPage.class));
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
                    startActivity(new Intent(ClientViewNotesClientPage.this,  MainActivity.class));
                }
                break;
        }
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
                        Toast.makeText(ClientViewNotesClientPage.this, "Could not retrieve note.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewNotesClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ClientViewNotesClientPage.this, "Could not modify notes.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewNotesClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Modified":
                    {
                        Toast.makeText(ClientViewNotesClientPage.this, "Patient note modified succesfully.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ClientViewNotesClientPage.this, "Could not delete medication.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewNotesClientPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Deleted":
                    {
                        Toast.makeText(ClientViewNotesClientPage.this, "Patient note deleted successfully.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }

    public void goBackbutton(View view)
    {
        Intent intent = new Intent(this, ClientAdmissionNotesPage.class);
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
            Intent intent = new Intent(getApplicationContext(), ClientViewNotesClientPage.class);
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
            notesinfo.setError("Notes of length 1-500");
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
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    deletePatientInformation();
                    Intent intent = new Intent(getApplicationContext(), ClientAdmissionNotesPage.class);
                    intent.putExtra("mrn", mrn);
                    intent.putExtra("admissionid", admissionid);
                    startActivity(intent);
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

                new TimePickerDialog(ClientViewNotesClientPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ClientViewNotesClientPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

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
}