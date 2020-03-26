package com.example.test3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
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

public class show_notice extends AppCompatActivity
{
    String notice, date, sender;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notice);

        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");//reference inside Uploads

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                // actually called for indiv items at the database refe...

                //fileName = dataSnapshot.getKey();     // return the filename

                for (DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    String notice_id = childsnapshot.getKey();
                    //Toast.makeText(show_notice.this, k, Toast.LENGTH_LONG).show();
                    DatabaseReference inside_notice = FirebaseDatabase.getInstance().getReference().child("Noties").child(notice_id);

                    for(DataSnapshot childsnapshot2 : childsnapshot.getChildren())
                    {
                        String k= childsnapshot2.getKey();
                        if (k.contentEquals("Date")) {
                            date = childsnapshot2.getValue(String.class);
                        }
                        if (k.contentEquals("Name")) {
                            sender = childsnapshot2.getValue(String.class);
                        }
                        if (k.contentEquals("Text")) {
                            notice = childsnapshot2.getValue(String.class);
                        }
                    }
                    ((NoticeViewCreator)recyclerView.getAdapter()).update(date,sender,notice);
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
        // custom adapters always
        // populate the recycler view with items

        linearLayoutManager = new LinearLayoutManager(show_notice.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        /*recyclerView.setLayoutManager(new LinearLayoutManager(show_notice.this));
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/

        NoticeViewCreator myAdapter = new NoticeViewCreator(recyclerView, show_notice.this, new ArrayList<String>(), new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        actionBar.setTitle("Notices");

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