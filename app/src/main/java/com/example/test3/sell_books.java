package com.example.test3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class sell_books extends AppCompatActivity
{
    Button send;
    FirebaseDatabase firebaseDatabase;
    String selltime;
    TextInputEditText name_edittext,books_edittext,price_edittext,mob_edittext,altmob_edittext,year_textInputEditText;
    Spinner spinner;
    String name,books,price,mob,altmob,year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_books);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        actionBar.setTitle("Sell Books");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        send = findViewById(R.id.sendbtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        name_edittext = findViewById(R.id.name);
        books_edittext = findViewById(R.id.books);
        price_edittext = findViewById(R.id.price);
        mob_edittext = findViewById(R.id.number1);
        altmob_edittext = findViewById(R.id.number2);
        year_textInputEditText = findViewById(R.id.year);

        /*spinner = findViewById(R.id.year);

        final ArrayList<String> branch = new ArrayList<>();
        branch.add("Select  Buyer's  Year");
        branch.add("FE");
        branch.add("SE");
        branch.add("TE");
        branch.add("BE");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branch);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
             {
                 switch (position)
                 {
                     case 1: year = "FE";  break;
                     case 2: year = "SE";  break;
                     case 3: year = "TE";  break;
                     case 4: year = "BE";  break;
                 }
             }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        // setup the alert builder
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose a Year");
        // add a radio button list
        String[] years = {"FE", "SE", "TE", "BE"};
        int checkedItem = -1; // No item selected
        builder1.setSingleChoiceItems(years, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: year = "FE";  break;
                    case 1: year = "SE";  break;
                    case 2: year = "TE";  break;
                    case 3: year = "BE";  break;
                }

            }
        });
        // add OK and Cancel buttons
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user clicked OK

                year_textInputEditText.setText(year);

            }
        });
        builder1.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final AlertDialog dialog1 = builder1.create();


        year_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                year_textInputEditText.clearFocus();
                dialog1.show();
            }
        });

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                selltime = System.currentTimeMillis() + "";

                name = name_edittext.getText().toString();
                books = books_edittext.getText().toString();
                price = price_edittext.getText().toString();
                mob = mob_edittext.getText().toString();
                altmob = altmob_edittext.getText().toString();

                databaseReference.child("Sell Books").child(year).child(selltime).child("Name").setValue(name);
                databaseReference.child("Sell Books").child(year).child(selltime).child("Books").setValue(books);
                databaseReference.child("Sell Books").child(year).child(selltime).child("Price").setValue(price);
                databaseReference.child("Sell Books").child(year).child(selltime).child("Mobile No").setValue(mob);
                databaseReference.child("Sell Books").child(year).child(selltime).child("Alt Mobile No").setValue(altmob);
                databaseReference.child("Sell Books").child(year).child(selltime).child("Year").setValue(year);

                Toast.makeText(sell_books.this, "Book Notice Sent to Buyer", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sellbooks_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.history)
        {
            Intent intent = new Intent(this, sell_books_history.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();

        //Intent intent = new Intent(this, student_activity.class);
        //startActivity(intent);

        return true;
    }
}