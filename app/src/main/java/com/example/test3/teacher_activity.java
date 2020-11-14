package com.example.test3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import com.example.test3.MainActivity;
import android.util.Log;

public class teacher_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    MainActivity obj = new MainActivity();
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uname,personName;
    String personphotoUri,personEmail;
    GridLayout gridLayout;
    ImageView userphoto;


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.teacher_activity);


        //user= mAuth.getCurrentUser();

        //textView = (TextView)findViewById(R.id.textViewname);
        //textView.setText(obj.personName.substring(0,obj.personName.indexOf(' ')));

        //setTitle(obj.personName);
        //getActionBar().setIcon(R.drawable.(obj.personPhoto));

        //personName = MyObjects.getInstance().firebaseuser.getDisplayName();

        personName = mAuth.getInstance().getCurrentUser().getDisplayName();
       //personPhoto = MyObjects.getInstance().firebaseuser.getPhotoUrl();

        //personName = MyObjects.getInstance().username;
        personphotoUri = MyObjects.getInstance().personphotoUri;
        personEmail = MyObjects.getInstance().user_email;

        //textView = (TextView)findViewById(R.id.name);
        //textView.setText(personName);

        gridLayout = (GridLayout)findViewById(R.id.grid);
        setSingleEvent(gridLayout);

        uname = getIntent().getStringExtra("Uname");

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        //{
           // getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
          //  getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));
       // }
        //else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //{
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light));

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  // to make status bar translucent
       // }

       // getWindow().setStatusBarColor(Color.TRANSPARENT);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        toolbar = findViewById(R.id.tool_Bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


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

        Glide.with(this)
                .load(personphotoUri)
                .into(userphoto);
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
            case R.id.settings:  settings();  break;
            case R.id.mark_attendance:  mark_attendance();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
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
                        recent_assignment();
                    else if(fin == 1)
                        recent_notice();
                    //else if(fin==2)
                        //upload();
                    //else
                    //    recent_notice();
                }
            });
        }
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
    void share()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: App link   " + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    void settings()
    {
        Intent intent = new Intent(this,settings.class);
        startActivity(intent);
    }

    void recent_assignment()
    {
        Intent intent = new Intent(this, recent_assignment.class);
        startActivity(intent);
        //intent.putExtra("Uname",uname);
       // finish();
    }
    void recent_notice()
    {
       Intent intent = new Intent(this,recent_notice.class);
       startActivity(intent);
    }

    void mark_attendance()
    {
        Intent intent = new Intent(this,mark_attendance.class);
        startActivity(intent);
    }

    protected void signOut()
    {
        MyObjects.getInstance().SignOut();
        finish();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}