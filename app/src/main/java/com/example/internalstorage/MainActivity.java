package com.example.internalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button okBtn;
    Button registration;
    String fileNameInternal = "fileInternal.txt";
    String fileNameExternal = "fileExternal.txt";
    CheckBox checkBox;
    File externalFile;

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
        checkBox = findViewById(R.id.checkbox);

        externalFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileNameExternal);
        final SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        final String key = "KEY_TEXT";

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                if (isChecked) {
                    editor.putString(key, "Checked");
                } else {
                    editor.putString(key, "Not checked");
                }
                editor.apply();
            }
        });

        String status = prefs.getString(key, "");
        if (status.equals("Checked")) {
            checkBox.setChecked(true);
        } else checkBox.setChecked(false);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNoInfo();

                if (checkBox.isChecked()) externalInput();
                else internalInput();

                login.setText(null);
                password.setText(null);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNoInfo();

                if (checkBox.isChecked()) externalOutput();
                else internalOutput();

                login.setText(null);
                password.setText(null);
            }
        });
    }

    private void isNoInfo() {
        if (TextUtils.isEmpty(login.getText()) || TextUtils.isEmpty(password.getText())) {
            Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        }
    }

    private String readInformation() {
        String loginText = login.getText().toString();
        String passwordText = password.getText().toString();
        return loginText + " " + passwordText;
    }

    private void internalInput() {
        try (FileOutputStream fileOutputStream = openFileOutput(fileNameInternal, Context.MODE_PRIVATE);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            bufferedWriter.write(readInformation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void externalInput() {
        if (isExternalStorageWritable()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(externalFile, true))) {
                writer.write(readInformation());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void internalOutput() {
        try (FileInputStream fileInputStream = openFileInput(fileNameInternal);
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

    private void externalOutput() {
        try (BufferedReader reader = new BufferedReader(new FileReader(externalFile))) {
            String savedInfo = reader.readLine();
            if (readInformation().equals(savedInfo)) {
                Toast.makeText(getApplicationContext(), "Вход совершён", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Неправильные данные", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}