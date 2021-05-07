package com.example.a_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> listLanguage;
    ArrayAdapter<String> adapter;

    Locale myLocale;
    String luuthaydoi = "language";
    int currentLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        addEvent();

    }

    private void addEvent() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setLocale("en");
                        saveChange(currentLanguage, position);
                        break;
                    case 1:
                        setLocale("vi");
                        saveChange(currentLanguage, position);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void saveChange(int current, int position){
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
        dialog.setTitle(R.string.notification);
        dialog.setMessage(R.string.ntf_change_language);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent);
                SettingsActivity.this.finish();
            }
        });
        /*button phu dinh*/
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if(current != position)
        {
            dialog.show();
        }
    }
    private void init() {
        spinner = findViewById(R.id.spinner);
        listLanguage = new ArrayList<>();
        listLanguage.add("English");
        listLanguage.add("Tiếng Việt");
        currentLanguage = spinner.getSelectedItemPosition();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_list_item_1, listLanguage);
        spinner.setAdapter(adapter);
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(luuthaydoi, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("language", spinner.getSelectedItemPosition());
        editor.commit();
    }
    //  Phuc hoi trang thai
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(luuthaydoi, MODE_PRIVATE);
        int position = preferences.getInt("language", 0);
        spinner.setSelection(position);
        currentLanguage = spinner.getSelectedItemPosition();
    }
}