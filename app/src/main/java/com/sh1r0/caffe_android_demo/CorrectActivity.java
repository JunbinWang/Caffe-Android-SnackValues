package com.sh1r0.caffe_android_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CorrectActivity extends Activity {

    private String result ="";
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);

        Intent intent = getIntent();
        final String filePath = intent.getStringExtra("imgPath");
        ImageView resultView = (ImageView) findViewById(R.id.image_user);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bmp  = BitmapFactory.decodeFile(filePath,options);
        resultView.setImageBitmap(bmp);
        Button submit = (Button) findViewById(R.id.correct_button);
        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.input_user);
                final String content =edit.getText().toString();
                if(content.length() == 0){
                    Toast.makeText(getApplicationContext(), "Input can not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            doPost(filePath,content);
                        }
                    }).start();
                    Toast.makeText(getApplicationContext(), "Upload Success.", Toast.LENGTH_SHORT).show();
                }
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
            new AlertDialog.Builder(this).setTitle("Help").setMessage("By upload the actual name of the snack to our server, you can help us improve the app :) ").setPositiveButton("GOT IT !",null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private String doPost(String imagePath,String labelName) {
        OkHttpClient client = new OkHttpClient();
        File file = new File(imagePath);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MediaType type = MediaType.parse("image/jpeg");
        builder.addFormDataPart("img",file.getName(), RequestBody.create(type,file));
        builder.addFormDataPart("labelName",labelName);
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url("http://10.154.137.153:3000/upload").post(requestBody).build();
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
}
