package com.example.a_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.LessonItem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LessonItemActivity extends AppCompatActivity {
    String DATABASE_NAME="database.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;

    TextView tvNameItem, tvIsCorrect, tvNumberQuestion, tvQuestion, countDownTv;
    ImageView imageQuestion;
    Intent intent2;
    EditText edtAnswer;
    Button btnConfirm;

    ArrayList<LessonItem> listLesson;
    int currentQuestion = 0;
    int correctAnswer = 0;

    Timer timer = null;
    TimerTask timerTask = null;
//      Time countdown
    CountDownTimer countDownTimer;
    long time = 600000; // 10 minutes

    int item = -1;
    int course = -1;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        xuLySaoChepCSDL();
        init();
        addEvent();


    }

    private void addEvent() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIsCorrect.setVisibility(View.VISIBLE);
                if(edtAnswer.getText().toString().equals(listLesson.get(currentQuestion).getCorrectAnswer()))
                {
                    correctAnswer++;
                    tvIsCorrect.setText(R.string.correct);
                    tvIsCorrect.setBackgroundColor(Color.parseColor("#FF31FF08"));
                }
                else {
                    tvIsCorrect.setText(R.string.incorrect);
                    tvIsCorrect.setBackgroundColor(Color.parseColor("#cf2127"));
                }
                currentQuestion++;
                if(currentQuestion == listLesson.size())
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LessonItemActivity.this);
                    dialog.setTitle("Kết quả: " + correctAnswer + " / " +listLesson.size());
                    dialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            double progressComplete = (double) correctAnswer / listLesson.size() * 100;
                            if(progressComplete >= 90)
                            {
                                progressComplete = 100;
                            }
                            intent2.putExtra("PROGRESS", progressComplete);
                            intent2.putExtra("ID", item);
                            setResult(2, intent2);
                            finish();
                        }
                    });
                    dialog.show();
                }
                else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            xuLyGanDuLieuCauHoi(currentQuestion);
                        }
                    }, 3000);
                }
            }
        });
    }

    private void init() {
        countDownTv = findViewById(R.id.tvTimeCountDown);
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished;
                updateTime();
            }

            @Override
            public void onFinish() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LessonItemActivity.this);
                dialog.setTitle("Kết quả: " + correctAnswer + " / " +listLesson.size());
                dialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double progressComplete = correctAnswer / currentQuestion;
                        if(progressComplete >= 0.9)
                        {
                            progressComplete = 100;
                        }
                        intent2.putExtra("PROGRESS", progressComplete);
                        intent2.putExtra("ID", item);
                        setResult(2, intent2);
                        finish();
                    }
                });
                dialog.show();
            }
        }.start();
        tvNameItem = findViewById(R.id.tvNameItem);
        tvIsCorrect = findViewById(R.id.tvResult);
        tvNumberQuestion = findViewById(R.id.tvNumberQuestion);
        tvQuestion = findViewById(R.id.tvQuestion);
        imageQuestion = findViewById(R.id.imageQuestion);
        edtAnswer = findViewById(R.id.edtAnswer);
        btnConfirm = findViewById(R.id.btnConfirm);
        intent2 = getIntent();

        listLesson = new ArrayList<>();

        course = intent2.getIntExtra("POSITION_COURSE", -1);
        item = intent2.getIntExtra("POSITION_ITEM", -1);
        fillQuestion(item, course);
        xuLyGanDuLieuCauHoi(currentQuestion);
    }

    private void updateTime() {
        int minutes = (int) time / 60000;
        int seconds = (int) time % 60000 / 1000;
        String timeUpdate;
        timeUpdate =  "" + minutes;
        timeUpdate += ":";
        if(seconds < 10)
        {
            timeUpdate += "0";
        }
        timeUpdate += seconds;
        countDownTv.setText(timeUpdate);
    }

    private void fillQuestion(int item, int course){
//        int position = item + 1;
        int position = course + 1;
        listLesson.clear();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from question where id_lesson = '"+item+"' and id_subject = '"+position+"' ;", null);
        while (cursor.moveToNext())
        {
            int id_question = cursor.getInt(0);
            String question = cursor.getString(1);
            String correctAnswer = cursor.getString(2);
            byte[] bitmapFactory = cursor.getBlob(3);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapFactory);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            listLesson.add(new LessonItem(id_question, question, bitmap, correctAnswer));
        }
        cursor.close();
    }

    private void xuLyGanDuLieuCauHoi(int current) {
        int numberQuestion = current + 1;
        tvIsCorrect.setVisibility(View.INVISIBLE);
        tvNumberQuestion.setText("Câu " + numberQuestion + ":");
        tvQuestion.setText(listLesson.get(current).getQuestion());
        imageQuestion.setImageBitmap(listLesson.get(current).getImage());
        edtAnswer.setText("");
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