package com.example.a_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfileActivity extends AppCompatActivity {
    String DATABASE_NAME="database.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;

    ImageView imageView;
    EditText edtName, edtNameAccount, edtPass, edtEmail;
    Button btnName, btnPass, btnEmail;
    Button btnLogout;
    Intent intent;

    @Override
    protected void onStop() {
        super.onStop();
    }

    int id_account = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        xuLySaoChepCSDL();
        init();
        addEvent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
    private void loadData() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from account where id = '"+id_account+"';",null);
        while (cursor.moveToNext())
        {
            String name = cursor.getString(1);
            String username = cursor.getString(2);
            String password = cursor.getString(3);
            String email = cursor.getString(4);
            byte[] bitmapFactory = cursor.getBlob(5);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapFactory);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            edtName.setText(name);
            edtNameAccount.setText(username);
            edtPass.setText(password);
            edtEmail.setText(email);
        }
        cursor.close();
    }

    private void addEvent() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                database.execSQL("update account set name = '"+name+"' where id = '"+id_account+"'");
                edtName.setCursorVisible(false);
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                database.execSQL("update account set email = '"+email+"' where id = '"+id_account+"'");
                edtEmail.setCursorVisible(false);
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtPass.getText().toString();
                database.execSQL("update account set password = '"+pass+"' where id = '"+id_account+"'");
                edtPass.setCursorVisible(false);
                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        imageView = findViewById(R.id.ivUser);
        edtName = findViewById(R.id.edtName);
        edtNameAccount = findViewById(R.id.edtNameAccount);
        edtPass = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        btnName = findViewById(R.id.btnProfileName);
        btnPass = findViewById(R.id.btnProfilePassword);
        btnEmail = findViewById(R.id.btnProfileEmail);
        btnLogout = findViewById(R.id.btnLogout);
        intent = getIntent();
        id_account = intent.getIntExtra("ID_ACCOUNT", -1);
        loadData();

    }

    private void xuLySaoChepCSDL() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
            }catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
}