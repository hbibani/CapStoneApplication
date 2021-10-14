package my.bop.finalassignment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.finalassignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


SharedPreferences pref;
String tokenString;
String usernameString;
String passwordString;
String mrn;
EditText username1;
EditText password1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pref=this.getSharedPreferences("NewsTweetSettings", 0);
        setContentView(R.layout.activity_login);
        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
    }

    public void loginButton (View view)
    {
        usernameString = username1.getText().toString();
        passwordString = password1.getText().toString();
        boolean validateusername = validateUserName();
        boolean validatepassword = validatePassword();
        if(validateusername && validatepassword)
        {
            getToken();
        }
    }

    private boolean validatePassword()
    {
        if(passwordString.isEmpty())
        {
            password1.setError("Field cannot be empty");
            return false;
        }
        else
        {
            password1.setError(null);
            return true;
        }
    }

    private boolean validateUserName()
    {
        if(usernameString.isEmpty())
        {
            username1.setError("Field cannot be empty");
            return false;
        }
        else
        {
            username1.setError(null);
            return true;
        }

    }

    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task)
            {
                //If task is failed then
                if (!task.isSuccessful())
                {
                    task.getException();
                }
                tokenString = task.getResult();
                loginUser(usernameString, passwordString, tokenString);
            }
        });
    }

    public void loginUser(String usernameString, String passwordString, String Token)
    {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[3];
            field[0] = "username";
            field[1] = "password";
            field[2] = "token";

            //Creating array for data
            String[] data = new String[3];
            data[0] = usernameString;
            data[1] = passwordString;
            data[2] = Token;

            PutData putData = new PutData("http://bopps2130.net/login.php", "POST", field, data);
            if (putData.startPut())
            {
                if (putData.onComplete())
                {
                    String result = putData.getResult();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Username", usernameString);
                    editor.putString("Type", result );
                    editor.apply();

                    switch(result)
                    {
                        case "Role: '1'":
                            startActivity(new Intent(MainActivity.this, AdminHomePage.class));
                            break;
                        case "Role: '2'":
                            startActivity(new Intent(MainActivity.this, ClientViewHomePage.class));
                            break;
                        case "Role: '3'":
                            if(getMrn())
                            {
                                SharedPreferences.Editor editor1 = pref.edit();
                                editor1.putString("mrn", mrn);
                                editor1.apply();
                                startActivity(new Intent(MainActivity.this, PatientHomePage.class));
                            }
                            break;
                        case "Error: Database connection":
                            Toast.makeText(MainActivity.this, "Error: Database connection", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                        {
                            Toast.makeText(MainActivity.this, "Password or username is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    }

    private boolean getMrn()
    {
        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[1];
        field[0] = "username";

        //Creating array for data
        String[] data = new String[1];
        data[0] = usernameString;


        PutData putData = new PutData("http://bopps2130.net/getpatientmrnlogin.php", "POST", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();

                switch(result)
                {
                    case "false":
                        Toast.makeText(MainActivity.this, "Could not retrieve mrn.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Error: Database connection":
                        Toast.makeText(MainActivity.this, "Error: Database connection", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                    {
                        mrn = result;
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @Override
    public void onBackPressed()
    {
        return;
    }

}