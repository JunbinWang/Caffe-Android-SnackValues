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

/**
 * Created by Even on 2017/3/16.
 */

public class SingleActivity extends ListActivity {

    private List<String> data = new ArrayList<String>();
    private Button goBack;
    ImageView singleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);
        ApplicationInfo appInfo = getApplicationInfo();

        Intent intent = getIntent();
        String resultData = intent.getStringExtra("resultData");
        String[] results = resultData.split(" ");

        singleView = (ImageView) findViewById(R.id.image_single);
        int resID = getResources().getIdentifier(results[9], "drawable", appInfo.packageName);
        singleView.setImageResource(resID);

        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(results[0]);
        data.add("");
        data.add("   Engry: " + results[1]+"kJ/"+results[2]+"kcal");
        data.add("   Fat: " + results[3]+"g, "+ "of which saturates: "+results[4]+"g");
        data.add("   Carbohydrates: " + results[5]+"g, "+ "of which sugars: "+results[6]+"g");
        data.add("   Protein: " + results[7]+"g");
        data.add( "   Salt: " + results[8]+"g");
        data.add("");
        setListAdapter(new ArrayAdapter<String>(this,R.layout.my_listitem,data));

        goBack = (Button) findViewById(R.id.goBackSingle);
        goBack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent bIntent = new Intent();
                bIntent.setClass(SingleActivity.this,InputActivity.class);
                startActivity(bIntent);
                finish();
            }});

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
