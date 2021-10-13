package my.bop.finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import com.example.finalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientHomePage extends AppCompatActivity
{

    SharedPreferences pref;
    String username;
    String type;
    private RecyclerView mRVAdmission;
    private RecyclerView mRVAdmission2;
    private AdapterPatientAdmission mAdapter;
    private AdapterNotification mAdapter2;
    JSONArray patients;
    JSONArray patients2;
    List<DataAdmission> data3;
    List<DataNotification> data2;
    String admissionid;
    String graphid;
    String maxCounter;
    int countdown;
    boolean checkonline;


    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        type = pref.getString("Type",null);
        username = pref.getString("Username", null);
        String s1 = "Role: '3'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_patient_home_page);
            admissionid = " ";

            try
            {
                checkonline = getAdmissionid();

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            if (checkonline)
            {

                getGetGraphID();
                initializeList();
                if (checkAddButtonTime())
                {
                    data2 = new ArrayList<>();
                    DataNotification notification = new DataNotification();
                    notification.notification = "Hello";
                    notification.time = "Time";
                    data2.add(notification);
                    mRVAdmission2 = (RecyclerView)findViewById(R.id.admissionList2);
                    mAdapter2 = new AdapterNotification(PatientHomePage.this, data2);
                    mRVAdmission2.setAdapter(mAdapter2);
                    mRVAdmission2.setLayoutManager(new LinearLayoutManager(PatientHomePage.this));
                }
                else
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            data2 = new ArrayList<>();
                            DataNotification notification = new DataNotification();
                            notification.notification = "Hello";
                            notification.time = "Time";
                            data2.add(notification);
                            mRVAdmission2 = (RecyclerView)findViewById(R.id.admissionList2);
                            mAdapter2 = new AdapterNotification(PatientHomePage.this, data2);
                            mRVAdmission2.setAdapter(mAdapter2);
                            mRVAdmission2.setLayoutManager(new LinearLayoutManager(PatientHomePage.this));
                        }

                    }, countdown);
                }

                createNotificationChannel();
            }
        }
        else
        {
            Toast.makeText(PatientHomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PatientHomePage.this,  MainActivity.class));
        }

    }

    private boolean getAdmissionid() throws JSONException
    {
        String[] field = new String[1];
        field[0] = "username";

        //Creating array for data
        String[] data = new String[1];
        data[0] = username;

        PutData putData = new PutData("http://bopps2130.net/getadmissionforpain.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                patients2 = new JSONArray(result);
                if(!result.equals("No admissions.") && !result.equals("Error: Database connection") && !result.equals("All fields are required"))
                {
                    for(int i = 0; i < patients2.length();i++)
                    {
                        JSONObject names = patients2.getJSONObject(i);
                        admissionid = names.getString("AdmissionID");
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }


    private void initializeList()
    {
        fetchPatientDetails();
    }


    public boolean getGetGraphID()
    {

        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getgraphid.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                if(!result.equals("Not"))
                {
                    graphid = result;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public void fetchPatientDetails()
    {
        data3 = new ArrayList<>();

        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getclientdetails.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "No active admissions.":
                        Toast.makeText(PatientHomePage.this, "No active admissions.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(PatientHomePage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        {
                            try
                            {
                                patients = new JSONArray(result);
                                data3.clear();

                                for (int i = 0; i < patients.length(); i++) {
                                    JSONObject names = patients.getJSONObject(i);
                                    DataAdmission admissionData = new DataAdmission();
                                    admissionData.patientmrn = names.getString("mrn");
                                    admissionData.bed = names.getString("Bed");
                                    admissionData.patientGender = names.getString("PatientGender");
                                    admissionData.admissionid = names.getString("AdmissionID");
                                    admissionData.starttime = names.getString("StartTime");
                                    admissionData.endtime = names.getString("EndTime");
                                    admissionData.concerned = names.getString("Concerned");
                                    admissionData.maxtimecounter = names.getString("PainTimer");
                                    maxCounter = names.getString("PainTimer");
                                    admissionData.graphid = graphid;
                                    data3.add(admissionData);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Setup and Handover data to recyclerview
                            mRVAdmission = (RecyclerView) findViewById(R.id.admissionList);
                            mAdapter = new AdapterPatientAdmission(PatientHomePage.this, data3);
                            mRVAdmission.setAdapter(mAdapter);
                            mRVAdmission.setLayoutManager(new LinearLayoutManager(PatientHomePage.this));
                    }
                }

            }
        }
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "firebaseNotifChannel";
            String description = "Recieve Firebase notification";
            int importance = IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onClickNavigation(MenuItem item) throws JSONException {
        switch (item.getItemId())
        {
            case R.id.action_home_patient:
                startActivity(new Intent(PatientHomePage.this, PatientHomePage.class));
                break;
            case R.id.action_logout_admin:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(PatientHomePage.this,  MainActivity.class));
            }
            break;
        }
    }


    private boolean checkAddButtonTime()
    {
        String[] field = new String[2];
        field[0] = "graphid";
        field[1] = "maxtimer";

        //Creating array for data
        String[] data = new String[2];
        data[0] = graphid;
        data[1] = maxCounter;

        PutData putData = new PutData("http://bopps2130.net/checkpainbuttontime.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "true":
                        return true;
                    default:
                        countdown = (Integer.parseInt(maxCounter)*60*1000) - Integer.parseInt(result)*1000;
                        Log.d("Current Time", Integer.toString(countdown));
                        return false;
                }

            }
        }

        return false;

    }

}

