package my.bop.finalassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalassignment.R;

public class PatientPainPage extends AppCompatActivity
{
    SharedPreferences pref;
    SeekBar painInput;
    String username;
    String type;
    String mrn;
    String admissionid;
    String graphid;
    Button button;
    String maxTimer;
    TextView instruction;
    TextView painscoreinstruction;
    ImageView painimage;
    int countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        String s = pref.getString("Type",null);
        type = pref.getString("Type",null);
        Log.d("Editor", type);
        username = pref.getString("Username", null);
        Log.d("Editor", username);
        mrn = pref.getString("mrn", null);
        Log.d("Editor", mrn);
        String s1 = "Role: '3'";
        if(s.equals(s1) ==  true)
        {
            setContentView(R.layout.activity_patient_pain_page);
            Intent intent = getIntent();
            admissionid = intent.getStringExtra("admissionid");
            graphid = intent.getStringExtra("graphid");
            maxTimer = intent.getStringExtra("maxtimernumber");
            SharedPreferences.Editor editor1 = pref.edit();
            editor1.putString("admissionid", admissionid);
            editor1.apply();
            painInput = (SeekBar) findViewById(R.id.seekBar);
            instruction = (TextView) findViewById(R.id.instructions);
            instruction.setText("Please wait "  + maxTimer + " minutes for next input.");
            painscoreinstruction = (TextView) findViewById(R.id.painscorename);
            button = (Button) findViewById(R.id.button1);
            painimage = (ImageView) findViewById(R.id.imagepain);

            if(checkAddButtonTime())
            {
                button.setVisibility(View.VISIBLE);
                painscoreinstruction.setVisibility(View.VISIBLE);
                instruction.setVisibility(View.INVISIBLE);
                painInput.setVisibility(View.VISIBLE);
                painimage.setVisibility(View.VISIBLE);
            }
            else
            {
                painscoreinstruction.setVisibility(View.INVISIBLE);
                instruction.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                painInput.setVisibility(View.INVISIBLE);
                painimage.setVisibility(View.INVISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        button.setVisibility(View.VISIBLE);
                        painscoreinstruction.setVisibility(View.VISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        painInput.setVisibility(View.VISIBLE);
                        painimage.setVisibility(View.VISIBLE);
                    }

                }, countdown);

            }
        }
        else
        {
            Toast.makeText(PatientPainPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PatientPainPage.this,  MainActivity.class));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_patient:
                startActivity(new Intent(PatientPainPage.this, PatientHomePage.class));
                break;
            case R.id.action_logout_admin:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(PatientPainPage.this,  MainActivity.class));
            }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void addPatientPainButton(View view)
    {
        String seekBarValue = Integer.toString(painInput.getProgress());

        if(addPainScoreToDataBase(seekBarValue))
        {
            ComponentName componentName = new ComponentName(this, ExampleJobService.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);

            if(resultCode == JobScheduler.RESULT_SUCCESS){

            }
        }

        Intent intent = new Intent(getApplicationContext(), PatientPainPage.class);
        intent.putExtra("admissionid",admissionid);
        intent.putExtra("graphid", graphid);
        intent.putExtra("maxtimernumber", maxTimer);
        startActivity(intent);
    }


    private boolean addPainScoreToDataBase(String painscore)
    {
        String[] field = new String[3];
        field[0] = "admissionid";
        field[1] = "painscore";
        field[2] = "graphid";

        //Creating array for data
        String[] data = new String[3];
        data[0] = admissionid;
        data[1] = painscore;
        data[2] = graphid;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/inputpainscore.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "Success":
                        return true;
                    case "Could not add pain score.":
                        Toast.makeText(PatientPainPage.this, "Could not add pain score.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(PatientPainPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        return false;
                }
            }
        }

        return false;
    }


    private boolean checkAddButtonTime()
    {
        String[] field = new String[2];
        field[0] = "graphid";
        field[1] = "maxtimer";

        //Creating array for data
        String[] data = new String[2];
        data[0] = graphid;
        data[1] = maxTimer;

        PutData putData = new PutData("http://uphill-leaper.000webhostapp.com/checkpainbuttontime.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "true":
                        return true;
                    case "Error: Database connection":
                        Toast.makeText(PatientPainPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        countdown = (Integer.parseInt(maxTimer)*60*1000) - Integer.parseInt(result)*1000;
                        return false;
                }
            }
        }

        return false;

    }
}