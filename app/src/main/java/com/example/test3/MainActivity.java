package com.example.test3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    protected FirebaseAuth mAuth;
    static String personName;
    String personEmail;
    Uri personPhoto;
    int teacher_flag = 0;
    String refreshedToken;
    //FirebaseDatabase myFirebase;
    //DatabaseReference reff;
    //User user;
    private DatabaseReference mDatabase;
    static private DatabaseReference finduser;
    FirebaseApp app;

    String year,division;
    int flag1 = 0,flag2 = 0;
    TextInputEditText year_textInputEditText,division_textInputEditText;
    TextInputLayout division_textInputLayout;

    DatabaseHelper databaseHelper;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        year_textInputEditText = (TextInputEditText)findViewById(R.id.year);
        division_textInputEditText = (TextInputEditText)findViewById(R.id.division);

        division_textInputLayout = findViewById(R.id.divisiontextinputlayout);

        databaseHelper = new DatabaseHelper(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        Task<InstanceIdResult> task = FirebaseInstanceId.getInstance().getInstanceId();
        task.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
        {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task)
            {
                if (task.isSuccessful())
                {
                    // Task completed successfully
                    InstanceIdResult authResult = task.getResult();
                    String fcmToken = authResult.getToken();
                }
                else
                {
                    // Task failed with an exception
                    Exception exception = task.getException();
                }
            }
        });

        /*  for full screen window (disappearing status bar )

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        */

        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));
        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue,this.getTheme()));

        String projectID = "secondary";

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("kkw-alerts---comp")
                .setApplicationId("1:252227408959:android:06723641f1c807e436d48a")
                .setApiKey("AIzaSyCnoAP94V_gUJbE86exGeV7pRL08TcKI3s")
                .setDatabaseUrl("https://kkw-alerts---comp.firebaseio.com")
                // setStorageBucket(...)
                .build();

        FirebaseApp.initializeApp(this /* Context */, options,projectID);       // Initialize with secondary app
        FirebaseApp secondary = FirebaseApp.getInstance(projectID);     // Retrieve secondary FirebaseApp
        firebaseFirestore = FirebaseFirestore.getInstance(secondary);      // Access a Cloud Firestore instance from your Activity

       /* // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
            db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Error adding document", e);
                    }
                });*/



       /* spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        final ArrayList<String> branch1 = new ArrayList<>();
        branch1.add("Select");
        branch1.add("TEACHER");
        branch1.add("FE");
        branch1.add("SE");
        branch1.add("TE");
        branch1.add("BE");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branch1);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0: year = "null";
                            spinner2.setSelection(0);
                            spinner2.setVisibility(View.INVISIBLE);
                            break;
                    case 1: year = "TEACHER";
                            spinner2.setSelection(0);
                            spinner2.setVisibility(View.INVISIBLE);
                            break;
                    case 2: year = "FE";  spinner2.setVisibility(View.VISIBLE);  break;
                    case 3: year = "SE";  spinner2.setVisibility(View.VISIBLE);  break;
                    case 4: year = "TE";  spinner2.setVisibility(View.VISIBLE);  break;
                    case 5: year = "BE";  spinner2.setVisibility(View.VISIBLE);  break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }


        });

        final ArrayList<String> branch2 = new ArrayList<>();
        branch2.add("Select Division");
        branch2.add("A");
        branch2.add("B");
        branch2.add("C");
        branch2.add("D");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branch2);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0: division = "null"; break;
                    case 1: division = "A";  break;
                    case 2: division = "B";  break;
                    case 3: division = "C";  break;
                    case 4: division = "D";  break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });*/

        // setup the alert builder
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose");
        // add a radio button list
        String[] years = {"TEACHER","FE", "SE", "TE", "BE"};
        int checkedItem = -1; // No item selected
        builder1.setSingleChoiceItems(years, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: year = "TEACHER";  break;
                    case 1: year = "FE";  break;
                    case 2: year = "SE";  break;
                    case 3: year = "TE";  break;
                    case 4: year = "BE";  break;
                }
            }
        });
        // add OK and Cancel buttons
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                year_textInputEditText.setText(year);

                if((year.equals("TEACHER")))
                    division_textInputLayout.setVisibility(View.GONE);
                else
                    division_textInputLayout.setVisibility(View.VISIBLE);
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

        // setup the alert builder
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Choose a Division");
        // add a radio button list
        String[] divisions = {"A", "B", "C", "D"};
        checkedItem = -1;
        builder2.setSingleChoiceItems(divisions, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: division = "A";  break;
                    case 1: division = "B";  break;
                    case 2: division = "C";  break;
                    case 3: division = "D";  break;
                }
            }
        });
        // add OK and Cancel buttons
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                division_textInputEditText.setText(division);
            }
        });
        builder2.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final AlertDialog dialog2 = builder2.create();


        division_textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                division_textInputEditText.clearFocus();
                dialog2.show();
            }
        });

        /*SharedPreferences sharedPreferences = getSharedPreferences("User Info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("year",year);
        editor.putString("division",division);
        editor.apply();*/
    }

    @Override
    public void onClick(View v)
    {
        // MyObjects.getInstance().getyear(year);
       // MyObjects.getInstance().getdivision(division);

        boolean status = isNetworkConnectionAvailable();
        if(status)
        {
            if(year.equals("TEACHER"))
                signIn();
            else
            {
                if (year == "null" && division == "null")
                    Toast.makeText(this, "Select year & division", Toast.LENGTH_SHORT).show();
                else if (year == "null")
                    Toast.makeText(this, "Select year", Toast.LENGTH_SHORT).show();
                else if (division == "null")
                    Toast.makeText(this, "Select division", Toast.LENGTH_SHORT).show();
                else
                    signIn();
            }
        }
    }

    public void checkNetworkConnection()
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*protected void signOut()
    {
        // Firebase sign out
        mAuth.signOut();
        //Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        updateUI(null);
                    }
                });
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                Log.w("sign in failed", "Google sign in failed", e);
            }
        }
    }
    protected void updateUI(@Nullable FirebaseUser account)
    {
        if (account != null)//account is not null that is user has successfullly signed in
        {
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);//Once the user has signed in mask the sign in option

            personName = account.getDisplayName();  //retrieving the name of the user from the object

            //personEmail = account.getEmail();//retrieving the email of the user from the object;
            personPhoto = account.getPhotoUrl();//retrieving the photo of the user

            /*mDatabase = FirebaseDatabase.getInstance().getReference();
            //int at_pos=personEmail.lastIndexOf('@');//getting position of
            //String email_substr=personEmail.substring(at_pos+1,personEmail.length());
            if(personEmail.contentEquals("adwaitgondhalekar@gmail.com"))
                writeNewTeacher(personName,personEmail);
            else
                writeNewUser(personName,personEmail);*/
            //finduser = FirebaseDatabase.getInstance().getReference();

            //final String uname = account.getDisplayName();

            final String user_email= account.getEmail();
            int at_pos = user_email.lastIndexOf('@');
            String after_at = user_email.substring(at_pos,user_email.length());

            if(after_at.contentEquals("kkwagh.edu.in") || user_email.contentEquals("adwaitgondhalekar@gmail.com") || user_email.contentEquals("atharvakulkarnithegreat@gmail.com"))
                teacher_flag=1;
            else
            {
                    if (year == "TEACHER")
                    {
                        Toast.makeText(this, "Sorry !! Ur not a TEACHER", Toast.LENGTH_LONG).show();
                        mGoogleSignInClient.signOut();
                        FirebaseAuth.getInstance().signOut();
                    }
                    else
                        teacher_flag = 0;
            }

            if(teacher_flag==1)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("TEACHERS");
                Intent intent = new Intent(MainActivity.this,teacher_activity.class);
                MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
                // MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());

                MyObjects.getInstance().getpersonphotoUri(account.getPhotoUrl().toString());

                MyObjects.getInstance().getUseremail(user_email);

                startActivity(intent);
                //finish();
            }
            else if(teacher_flag == 0)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("STUDENTS-"+year+"-"+division);

                // MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());

                MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);

                //MyObjects.getInstance().getyear(year);
                MyObjects.getInstance().getpersonphotoUri(account.getPhotoUrl().toString());

                MyObjects.getInstance().getUseremail(user_email);

                Intent intent = new Intent(MainActivity.this,student_activity.class);
                startActivity(intent);

                //finish();
            }
        }
        else
        {
            //mStatusTextView.setText(R.string.signed_out);
            //Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out).setVisibility(View.GONE);

            //Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewStudent(final String student_name,final String student_email)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("STUDENTS-"+year+"-"+division);
       /* reff = FirebaseDatabase.getInstance().getReference();
        if(reff!=null)
        {
            reff.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot datas: dataSnapshot.getChildren())
                    {
                        String names = datas.child("students").getValue().toString();
                        int eq_pos = names.lastIndexOf('=');
                        String fname = names.substring(1, eq_pos);
                        textview.setText(fname);
                        if (fname.equalsIgnoreCase(student_name))
                        {
                            User user = new User(student_name, student_email);
                            mDatabase.child("users").child("students").child(student_name).setValue(student_email);
                        }
                        else
                        {
                            User user = new User(student_name, student_email);
                            mDatabase.child("users").child("students").child(student_name).setValue(student_email);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                    {
                }
            });
        }*/

       // User user = new User(student_name,student_email);

        mDatabase = FirebaseDatabase.getInstance(app).getReference();

        mDatabase.child("users").child("students").child(year).child(division).child(student_name).child("Email").setValue(student_email);

        //mDatabase.child("users").child("students").child(year).child(division).child(Userid);

        //mDatabase.child("users").child("students").child(student_name).child("Uid").setValue(uid);


        // Create a new user with a first and last name
       //** Map<String, Object> user = new HashMap<>();
        /*user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);*/

        // Add a new document with a generated ID
      /*  firebaseFirestore.collection(division).document("Users").collection("Students").document(student_email).set(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Error adding document", e);
                    }
                });


        */


        Intent intent = new Intent(this, student_activity.class);
        MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
        //MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());
        //intent.putExtra("Name",mGo
        //intent.putExtra("Serializable",mAuth);
        // int  mauthref = &mAuth;
        //MyObjects ob = new MyObjects(mAuth,mGoogleSignInClient);

        //MyObjects.getInstance().getyear(year);
        //MyObjects.getInstance().getdivision(division);

        startActivity(intent);
        //signOut();
        //finish();

        // finish();

        //demoref.push().setValue(student_email);
    }
    private void writeNewTeacher(String teacher_name,String teacher_email)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("TEACHERS");
      // **  User user= new User(teacher_name,teacher_email);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //mDatabase.child("users").child("teachers").child(teacher_name).child("Email").setValue(teacher_email);
        mDatabase.child("users").child("teachers").child(teacher_name).setValue(teacher_email);


        // Create a new user with a first and last name
     /*   Map<String, Object> user = new HashMap<>();
        /*user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);*/

      /*  // Add a new document with a generated ID
        firebaseFirestore.collection("B").document("Users").collection("Teachers").document(teacher_email).set(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Error adding document", e);
                    }
                });*/



        // mDatabase.child("users").child("teachers").child(teacher_name).child("Uid").setValue(uid);
        Intent intent = new Intent(this,teacher_activity.class);
        MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
        //MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());

        startActivity(intent);
        //signOut();
        //finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        MyObjects.getInstance().GetFireBaseUser(currentUser);

        //String temp = mAuth.getCurrentUser().getUid();

        //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();

        if(currentUser!=null)
        {
            //Toast.makeText(this, "yo", Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }
        /*if(currentUser!=null)
        {
            if (currentUser.getEmail().contentEquals("adwaitgondhalekar@gmail.com")) {
                Intent intent = new Intent(MainActivity.this, teacher_activity.class);
                startActivity(intent);
                signOut();
            } else {
                Intent intent = new Intent(MainActivity.this, student_activity.class);
                startActivity(intent);
                signOut();
            }
        }*/
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            MyObjects.getInstance().GetFireBaseUser(user);

                            //user.updateEmail("atharva");
                            //Toast.makeText(MainActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();

                            //updateUI(user);
                            WriteNewUser(user);

                            // Toast.makeText(MainActivity.this, "Firebase successfully linked", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("failed", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                            // Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this, "Firebase linking failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void WriteNewUser(FirebaseUser user)
    {
        String new_useremail = user.getEmail();
        String new_username = user.getDisplayName();
        String uid = user.getUid();

        MyObjects.getInstance().getpersonphotoUri(user.getPhotoUrl().toString());

        //MyObjects.getInstance().getUsername(new_username);
        MyObjects.getInstance().getUseremail(new_useremail);
        MyObjects.getInstance().getUid(uid);

        databaseHelper.insertData(year,division);               // Insert data into SqLite Database

        int at_pos = new_useremail.lastIndexOf('@');
        String after_at = new_useremail.substring(at_pos,new_useremail.length());

        if(after_at.contentEquals("kkwagh.edu.in") || new_useremail.contentEquals("adwaitgondhalekar@gmail.com") || new_useremail.contentEquals("atharvakulkarnithegreat@gmail.com"))
                writeNewTeacher(new_username, new_useremail);
        else
            {
            if (year.equals("TEACHER"))
            {
                Toast.makeText(this, "Sorry !!  Ur not a TEACHER", Toast.LENGTH_LONG).show();
                mGoogleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
            }
            else
                writeNewStudent(new_username, new_useremail);
        }
    }
}