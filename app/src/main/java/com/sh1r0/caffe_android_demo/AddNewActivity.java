package com.sh1r0.caffe_android_demo;

/**
 * Created by Even on 2017/3/23.
 */

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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

public class AddNewActivity extends AppCompatActivity {

    private String result ="";
    Bitmap bmp;
    private EditText snakeName;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
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

        snakeName = (EditText) findViewById(R.id.snackName);
        snakeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        submit = (Button) findViewById(R.id.btnGo);
        submit.setEnabled(true);
        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(!validateName()) return;
                final String content =snakeName.getText().toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        doPost(filePath,content);
                    }
                }).start();

                final ProgressDialog dialog = ProgressDialog.show(AddNewActivity.this, "Uploading...", "Wait for one sec...", true);
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

    private static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private boolean validateName() {
        if (snakeName.getText().toString().trim().isEmpty()) {
            snakeName.setError("Snack Name can not be empty");
            return false;
        } else {
           return true;
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
