package com.example.weka;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager manager;
    Sensor sensor;
    public int stage=0;
    double result=0;
    int count=0;

    TextView resultTextView;
    TextView statusTextView;
    LinearLayout ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView)findViewById(R.id.resultTextView);
        statusTextView = (TextView)findViewById(R.id.statusTextView);
        ll = (LinearLayout)findViewById(R.id.ll);


        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        File delfile = new File(Environment.getExternalStorageDirectory().getPath() + "/stand.csv");
        delfile.delete();
        File delfile2 = new File(Environment.getExternalStorageDirectory().getPath() + "/walk.csv");
        delfile2.delete();
        File delfile3 = new File(Environment.getExternalStorageDirectory().getPath() + "/run.csv");
        delfile3.delete();
        File delfile4 = new File(Environment.getExternalStorageDirectory().getPath() + "/acceleration.arff");
        delfile4.delete();
    }

    // メニュー作成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);

        return true;
    }

    // メニューアイテム選択イベント
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SubActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (stage == 1) {
            resultTextView.setText(String.valueOf(result));

            if(result==0.0) {
                statusTextView.setText("立ち状態");
                ll.setBackgroundColor(Color.rgb(192, 255, 0));
            }
            else if(result==1.0) {
                statusTextView.setText("歩き状態");
                ll.setBackgroundColor(Color.rgb(54, 255, 105));
            }
            else if(result==2.0) {
                statusTextView.setText("走り状態");
                ll.setBackgroundColor(Color.rgb(252, 255, 0));
            }
            else {
                statusTextView.setText("状態");
                ll.setBackgroundColor(Color.rgb(222, 222, 222));
            }

            if(count==20) {
                try {
                    DataSource source = new DataSource(Environment.getExternalStorageDirectory().getPath() + "/acceleration.arff");
                    Instances instances = source.getDataSet();
                    instances.setClassIndex(3);
                    Classifier classifier = new SMO();
                    classifier.buildClassifier(instances);

                    Evaluation eval = new Evaluation(instances);
                    eval.evaluateModel(classifier, instances);
                    System.out.println(eval.toSummaryString());

                    Attribute xValue = new Attribute("xValue", 0);
                    Attribute yValue = new Attribute("yValue", 1);
                    Attribute zValue = new Attribute("zValue", 2);
                    FastVector sta = new FastVector(3);
                    sta.addElement("stand");
                    sta.addElement("walk");
                    sta.addElement("run ");
                    Attribute status = new Attribute("status", sta, 3);

                    Instance instance = new DenseInstance(3);
                    instance.setValue(xValue, event.values[0]);
                    instance.setValue(yValue, event.values[1]);
                    instance.setValue(zValue, event.values[2]);
                       instance.setDataset(instances);

                    result = classifier.classifyInstance(instance);

                    Log.v("ログ", String.valueOf(result));

                } catch (Exception e) {
                    Log.v("ログ", "#");
                    e.printStackTrace();
                }
                count=0;
            }
            count++;
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public void start(View v){
        String line_stand[] = new String[1000];
        int line_stand_num=0;
        String line_walk[] = new String[1000];
        int line_walk_num=0;
        String line_run[] = new String[1000];
        int line_run_num=0;

        String line_arff[] = new String[1000];
        int line_arff_num=0;

        try {
            //出力先を作成する
            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/acceleration.arff", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            //内容を指定する
            pw.print("@relation acceleration\n" +
                    "\n" +
                    "@attribute xValue real\n" +
                    "@attribute yValue real\n" +
                    "@attribute zValue real\n" +
                    "@attribute status {stand, walk, run}\n" +
                    "\n" +
                    "@data");

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

        try {//standよみこみ
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/stand.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            // 1行ずつCSVファイルを読み込む
            while ((line_stand[line_stand_num] = br.readLine()) != null) {
                //Log.v("ログ", line_stand[line_stand_num] + " ; " + String.valueOf(line_stand_num));
                line_stand_num++;
            }
            br.close();

        } catch (IOException e) {
            Log.v("ログ", "#3");
            System.out.println(e);
        }

        try {//walkよみこみ
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/walk.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            // 1行ずつCSVファイルを読み込む
            while ((line_walk[line_walk_num] = br.readLine()) != null) {
                //Log.v("ログ", line_walk[line_walk_num] + " ; " + String.valueOf(line_walk_num));
                line_walk_num++;
            }
            br.close();

        } catch (IOException e) {
            Log.v("ログ", "#3");
            System.out.println(e);
        }

        try {//runよみこみ
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/run.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            // 1行ずつCSVファイルを読み込む
            while ((line_run[line_run_num] = br.readLine()) != null) {
                //Log.v("ログ", line_run[line_run_num] + " ; " + String.valueOf(line_run_num));
                line_run_num++;
            }
            br.close();

        } catch (IOException e) {
            Log.v("ログ", "#3");
            System.out.println(e);
        }

       try {
            //出力先を作成する
            FileWriter fw2 = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/acceleration.arff", true);
            PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));


            //内容を指定する
            for(int i=0;i<line_stand_num;i++) {
                pw2.print(line_stand[i]);
                pw2.println();
            }

            for(int i=0;i<line_walk_num;i++) {
                pw2.print(line_walk[i]);
                pw2.println();
            }

            for(int i=0;i<line_run_num;i++) {
                pw2.print(line_run[i]);
                pw2.println();
            }

            //ファイルに書き出す
            pw2.close();

            //終了メッセージを画面に出力する
            Log.v("ログ", "arff出力が完了しました。");

        } catch (IOException ex) {
            //例外時処理
            Log.v("ログ", "#2");
            ex.printStackTrace();
        }

        try {//arffよみこみ
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/acceleration.arff");
            BufferedReader br = new BufferedReader(new FileReader(f));
            // 1行ずつCSVファイルを読み込む
            while ((line_arff[line_arff_num] = br.readLine()) != null) {
                Log.v("ログ", line_arff[line_arff_num] );
                line_arff_num++;
            }
            br.close();

        } catch (IOException e) {
            Log.v("ログ", "#3");
            System.out.println(e);
        }

        stage=1;

        Log.v("ログ", "#4");


    }
}
