package com.example.test3;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class notice extends AppCompatActivity implements View.OnClickListener
{
    FirebaseDatabase firebaseDatabase;
    private RequestQueue requestQueue;
    Button button;
    String noticetime;
    String teacher_name;
    String notice;
    TextInputEditText textInputEditText;
    String date;
    // FirebaseUser user;
    String user_email;
    FirebaseAuth auth;
    private String Url = "https://fcm.googleapis.com/fcm/send" ;
    private final String CHANNEL_ID = "big_text_style_notification";
    private final int NOTIFICATION_ID = 02;

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

        textInputEditText = (TextInputEditText) findViewById(R.id.notice);

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        firebaseDatabase = FirebaseDatabase.getInstance();
        int yr = Calendar.getInstance().get(Calendar.YEAR);
        int m =  Calendar.getInstance().get(Calendar.MONTH);
        int d =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        date = d + "/" + (m+1) + "/" + yr;

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Send Notice");

        getWindow().setStatusBarColor(getResources().getColor(R.color.green, this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar
    }


    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v)
    {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure you want to send this notice ?");

        // Set Alert Title
        builder.setTitle("Confirmation !");

        // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will close
        builder.setCancelable(true);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.

        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                notice = textInputEditText.getText().toString();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                noticetime = System.currentTimeMillis() + "";

                databaseReference.child("Notices").child(teacher_name).child(noticetime).child("Date").setValue(date);
                databaseReference.child("Notices").child(teacher_name).child(noticetime).child("Name").setValue(teacher_name);
                databaseReference.child("Notices").child(teacher_name).child(noticetime).child("Text").setValue(notice);

                Toast.makeText(notice.this, "Notice Successfully Sent!!!", Toast.LENGTH_SHORT).show();

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

            private void sendNotification(String mHeading, String mBody) {
                FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");

// init request
                requestQueue = Volley.newRequestQueue(notice.this);

                JSONObject mainObject = new JSONObject();
                try {
                    mainObject.put("to", "/topics/" + "NOTIFICATIONS");
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
        builder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
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