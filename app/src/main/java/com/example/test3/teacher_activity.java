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
    TextView textView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uname,personName;
    Uri personPhoto;
    GridLayout gridLayout;
    ImageView imageView;

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

        personName = MyObjects.getInstance().firebaseuser.getDisplayName();
       // personName = mAuth.getInstance().getCurrentUser().getDisplayName();
       personPhoto = MyObjects.getInstance().firebaseuser.getPhotoUrl();

        textView = (TextView)findViewById(R.id.name);
        textView.setText(personName);

        imageView = (ImageView)findViewById((R.id.image));
        imageView.setImageURI(personPhoto);
        //imageView.setImageResource(R.drawable.horn);

        gridLayout = (GridLayout)findViewById(R.id.grid);
        setSingleEvent(gridLayout);

        uname = getIntent().getStringExtra("Uname");

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

        	        drawerLayout = findViewById(R.id.drawerlayout);
        	        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                               R.string.open, R.string.close);

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
                        upload();
                    else if(fin ==1)
                        notice();
                    else if(fin==2)
                        recent_assignment();
                    else
                        recent_notice();
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

    void upload()
    {
        Intent intent = new Intent(this, upload.class);
        startActivity(intent);
        //intent.putExtra("Uname",uname);
       // finish();
    }
    void notice()
    {
       Intent intent =new Intent(this,notice.class);
       startActivity(intent);
    }

    void recent_assignment()
    {
        Intent intent =new Intent(this,recent_assignment.class);
        startActivity(intent);
    }

    void recent_notice()
    {
        Intent intent =new Intent(this,recent_notice.class);
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

        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}