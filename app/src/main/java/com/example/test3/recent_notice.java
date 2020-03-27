package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_notice);
        imageButton = (ImageButton)findViewById(R.id.deletebtn);

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices").child(teacher_name);

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    String k = childsnapshot.getKey();
                    noticeid = childsnapshot.getRef().getParent().getKey();

                    //Toast.makeText(recent_notice.this, noticeid, Toast.LENGTH_SHORT).show();
                    if (k.contentEquals("Date")) {
                        date = childsnapshot.getValue(String.class);
                    }
                    /*if (k.contentEquals("Name")) {
                        sender = childsnapshot.getValue(String.class);
                    }*/
                    if (k.contentEquals("Text")) {
                        notice = childsnapshot.getValue(String.class);
                    }
                }

                ((RecentNoticeViewCreator)recyclerView.getAdapter()).update(date,notice,noticeid);
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

        RecentNoticeViewCreator myAdapter = new RecentNoticeViewCreator(recyclerView, recent_notice.this, new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Recent Notice");

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
}