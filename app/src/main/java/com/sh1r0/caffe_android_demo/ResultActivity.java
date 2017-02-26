package com.sh1r0.caffe_android_demo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Even on 2017/2/16.
 */

public class ResultActivity extends ListActivity {

    private List<String> data = new ArrayList<String>();
    private Button goBack;
    private Button correct;
    private OkHttpClient client = new OkHttpClient();
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        final String filePath  = intent.getStringExtra("imgPath");
        String resultData = intent.getStringExtra("resultData");
        String[] results = resultData.split(" ");
        ImageView resultView = (ImageView) findViewById(R.id.image_result);
        //Bitmap bmp  = BitmapFactory.decodeFile("/data/data/com.sh1r0.caffe_android_demo/res/drawable/"+results[9]+"");
        // resultView.setImageBitmap(bmp);
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(results[9], "drawable", appInfo.packageName);
        resultView.setImageResource(resID);
        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(results[0]);
        data.add("   Engry: " + results[1]+"kJ/"+results[2]+"kcal");
        data.add("   Fat: " + results[3]+"g, "+ "of which saturates: "+results[4]+"g");
        data.add("   Carbohydrates: " + results[5]+"g, "+ "of which sugars: "+results[6]+"g");
        data.add("   Protein: " + results[7]+"g");
        data.add( "   Salt: " + results[8]+"g");
        data.add("");
        setListAdapter(new ArrayAdapter<String>(this,R.layout.my_listitem,data));

        goBack = (Button) findViewById(R.id.goBack);
        goBack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            Intent bIntent = new Intent();
            bIntent.setClass(ResultActivity.this,MainActivity.class);
            startActivity(bIntent);
        }});

        correct = (Button) findViewById(R.id.goCorrect);
        correct.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            Intent cIntent = new Intent();
            cIntent.putExtra("imgPath",filePath);
            cIntent.setClass(ResultActivity.this,CorrectActivity.class);
            startActivity(cIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            new AlertDialog.Builder(this).setTitle("Help").setMessage("If you are unhappy with the result, you can click the 'Correct it' button to upload your image to our server and help us correct it.").setPositiveButton("GOT IT !",null).show();
        }
        return super.onOptionsItemSelected(item);
    }


}
