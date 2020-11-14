package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class recent_notice extends AppCompatActivity
{
    String teacher_name,date,notice,sender,noticeid;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageButton imageButton;

    String year;
    String division;
    String teacher_name2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_notice);
        imageButton = (ImageButton) findViewById(R.id.deletebtn);

        boolean status = isNetworkConnectionAvailable();

        if (!status)
        {
           // Intent intent = new Intent(this, nointernetpage.class);
            //startActivity(intent);

            finish();
        }
        else
            recent_notice();
    }
    void recent_notice()
    {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                for(DataSnapshot childsnapshot1 : dataSnapshot.getChildren())
                {
                    year = childsnapshot1.getRef().getParent().getKey();

                    for(DataSnapshot childsnapshot2 : childsnapshot1.getChildren())
                    {
                        division = childsnapshot2.getRef().getParent().getKey();

                        for(DataSnapshot childsnapshot3 : childsnapshot2.getChildren())
                        {
                            teacher_name2 = childsnapshot3.getRef().getParent().getKey();

                            if(teacher_name2.equals(teacher_name))
                            {
                                for (DataSnapshot childsnapshot4 : childsnapshot3.getChildren())
                                {
                                    String k = childsnapshot4.getKey();
                                    noticeid = childsnapshot4.getRef().getParent().getKey();

                                    //Toast.makeText(recent_notice.this, noticeid, Toast.LENGTH_SHORT).show();
                                    if(k.contentEquals("Date"))
                                        date = childsnapshot4.getValue(String.class);

                    /*if (k.contentEquals("Name")) {
                        sender = childsnapshot.getValue(String.class);
                    }*/
                                    if(k.contentEquals("Text"))
                                        notice = childsnapshot4.getValue(String.class);
                                }
                                ((RecentNoticeViewCreator)recyclerView.getAdapter()).update(year,division,date,notice,noticeid);
                            }

                        }
                    }
                }

                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.loading).setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(recent_notice.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        /*recyclerView.setLayoutManager(new LinearLayoutManager(show_notice.this));
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/

        RecentNoticeViewCreator myAdapter = new RecentNoticeViewCreator(recyclerView, recent_notice.this,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Recent Notice");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar
    }

    public void checkNetworkConnection()
    {

    }

    public boolean isNetworkConnectionAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else
        {
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}