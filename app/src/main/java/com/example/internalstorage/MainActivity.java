package com.example.internalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button okBtn;
    Button registration;
    String filename = "file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        okBtn = findViewById(R.id.ok_btn);
        registration = findViewById(R.id.registration_btn);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(login.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT).show();
                }

                try (FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
                ) {
                    bufferedWriter.write(readInformation());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                login.setText(null);
                password.setText(null);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (FileInputStream fileInputStream = openFileInput(filename);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
                ) {
                    String savedInfo = bufferedReader.readLine();
                    if (readInformation().equals(savedInfo)) {
                        Toast.makeText(getApplicationContext(), "Вход совершён", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Неправильные данные", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String readInformation() {
        String loginText = login.getText().toString();
        String passwordText = password.getText().toString();
        return loginText + " " + passwordText;
    }
}