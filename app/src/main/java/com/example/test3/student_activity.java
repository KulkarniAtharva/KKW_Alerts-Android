package com.example.test3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.URL;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class student_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    MainActivity obj= new MainActivity();
   // MyObjects ob = new MyObjects();
    protected FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GridLayout gridLayout;
    String personName;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String personphotoUri,personEmail;
    CircleImageView userphoto;

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
        //personphotoUri = MyObjects.getInstance().getPhotoUrl();
        personphotoUri = MyObjects.getInstance().personphotoUri;
        personEmail = MyObjects.getInstance().user_email;

        //textView = (TextView)findViewById(R.id.name);
        //textView.setText(personName);

        //imageView.setImageURI(personphotoUri);

            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), personphotoUri);
       // imageView.setImageURI(personphotoUri);





        gridLayout = (GridLayout)findViewById(R.id.grid);
        setSingleEvent(gridLayout);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        //{
        //getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        // getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  // to make status bar translucent

       // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);



        // }
        //else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //{
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light));
        // }

        toolbar = findViewById(R.id.tool_Bar);


       // TextView textView = toolbar.findViewById(R.id.toolbar_title);
        //textView.setText("");

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        /*  need to set this in android manifest otherwise error comes as

            Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a toolbar instead

            android:theme="@style/Theme.AppCompat.Light.NoActionBar"
        */


        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white,getTheme()));  // hamburger icon colour
        toggle.syncState();



        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        TextView username = (TextView)hView.findViewById(R.id.user_name);
        username.setText(personName);
        TextView email = (TextView)hView.findViewById(R.id.email);
        email.setText(personEmail);

        userphoto = hView.findViewById(R.id.user_photo);

        Glide.with(this).load(personphotoUri).centerCrop().into(userphoto);
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
            case R.id.sell_books:  sell_books();  break;
            case R.id.buy_books:  buy_books();  break;
            case R.id.settings: settings();  break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.notification)
        {

        }

        return super.onOptionsItemSelected(item);
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
                    else if(fin == 2)
                        timetable();
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

    }

    void settings()
    {
        Intent intent = new Intent(this,settings.class);
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

    void timetable()
    {
        Intent intent = new Intent(this, timetable.class);
        startActivity(intent);
    }

    void sell_books()
    {
        Intent intent = new Intent(this, sell_books.class);
        startActivity(intent);
    }

    void buy_books()
    {
        Intent intent = new Intent(this, buy_books.class);
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



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message show for the Alert time
        // builder.setMessage("Are you sure want to Exit ?");

        // Set Alert Title
        // builder.setTitle("Exit ?");

        // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will close
        builder.setCancelable(true);

        /*
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
             @Override
             public void onClick(DialogInterface dialog, int which)
             {
                  //onBackPressed();
                 //dialog.cancel();
                  Intent a = new Intent(Intent.ACTION_MAIN);
                  a.addCategory(Intent.CATEGORY_HOME);
                  a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(a);
             }
        });

        // Set the Negative button with No name OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            }
        });*/



        // Create the Alert dialog
        final AlertDialog alertDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.exit_dialog, null);

        alertDialog.setView(dialoglayout);

        // Show the Alert Dialog box
        alertDialog.show();

        Button yes = dialoglayout.findViewById(R.id.yes);
        Button no = dialoglayout.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        // builder.




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

 /*<RelativeLayout
                    android:layout_width="match_parent"
                    android:layout_height="10dp"
                    android:layout_weight="2"
                    android:background="#0F2C5E">

                    <TextView
                        android:id="@+id/name"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_centerInParent="true"
                        android:fontFamily="sans-serif"
                        android:textColor="@android:color/white"
                        android:textSize="34sp" />

                </RelativeLayout>*/