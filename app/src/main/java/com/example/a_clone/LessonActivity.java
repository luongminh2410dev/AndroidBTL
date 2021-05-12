package com.example.a_clone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.Adapter.LessonAdapter;
import com.example.Data.ConnectDatabase;
import com.example.model.Lesson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LessonActivity extends ConnectDatabase {
    SQLiteDatabase database;
    GridView gridView;
    LessonAdapter adapter;
    ArrayList<Lesson> dsItem;

    int id_account = -1;
    Intent intent1;
    int courseChoosed = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        xuLySaoChepCSDL();
        init();
        addEvent();
    }

    private void addEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseLesson(position);
            }
        });
    }

    private void chooseLesson(int position) {
        Intent intent2 = new Intent(LessonActivity.this, LessonItemActivity.class);
        int id = dsItem.get(position).getId();
        if(dsItem.get(position).getProgress() >= 100)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LessonActivity.this);
                dialog.setTitle(R.string.notification);
                dialog.setMessage(R.string.ntf_course_done);
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent2.putExtra("POSITION_COURSE", courseChoosed);
                        intent2.putExtra("POSITION_ITEM", id);
                        startActivityForResult(intent2, 1);
                    }
                });
                /*button phu dinh*/
                dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        else {
            intent2.putExtra("POSITION_COURSE", courseChoosed);
            intent2.putExtra("POSITION_ITEM", id);
            startActivityForResult(intent2, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Lay progress lesson
        if(requestCode == 1 && resultCode == 2)
        {
            double newProgress = data.getDoubleExtra("PROGRESS", 0);
            int id = data.getIntExtra("ID", 0);
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("update chiTietLesson set progress = '"+newProgress+"'  where id_account = '"+id_account+"' and id_lesson = '"+id+"'", null);
            cursor.moveToFirst();
            cursor.close();
            readLesson(courseChoosed);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void init() {
        gridView = findViewById(R.id.gvItemCourse);
        dsItem = new ArrayList<>();

        intent1 = getIntent();
        courseChoosed = intent1.getIntExtra("COURSE_CHOOSED", -1);
        id_account = intent1.getIntExtra("ID_ACCOUNT", -1);
        adapter = new LessonAdapter(this, R.layout.list_item, dsItem);
        gridView.setAdapter(adapter);
        readLesson(courseChoosed);

    }

    private void readLesson(int courseChoosed) {
        int position = courseChoosed + 1;
        dsItem.clear();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from lesson where id_subject = '"+position+"' ", null);
        while (cursor.moveToNext())
        {
            int id_lesson = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] bitmapFactory = cursor.getBlob(2);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapFactory);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Cursor cursor1 = database.rawQuery("select * from chiTietLesson where id_account = '"+id_account+"' and id_lesson = '"+id_lesson+"' ;", null);
            while (cursor1.moveToNext())
            {
                int progress = cursor1.getInt(1);
                dsItem.add(new Lesson(id_lesson,bitmap, name, progress));
                adapter.notifyDataSetChanged();
            }
        }
        cursor.close();
    }
}