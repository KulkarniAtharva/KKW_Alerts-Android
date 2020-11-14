package com.example.test3;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class download extends AppCompatActivity
{
    String fileName;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String url;
    String title,due_date,given_date,description,uid,username,userphotoUri;

    ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();


    CustomAdapter adapter;

    private String year,division,y,d,n,email_all,user_email;
    int flag = 0,count;

    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    DatabaseReference databaseReference;
    DatabaseHelper databaseHelper;

    static int count2 = 0,count3 = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        searchView = findViewById(R.id.action_search);

        /*Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Userid = bundle.getString("Userid");*/


        user_email = MyObjects.getInstance().user_email;

        databaseHelper = new DatabaseHelper(this);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Assignments");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

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

        /* databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("students");//reference inside Users

            databaseReference.addChildEventListener(new ChildEventListener()
            {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                {
                    count++;    // to manage asynchronous call


                    // actually called for indiv items at the database refe...

                    //fileName = dataSnapshot.getKey();     // return the filename

                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                        flag = 0;
                        y = childsnapshot.getRef().getParent().getKey();

                        for (DataSnapshot childsnapshot2 : childsnapshot.getChildren()) {
                            d = childsnapshot2.getRef().getParent().getKey();

                            for (DataSnapshot childsnapshot3 : childsnapshot2.getChildren()) {
                                n = childsnapshot3.getRef().getParent().getKey();

                                String key = childsnapshot3.getKey();
                                //Toast.makeText(download.this, key, Toast.LENGTH_SHORT).show();
                                if (key.contentEquals("Email")) {
                                    email_all = childsnapshot3.getValue(String.class);

                                    if (email_all.equals(user_email)) {
                                        flag = 1;
                                        break;
                                    }
                                }
                            }
                            if (flag == 1)
                                break;
                        }
                        if (flag == 1) {
                            division = d;
                            year = y;
                            break;
                        }
                    }

                    if(year != null && division != null && count == 1)
                    {
                        /*if (!status)
                        {
                            Intent intent = new Intent(download.this, nointernetpage.class);
                            startActivity(intent);

                            finish();
                        }
                        else*/
             /*               download(year, division,"null");
                    }
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
            });*/

        //year = MyObjects.getInstance().year;
        //division = MyObjects.getInstance().division;

        /*SharedPreferences sharedPreferences = getSharedPreferences("User Info",Context.MODE_PRIVATE);
        year = sharedPreferences.getString("year","");
        division = sharedPreferences.getString("division","");*/

        Cursor res = databaseHelper.getData("1");
        if(res.moveToFirst())
        {
            year = res.getString(1);
            division = res.getString(2);
        }

        download(year,division,"null");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null)
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
                    Toast.makeText(download.this, query, Toast.LENGTH_SHORT).show();

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



                    download(year,division,newText);

                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed()
    {
        if (!searchView.isIconified())
        {
            searchView.setIconified(true);
            //findViewById(R.id.default_title).setVisibility(View.VISIBLE);
        }
        else
            {
            super.onBackPressed();
        }
    }

    void download(final String year, final String division, final String search)
    {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        count3 = 0;


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                boolean status = isNetworkConnectionAvailable();

                @Override
                public void onRefresh()
                {
                    if(status)
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

                                Toast.makeText(download.this, "Assignments Refreshed", Toast.LENGTH_SHORT).show();

                                download(year, division, "null");
                            }
                        }, 700);
                        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
                    }
                    else
                        {
                        Toast.makeText(download.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });



        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, titleList);
       // arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text2, dateList);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads").child(year).child(division);//reference inside Uploads

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
              /*for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
               {
                    url = childsnapshot.getValue(String.class);
                   break;
               }
                for(DataSnapshot childsnapshot : dataSnapshot.getChildren())
                {
                    title = childsnapshot.getValue(String.class);
                }
                //Toast.makeText(download.this, title, Toast.LENGTH_SHORT).show();*/


                for(DataSnapshot childsnapshot:dataSnapshot.getChildren())
                {
                    uid = childsnapshot.getRef().getParent().getKey();
                    //Toast.makeText(download.this, t_name, Toast.LENGTH_SHORT).show();
                    String assignmentid = childsnapshot.getKey();
                    //Toast.makeText(download.this, key, Toast.LENGTH_SHORT).show();

                    for(DataSnapshot childsnapshot2 : childsnapshot.getChildren())
                    {
                        String key = childsnapshot2.getKey();
                        //Toast.makeText(download.this, key, Toast.LENGTH_SHORT).show();
                        if (key.contentEquals("Title"))
                            title = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("Assignment Url"))
                            url = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("Due Date"))
                            due_date = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("Given Date"))
                            given_date = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("Description"))
                            description = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("Teachername"))
                            username = childsnapshot2.getValue(String.class);
                        if (key.contentEquals("TeacherphotoUri"))
                            userphotoUri = childsnapshot2.getValue(String.class);
                    }
                    String substring_title = "";

                    if(search.length() <= title.length() )
                            substring_title = title.substring(0,search.length());

                    if(search.equalsIgnoreCase(substring_title) || search.equals("null"))
                    {
                        count3++;
                        ((DownloadViewCreator) recyclerView.getAdapter()).update(title, url, given_date, username,userphotoUri, description, due_date);
                    }

                    //titleList.add(new String(title));
                    //dateList.add(given_date);

                    // Notify the ArrayAdapter that there was a change
                    //arrayAdapter.notifyDataSetChanged();

                    //dummyDataList.add(new String(title, given_date));

                    //adapter = new CustomAdapter(download.this,title,given_date);
                  //  listView.setAdapter(adapter);
                }
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.loading).setVisibility(View.GONE);
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

        recyclerView.setVisibility(View.VISIBLE);

       // recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // custom adapters always
        // populate the recycler view with items
        linearLayoutManager = new LinearLayoutManager(download.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // recyclerView.setLayoutManager(new LinearLayoutManager(download.this));
        DownloadViewCreator myAdapter = new DownloadViewCreator(recyclerView,download.this,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);
    }

    public void checkNetworkConnection()
    {
    }

    public boolean isNetworkConnectionAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
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

/*
    <ListView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/listview"
        android:divider="@color/green"
        android:dividerHeight="1dp"/>

        <TextView
            android:id="@+id/teachername"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="5dp"
            android:textColor="#FD0303"
            android:textStyle="italic"/>


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:layout_marginTop="15dp"
            android:layout_marginLeft="5dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Due On :-    "
                android:textColor="@color/green"/>

            <TextView
                android:id="@+id/dued"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp" />

        </LinearLayout>


        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Given On :- "
                android:textColor="@color/green" />


 */


