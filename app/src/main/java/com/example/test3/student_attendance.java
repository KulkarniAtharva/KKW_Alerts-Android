package com.example.test3;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
//import android.webkit.WebView;
import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;


public class student_attendance extends AppCompatActivity {
    boolean flag =true;
    boolean webviewInfo = false;
    final String erpUrl = "https://erp.kkwagh.edu.in/";
    final String attendance = "https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx?MENU_CODE=MWEBSTUATTEN_SLF_ATTEN";
    final String timetable = "https://erp.kkwagh.edu.in/Student/StudentSelfTimeTable.aspx?MENU_CODE=MWEBSTUATTEN_TT";
    final String SYLLABUS = "https://erp.kkwagh.edu.in/STUDENT/SubjectlistSyllabus.aspx?MENU_CODE=MWEBSTUSYLL_SUBWS";
    final String WEBVIEW = "https://erp.kkwagh.edu.in/MainNew.aspx?Usertype=STUDENT&modulecode=WEBSTUDACS&Userselect=S";
    ProgressBar progressBar;
    TextView fetch,calc;
    RelativeLayout background;
    CardView attendanceLayout;
    GridLayout gridLayout;
    ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.test3", MODE_PRIVATE);
        setContentView(R.layout.student_attendance);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        progressBar  = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        btnLogout = findViewById(R.id.logout);
        fetch = findViewById(R.id.fetching);
        calc = findViewById(R.id.calc);
        background = findViewById(R.id.background);
        attendanceLayout = findViewById(R.id.attendanceLayout);
        String sharedUname = getIntent().getStringExtra("uname");
        String sharedPass = getIntent().getStringExtra("password");
        final WebView webView = findViewById(R.id.webviewHome);
        webView.getSettings().setJavaScriptEnabled(true);
        final String unameJS = "javascript:document.getElementById('txtUserId').value='" + sharedUname + "';";
        //final String captchaJS = "javascript:document.getElementById('txtCaptcha').value=code";
        final String captchaJS = "function funValidateCaptcha() {return true;}";
        final String loginJS = "javascript:document.getElementById('btnLogin').click()";
        final String pwdJS = "javascript:document.getElementById('txtPassword').value='" + sharedPass + "';";
        final String jsFunction = "function login(){ " + unameJS + "\n" + pwdJS + "javascript:document.getElementById('txtCaptcha').value='123';\n" + loginJS + ";}login();";
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(final WebView view, final String url) {
                if(!flag) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                if (url.equals(erpUrl)) {
                    view.evaluateJavascript(captchaJS, null);
                    view.evaluateJavascript(jsFunction, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            view.loadUrl(attendance);
                        }
                    });
                }
                else if (url.equals(attendance)){
                    if (flag==true) {
                        flag=false;
                        view.evaluateJavascript("(function() { return document.getElementById('span_username').innerText.split(' ')[0]; })();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                TextView name = findViewById(R.id.name);
                                //String[] nameList= value.split(" ");
                                //String firstName = nameList[0].substring(1).toLowerCase();
                                //firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
                                background.setVisibility(View.VISIBLE);
                                name.setText("Heyyy");// +value.substring(1,value.length()-1) + "!");
                                progressBar.setVisibility(View.INVISIBLE);
                                gridLayout = findViewById(R.id.gridLayout);
                                gridLayout.setVisibility(View.VISIBLE);

                                attendanceLayout.setVisibility(View.VISIBLE);
                                btnLogout.setVisibility(View.VISIBLE);

                                Log.i("Parent Width",Integer.toString(gridLayout.getWidth()));
                                CardView card = findViewById(R.id.card1);
                                RelativeLayout mainLayout = findViewById(R.id.mainLayout);
                                int screenHeight = mainLayout.getHeight();
                                background.getLayoutParams().height = (int) (screenHeight/4.2);
                                background.requestLayout();
                                int width = card.getWidth();
                                //width = pxToDp(width);
                                Log.i("Child Width",Integer.toString(width));
                                ImageView img1 = findViewById(R.id.img1);
                                ImageView img2 = findViewById(R.id.img2);
                                ImageView img3 = findViewById(R.id.img3);
                                ImageView img4 = findViewById(R.id.img4);
                                img1.getLayoutParams().width = width;
                                img1.getLayoutParams().height = width;
                                img2.getLayoutParams().width = width;
                                img2.getLayoutParams().height = width;
                                img3.getLayoutParams().width = width;
                                img3.getLayoutParams().height = width;
                                img4.getLayoutParams().width = width;
                                img4.getLayoutParams().height = width;
                                img1.requestLayout();
                                img2.requestLayout();
                                img3.requestLayout();
                                img4.requestLayout();

                            }
                        });
                        view.evaluateJavascript("(function(){ var op = '';for(var i=2;i<=4;i++){op +=(document.getElementsByClassName('info')[0].children[i].innerText) + ',';} return op;})()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.i("Value", value);
                                if (value == "null"){
                                    value = "-";
                                }
                                fetch.setVisibility(View.GONE);
                                TextView output = findViewById(R.id.txtAttendance);
                                value = value.substring(1,value.length()-2);
                                /*StringTokenizer s = new StringTokenizer(value,"\\t");
                                List<String> values = new ArrayList<String>();
                                while (s.hasMoreTokens()){
                                    values.add(s.nextToken());
                                }
                                String finalAttendance = values.get(values.size()-1).substring(0,values.get(values.size()-1).indexOf('"'));
                                int totalLectures = Integer.parseInt(values.get(values.size()-2));
                                int attended = Integer.parseInt(values.get(values.size()-3));
                                output.append(finalAttendance);
                                //String finalAttendance = value;
                                calc.setVisibility(View.VISIBLE);
                                float fAttendance = Float.parseFloat(finalAttendance);
                                float sharedAttendance = sharedPreferences.getFloat("attendance", 0);*/
                                StringTokenizer s = new StringTokenizer(value,",");
                                List<String> values = new ArrayList<String>();
                                while (s.hasMoreTokens()){
                                    values.add(s.nextToken());
                                }
                                String finalAttendance = values.get(values.size()-1);
                                int totalLectures = Integer.parseInt(values.get(values.size()-2));
                                int attended = Integer.parseInt(values.get(values.size()-3));
                                output.append(finalAttendance);
                                //String finalAttendance = value;
                                calc.setVisibility(View.VISIBLE);
                                float fAttendance = Float.parseFloat(finalAttendance);
                                float sharedAttendance = sharedPreferences.getFloat("attendance", 0);
                                if(sharedAttendance<75 && fAttendance>75) {
                                    Log.i("Animation","check");
                                    LottieAnimationView animationView = findViewById(R.id.animationView);
                                    animationView.setVisibility(View.VISIBLE);
                                }
                                sharedPreferences.edit().putFloat("attendance",fAttendance).apply();
                                if(fAttendance < 30){
                                    calc.setText("Aal iz Well bolo");
                                }
                                else if(fAttendance<75){
                                    int toAttend = (75*totalLectures-100*attended)/25 + 1 ;
                                    calc.setText("Lectures to attend: "+String.valueOf(toAttend));
                                }
                                else {//if(fAttendance<85){
                                    int toAttend =(int)((attended - 0.75*totalLectures)/0.75);
                                    calc.setText("Possible lecture/prac leaves: "+String.valueOf(toAttend));
                                }
                                // else {
                                //   calc.setText("Enjoy your vacation");
                                //}
                            }
                        });
                    }
                    else {
                        view.evaluateJavascript("(function() { var content = document.getElementById(\"divReport\").innerHTML;\n" +
                                "\tdocument.write(\"<html>\"\n" +
                                "                                    + \"<head><title></title><link href='../include/style.css' rel='stylesheet' type='text/css' /> <link href='../StyleSheet.css' rel='stylesheet' type='text/css' /> </head>\"\n" +
                                "                                    + \"<body onload='window.print();window.close();'>\"\n" +
                                "                                     + content+ \"<br/>\" +\n" +
                                "                                    \"</body></html>\"); })();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                view.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        value = StringEscapeUtils.unescapeJava(value);
                                        view.setVisibility(View.VISIBLE);
                                        view.loadData(value,"text/html","Unicode");
                                    }
                                });
                            }
                        });
                    }
                }

                else if (url.equals(timetable)){
                    view.evaluateJavascript("(function() { var content = document.getElementById(\"divReport\").innerHTML;\n" +
                            "\tdocument.write(\"<html>\"\n" +
                            "                                    + \"<head><title></title><link href='../include/style.css' rel='stylesheet' type='text/css' /> <link href='../StyleSheet.css' rel='stylesheet' type='text/css' /> </head>\"\n" +
                            "                                    + \"<body onload='window.print();window.close();'>\"\n" +
                            "                                     + content+ \"<br/>\" +\n" +
                            "                                    \"</body></html>\"); })();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            view.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    value = StringEscapeUtils.unescapeJava(value);
                                    view.setVisibility(View.VISIBLE);
                                    view.loadData(value,"text/html","Unicode");
                                }
                            });
                        }
                    });
                }
                else if(url.equals(WEBVIEW)) {
                    webviewInfo = true;
                    webView.evaluateJavascript("hide()",null);
                    view.setVisibility(View.VISIBLE);
                }
                else if(url.equals(SYLLABUS)){
                    view.setVisibility(View.VISIBLE);
                }
                else {

                }

            }

        });
        webView.loadUrl(erpUrl);
    }

    @Override
    public void onBackPressed() {
        background.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        WebView webView = findViewById(R.id.webviewHome);
        if(attendanceLayout.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else {
            btnLogout.setVisibility(View.VISIBLE);
            gridLayout = findViewById(R.id.gridLayout);
            gridLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            calc.setVisibility(View.VISIBLE);
            attendanceLayout.setVisibility(View.VISIBLE);
        }
    }

    public void fullAttendance(View view){
        calc.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        attendanceLayout.setVisibility(View.GONE);
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        final WebView webView = findViewById(R.id.webviewHome);
        webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx?MENU_CODE=MWEBSTUATTEN_SLF_ATTEN");

        // webView.loadUrl("https://www.google.co.in");
        //webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SubjectlistSyllabus.aspx?MENU_CODE=MWEBSTUSYLL_SUBWS");

    }

    public void timeTable(View view){
        calc.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        attendanceLayout.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.GONE);
        final WebView webView = findViewById(R.id.webviewHome);
        webView.loadUrl("https://erp.kkwagh.edu.in/Student/StudentSelfTimeTable.aspx?MENU_CODE=MWEBSTUATTEN_TT");

    }

    public void logOut(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.test3",MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void syllabus(View view) {
        calc.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        attendanceLayout.setVisibility(View.GONE);
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.GONE);
        final WebView webView =findViewById(R.id.webviewHome);
        webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SubjectlistSyllabus.aspx?MENU_CODE=MWEBSTUSYLL_SUBWS");

        //webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx?MENU_CODE=MWEBSTUATTEN_SLF_ATTEN");

        //webView.loadUrl("https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx");
    }

    public void website(View view) {
        calc.setVisibility(View.GONE);
        attendanceLayout.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setVisibility(View.GONE);
        final WebView webView = findViewById(R.id.webviewHome);
        webView.loadUrl("https://erp.kkwagh.edu.in/MainNew.aspx?Usertype=STUDENT&modulecode=WEBSTUDACS&Userselect=S");
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = student_attendance.this.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
