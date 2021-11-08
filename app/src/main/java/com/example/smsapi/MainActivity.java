package com.example.smsapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText editTextPhoneNumber;
    EditText editTextVerifyOTP;
    Button buttonSendOTP;
    Button buttonVerifyOTP;
    int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        editTextVerifyOTP = (EditText) findViewById(R.id.editTextTextEnterOTP);
        buttonSendOTP = (Button) findViewById(R.id.buttonSendOTP);
        buttonVerifyOTP = (Button) findViewById(R.id.buttonVerifyOTP);

        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a background thread to send OTP
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //TODO PUT YOUR API KEY HERE
                            String apiKey = "api_key=" + "YOUR_API_KEY";
                            Random random = new Random();
                            randomNumber = random.nextInt(999999);
                            String message = "&msg=" + "Your KitBag OTP is: " + randomNumber;
                            String numbers = "&to=" + editTextPhoneNumber.getText().toString();
                            String data = apiKey + message + numbers;
                            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.sms.net.bd/sendsms?").openConnection();
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                            conn.getOutputStream().write(data.getBytes("UTF-8"));
                            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            final StringBuffer stringBuffer = new StringBuffer();
                            String line;
                            while ((line = rd.readLine()) != null) {
                                stringBuffer.append(line);
                            }
                            rd.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        // Can be uses when send OTP in Main Thread
        //StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().build();
        //StrictMode.setThreadPolicy(st);

        buttonVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(randomNumber).equals(editTextVerifyOTP.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Matched", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "OTP Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}