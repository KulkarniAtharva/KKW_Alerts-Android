package com.example.test3;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class recent_assignment extends AppCompatActivity
{
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String teacher_name,title,due_date,assign_url,assignment_id,given_date;

    String year,division,description;
    String teacher_name2;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    FloatingActionButton add;
    BottomSheetBehavior bottomSheetBehavior;
    View view;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_assignment);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        searchView = findViewById(R.id.action_search);

        view = findViewById(R.id.cardBottom);
        bottomSheetBehavior = BottomSheetBehavior.from(view);

        add = findViewById(R.id.add);

        toolbar = findViewById(R.id.tool_Bar);
        toolbar.setTitle("Recent Uploads");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // back button pressed
                onBackPressed();
            }
        });

        boolean status = isNetworkConnectionAvailable();

        Utils.getDatabase();

        /*if (status == false)
        {
            Intent intent = new Intent(this, nointernetpage.class);
            startActivity(intent);

            finish();
        }
        else*/
            recent_assignment("null");

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(recent_assignment.this,upload.class);
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // implement Handler to wait for 3 seconds and then update UI means update value of TextView
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // cancle the Visual indication of a refresh
                        swipeRefreshLayout.setRefreshing(false);
                        // Generate a random integer number
                        /*Random r = new Random();
                        int i1 = r.nextInt(80 - 65) + 65;
                        // set the number value in TextView
                        textView.setText(String.valueOf(i1));*/

                        Toast.makeText(recent_assignment.this, "Assignments Refreshed", Toast.LENGTH_SHORT).show();

                        recent_assignment("null");
                    }
                }, 1500);

                swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if(searchItem != null)
        {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener()
            {
                @Override
                public boolean onClose()
                {
                    //some operation
                    return false;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //some operation
                }
            });
            EditText searchPlate = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");

            View searchPlateView = searchView.findViewById(androidx.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    // use this method when query submitted
                    Toast.makeText(recent_assignment.this, query, Toast.LENGTH_SHORT).show();

                   /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Uploads");//Your firebase node you want to search inside..

                    FirebaseRecyclerOptions<> options =
                            new FirebaseRecyclerOptions.Builder<Products>()//the Products is a class that get and set Strings from Firebase Database.
                                    .setQuery(reference.orderByChild("name").startAt(searchInputUpper).endAt(searchInputLower + "\uf8ff"),download.class)
                                    .build();*/



                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {
                    // use this method for auto complete search process



                    recent_assignment(newText);

                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.filter)
            filter();

        return super.onOptionsItemSelected(item);
    }

    void filter()
    {
        final LinearLayout transBg = findViewById(R.id.transBg);

        transBg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        view.setVisibility(View.VISIBLE);

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

            transBg.setVisibility(View.VISIBLE);
        }

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        view.setVisibility(View.GONE);
                        transBg.setVisibility(View.GONE);

                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:

                       /* if (bottomSheetBehavior instanceof LockableBottomSheetBehavior)
                            ((LockableBottomSheetBehavior) bottomSheetBehavior).setLocked(true);*/

                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(!searchView.isIconified())
        {
            searchView.setIconified(true);
            //findViewById(R.id.default_title).setVisibility(View.VISIBLE);
        }
        else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
        {
            super.onBackPressed();
        }
    }

    void recent_assignment(final String search)
    {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        //ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

       /* // Set BackgroundDrawable
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Recent Uploads");*/

      //  getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
     //   getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  // to make status bar translucent

        //actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        teacher_name = MyObjects.getInstance().firebaseuser.getDisplayName();
       // teacher_name = MyObjects.getInstance().username;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads");

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                for(DataSnapshot childsnapshot1 : dataSnapshot.getChildren())
                {
                    year = childsnapshot1.getRef().getParent().getKey();

                    for (DataSnapshot childsnapshot2 : childsnapshot1.getChildren())
                    {
                        division = childsnapshot2.getRef().getParent().getKey();

                        for (DataSnapshot childsnapshot3 : childsnapshot2.getChildren())
                        {
                            teacher_name2 = childsnapshot3.getRef().getParent().getKey();

                            if (teacher_name2.equals(teacher_name))
                            {
                                for (DataSnapshot childsnapshot4 : childsnapshot3.getChildren())
                                {
                                    String key = childsnapshot4.getKey();
                                    assignment_id = childsnapshot4.getRef().getParent().getKey();

                                    if (key.contentEquals("Title"))
                                        title = childsnapshot4.getValue(String.class);
                                    if (key.contentEquals("Due Date"))
                                        due_date = childsnapshot4.getValue(String.class);
                                    if (key.contentEquals("Given Date"))
                                        given_date = childsnapshot4.getValue(String.class);
                                    if (key.contentEquals("Assignment Url"))
                                        assign_url = childsnapshot4.getValue(String.class);
                                    if (key.contentEquals("Description"))
                                        description = childsnapshot4.getValue(String.class);
                                }

                                String substring_title="";

                                if(search.length() <= title.length() )
                                    substring_title = title.substring(0,search.length());

                                if(search.equalsIgnoreCase(substring_title) || search.equals("null"))
                                    ((RecentAssignmentViewCreator) recyclerView.getAdapter()).update(year,division,title, due_date, given_date, assignment_id, assign_url,description);
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
        recyclerView.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(recent_assignment.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*recyclerView.setLayoutManager(new LinearLayoutManager(show_notice.this));
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/

        RecentAssignmentViewCreator myAdapter = new RecentAssignmentViewCreator(recyclerView, recent_assignment.this,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);
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