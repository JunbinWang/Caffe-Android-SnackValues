package com.sh1r0.caffe_android_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Even on 2017/3/15.
 */

public class InputActivity extends Activity {

    private Button btnGo;
    private Button btnAddNew;
    private String imgPath;
    private Uri fileUri;
    private Spinner spinner;
    private static String[] IMAGENET_CLASSES;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_SELECT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("synset_words.txt");
            Scanner sc = new Scanner(is);
            List<String> lines = new ArrayList<String>();
            while (sc.hasNextLine()) {
                final String temp = sc.nextLine();
                lines.add(temp.substring(temp.indexOf(" ") + 1));
            }
            IMAGENET_CLASSES = lines.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();
        spinner = (Spinner) findViewById(R.id.spinner);

        btnGo = (Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int position = spinner.getSelectedItemPosition();
                Intent intent = new Intent();
                intent.setClass(InputActivity.this, SingleActivity.class);
                intent.putExtra("resultData",IMAGENET_CLASSES[position]);
                startActivity(intent);
                finish();
            }
        });

        btnAddNew = (Button) findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(InputActivity.this).setTitle("Upload New Snacks")
                        .setMessage("ou can upload new kind of snacks to help us improve the system")//设置显示的内容
                            .setPositiveButton("Take Photos",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                    startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                                }})
                        .setNegativeButton("Select Images",new DialogInterface.OnClickListener() {//添加返回按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, REQUEST_IMAGE_SELECT);
                            }})
                        .show();//在按键响应事件中显示此对话框
            }
        });

    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Caffe-Android-Demo");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_SELECT) && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imgPath = fileUri.getPath();
            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = InputActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
            }

            Intent intent = new Intent();
            intent.setClass(InputActivity.this, AddNewActivity.class);
            intent.putExtra("imgPath",imgPath);
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
