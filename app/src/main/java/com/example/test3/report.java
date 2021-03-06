package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class report extends AppCompatActivity
{
    TextInputEditText textInputEditText1;
    TextInputEditText textInputEditText2;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        textInputEditText1 = (TextInputEditText)findViewById(R.id.subject);
        textInputEditText2 = (TextInputEditText)findViewById(R.id.message);
        button = findViewById(R.id.sendbtn);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String to = "atharvakulkarni2204@gmail.com";
                String subject = textInputEditText1.getText().toString();
                String message = textInputEditText2.getText().toString();

                if(TextUtils.isEmpty(subject) && TextUtils.isEmpty(message))
                {
                    textInputEditText1.setError("Subject is Mandatory");
                    textInputEditText2.setError("Message is Mandatory");
                }
                else if(TextUtils.isEmpty(message))
                    textInputEditText2.setError("Message is Mandatory");
                else if(TextUtils.isEmpty(subject))
                    textInputEditText1.setError("Subject is Mandatory");
                else
                    {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                    //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                    //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    //need this to prompts email client only
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            }
        });





        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Report bugs");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}