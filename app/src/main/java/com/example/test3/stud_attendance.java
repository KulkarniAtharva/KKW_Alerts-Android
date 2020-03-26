package com.example.test3;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.roger.catloadinglibrary.CatLoadingView;

public class stud_attendance extends AppCompatActivity
{
    WebView webView;
    CatLoadingView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_attendance);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color using parseColor method with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Your Attendance");

        getWindow().setStatusBarColor(getResources().getColor(R.color.green, this.getTheme()));

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx?MENU_CODE=MWEBSTUATTEN_SLF_ATTEN");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

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

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}