package com.example.test3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class about  extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        actionBar.setTitle("About");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        TextView textView1 =(TextView) findViewById(R.id.atharva);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView2 =(TextView) findViewById(R.id.adwait);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView3 =(TextView) findViewById(R.id.shreya);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView4 =(TextView) findViewById(R.id.radha);
        textView4.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}