package com.example.uber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DriverLoginActivity extends AppCompatActivity {
    private Button mLogin,mRefistartion;
    private EditText mEmail , mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

//        mEmail = findViewById(R.id.email);
        mLogin = findViewById(R.id.login);
//        mPassword = findViewById(R.id.password);
        mRefistartion = findViewById(R.id.registration);
    }
}
