package com.example.test3;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import static com.android.volley.VolleyLog.TAG;

public class recent_assignment_each extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    String title,description,duedate,givendate,year,division,url,teachername,assignmentid;
    TextView title_textview,description_textview,duedate_textview,givendate_textview,yeardivision_textview;
    Button viewfile;
    int position;
    TextInputEditText new_duedate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_assignment_each);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Recent Upload");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        duedate = getIntent().getStringExtra("duedate");
        givendate = getIntent().getStringExtra("givendate");
        year = getIntent().getStringExtra("year");
        division = getIntent().getStringExtra("division");
        url = getIntent().getStringExtra("url");
        position =getIntent().getIntExtra("position",0);
        assignmentid = getIntent().getStringExtra("assignmentid");

        title_textview = findViewById(R.id.title);
        description_textview = findViewById(R.id.description);
        duedate_textview = findViewById(R.id.duedate);
        givendate_textview = findViewById(R.id.givendate);
        yeardivision_textview = findViewById(R.id.year_division);
        viewfile = findViewById(R.id.viewfile);

       /* Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        title = bundle.getString("title");*/

        int given_day = Integer.parseInt(givendate.substring(0,givendate.indexOf('/')));
        int given_month = Integer.parseInt(givendate.substring(givendate.indexOf('/')+1,givendate.lastIndexOf('/')));
        int given_year = Integer.parseInt(givendate.substring(givendate.lastIndexOf('/')+1));

        int today_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int today_month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int today_year = Calendar.getInstance().get(Calendar.YEAR);

        if(given_year == today_year && given_month == today_month)
        {
            if(today_day - given_day == 1)
                givendate = "Yesterday";
            else if(today_day == given_day)
                givendate = "Today";
        }

        title_textview.setText(title);
        description_textview.setText(description);
        duedate_textview.setText("Due Date :   "+duedate);
        givendate_textview.setText(givendate);
        yeardivision_textview.setText(year+" "+division);

        viewfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();

                intent.setDataAndType(Uri.parse(url),Intent.ACTION_VIEW);
                startActivity(Intent.createChooser(intent, "Preview with:"));

               // intent.setData()
                //myIntent.setData(Uri.parse(localFile.getTitle()))
            }
        });

        /*delete.setOnClickListener(new View.OnClickListener()
        {
              @Override
              public void onClick(View v)
              {
                   delete();
              }
        });

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(recent_assignment_each.this,upload.class);


                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recent_upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.edit)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog = builder.create();

            View dialoglayout = getLayoutInflater().inflate(R.layout.recentuploadtostudents_edit,null);

            alertDialog.setView(dialoglayout);

            alertDialog.show();

            new_duedate = dialoglayout.findViewById(R.id.new_duedate);

            new_duedate.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {

                    showDatePickerDialog();
                    new_duedate.clearFocus();
                }
            });

            Button edit = dialoglayout.findViewById(R.id.edit);
            Button cancel = dialoglayout.findViewById(R.id.cancel);

            edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    alertDialog.dismiss();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    alertDialog.dismiss();
                }
            });
        }
        else if(id == R.id.delete)
            delete();

        return super.onOptionsItemSelected(item);
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

    void delete()
    {
        //Toast.makeText(context, "clicked at pos "+position, Toast.LENGTH_SHORT).show();

        //Toast.makeText(context, notices.get(position), Toast.LENGTH_SHORT).show();

        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(recent_assignment_each.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure you want to delete this File ?");

        // Set Alert Title
        builder.setTitle("Confirmation !");

        // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will close
        builder.setCancelable(true);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // When the user click yes button

                //final String date = dates.get(position);
                //final String notice_id = noticesids.get(position);

                teachername = MyObjects.getInstance().firebaseuser.getDisplayName();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Uploads").child(year).child(division).child(teachername);

                databaseReference.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            String assignid = childSnapshot.getRef().getParent().getKey();
                            if (key.contentEquals("Assignment Url")) {
                                String assignurl = childSnapshot.getValue(String.class);
                                if (url.contentEquals(assignurl) && assignmentid.contentEquals(assignid)) {
                                    //below is the code for deleting a file from firebase storage using the download url
                                    StorageReference storageReference =
                                            FirebaseStorage.getInstance().getReference().child("Uploads").child(year).child(division).child(teachername).child(assignid);

                                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            finish();
                                            Log.d(TAG, "onSuccess: deleted file successfully");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.d(TAG, "onFailure: File is not delete!");
                                        }
                                    });

                                    childSnapshot.getRef().getParent().removeValue();
                                    //Toast.makeText(context, childSnapshot.getRef().getParent().getKey(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
            }
        });

        // Set the Negative button with No name
        // OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
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

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        String due_d = dayOfMonth +"/"+ (month+1) + "/" + year;
        new_duedate.setText(due_d);
    }
}