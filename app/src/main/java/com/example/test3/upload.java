package com.example.test3;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

public class upload extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
    Button choose;
    FirebaseStorage storage;        // used for uploading files
    FirebaseDatabase database;      // used for store URLs of uploaded files
    FirebaseFirestore firebaseFirestore;
    Uri pdfuri;
    ProgressBar progressBar;
    TextView notification;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String filename,filenamefordb;
    TextInputEditText title_textInputEditText,description_textInputEditText,duedate_textInputEditText;
    TextInputEditText year_textInputEditText,division_textInputEditText;
    TextView progresspercent,givendate,duedate_txt;
    String title,url,given_d,due_d,description;
    private RequestQueue requestQueue;
    private String Url = "https://fcm.googleapis.com/fcm/send" ;

    String year,division;

    int progress = 0;
    Handler handler;


    private final String CHANNEL_ID = "simple_notification";
    private final int NOTIFICATION_ID = 01;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  // to make status bar translucent

        Toolbar toolbar = findViewById(R.id.tool_Bar);
        toolbar.setTitle("Upload");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mAuth.getInstance();
        MyObjects.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        choose = (Button)findViewById(R.id.choose);
        findViewById(R.id.choose).setOnClickListener(this);

        storage = FirebaseStorage.getInstance();  // return an object of firebase storage
        database = FirebaseDatabase.getInstance();  // returns an object of firebase database


       /* String projectID = "secondary1";

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("kkw-alerts---comp")
                .setApplicationId("1:252227408959:android:06723641f1c807e436d48a")
                .setApiKey("AIzaSyCnoAP94V_gUJbE86exGeV7pRL08TcKI3s")
                .setDatabaseUrl("https://kkw-alerts---comp.firebaseio.com")
                .setStorageBucket("kkw-alerts---comp.appspot.com")
                .build();

        FirebaseApp.initializeApp(this /* Context *//*, options,projectID);       // Initialize with secondary app
        FirebaseApp secondary = FirebaseApp.getInstance(projectID);     // Retrieve secondary FirebaseApp
        firebaseFirestore = FirebaseFirestore.getInstance(secondary);      // Access a Cloud Firestore instance from your Activity
        storage = FirebaseStorage.getInstance(secondary);   */

        notification = findViewById(R.id.notification);

        title_textInputEditText = (TextInputEditText)findViewById(R.id.title);
        description_textInputEditText = (TextInputEditText)findViewById(R.id.description);
        duedate_textInputEditText = (TextInputEditText)findViewById(R.id.due_date);
        year_textInputEditText = (TextInputEditText)findViewById(R.id.year);
        division_textInputEditText = (TextInputEditText)findViewById(R.id.division);

        duedate_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                duedate_textInputEditText.clearFocus();
                showDatePickerDialog();
            }
        });

        //textInputEditText.addTextChangedListener(watcher);

       // getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
       // getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

       // ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        //ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        //actionBar.setBackgroundDrawable(colorDrawable);
       // actionBar.setTitle("Upload");

        int yr = Calendar.getInstance().get(Calendar.YEAR);
        int m =  Calendar.getInstance().get(Calendar.MONTH);
        int d =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        SetGivenDate(yr,m+1,d);

       // actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar



        /*spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        final ArrayList<String> branch1 = new ArrayList<>();
        branch1.add("Select Year");
        branch1.add("FE");
        branch1.add("SE");
        branch1.add("TE");
        branch1.add("BE");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branch1);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0: year = "null"; break;
                    case 1: year = "FE";  break;
                    case 2: year = "SE";  break;
                    case 3: year = "TE";  break;
                    case 4: year = "BE";  break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayList<String> branch2 = new ArrayList<>();
        branch2.add("Select Division");
        branch2.add("A");
        branch2.add("B");
        branch2.add("C");
        branch2.add("D");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branch2);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0: division = "null"; break;
                    case 1: division = "A";  break;
                    case 2: division = "B";  break;
                    case 3: division = "C";  break;
                    case 4: division = "D";  break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose a Year");
        // add a radio button list
        String[] years = {"FE", "SE", "TE", "BE"};
        int checkedItem = -1; // No item selected
        builder1.setSingleChoiceItems(years, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: year = "FE";  break;
                    case 1: year = "SE";  break;
                    case 2: year = "TE";  break;
                    case 3: year = "BE";  break;
                }

            }
        });
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                year_textInputEditText.setText(year);

            }
        });
        builder1.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final AlertDialog dialog1 = builder1.create();


        year_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                year_textInputEditText.clearFocus();
                dialog1.show();
            }
        });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Choose a Division");
        // add a radio button list
        String[] divisions = {"A", "B", "C", "D"};
         checkedItem = -1;
        builder2.setSingleChoiceItems(divisions, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: division = "A";  break;
                    case 1: division = "B";  break;
                    case 2: division = "C";  break;
                    case 3: division = "D";  break;
                }
            }
        });
        // add OK and Cancel buttons
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user clicked OK
                division_textInputEditText.setText(division);
            }
        });
        builder2.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final AlertDialog dialog2 = builder2.create();


        division_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                division_textInputEditText.clearFocus();
                dialog2.show();
            }
        });
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
                    if (ContextCompat.checkSelfPermission(upload.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast toast = Toast.makeText(this, "Make Sure File is Selected from FILE MANAGER", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        selectfile();
                    }
                    else
                        ActivityCompat.requestPermissions(upload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                break;
            }
        }
    }

    protected void SetGivenDate(int year,int month,int DayOfMonth)
    {
        given_d = DayOfMonth +"/"+ month +"/"+ year;
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,

                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // this sets today's date as minimum date and all the past dates are disabled.
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.upload)
        {
            title = title_textInputEditText.getText().toString();
            description = description_textInputEditText.getText().toString();

            if(TextUtils.isEmpty(title) && TextUtils.isEmpty(year) && TextUtils.isEmpty(division))
            {
                title_textInputEditText.setError("Title is Mandatory");
                year_textInputEditText.setError("Year is Mandatory");
                division_textInputEditText.setError("Division is Mandatory");
            }
            else if(TextUtils.isEmpty(title) && TextUtils.isEmpty(year))
            {
                title_textInputEditText.setError("Title is Mandatory");
                year_textInputEditText.setError("Year is Mandatory");
            }
            else if(TextUtils.isEmpty(year) && TextUtils.isEmpty(division))
            {
                year_textInputEditText.setError("Year is Mandatory");
                division_textInputEditText.setError("Division is Mandatory");
            }
            else if(TextUtils.isEmpty(title) && TextUtils.isEmpty(division))
            {
                title_textInputEditText.setError("Title is Mandatory");
                division_textInputEditText.setError("Division is Mandatory");
            }
            else
            {
                progress = 0;
                if (pdfuri != null)   //the user has selected a file
                    uploadfile(pdfuri);
                else
                    Toast.makeText(this, "Select a file !!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadfile(final Uri pdfuri)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.uploading_dialog, null);

        alertDialog.setView(dialoglayout);

        progressBar = dialoglayout.findViewById(R.id.progressBar);
        progresspercent = dialoglayout.findViewById(R.id.progress_percent);

        TextView dialog_title = dialoglayout.findViewById(R.id.title);
        dialog_title.setText(title);


        /*Progress Bar code
         progressBar = new ProgressBar(this);
        progressBar
       */
        //File file = new File(String.valueOf(pdfuri));
        //final String filename = file.get

        final String u_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        //final String u_name = MyObjects.getInstance().username;

        //final String filename = System.currentTimeMillis() + "";
        //final String filename = pdfuri.getLastPathSegment().toString();
        String path = pdfuri.getPath();

        final String uid = MyObjects.getInstance().firebaseuser.getUid();
        final String photouri = MyObjects.getInstance().personphotoUri;


        if (path != null)
        {
            filename = path.substring(path.lastIndexOf('/'), path.lastIndexOf('.'));
            filenamefordb = System.currentTimeMillis()+"";
        }

        final StorageReference storageReference = storage.getReference().child("Uploads").child(division).child(u_name).child(filenamefordb);   // returns root path of the database

        Toast.makeText(this, storageReference+"", Toast.LENGTH_SHORT).show();

        storageReference.putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        // String url = taskSnapshot.getStorage().getDownloadUrl().toString();  //return the url of the uploaded file

                        // String url = storageReference.getDownloadUrl().getResult().toString();

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri downloadUrl)
                            {
                                alertDialog.dismiss();

                                url = downloadUrl.toString();

                                final DatabaseReference reference = database.getReference();      //return the path to the root
                                //String url = taskSnapshot.getStorage().child("Uploads").child(u_name).child(filename).getDownloadUrl().toString();

                                reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Assignment Url").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Title").setValue(title);
                                            reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Description").setValue(description);
                                            reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Given Date").setValue(given_d);

                                            if(due_d != null)
                                                reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Due Date").setValue(due_d);
                                            else
                                                reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Due Date").setValue("null");
                                            reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("Teachername").setValue(u_name);
                                            reference.child("Uploads").child(year).child(division).child(uid).child(filenamefordb).child("TeacherphotoUri").setValue(photouri);

                                            sendNotification(u_name,title);
                                        }
                                        else
                                            Toast.makeText(upload.this, "File Not Uploaded", Toast.LENGTH_LONG).show();
                                    }
                                });

                             /*   // Create a new user with a first and last name
                                Map<String, Object> user = new HashMap<>();
                                user.put("Title", title);
                                user.put("Description", description);
                                user.put("Given date", given_d);
                                if(due_d != null)
                                    user.put("Due Date",due_d);
                                else
                                    user.put("Due Date","null");
                                user.put("TeacherName",u_name);
                                user.put("TeacherPhotoUri",photouri);


                                // Add a new document with a generated ID
                                firebaseFirestore.collection(division).document("Assignments").collection(uid).document(filenamefordb).set(user).addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(upload.this, "Success", Toast.LENGTH_SHORT).show();
                                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener()
                                        {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                Toast.makeText(upload.this, "Failure", Toast.LENGTH_SHORT).show();
                                                //Log.w(TAG, "Error adding document", e);
                                            }
                                        });*/


                            }
                        });

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
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(upload.this, "File Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                builder.setCancelable(false);

                //Track the progress of = Our upload...........
                int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress(currentprogress);
                progresspercent.setText(currentprogress+" %");

                alertDialog.show();

                //mView.show(getSupportFragmentManager(), "");
            }
        });
    }
    private void sendNotification(String mHeading, String mBody)
    {
        //user_email = user_email.substring(user_email.lastIndexOf('@'),user_email.length());
        // if(user_email.contentEquals("adwaitgondhalekar@gmail.com"))
        //FirebaseMessaging.getInstance().subscribeToTopic(" NONOTIFICATIONS");
        //else
        // FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");


// init request
        requestQueue = Volley.newRequestQueue(upload.this);

        JSONObject mainObject = new JSONObject();
        try
        {
            mainObject.put("to", "/topics/"+"STUDENTS-"+year+"-"+division);
            //mainObject.put("to","AoaWT33NagbjVo9xavS0mug6Sn83");
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", mHeading);
            notificationObject.put("body", mBody);
            mainObject.put("notification", notificationObject);
            JsonObjectRequest request = new
                    JsonObjectRequest(Request.Method.POST, Url,
                            mainObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // send successfully
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // failed
                        }
                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> header = new HashMap<>();
                            header.put("content-type", "application/json");
                            header.put("authorization", "key=AIzaSyBt-7syrkRRd9Vc7k6-gNjbvuXNbh6wo4Y");
                            return header;
                        }
                    };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
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
            notification.setText("Selected file is :  " + data.getData().getLastPathSegment());
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
        due_d = dayOfMonth +"/"+ (month+1) + "/" + year;
        duedate_textInputEditText.setText(due_d);
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


   /* Video 1 failed code
     private void showfilechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,));
    }*/
}