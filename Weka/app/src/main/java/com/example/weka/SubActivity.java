package com.example.weka;


import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class SubActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager manager;
    Sensor sensor;
    TextView xTextView;
    TextView yTextView;
    TextView zTextView;
    int stage=0;
    double synthetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        xTextView = (TextView)findViewById(R.id.xValue);
        yTextView = (TextView)findViewById(R.id.yValue);
        zTextView = (TextView)findViewById(R.id.zValue);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);





    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xTextView.setText(String.valueOf(event.values[0]));
        yTextView.setText(String.valueOf(event.values[1]));
        zTextView.setText(String.valueOf(event.values[2]));
        //Log.v("ログ", String.valueOf(stage));
        if(stage==1) {
            try {
                //出力先を作成する
                FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/stand.csv", true);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                //内容を指定する
                pw.print(String.valueOf(event.values[0]));
                pw.print(",");
                pw.print(String.valueOf(event.values[1]));
                pw.print(",");
                pw.print(String.valueOf(event.values[2]));
                pw.print(",");
                pw.print("stand");
                pw.println();

                //ファイルに書き出す
                pw.close();

                //終了メッセージを画面に出力する
                //Log.v("ログ", "#出力が完了しました。");


            } catch (IOException ex) {
                //例外時処理
                Log.v("ログ", "#");
                ex.printStackTrace();
            }

        }
        else if(stage==2) {
            try {
                //出力先を作成する
                FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/walk.csv", true);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                //内容を指定する
                pw.print(String.valueOf(event.values[0]));
                pw.print(",");
                pw.print(String.valueOf(event.values[1]));
                pw.print(",");
                pw.print(String.valueOf(event.values[2]));
                pw.print(",");
                pw.print("walk");
                pw.println();

                //ファイルに書き出す
                pw.close();

                //終了メッセージを画面に出力する
                //Log.v("ログ", "#出力が完了しました。");



            } catch (IOException ex) {
                //例外時処理
                Log.v("ログ", "#");
                ex.printStackTrace();
            }

        }
        else if(stage==3) {
            try {
                //出力先を作成する
                FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/run.csv", true);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                //内容を指定する
                pw.print(String.valueOf(event.values[0]));
                pw.print(",");
                pw.print(String.valueOf(event.values[1]));
                pw.print(",");
                pw.print(String.valueOf(event.values[2]));
                pw.print(",");
                pw.print("run");
                pw.println();

                //ファイルに書き出す
                pw.close();

                //終了メッセージを画面に出力する
                //Log.v("ログ", "#出力が完了しました。");


            } catch (IOException ex) {
                //例外時処理
                Log.v("ログ", "#");
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void study_stand(View v){
        stage=1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubActivity.this);
        alertDialog.setTitle("サンプリング中");
        alertDialog.setMessage("10秒程度[止まって]ください");
        alertDialog.setPositiveButton("終了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                CheckBox check_stand = (CheckBox)findViewById(R.id.check_stand);
                check_stand.setChecked(true);
                //textView.setText("こんにちは");
            }
        });
        alertDialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                //textView.setText("");
            }
        });
        alertDialog.create().show();

    }

    public void study_walk(View v){
        stage=2;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubActivity.this);
        alertDialog.setTitle("サンプリング中");
        alertDialog.setMessage("10秒程度[歩いて]ください");
        alertDialog.setPositiveButton("終了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                CheckBox check_walk = (CheckBox)findViewById(R.id.check_walk);
                check_walk.setChecked(true);
                //textView.setText("こんにちは");
            }
        });
        alertDialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                //textView.setText("");
            }
        });
        alertDialog.create().show();

    }

    public void study_run(View v){
        stage=3;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubActivity.this);
        alertDialog.setTitle("サンプリング中");
        alertDialog.setMessage("10秒程度[走って]ください");
        alertDialog.setPositiveButton("終了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                CheckBox check_run = (CheckBox)findViewById(R.id.check_run);
                check_run.setChecked(true);
                //textView.setText("こんにちは");
            }
        });
        alertDialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stage=0;
                //textView.setText("");
            }
        });
        alertDialog.create().show();

    }



}
