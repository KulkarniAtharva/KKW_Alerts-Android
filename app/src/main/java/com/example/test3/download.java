package com.example.test3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DataTruncation;
import java.util.ArrayList;

public class download extends AppCompatActivity
{
    String fileName;
    RecyclerView recyclerView;
    String url;
    String title;
    int field_count;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);

        recyclerView = findViewById(R.id.recyclerView);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads");//reference inside Uploads

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                // actually called for indiv items at the database refe...

                fileName = dataSnapshot.getKey();     // return the filename
                //String url="";
                //String Title = dataSnapshot.child(fileName).child("Title").getValue(String.class); // Title is not getting any value so url is also not getting retrieved
                //Toast.makeText(download.this, fileName, Toast.LENGTH_SHORT).show();
                //String url = dataSnapshot.child(fileName).child("Assignment Url").getValue(String.class);   // return url for filename


               /* field_count=0;
                for(DataSnapshot childsnapshot : dataSnapshot.getChildren() )
                {
                  String children = childsnapshot.getValue(String.class);
                    if(field_count==0)
                    {
                        url=children;
                    }
                    if(field_count==3)
                    {
                        title = children;
                    }
                    field_count++;
                }*/
               for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
               {
                    url = childsnapshot.getValue(String.class);
                   break;
               }
                for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    title = childsnapshot.getValue(String.class);
                }
                //Toast.makeText(download.this, title, Toast.LENGTH_SHORT).show();
                ((DownloadViewCreator)recyclerView.getAdapter()).update(title,url);
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

        recyclerView = findViewById(R.id.recyclerView);
        // custom adapters always
        // populate the recycler view with items
        recyclerView.setLayoutManager(new LinearLayoutManager(download.this));
        DownloadViewCreator myAdapter = new DownloadViewCreator(recyclerView,download.this,new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }
}