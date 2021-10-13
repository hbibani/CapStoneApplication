package my.bop.finalassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalassignment.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClientViewGraphPage extends AppCompatActivity
{
    SharedPreferences pref;
    String mrn;
    String admissionid;
    String username;
    String role;
    String bednumber2;
    String concerned;
    String gender;
    SeekBar painInput;
    String graphid;
    List<DataAUC> data1;
    private RecyclerView mRVAdmission;
    private AdapterGraph mAdapter;
    EditText date_time_in2;
    String datetime2;
    JSONArray med;
    JSONArray medications;
    ArrayList<Entry> dataVals1;
    ArrayList<Entry> dataVals2;
    LineChart mpLineChart;
    ArrayList<timeObjectGraph> thePain;
    ArrayList<timeObjectGraph> theMedication;
    JSONArray admissionarray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences("NewsTweetSettings", 0);
        role = pref.getString("Type", null);
        username = pref.getString("Username", null);
        String s1 = "Role: '2'";

        if (role.equals(s1) == true)
        {
            setContentView(R.layout.activity_client_view_graph_page);
            Intent intent = getIntent();
            mrn = intent.getStringExtra("mrn");
            admissionid = intent.getStringExtra("admissionid");
            SharedPreferences.Editor editor1 = pref.edit();
            editor1.putString("mrn", mrn);
            editor1.putString("admissionid", admissionid);
            editor1.apply();

            if(getNeededValues())
            {
                dataVals1 = new ArrayList<Entry>();
                dataVals2 = new ArrayList<Entry>();
                dataValues1();
                dataValues2();
                setDataValEntries();
                mpLineChart = findViewById(R.id.line_chart);
                LineDataSet lineDataSet1 = new LineDataSet(dataVals1, "Data set 1");
                LineDataSet lineDataSet2 = new LineDataSet(dataVals2, "Data set 2");
                lineDataSet1.setColor(Color.WHITE);
                lineDataSet1.setCircleColor(Color.BLACK);
                lineDataSet1.setCircleHoleRadius(5);
                lineDataSet1.setValueTextSize(0);
                lineDataSet2.setLineWidth(5);
                lineDataSet2.setCircleRadius(5);
                lineDataSet2.setCircleHoleRadius(1);
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet1);
                dataSets.add(lineDataSet2);
                XAxis xAxis = mpLineChart.getXAxis();
                xAxis.setGranularity(1.0f);
                YAxis yAxis = mpLineChart.getAxisRight();
                yAxis.setGranularity(1.0f);
                YAxis yAxis2 = mpLineChart.getAxisLeft();
                mpLineChart.getAxisLeft().setAxisMaxValue(13.0f);
                mpLineChart.getAxisLeft().setAxisMinValue(0f);
                mpLineChart.getAxisRight().setAxisMaxValue(13.0f);
                mpLineChart.getAxisRight().setAxisMinValue(-1f);
                mpLineChart.setBackgroundColor(Color.WHITE);
                mpLineChart.setNoDataText("No Data");
                mpLineChart.setNoDataTextColor(Color.BLUE);
                mpLineChart.setDrawGridBackground(false);
                Description description = new Description();
                description.setText("Pain Graph");
                description.setTextColor(Color.GREEN);
                description.setTextSize(15);
                mpLineChart.setDescription(description);
                Legend legend;
                legend = mpLineChart.getLegend();
                legend.setEnabled(true);
                LegendEntry[] legendEntries = new LegendEntry[2];
                LegendEntry entry1 = new LegendEntry();
                entry1.formColor = Color.BLACK;
                entry1.label = "Medicine";
                legendEntries[0] = entry1;
                LegendEntry entry2 = new LegendEntry();
                entry2.formColor = Color.CYAN;
                entry2.label = "Pain Score";
                legendEntries[1] = entry2;
                legend.setCustom(legendEntries);
                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
                painInput = (SeekBar) findViewById(R.id.seekBar);
                date_time_in2 =findViewById(R.id.date_time_input3);
                date_time_in2.setInputType(InputType.TYPE_NULL);
                datetime2 = "";

                date_time_in2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateTimeDialog1(date_time_in2);
                    }
                });
                initializeList();
            }
        }
        else
        {
            Toast.makeText(ClientViewGraphPage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientViewGraphPage.this, MainActivity.class));
        }
    }

    public boolean getNeededValues()
    {
        String[] field = new String[1];
        field[0] = "admissionid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = admissionid;

        PutData putData = new PutData("http://bopps2130.net/getAdmissionGraphPage.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "None":
                        Toast.makeText(ClientViewGraphPage.this, "Could not get needed values.", Toast.LENGTH_SHORT).show();
                        return false;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                    {
                        try
                        {
                            admissionarray = new JSONArray(result);
                            for(int i = 0; i < admissionarray.length();i++)
                            {
                                JSONObject names =  admissionarray.getJSONObject(i);
                                bednumber2 = names.getString("Bed");
                                concerned = names.getString("Concerned");
                                gender = names.getString("PatientGender");
                                graphid = names.getString("GraphID");
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
        }

        return false;
    }

    private void setDataValEntries()
    {
        double lowestValue = 0;

        if(thePain.size() == 0 && theMedication.size() == 0)
        {
            return;
        }

        if(thePain.size() == 1)
        {
            lowestValue = thePain.get(0).timeForInstance;
        }
        else if(thePain.size() > 1)
        {
            lowestValue = thePain.get(0).timeForInstance;
            for(int i = 1 ; i < thePain.size(); i++)
            {
                if(thePain.get(i).timeForInstance < lowestValue)
                {
                     lowestValue = thePain.get(i).timeForInstance;
                }
            }

            timeObjectGraph temp = new timeObjectGraph();
            for(int i = 0 ; i < thePain.size(); i++)
            {
                for(int j = i + 1; j < thePain.size(); j++)
                {
                    if(thePain.get(i).timeForInstance > thePain.get(j).timeForInstance)
                    {
                        temp.timeForInstance = thePain.get(i).timeForInstance;
                        temp.value = thePain.get(i).value;
                        thePain.get(i).timeForInstance = thePain.get(j).timeForInstance;
                        thePain.get(i).value = thePain.get(j).value;
                        thePain.get(j).timeForInstance = temp.timeForInstance;
                        thePain.get(j).value = temp.value;
                    }
                }
            }
        }

        double lowestValue2 = 0;

        if(theMedication.size() == 1)
        {
            lowestValue2 = theMedication.get(0).timeForInstance;
        }
        else if(theMedication.size() > 1)
        {
            lowestValue2 = theMedication.get(0).timeForInstance;
            for(int i = 1 ; i < theMedication.size(); i++)
            {
                if(theMedication.get(i).timeForInstance < lowestValue2)
                {
                    lowestValue2 = theMedication.get(i).timeForInstance;
                }
            }

            timeObjectGraph temp = new timeObjectGraph();
            for(int i = 0 ; i < theMedication.size(); i++)
            {
                for(int j = i + 1; j < theMedication.size(); j++)
                {
                    if(theMedication.get(i).timeForInstance > theMedication.get(j).timeForInstance)
                    {
                        temp.timeForInstance = theMedication.get(i).timeForInstance;
                        temp.value = 12;
                        theMedication.get(i).timeForInstance = theMedication.get(j).timeForInstance;
                        theMedication.get(i).value = 12;
                        theMedication.get(j).timeForInstance = temp.timeForInstance;
                        theMedication.get(j).value = 12;
                    }
                }
            }
        }

        double lastLowestValue = 0;

        if(theMedication.size() > 0 && thePain.size() == 0)
        {
            lastLowestValue = lowestValue2;
        }

        if(theMedication.size() == 0 && thePain.size() > 0)
        {
            lastLowestValue = lowestValue;
        }

        if(theMedication.size() > 0 && thePain.size() > 0 )
        {
            if(lowestValue < lowestValue2)
            {
                lastLowestValue = lowestValue;
            }
            else
            {
                lastLowestValue = lowestValue2;
            }
        }

        if(theMedication.size() > 0)
        {
            for(int i = 0 ; i < theMedication.size(); i++)
            {
                theMedication.get(i).timeForInstance = (((theMedication.get(i).timeForInstance - lastLowestValue) / 1000) / 60);
                dataVals1.add(new Entry((float)theMedication.get(i).timeForInstance, 12));
            }
        }

        if(thePain.size() > 0)
        {
            for(int i = 0; i < thePain.size(); i++)
            {
                thePain.get(i).timeForInstance = (((thePain.get(i).timeForInstance - lastLowestValue) / 1000) / 60);
                dataVals2.add(new Entry((float)thePain.get(i).timeForInstance, thePain.get(i).value));
            }
        }
    }

    private void dataValues2()
    {
        thePain = new ArrayList<timeObjectGraph>();

        String[] field = new String[1];
        field[0] = "graphid";

        //Creating array for data
        String[] data = new String[1];
        data[0] = graphid;

        PutData putData = new PutData("http://bopps2130.net/getgraphvaluesandtime.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                switch (result)
                {
                    case "Not":
                        Toast.makeText(ClientViewGraphPage.this, "No pain inputs.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        {
                            try
                            {
                                medications = new JSONArray(result);
                                for (int i = 0; i < medications.length(); i++)
                                {
                                    JSONObject names = medications.getJSONObject(i);
                                    timeObjectGraph entry = new timeObjectGraph();
                                    String timestring = names.getString("Time");
                                    entry.timeForInstance = astroZeneca(timestring);
                                    entry.value = Integer.parseInt(names.getString("PainScore"));
                                    thePain.add(entry);
                                }

                            }
                            catch (JSONException | ParseException e)
                            {
                                e.printStackTrace();
                            }
                        }
                }
            }
        }
    }

    private void dataValues1()
    {
        theMedication = new ArrayList<timeObjectGraph>();

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
                        Toast.makeText(ClientViewGraphPage.this, "No medication inputs.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    {
                        try
                        {
                            med = new JSONArray(result);

                            for(int i = 0; i < med.length();i++)
                            {
                                JSONObject names = med.getJSONObject(i);
                                String timestring = names.getString("Time");
                                timeObjectGraph entry = new timeObjectGraph();
                                entry.timeForInstance = astroZeneca(timestring);
                                entry.value = 12;
                                theMedication.add(entry);
                            }

                        }
                        catch (JSONException | ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public float astroZeneca(String myDate) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        return date.getTime();
    }

    public void viewPatientGraphValues(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionModifyGraphPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        intent.putExtra("graphid", graphid);
        startActivity(intent);
    }

    public void otherDetailsClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClientViewGraphOtherPage.class);
        intent.putExtra("mrn", mrn);
        intent.putExtra("admissionid", admissionid);
        startActivity(intent);
    }

    public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter
    {
        @Override
        public String getFormattedValue(float value)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(new Date((long)value));
        }
    }

    public void onClickNavigation(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home_client:
                startActivity(new Intent(ClientViewGraphPage.this, ClientViewHomePage.class));
                break;
            case R.id.action_addpatient_client:
                startActivity(new Intent(ClientViewGraphPage.this, ClientAddPatientPage.class));
                break;
            case R.id.action_add_admission_client:
                startActivity(new Intent(ClientViewGraphPage.this, ClientAddAdmissionPage.class));
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
                startActivity(new Intent(ClientViewGraphPage.this,  MainActivity.class));
            }
            break;
        }
    }

    public void initializeList()
    {
        data1 = new ArrayList<>();
        DataAUC AUCData = new DataAUC();
        int totalAUC = 0;

        for(int i = 0; i < thePain.size(); i++)
        {
            totalAUC = thePain.get(i).value + totalAUC;
        }

        AUCData.AUC = Integer.toString(totalAUC);
        AUCData.concerned = concerned;
        AUCData.patientmrn = mrn;
        AUCData.bed = bednumber2;
        AUCData.patientGender = gender;
        data1.add(AUCData);
        // Setup and Handover data to recyclerview
        mRVAdmission = (RecyclerView)findViewById(R.id.admissionList2);
        mAdapter = new AdapterGraph(ClientViewGraphPage.this, data1);
        mRVAdmission.setLayoutManager(new LinearLayoutManager(ClientViewGraphPage.this));
        mRVAdmission.setAdapter(mAdapter);
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
        Intent intent = new Intent(getApplicationContext(), ClientAdmissionNotesPage.class);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void addPatientPainButton(View view) throws ParseException {
        String seekBarValue = Integer.toString(painInput.getProgress());
        datetime2 = date_time_in2.getText().toString();
        boolean validatedate = validateDatePain(datetime2);
        if(validatedate)
        {
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

                Intent intent = new Intent(getApplicationContext(), ClientViewGraphPage.class);
                intent.putExtra("mrn", mrn);
                intent.putExtra("admissionid", admissionid);
                startActivity(intent);
            }
        }
    }


    private boolean validateDatePain(String datetime22) throws ParseException
    {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        double epoch1 = date.getTime();
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime22);
        double epoch2 = date1.getTime();
        if(epoch2 > epoch1)
        {
            date_time_in2.setError("Cannot add pain in the future.");
            return false;
        }

        if(datetime22.isEmpty())
        {
            date_time_in2.setError("Field cannot be empty");
            return false;
        }
        else
        {
            date_time_in2.setError(null);
            return true;
        }
    }

    private boolean addPainScoreToDataBase(String painscore)
    {
        String[] field = new String[4];
        field[0] = "admissionid";
        field[1] = "painscore";
        field[2] = "graphid";
        field[3] = "datetime";

        //Creating array for data
        String[] data = new String[4];
        data[0] = admissionid;
        data[1] = painscore;
        data[2] = graphid;
        data[3] = datetime2;

        PutData putData = new PutData("http://bopps2130.net/inputpainscoreclinadmin.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.d("Max Value", result);
                switch(result)
                {
                    case "Success":
                        return true;
                    case "Error: Database connection":
                        Toast.makeText(ClientViewGraphPage.this, "Error: Database connection.", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        }

        return false;
    }

    private void showDateTimeDialog1(final EditText date_time_in0in)
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
                        date_time_in0in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime2 = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(ClientViewGraphPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ClientViewGraphPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}