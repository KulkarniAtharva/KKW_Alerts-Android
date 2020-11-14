package com.example.test3;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;


public class notice extends AppCompatActivity implements View.OnClickListener
{
    FirebaseDatabase firebaseDatabase;
    private RequestQueue requestQueue;
    Button button;
    String noticetime,teacher_name,title,notice;
    TextInputEditText title_textInputEditText,notice_textInputEditText,year_textInputEditText,division_textInputEditText;
    // FirebaseUser user;
    String user_email;
    FirebaseAuth auth;
    private String Url = "https://fcm.googleapis.com/fcm/send" ;
    private final String CHANNEL_ID = "big_text_style_notification";
    private final int NOTIFICATION_ID = 02;

   FloatingActionButton history;

    String year,division,date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        //auth.getInstance();
        //user_email = auth.getInstance().getCurrentUser().getEmail();
        //user_email = user_email.substring(user_email.lastIndexOf('@'),user_email.length());

        button = (Button)findViewById(R.id.sendbtn);
        button.setOnClickListener(this);

        title_textInputEditText = (TextInputEditText) findViewById(R.id.title);
        notice_textInputEditText = (TextInputEditText) findViewById(R.id.notice);
        year_textInputEditText = (TextInputEditText) findViewById(R.id.year);
        division_textInputEditText = (TextInputEditText) findViewById(R.id.division);

        history = findViewById(R.id.history);

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        user_email = MyObjects.getInstance().firebaseuser.getEmail();
        firebaseDatabase = FirebaseDatabase.getInstance();
        int yr = Calendar.getInstance().get(Calendar.YEAR);
        int m =  Calendar.getInstance().get(Calendar.MONTH);
        int d =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        date = d + "/" + (m+1) + "/" + yr;

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Send Notice");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(notice.this,recent_notice.class);
                startActivity(intent);
            }
        });


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

        // setup the alert builder
        final androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
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
        // add OK and Cancel buttons
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user clicked OK

                year_textInputEditText.setText(year);

            }
        });
        builder1.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final androidx.appcompat.app.AlertDialog dialog1 = builder1.create();


        year_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                year_textInputEditText.clearFocus();
                dialog1.show();
            }
        });


        // setup the alert builder
        androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(this);
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
        final androidx.appcompat.app.AlertDialog dialog2 = builder2.create();


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

    @Override
    public void onClick(View v) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure you want to send this notice ?");

        // Set Alert Title
        builder.setTitle("Confirmation !");

        // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will close
        builder.setCancelable(true);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.

        title = title_textInputEditText.getText().toString();
        notice = notice_textInputEditText.getText().toString();

        if(TextUtils.isEmpty(title) && TextUtils.isEmpty(notice) && TextUtils.isEmpty(year) && TextUtils.isEmpty(division))
        {
            title_textInputEditText.setError("Title is Mandatory");
            notice_textInputEditText.setError("Notice is Mandatory");
            year_textInputEditText.setError("Year is Mandatory");
            division_textInputEditText.setError("Division is Mandatory");
        }
        else if(TextUtils.isEmpty(title))
            title_textInputEditText.setError("Title is Mandatory");
        else if(TextUtils.isEmpty(notice))
            notice_textInputEditText.setError("Notice is Mandatory");
        else
        {
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    noticetime = System.currentTimeMillis() + "";

                    databaseReference.child("Notices").child(year).child(division).child(teacher_name).child(noticetime).child("Date").setValue(date);
                    // databaseReference.child("Notices").child(teacher_name).child(noticetime).child("Name").setValue(teacher_name);
                    databaseReference.child("Notices").child(year).child(division).child(teacher_name).child(noticetime).child("Title").setValue(title);
                    databaseReference.child("Notices").child(year).child(division).child(teacher_name).child(noticetime).child("Notice").setValue(notice);

                    Toast.makeText(notice.this, "Notice Successfully Sent!!!", Toast.LENGTH_SHORT).show();

                    //if(user_email.compareTo("adwaitgondhalekar@gmail.com")!=0)
                    sendNotification(teacher_name, notice);

                    // To send notification to teacher about successfully uploaded file

                    createNotificationChannel();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.horn);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.horn));
                    builder.setContentTitle("Notice Sent Successfully");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notice));
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                }

                private void sendNotification(String mHeading, String mBody)
                {
                    //user_email = user_email.substring(user_email.lastIndexOf('@'),user_email.length());
                    // if(user_email.contentEquals("adwaitgondhalekar@gmail.com"))
                    //FirebaseMessaging.getInstance().subscribeToTopic(" NONOTIFICATIONS");
                    //else
                    // FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");


// init request
                    requestQueue = Volley.newRequestQueue(notice.this);

                    JSONObject mainObject = new JSONObject();
                    try {
                        mainObject.put("to", "/topics/" + "STUDENTS");
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

                // Send notice to teacher of successfull notice sent
                //create notification channel if you target android 8.0 or higher version
                private void createNotificationChannel() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "BigTextStyle Notification";
                        String description = "Include all the BigTextStyle notification";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;

                        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                        notificationChannel.setDescription(description);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
            });


            // Set the Negative button with No name OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                }
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }
    }
}