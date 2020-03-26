package com.example.test3;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.File;
import java.net.URL;
import java.util.Calendar;

public class upload extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
    Button choose,upload;
    FirebaseStorage storage;        // used for uploading files
    FirebaseDatabase database;      // used for store URLs of uploaded files
    Uri pdfuri;
    ProgressBar progressBar;
    TextView notification;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String filename,filenamefordb;
    TextInputEditText textInputEditText;
    TextView progresspercent,givendate,duedate_txt;
    ImageButton duedate_btn;
    String title,url,given_d,due_d;

    int progress = 0;
    Handler handler;
    CatLoadingView mView;

    private final String CHANNEL_ID = "simple_notification";
    private final int NOTIFICATION_ID = 01;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        mAuth.getInstance();
        MyObjects.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        choose = (Button)findViewById(R.id.choose);
        findViewById(R.id.choose).setOnClickListener(this);

        upload = (Button)findViewById(R.id.upload);
        findViewById(R.id.upload).setOnClickListener(this);

        storage = FirebaseStorage.getInstance();  // return an object of firebase storage
        database = FirebaseDatabase.getInstance();  // returns an object of firebase database

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progresspercent = (TextView)findViewById(R.id.progress_percent);
        notification = findViewById(R.id.notification);

        givendate = (TextView) findViewById(R.id.given_date);

        duedate_txt = (TextView)findViewById(R.id.due_date_txt);
        duedate_btn = (ImageButton) findViewById(R.id.due_date_btn);
        duedate_btn.setOnClickListener(this);

        textInputEditText = (TextInputEditText)findViewById(R.id.title);

        getWindow().setStatusBarColor(getResources().getColor(R.color.green, this.getTheme()));

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Upload");

        int yr = Calendar.getInstance().get(Calendar.YEAR);
        int m =  Calendar.getInstance().get(Calendar.MONTH);
        int d =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        SetGivenDate(yr,m,d);

        //mView = new CatLoadingView();

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    /*@Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            // do your stuff
        } else
            {
            signInAnonymously();
        }
    }*/
    /*private void signInAnonymously()
    {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Fail", "signInAnonymously:FAILURE", exception);
                    }
                });
    }*/

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.choose:
            {
                if (ContextCompat.checkSelfPermission(upload.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Make Sure File is Selected from FILE MANAGER", Toast.LENGTH_LONG).show();
                    selectfile();
                }
                else
                    ActivityCompat.requestPermissions(upload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                break;
            }
            case R.id.upload:
            {
                progress = 0;
                if (pdfuri != null)   //the user has selected a file
                {
                    uploadfile(pdfuri);
                    title = textInputEditText.getText().toString();
                }
                else
                    Toast.makeText(this, "Select a file!!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.due_date_btn:
            {
                showDatePickerDialog();
                break;
            }
        }
    }
        /*switch (v.getId())
        {
            case R.id.choose: showfilechooser();  break;
            case R.id.upload:
        }*/


    protected void SetGivenDate(int year,int month,int DayOfMonth)
    {
        given_d = DayOfMonth +"/"+ month +"/"+ year;
        givendate.setText(given_d);
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,

                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void uploadfile(final Uri pdfuri) {
        /*Progress Bar code
         progressBar = new ProgressBar(this);
        progressBar
       */
        //File file = new File(String.valueOf(pdfuri));
        //final String filename = file.get

        final String u_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        //final String filename = System.currentTimeMillis() + "";
        //final String filename = pdfuri.getLastPathSegment().toString();
        String path = pdfuri.getPath();

        if (path != null) {
            filename = path.substring(path.lastIndexOf('/'), path.lastIndexOf('.'));
            filenamefordb = System.currentTimeMillis()+"";
        }

        final StorageReference storageReference = storage.getReference().child("Uploads").child(u_name).child(filenamefordb);   // returns root path of the database
        storageReference.putFile(pdfuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // String url = taskSnapshot.getStorage().getDownloadUrl().toString();  //return the url of the uploaded file

                        /*String url = storageReference.getDownloadUrl().getResult().toString();*/

                        //String url;

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                url = downloadUrl.toString();

                                final DatabaseReference reference = database.getReference();      //return the path to the root
                                //String url = taskSnapshot.getStorage().child("Uploads").child(u_name).child(filename).getDownloadUrl().toString();


                                reference.child("Uploads").child(u_name).child(filenamefordb).child("Assignment Url").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            reference.child("Uploads").child(u_name).child(filenamefordb).child("Title").setValue(title);
                                            reference.child("Uploads").child(u_name).child(filenamefordb).child("Given Date").setValue(given_d);
                                            reference.child("Uploads").child(u_name).child(filenamefordb).child("Due Date").setValue(due_d);


                                            //Toast.makeText(upload.this, "File Successfully Uploaded", Toast.LENGTH_LONG).show();

                                            /*toast = Toast.makeText(upload.this,"",Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            View view = getLayoutInflater().inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast));*/

                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                            toast.setView(getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast)));
                                            toast.show();
                                        } else
                                            Toast.makeText(upload.this, "File Not Uploaded", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                        // String url = "SAk";
                        //Toast.makeText(upload.this,temp.toString() , Toast.LENGTH_LONG).show();

                        /*storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                 downloadUrl = uri;
                                //Do what you want with the url
                            }
                            //Toast.makeText(MtActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                        });
                        String url = downloadUrl.toString();
                        Toast.makeText(upload.this, url, Toast.LENGTH_LONG).show();*/

                        //store the url in realtime database
                        //String url = storageReference.child("Uploads").child(u_name).child(filename).getDownloadUrl().toString();


                        // To send notification to teacher about successfully uploaded file
                        createNotificationChannel();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                        builder.setSmallIcon(R.drawable.horn);
                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.horn));
                        builder.setContentTitle("File Successfully Uploaded");
                        builder.setContentText(filename.substring(1));
                        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(upload.this, "File Not Uploaded hi", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //Track the progress of = Our upload...........
                int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                Toast.makeText(upload.this, Integer.toString(currentprogress), Toast.LENGTH_SHORT).show();

                /*for(int j=0;j<33;j++)
                {
                    try
                    {
                        Thread.sleep(20);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    //Toast.makeText(upload.this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
                    progressBar.setProgress(progress);
                    progresspercent.setText("Uploading "+progress + " %");
                    progress++;
                }*/

                /*handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(progressBar.getProgress()<100)
                        {
                            progressBar.setProgress(progress);
                            progress++;
                            handler.postDelayed(this,150);
                            progresspercent.setText("Uploading "+progress+ " %");
                        }
                        else
                            progresspercent.setText("Uploaded 100 %");
                    }
                },100);*/

                //mView.show(getSupportFragmentManager(), "");
            }

            int status = 0;
            Handler handler = new Handler();

            String msg = "Atharva";

            public void showDialog(Activity activity, String msg) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.upload);

                final ProgressBar text = (ProgressBar) dialog.findViewById(R.id.progress_horizontal);
                final TextView text2 = dialog.findViewById(R.id.value123);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (status < 100) {

                            status += 1;

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    text.setProgress(status);
                                    text2.setText(String.valueOf(status));

                                    if (status == 100) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                }).start();


                dialog.show();

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });

    }

    //create notification channel if you target android 8.0 or higher version
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Simple Notification";
            String description = "Include all the simple notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void selectfile()
    {
        //  to offer user to select file using file manager
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(intent.ACTION_GET_CONTENT);    // To fetch files
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86 && resultCode == RESULT_OK && data != null)
        {
            pdfuri = data.getData();    // return the uri of selected file
            notification.setText("Selected file is : " + data.getData().getLastPathSegment());
        }
        else
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            selectfile();
        else
            Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        due_d = dayOfMonth +"/"+ month + "/" + year;

        duedate_txt.setText(due_d);
    }


   /* Video 1 failed code
     private void showfilechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,));
    }*/
}