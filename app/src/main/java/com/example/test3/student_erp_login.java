package com.example.test3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class student_erp_login extends AppCompatActivity
{
    AutoCompleteTextView txtUsername;
    EditText txtPassword;
    TextView connectivity;
    LottieAnimationView lottie;
    boolean checkCredentials = true;
    final String erpUrl = "https://erp.kkwagh.edu.in/";
    final String attendance = "https://erp.kkwagh.edu.in/STUDENT/SelfAttendence.aspx?MENU_CODE=MWEBSTUATTEN_SLF_ATTEN";
    ArrayList<String> usernames = new ArrayList<>();
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_erp_login);
        txtUsername = (AutoCompleteTextView)findViewById(R.id.txtUsername);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, usernames);
        txtUsername.setAdapter(adapter);
        txtPassword = findViewById(R.id.txtPassword);
        connectivity = findViewById(R.id.connectivity);
        btnLogin = findViewById(R.id.btnLogin);
        lottie = findViewById(R.id.lottie_view);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.nubaf.erp", MODE_PRIVATE);
        String sharedUname = sharedPreferences.getString("username", "");
        String sharedPass = sharedPreferences.getString("password", "");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                connectivity.setText("");
                connectivity.setVisibility(View.INVISIBLE);
                hideKeyboard(student_erp_login.this);
                if (!checkConnectivity()) {
                    connectivity.setVisibility(View.VISIBLE);
                    connectivity.setText("No internet connection");
                    lottie.setVisibility(View.INVISIBLE);
                    btnLogin.setClickable(false);
                }
                final String uname = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();
                if ((uname.equals("") || password.equals("")) && checkConnectivity()) {
                    connectivity.setVisibility(View.VISIBLE);
                    if (uname.equals("")) {
                        connectivity.setText("Username is required");
                    } else {
                        connectivity.setText("Password is required");
                    }
                } else {
                    lottie.setVisibility(View.VISIBLE);
                    final WebView webView = findViewById(R.id.webview);
                    webView.getSettings().setJavaScriptEnabled(true);
                    //webView.setVisibility(View.VISIBLE);
                    final String unameJS = "javascript:document.getElementById('txtUserId').value='" + uname + "';";
                    final String pwdJS = "javascript:document.getElementById('txtPassword').value='" + password + "';";
                    //final String captchaJS = "javascript:document.getElementById('txtCaptcha').value=code";
                    final String captchaJS = "function funValidateCaptcha() {return true;}";
                    final String loginJS = "javascript:document.getElementById('btnLogin').click()";
                    final String jsFunction = "function login(){ " + unameJS + "\n" + pwdJS + "javascript:document.getElementById('txtCaptcha').value='123';\n" + loginJS + ";}login();";
                    final String failedLogin = "function check() { var a=document.getElementById('jsAlert1_popup'); if(a){return 1;} else{return 0;} }check();";
                    final String disablePopup = "function disable() {document.getElementById('jsAlert1_okButton').click();}disable();";
                    webView.setWebViewClient(new WebViewClient() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                            view.loadUrl(request.getUrl().toString());
                            return true;
                        }

                        @Override
                        public void onPageFinished(final WebView view, final String url) {
                            if (url.equals(erpUrl) && checkConnectivity()) {
                                view.evaluateJavascript(captchaJS, null);
                                view.evaluateJavascript(jsFunction, null);
                                //Toast.makeText(MainActivity.this, "Executed", Toast.LENGTH_SHORT).show();
                                final Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.evaluateJavascript(failedLogin, new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                //Log.i("Login", value);
                                                if (value.equals(Integer.toString(1))) {
                                                    txtPassword.setText("");
                                                    txtUsername.setText("");
                                                    connectivity.setText("Wrong credentials");
                                                    connectivity.setVisibility(View.VISIBLE);
                                                    lottie.setVisibility(View.GONE);
                                                    checkCredentials = false;
                                                    txtUsername.requestFocus();
                                                }
                                            }
                                        });
                                        //view.evaluateJavascript(disablePopup, null);

                                    }
                                }, 2000);

                             /*   view.evaluateJavascript(failedLogin, new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        Log.i("Login",value);
                                    }
                                });*/
                            } else if (url.equals("https://erp.kkwagh.edu.in/MainNew.aspx?Usertype=STUDENT&modulecode=WEBSTUDACS&Userselect=S")) {
                                sharedPreferences.edit().putString("username", uname).apply();
                                sharedPreferences.edit().putString("password", password).apply();
                                usernames.add(uname);
                                Intent intent = new Intent(getApplicationContext(), student_attendance.class);
                                intent.putExtra("uname", uname);
                                intent.putExtra("password", password);
                                lottie.setVisibility(View.GONE);
                                startActivity(intent);
                            } else {

                            }
                        }

                    });
                    webView.loadUrl(erpUrl);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (checkCredentials == true) {
                                connectivity.setVisibility(View.VISIBLE);
                                connectivity.setText("Weak internet connection, hold on...");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        connectivity.setVisibility(View.VISIBLE);
                                        connectivity.setText("RIP Your Internet");
                                        AlertDialog.Builder builder;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(student_erp_login.this, android.R.style.Theme_Material_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(student_erp_login.this);
                                        }
                                        builder.setTitle("Weak Internet Connection")
                                                .setMessage("Please try again later.")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        System.exit(0);
                                                    }
                                                })
                                                .setIcon(android.R.drawable.alert_light_frame)
                                                .setCancelable(false)
                                                .show();
                                    }
                                }, 50000);
                            }
                        }
                    }, 20000);
                }
            }
        });

        if (!sharedUname.equals("") && !sharedPass.equals("")) {
            if (checkConnectivity()) {
                TextView uname = findViewById(R.id.txtUsername);
                uname.setText(sharedUname);
                TextView password = findViewById(R.id.txtPassword);
                password.setText(sharedPass);
                Intent intent = new Intent(getApplicationContext(), student_attendance.class);
                intent.putExtra("uname", sharedUname);
                intent.putExtra("password", sharedPass);
                startActivity(intent);
                finish();
            } else {
                connectivity.setVisibility(View.VISIBLE);
                connectivity.setText("No internet connection");
                lottie.setVisibility(View.GONE);
                btnLogin.setClickable(false);
            }
        } else {
            if (!checkConnectivity()) {
                connectivity.setVisibility(View.VISIBLE);
                connectivity.setText("No internet connection");
                lottie.setVisibility(View.GONE);
                btnLogin.setClickable(false);
            }
        }
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void triggerRebirth() {
        Context context = student_erp_login.this;
        Intent mStartActivity = new Intent(student_erp_login.this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}