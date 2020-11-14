package com.example.test3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class timetable extends AppCompatActivity
{
    WebView webView;

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1976D3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Your Timetable");

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));


        boolean status = isNetworkConnectionAvailable();

        if(!status)
        {
            /*Intent intent = new Intent(this, nointernetpage.class);
            startActivity(intent);

            finish();*/

            imageView = findViewById(R.id.nointernetpage);
            imageView.setVisibility(View.VISIBLE);

            actionBar.setTitle("");
        }
        else
            {
            webView = (WebView) findViewById(R.id.webview);
            webView.loadUrl("https://erp.kkwagh.edu.in/Student/StudentSelfTimeTable.aspx?MENU_CODE=MWEBSTUATTEN_TT");
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar
        }

        //mView = new CatLoadingView();
    }

    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            findViewById(R.id.loading).setVisibility(View.VISIBLE);

            //mView.show(getSupportFragmentManager(),"");
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            findViewById(R.id.progress).setVisibility(View.GONE);
            findViewById(R.id.loading).setVisibility(View.GONE);

            //mView.dismiss();
        }
    }

    public void checkNetworkConnection()
    {

    }

    public boolean isNetworkConnectionAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(isConnected)
        {
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