package com.sh1r0.caffe_android_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Even on 2017/2/16.
 */

public class ResultActivity extends AppCompatActivity {

    Bitmap bmp;
    ImageView image_database;
    ImageView image_user;
    private TextView name;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ApplicationInfo appInfo = getApplicationInfo();

        Intent intent = getIntent();
        String resultData = intent.getStringExtra("resultData");
        String[] results = resultData.split(" ");

        image_database = (ImageView) findViewById(R.id.image_database);
        image_user = (ImageView) findViewById(R.id.image_user);

        int resID = getResources().getIdentifier(results[9], "drawable", appInfo.packageName);
        image_database.setImageResource(resID);

        filePath = intent.getStringExtra("imgPath");
       // BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inSampleSize = 4;
      //  bmp = BitmapFactory.decodeFile(filePath, options);

        int pxHeight = (int) convertDpToPixel(140,this);
        int pxWidth = (int) convertDpToPixel(120,this);
        bmp = BitmapFactory.decodeFile(filePath);
        bmp = Bitmap.createScaledBitmap(bmp,pxWidth,pxHeight,false);
        image_user.setImageBitmap(bmp);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            case R.id.correctItem:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Correct The Result");
        builder.setMessage("If the result is not correct, you can upload the correct name of the snack to the server side!");
        //在这里添加监听！
        builder.setNegativeButton("CORRECT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent cIntent = new Intent();
                cIntent.putExtra("imgPath",filePath);
                cIntent.setClass(ResultActivity.this,CorrectActivity.class);
                startActivity(cIntent);
            }});

        builder.setPositiveButton("CANCEL", null);
        builder.show();
    }


    private static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
