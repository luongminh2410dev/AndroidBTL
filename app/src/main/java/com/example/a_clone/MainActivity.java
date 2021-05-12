package com.example.a_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapter.CourseAdapter;
import com.example.Data.ConnectDatabase;
import com.example.model.Course;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends ConnectDatabase {
    GridView gvCourse;
    CourseAdapter courseAdapter;
    ArrayList<Course> dsCourse;
    NavigationView navigationView;
    ImageView imageView;
    TextView tvNameUser, tvPoint;
    Intent intent;
    int id_account = -1;
    ListView lvRank;
    ArrayList<String> listRank;
    ArrayAdapter<String> adapterRank;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xuLySaoChepCSDL();
        init();
        addEvent();
        showData();
        showInfoAccount();
    }

    private void showInfoAccount() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from account where id = '"+intent.getIntExtra("ID_ACCOUNT", -1)+"'",null);
        while (cursor.moveToNext())
        {
            String nameUser = cursor.getString(1);
            byte[] bitmapFactory = cursor.getBlob(5);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapFactory);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            tvNameUser.setText(nameUser);
        }
        cursor.close();
    }

    private void showData() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from subjects",null);
        dsCourse.clear();
        while (cursor.moveToNext())
        {
            String nameCourse = cursor.getString(1);
            byte[] bitmapFactory = cursor.getBlob(2);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapFactory);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            dsCourse.add(new Course( bitmap, nameCourse));
            courseAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    private void addEvent() {
        gvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseCourse(position);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                xulyMenuInfo(item);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRank();
        showRank();
    }

    private void xulyMenuInfo(MenuItem item) {
        Intent intent1 = null;
        switch (item.getItemId())
        {
            case R.id.item_home:
                finish();
                startActivity(getIntent());
                break;
            case R.id.item_profile:
                intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                break;
            case R.id.item_contact:
                intent1 = new Intent(MainActivity.this, ContactUsActivity.class);
                break;
            case R.id.item_setting:
                intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.item_exit:
                finish();
                break;
        }
        intent1.putExtra("ID_ACCOUNT", id_account);
        startActivity(intent1);
    }

    private void chooseCourse(int position) {
        Intent intent1 = new Intent(MainActivity.this, LessonActivity.class);
        intent1.putExtra("COURSE_CHOOSED", position);
        intent1.putExtra("ID_ACCOUNT", intent.getIntExtra("ID_ACCOUNT", -1));
        startActivity(intent1);
    }

    private void init() {
        //Khoi tao tab host(Bao dung cac tab con)
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

//        Khoi tao tab1
        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator(getString(R.string.course));
        tabHost.addTab(tab1);

//        Khoi tao tab2
        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator(getString(R.string.rank));
        tabHost.addTab(tab2);
//        Tab Coures
        gvCourse = findViewById(R.id.gvCourse);
        dsCourse = new ArrayList<>();
        navigationView = findViewById(R.id.menuInfo);
        imageView = findViewById(R.id.ivUser);
        tvNameUser = findViewById(R.id.tvNameUser);
        courseAdapter = new CourseAdapter(this, R.layout.main_item, dsCourse);
        gvCourse.setAdapter(courseAdapter);
        intent = getIntent();
        id_account = intent.getIntExtra("ID_ACCOUNT", -1);

//        Tab Ranking
        tvPoint = findViewById(R.id.pointtv);
        lvRank = findViewById(R.id.lvRank);
        listRank = new ArrayList<>();
        adapterRank = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listRank);
        lvRank.setAdapter(adapterRank);
        updateRank();
        showRank();
    }

    private void showRank() {
        adapterRank.clear();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from ranking order by totalPoint DESC", null);
        int i = 1;
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            Cursor cursor1 = database.rawQuery("select * from account where id = '"+id+"' ", null);
            while (cursor1.moveToNext()) {
                if(cursor1.getInt(0) == id_account)
                {
                    listRank.add(i + " \t " + "Me" + " \t " + cursor.getInt(1));
                }
                else{
                    listRank.add(i + " \t " + cursor1.getString(1) + " \t " + cursor.getInt(1));
                }
                i++;
            }
            cursor1.close();
        }
        cursor.close();
        adapterRank.notifyDataSetChanged();
    }

    private void updateRank()
    {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        database.enableWriteAheadLogging();
        Cursor cs = database.rawQuery("select * from chiTietLesson where id_account = '"+id_account+"'", null);
        int totalPoint = 0;
        while (cs.moveToNext())
        {
            if(cs.getInt(1) == 100)
            {
                totalPoint += 30;
            }
        }
        tvPoint.setText(totalPoint + "");
        cs.close();
        database.execSQL("update ranking set totalPoint = '"+totalPoint+"' where id = '"+id_account+"'");
//        ContentValues values = new ContentValues();
//        values.put("totalPoint", totalPoint);
//        database.update("ranking", values, "id=?", new String[]{id_account + ""});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                courseAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}