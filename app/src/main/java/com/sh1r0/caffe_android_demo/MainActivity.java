package com.sh1r0.caffe_android_demo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sh1r0.caffe_android_lib.CaffeMobile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements CNNListener {
    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_SELECT = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private String imgPath;
    private static String[] IMAGENET_CLASSES;

    private Uri fileUri;
    private ProgressDialog dialog;
    private CaffeMobile caffeMobile = null;
    File sdcard = Environment.getExternalStorageDirectory();
    String modelDir = sdcard.getAbsolutePath() + "/caffe_mobile/new_snack_model";
    String modelProto = modelDir + "/deploy.prototxt";
    String modelBinary = modelDir + "/caffenet_train_iter_50000.caffemodel";
    String meanFileDir = modelDir + "/train_mean.binaryproto";

    RecyclerView rv;
    private BottomSheetDialog mBottomSheetDialog;
    private FloatingActionButton fabButton;
    private static final int TAKE_PHOTO_BUTTON = 0;
    private static final int SELECT_PHOTO_BUTTON = 1;
    private static final int CANCEL_BUTTON = 2;
    private static final int DIALOG_PEEK_HEIGHT = 800;
    private boolean isAddNew = false;

    static {
        System.loadLibrary("caffe");
        System.loadLibrary("caffe_jni");
    }

    private List<Snack> snacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(caffeMobile == null) {
            caffeMobile = new CaffeMobile();
            caffeMobile.setNumThreads(4);
            caffeMobile.loadModel(modelProto, modelBinary);
            caffeMobile.setMean(meanFileDir);
        }

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("snack_data.txt");
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

        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        initialRecycleData();
        RVAdapter adapter = new RVAdapter(snacks);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SingleImageResult.class);
                        intent.putExtra("resultData",IMAGENET_CLASSES[position]);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        fabButton = (FloatingActionButton)findViewById(R.id.fab);
        fabButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_SELECT) && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imgPath = fileUri.getPath();
            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = MainActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
            }
            if(!isAddNew) {
                dialog = ProgressDialog.show(MainActivity.this, "Predicting...", "Wait for one sec...", true);
                CNNTask cnnTask = new CNNTask(MainActivity.this);
                cnnTask.execute(imgPath);
            }else{
                isAddNew = false;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddNewActivity.class);
                intent.putExtra("imgPath",imgPath);
                startActivity(intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private class CNNTask extends AsyncTask<String, Void, Integer> {
        private CNNListener listener;
        private long startTime;
        public CNNTask(CNNListener listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            startTime = SystemClock.uptimeMillis();
            return caffeMobile.predictImage(strings[0])[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.i(LOG_TAG, String.format("elapsed wall time: %d ms", SystemClock.uptimeMillis() - startTime));
            listener.onTaskCompleted(integer);
            super.onPostExecute(integer);
        }
    }

    @Override
    public void onTaskCompleted(int result) {

        if (dialog != null) {
            dialog.dismiss();
        }
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ResultActivity.class);
        intent.putExtra("resultData",IMAGENET_CLASSES[result]);
        intent.putExtra("imgPath",imgPath);
        startActivity(intent);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // show the bottomshettDialog when the Camera icon is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        showBottomSheetDialog();
        return super.onOptionsItemSelected(item);
    }


    // initial the Recycle View data for the main page.
    private void initialRecycleData(){
        ApplicationInfo appInfo = getApplicationInfo();
        snacks = new ArrayList<>();
        for (int i=0;i<IMAGENET_CLASSES.length;i++){
            String[] snackData = IMAGENET_CLASSES[i].split(" ");
            int imgResID = getResources().getIdentifier(snackData[9], "drawable", appInfo.packageName);
            snacks.add(new Snack(snackData[0],snackData[1],imgResID));
        }
    }


    private void showBottomSheetDialog() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BottomSheetDialogAdapter(createItems(), new BottomSheetDialogAdapter.ItemListener() {
            @Override
            public void onItemClick(BottomSheetItem item) {
                if (mBottomSheetDialog != null) {
                }
            }
        }));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        isAddNew = false;
                        if(position==TAKE_PHOTO_BUTTON){
                            mBottomSheetDialog.dismiss();
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                      }else if(position==SELECT_PHOTO_BUTTON){
                            mBottomSheetDialog.dismiss();
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, REQUEST_IMAGE_SELECT);
                        }else if(position==CANCEL_BUTTON){
                            mBottomSheetDialog.dismiss();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        //set the divider
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mBottomSheetDialog.setContentView(view);
        // set the height
        View dialogView = mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(dialogView).setPeekHeight(DIALOG_PEEK_HEIGHT);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

    }

    // Initial the data for bottom sheet dialog
    private List<BottomSheetItem> createItems() {

        ArrayList<BottomSheetItem> items = new ArrayList<>();
        items.add(new BottomSheetItem("Take A Photo"));
        items.add(new BottomSheetItem("Select A photo"));
        items.add(new BottomSheetItem("Cancel"));
        return items;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Add New Snacks");
        builder.setMessage("You can add new snacks to this App by uploading images from your mobile phone!");
       //在这里添加监听！
        builder.setNegativeButton("Select A Photo  ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isAddNew = true;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_IMAGE_SELECT);
            }});

        builder.setPositiveButton("   Take A Photo   ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isAddNew = true;
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }});
        builder.show();
    }

}
