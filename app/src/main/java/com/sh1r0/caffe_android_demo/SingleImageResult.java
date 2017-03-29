package com.sh1r0.caffe_android_demo;

/**
 * Created by Even on 2017/3/28.
 */

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Even on 2017/3/16.
 */

public class SingleImageResult extends AppCompatActivity {

    private TextView name;
    private ImageView imageDatabase;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_result);
        ApplicationInfo appInfo = getApplicationInfo();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String resultData = intent.getStringExtra("resultData");
        String[] results = resultData.split(" ");

        imageDatabase = (ImageView) findViewById(R.id.image_database);
        int resID = getResources().getIdentifier(results[9], "drawable", appInfo.packageName);
        imageDatabase.setImageResource(resID);

        name = (TextView) findViewById(R.id.name);
        name.setText(results[0]);
        et1 = (EditText) findViewById(R.id.et1);
        et1.setText("Energy: "+results[1]+"kJ / "+results[2]+"kCal");
        et2 = (EditText) findViewById(R.id.et2);
        et2.setText("Fat: "+results[3]+"g, of which Saturates: "+results[4]+"g");
        et3 = (EditText) findViewById(R.id.et3);
        et3.setText("Carbojydrates: "+results[5]+"g, of which Sugars: "+results[6]+"g");
        et4 = (EditText) findViewById(R.id.et4);
        et4.setText("Protein: "+results[7]+"g");
        et5 = (EditText) findViewById(R.id.et5);
        et5.setText("Salt: "+results[8]+"g");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Correct The Result");
        builder.setMessage("If the result is not correct, you can upload the correct name of the snack to the server side!");
        //在这里添加监听！
        builder.setNegativeButton("CORRECT", null);
        builder.setPositiveButton("CANCEL", null);
        builder.show();
    }
}
