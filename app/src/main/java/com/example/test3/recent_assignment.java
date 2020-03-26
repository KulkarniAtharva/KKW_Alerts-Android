package com.example.test3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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

public class recent_assignment extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String teacher_name,title,due_date,assign_url,assignment_id,given_date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_assignment);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Recent Assignment");

        getWindow().setStatusBarColor(getResources().getColor(R.color.green, this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads").child(teacher_name);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                for(DataSnapshot childsnapshot:dataSnapshot.getChildren())
                {
                    String key = childsnapshot.getKey();
                    assignment_id = childsnapshot.getRef().getParent().getKey();

                    if(key.contentEquals("Title"))
                    {
                        title = childsnapshot.getValue(String.class);
                    }
                    if(key.contentEquals("Due Date")) {
                        due_date = childsnapshot.getValue(String.class);
                    }
                    if(key.contentEquals("Given Date"))
                    {
                        given_date = childsnapshot.getValue(String.class);
                    }
                    if(key.contentEquals("Assignment Url"))
                    {
                        assign_url = childsnapshot.getValue(String.class);
                    }
                }
                ((RecentAssignmentViewCreator)recyclerView.getAdapter()).update(title,due_date,given_date,assignment_id,assign_url);
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
        linearLayoutManager = new LinearLayoutManager(recent_assignment.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*recyclerView.setLayoutManager(new LinearLayoutManager(show_notice.this));
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/

        RecentAssignmentViewCreator myAdapter = new RecentAssignmentViewCreator(recyclerView, recent_assignment.this, new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}