package com.sh1r0.caffe_android_demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Even on 2017/2/24.
 */

public class CorrectActivity extends AppCompatActivity {

    private String result ="";
    Bitmap bmp;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        final String filePath = intent.getStringExtra("imgPath");

        ImageView image_user = (ImageView) findViewById(R.id.image_user);

        bmp  = BitmapFactory.decodeFile(filePath);
        int pxHeight = (int) convertDpToPixel(160,this);
        int pxWidth = (int) convertDpToPixel(140,this);
        bmp = Bitmap.createScaledBitmap(bmp,pxWidth,pxHeight,false);
        image_user.setImageBitmap(bmp);

        submit = (Button) findViewById(R.id.btnGo);
        submit.setEnabled(true);
        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                final String content =spinner.getSelectedItem().toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        doPost(filePath,content);
                    }
                }).start();

                final ProgressDialog dialog = ProgressDialog.show(CorrectActivity.this, "Uploading...", "Wait for one sec...", true);
                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload Success.", Toast.LENGTH_SHORT).show();

                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 1200);
                submit.setEnabled(false);
                submit.setAlpha(.5f);
            }});
    }


    private String doPost(String imagePath,String labelName) {
        OkHttpClient client = new OkHttpClient();
        File file = new File(imagePath);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MediaType type = MediaType.parse("image/jpeg");
        builder.addFormDataPart("img",file.getName(), RequestBody.create(type,file));
        builder.addFormDataPart("labelName",labelName);
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url("http://10.154.137.167:3000/upload").post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = "fail";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result =  "success";
            }
        });
        return result;
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

    private static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}
