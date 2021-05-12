package com.example.a_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Data.ConnectDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends ConnectDatabase {
    EditText edtUserName, edtPassword;
    Button btnLogin, btnExit;
    ProgressDialog dialog;
    CheckBox checkBox;
    String luuthongtin = "log-in";
    AlertDialog.Builder regist;
    SQLiteDatabase database;
    int id_account = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        xuLySaoChepCSDL();
        init();
        addEvent();
    }

    private boolean kiemTraDangNhap() {
        if(edtUserName.getText().toString() != "" && edtPassword.getText().toString() != "")
        {
            String username = edtUserName.getText().toString();
            String password = edtPassword.getText().toString();
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("select * from account where username = '" + username + "' and password = '" + password + "' ", null);
            while (cursor.moveToNext())
            {
                id_account = cursor.getInt(0);
            }
            if(((cursor != null) && (cursor.getCount() > 0)))
            {
                cursor.close();
                return true;
            }
            cursor.close();
            return false;
        }
        return false;
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                if(kiemTraDangNhap())
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.cancel();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("ID_ACCOUNT", id_account);
                            startActivity(intent);
                        }
                    }, 3000);
                }
                else {
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moveTaskToBack(true);
//                System.exit(1);
                regist.show();
            }
        });
    }

    private void init() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        checkBox = findViewById(R.id.checkBox);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Signing...");

        regist = new AlertDialog.Builder(LoginActivity.this);
        regist.setTitle("Đăng ký");
        regist.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        regist.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(luuthongtin, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", edtUserName.getText().toString());
        editor.putString("PassWord", edtPassword.getText().toString());
        editor.putBoolean("Save", checkBox.isChecked());
        editor.commit();
    }
    //  Phuc hoi trang thai
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(luuthongtin, MODE_PRIVATE);
        String username = preferences.getString("UserName", "");
        String password = preferences.getString("PassWord", "");
        Boolean save = preferences.getBoolean("Save",false);
        if(save)
        {
            edtUserName.setText(username);
            edtPassword.setText(password);
            checkBox.setChecked(save);
        }
    }
}