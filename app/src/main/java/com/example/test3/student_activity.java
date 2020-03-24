package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class student_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    MainActivity obj= new MainActivity();
   // MyObjects ob = new MyObjects();
    protected FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView textView;
    GridLayout gridLayout;
    String personName;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity);
        //mAuth = ob.mAuth;
        mAuth = FirebaseAuth.getInstance();
        //mGoogleSignInClient = ob.mGoogleSignInClient;
        //textView = (TextView)findViewById(R.id.textViewname);
        //textView.setText(obj.personName.substring(0,obj.personName.indexOf(' ')));

        //setTitle(obj.personName);
        //getActionBar().setIcon(R.drawable.(obj.personPhoto));

        // account = GoogleSignIn.getLastSignedInAccount(this);
        personName = mAuth.getCurrentUser().getDisplayName();
       // personPhoto = MyObjects.getInstance().firebaseUser.getPhotoUrl();

        textView = (TextView)findViewById(R.id.name);
        textView.setText(personName);

        gridLayout = (GridLayout)findViewById(R.id.grid);
        setSingleEvent(gridLayout);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        //{
        getWindow().setStatusBarColor(getResources().getColor(R.color.green, this.getTheme()));
        // }
        //else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //{
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light));
        // }

        toolbar = findViewById(R.id.tool_Bar);
        setSupportActionBar(toolbar);

        /*  need to set this in android manifest otherwise error comes as

            Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a toolbar instead

            android:theme="@style/Theme.AppCompat.Light.NoActionBar"
        */


        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.about:    about();  break;
            case R.id.report:   report();   break;
            case R.id.rate:   rate(); break;
            case R.id.share:   share();  break;
            case R.id.logout:   signOut();  break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        // menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.report)),"About");
        // menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.report)),"Report");
        //menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.report)),"Share");
        //  menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.report)),"Rate");
        //menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.report)),"Logout");

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.about : about(); break;
            case R.id.report : report(); break;
            case R.id.share : share(); break;
            case R.id.rate : rate(); break;
            case R.id.logout : signOut(); break;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void setSingleEvent(GridLayout gridLayout)
    {
        for(int i=0;i<gridLayout.getChildCount();i++)
        {
            CardView cardView = (CardView)gridLayout.getChildAt(i);
            final int fin = i;
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Toast.makeText(teacher_activity.this, "Clicked at index "+ fin, Toast.LENGTH_SHORT).show();

                    if(fin == 0)
                        download();
                    else if(fin == 1)
                        show_notice();
                    else if(fin == 3)
                        attendance();
                }
            });
        }
    }

    void show_notice()
    {
        Intent intent = new Intent(this, show_notice.class);
        startActivity(intent);
    }

    void about()
    {
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
    void report()
    {
        Intent intent = new Intent(this, report.class);
        startActivity(intent);
    }
    void rate()
    {
        Intent intent = new Intent(this, rate.class);
        startActivity(intent);
    }
    void share()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: App link   " + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    void download()
    {
        Intent intent = new Intent(this, download.class);
        startActivity(intent);
        //intent.putExtra("Uname",uname);
        // finish();
    }

    void attendance()
    {
        Intent intent = new Intent(this, stud_attendance.class);
        startActivity(intent);
    }

    protected void signOut()
    {
        MyObjects.getInstance().SignOut();
        finish();
        // Firebase sign out
        //mAuth.signOut();
       //Toast.makeText(this, "Sign out from firebase", Toast.LENGTH_SHORT).show();

        // Google sign out
       /* mGoogleSignInClient.signOut().addOnCompleteListener(this,

               new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        startActivity(new Intent(student_activity.this,MainActivity.class));
                       // UpdateUI(null);
                        finish();
                    }
                });*/


    }

    @Override
    public void onBackPressed()
    {
        // Put your own code here which you want to run on back button click.

        //Toast.makeText(MainActivity.this,"Back Button is clicked.", Toast.LENGTH_LONG).show();

        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    protected void UpdateUI(FirebaseUser account)
    {
        if(account==null)
            Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}