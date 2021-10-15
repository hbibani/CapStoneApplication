package my.bop.finalassignment;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ExampleJobService extends JobService
{
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        sendNotificationToPatient(params);
        return false;
    }

    private void sendNotificationToPatient(JobParameters params)
    {
        SharedPreferences pref;
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        String mrn = pref.getString("mrn",null);
        String admissionid = pref.getString("admissionid", null);

        int millisecondTimer = getMillisecondValue(admissionid);
        millisecondTimer = millisecondTimer*60*1000;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                    String[] field = new String[1];
                    field[0] = "mrn";

                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = mrn;

                    PutData putData = new PutData("http://bopps2130.net/send15pushnotification.php", "POST", field, data);
                    if (putData.startPut())
                    {
                        if (putData.onComplete())
                        {
                            String result = putData.getResult();
                        }
                    }

                }

        }, millisecondTimer);

    }

    private int getMillisecondValue(String admissionid)
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getTimerValue.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                if(!result.equals("Not") && !result.equals("Error: Database connection") && !result.equals("All fields are required"))
                {
                    return Integer.parseInt(result);
                }
            }
        }

        return 15;
    }


    @Override
    public boolean onStopJob(JobParameters params)
    {
        return false;
    }
}
