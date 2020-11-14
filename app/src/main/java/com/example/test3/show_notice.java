package com.example.test3;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class show_notice extends AppCompatActivity
{
    String notice, date, sender,title;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    View view;

    ListView listView;

    private String year,division,y,d,n,email_all,user_email;
    int flag = 0,count = 0,count3=0;
    boolean status;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    ActionBar actionBar;
    Window window;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notice);

        recyclerView = findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        searchView = findViewById(R.id.action_search);

       // bottomSheetDialog = (BottomSheetDialog) DialogInterface;

        view = findViewById(R.id.cardBottom);

        bottomSheetBehavior = BottomSheetBehavior.from(view);

       // status = isNetworkConnectionAvailable();


        //year = MyObjects.getInstance().year;
        //division = MyObjects.getInstance().division;

       /* Bundle bundle = getIntent().getExtras();
        assert bundle!= null;
        year = bundle.getString("year");
        division = bundle.getString("division");*/

        user_email = MyObjects.getInstance().user_email;

        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Notice");

        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        window.setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));
       // window.setStatusBarColor(Color.parseColor(color));


        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        count = 0;
        /*
            DISK PERSISTENCE


            Firebase apps automatically handle temporary network interruptions. Cached data is available while offline and
            Firebase resends any writes when network connectivity is restored.

            When you enable disk persistence, your app writes the data locally to the device so your app can maintain state
            while offline, even if the user or operating system restarts the app.

         */
        Utils.getDatabase();

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("students");//reference inside Users

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                count++;    // to manage asynchronous call

                // actually called for indiv items at the database refe...

                //fileName = dataSnapshot.getKey();     // return the filename

                for (DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    flag = 0;
                    y = childsnapshot.getRef().getParent().getKey();

                    for (DataSnapshot childsnapshot2 : childsnapshot.getChildren())
                    {
                        d = childsnapshot2.getRef().getParent().getKey();

                        for (DataSnapshot childsnapshot3 : childsnapshot2.getChildren())
                        {
                            n = childsnapshot3.getRef().getParent().getKey();

                            String key = childsnapshot3.getKey();
                            //Toast.makeText(download.this, key, Toast.LENGTH_SHORT).show();
                            if (key.contentEquals("Email"))
                            {
                                email_all = childsnapshot3.getValue(String.class);

                                if (email_all.equals(user_email))
                                {
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 1)
                            break;
                    }
                    if (flag == 1)
                    {
                        division = d;
                        year = y;
                        break;
                    }
                }

                if(year != null && division != null && count == 1)
                {
                   /* if (!status)
                    {
                        Intent intent = new Intent(show_notice.this, nointernetpage.class);
                        startActivity(intent);

                        finish();
                    }
                    else*/




         /*   }

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
        });*/

        databaseHelper = new DatabaseHelper(this);

        Cursor res = databaseHelper.getData("1");
        if(res.moveToFirst())
        {
            year = res.getString(1);
            division = res.getString(2);
        }

        show_notice1(year, division,"null");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null)
        {
            searchView =  (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener()
            {
                @Override
                public boolean onClose()
                {
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
                    Toast.makeText(show_notice.this, query, Toast.LENGTH_SHORT).show();

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

                    show_notice1(year,division,newText);

                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    void show_notice1(final String year , final String division,final String search)
    {
        count3=0;

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

                        Toast.makeText(show_notice.this, "Assignments Refreshed", Toast.LENGTH_SHORT).show();

                        show_notice1(year,division,"null");
                    }
                }, 700);

                swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices").child(year).child(division);//reference inside Uploads

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                // actually called for indiv items at the database refe...

                //fileName = dataSnapshot.getKey();     // return the filename

                for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    //String notice_id = childsnapshot.getKey();
                    sender = childsnapshot.getRef().getParent().getKey();
                    //Toast.makeText(show_notice.this, k, Toast.LENGTH_LONG).show();
                    //DatabaseReference inside_notice = FirebaseDatabase.getInstance().getReference().child("Noties").child(notice_id);

                    for(DataSnapshot childsnapshot2 : childsnapshot.getChildren())
                    {
                        String k= childsnapshot2.getKey();
                        if (k.contentEquals("Date"))
                            date = childsnapshot2.getValue(String.class);
                        if (k.contentEquals("Title"))
                           title = childsnapshot2.getValue(String.class);
                        if (k.contentEquals("Notice"))
                            notice = childsnapshot2.getValue(String.class);
                    }

                    String substring_title="";

                    if(search.length() <= title.length() )
                        substring_title = title.substring(0,search.length());

                    if(search.equalsIgnoreCase(substring_title) || search.equals("null"))
                    {
                        count3++;
                        ((NoticeViewCreator) recyclerView.getAdapter()).update(sender, date, title, notice);
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

        // custom adapters always
        // populate the recycler view with items

        linearLayoutManager = new LinearLayoutManager(show_notice.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*recyclerView.setLayoutManager(new LinearLayoutManager(show_notice.this));
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/

        NoticeViewCreator myAdapter = new NoticeViewCreator(recyclerView,bottomSheetBehavior,actionBar,searchView,window,show_notice.this, new ArrayList<String>(), new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());

        recyclerView.setAdapter(myAdapter);
    }

    public void checkNetworkConnection()
    {

    }

    public void onBackPressed()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if(!searchView.isIconified())
        {
            searchView.setIconified(true);
            //findViewById(R.id.default_title).setVisibility(View.VISIBLE);
        }
        else
            finish();
    }

    public boolean isNetworkConnectionAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected)
        {
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


/*
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_marginRight="5dp"
        android:layout_marginLeft="5dp"
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/green"/>



 */

/*

<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/cardView"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        app:cardCornerRadius="4dp"
        app:cardUseCompatPadding="true"
        app:cardMaxElevation="1dp"
        app:cardElevation="1dp">

<TextView
    android:id="@+id/givend"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="right"
    android:layout_marginRight="5dp" />

<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:layout_gravity="bottom">

    <androidx.cardview.widget.CardView
        android:id="@+id/cardView_circle"
        android:layout_width="60dp"
        android:layout_height="60dp"
        app:cardCornerRadius="60dp"
        app:cardElevation="3dp"
        android:layout_marginLeft="10dp"
        android:layout_marginBottom="10dp"
        app:cardBackgroundColor="@color/lightgreen">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/initial"
            android:textStyle="bold"
            android:textSize="25dp"
            android:layout_gravity="center_horizontal|center_vertical"
            android:textColor="@color/black"/>

    </androidx.cardview.widget.CardView>

    <TextView
        android:id="@+id/nameofFile"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:textAlignment="center"
        android:textStyle="bold|italic"
        android:textSize="20dp"
        android:textColor="@color/black"/>

</LinearLayout>

</androidx.cardview.widget.CardView>

 */