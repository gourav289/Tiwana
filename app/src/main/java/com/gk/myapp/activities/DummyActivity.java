package com.gk.myapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.distance_db.DatabaseHandler;
import com.gk.myapp.distance_db.DistanceModel;
import com.gk.myapp.utils.MarshMallowPermission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Gk on 11-12-2016.
 */
public class DummyActivity extends BaseActivity {
    TextView btn,txtDistance,txtWrite;
    DatabaseHandler db;
    String showData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIds();
        db =new DatabaseHandler(this);

//        txtDistance.setText(db.getTotalDistance());
        List<DistanceModel> data = db.getPerData();

        for (int i = 0; i < data.size(); i++) {
            showData=showData+"\n\n\n========================="+"\n\n"+data.get(i).getAddress()/*+" , "+data.get(i).getLng()+" || "+data.get(i).getDistance()+" || "+data.get(i).getDate()*/+"\n-----------------------------\nResponse: "+data.get(i).getResponse();
        }

//        writeToFile(showData,this);
        btn.setText(showData);
    }

//    private void writeToFile(String data,Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }

    public void writeHighScore(String highestScore) {
        File data = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
        File file = new File(data, "data.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.valueOf(highestScore));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getIds() {
        btn = (TextView) findViewById(R.id.textView);
        txtDistance= (TextView) findViewById(R.id.txt_distance);
        txtWrite= (TextView) findViewById(R.id.txt_write);

        txtWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DummyActivity.this);
                    if (marshMallowPermission.checkPermissionForExternalStorage()) {
                        new WriteFile().execute(showData);
                    } else {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                } else {
                    new WriteFile().execute(showData);
                }

            }
        });

        txtDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deletePerRecords();
                btn.setText("");
            }
        });
    }

    @Override
    protected String setHeaderTitle() {
        return "My new Title";
    }

    @Override
    protected int setView() {
        return R.layout.dummy1;
    }

    class WriteFile extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... strings) {
            writeHighScore(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgress();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            new WriteFile().equals(showData);
        }
    }

}
