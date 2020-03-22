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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class student_activity extends AppCompatActivity implements View.OnClickListener
{
    MainActivity obj= new MainActivity();
   // MyObjects ob = new MyObjects();
    protected FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView textView;
    GridLayout gridLayout;
    String personName;
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

        //Toast.makeText(this,  Toast.LENGTH_SHORT).show();

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
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

   /* private Object menuIconWithText(Drawable drawable)
    {

    }*/


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
    }

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

    @Override
    public void onClick(View v)
    {
        //switch(v.getId())
        //{
         //   case R.id.sign_out:

                signOut();
                //finish();
         //       break;
        //}
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
}