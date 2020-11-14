package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class download_each extends AppCompatActivity
{
    String title,description,duedate,givendate,teachername,url;
    TextView title_textview,description_textview,duedate_textview,givendate_textview,teachername_textview;
    Button viewfile;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_each);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Assignment");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        duedate = getIntent().getStringExtra("duedate");
        givendate = getIntent().getStringExtra("givendate");
        teachername = getIntent().getStringExtra("teachername");
        url = getIntent().getStringExtra("url");
        position =getIntent().getIntExtra("position",0);

        title_textview = findViewById(R.id.title);
        description_textview = findViewById(R.id.description);
        duedate_textview = findViewById(R.id.duedate);
        givendate_textview = findViewById(R.id.givendate);
        teachername_textview = findViewById(R.id.teachername);
        viewfile = findViewById(R.id.viewfile);

       /* Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        title = bundle.getString("title");*/

        int given_day = Integer.parseInt(givendate.substring(0,givendate.indexOf('/')));
        int given_month = Integer.parseInt(givendate.substring(givendate.indexOf('/')+1,givendate.lastIndexOf('/')));
        int given_year = Integer.parseInt(givendate.substring(givendate.lastIndexOf('/')+1));

        int today_day =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int today_month =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int today_year = Calendar.getInstance().get(Calendar.YEAR);

        if(given_year == today_year && given_month == today_month)
        {
            if(today_day - given_day == 1)
                givendate = "Yesterday";
            else if(today_day == given_day)
                givendate = "Today";
        }

        title_textview.setText(title);
        description_textview.setText(description);
        duedate_textview.setText("Due Date :   "+duedate);
        givendate_textview.setText(givendate);
        teachername_textview.setText(teachername);

        viewfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();

                intent.setDataAndType(Uri.parse(url),Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });


    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
