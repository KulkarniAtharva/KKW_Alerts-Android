package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    protected FirebaseAuth mAuth;
    private TextView textview;
    static String personName;
    String personEmail;
    Uri personPhoto;
    int teacher_flag =0;
    String refreshedToken;
    //FirebaseDatabase myFirebase;
    //DatabaseReference reff;
    //User user;
    private DatabaseReference mDatabase;
    static private DatabaseReference finduser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        task.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    // Task completed successfully
                    InstanceIdResult authResult = task.getResult();
                    String fcmToken = authResult.getToken();
                }
                else {
                    // Task failed with an exception
                    Exception exception = task.getException();
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v)
    {
         signIn();
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

            String Userid = account.getEmail();
            int at_pos = Userid.lastIndexOf('@');
            String after_at = Userid.substring(at_pos,Userid.length());

            if(after_at.contentEquals("kkwagh.edu.in") || Userid .contentEquals("adwaitgondhalekar@gmail.com"))
                teacher_flag=1;
            else
                teacher_flag=0;

          /* if(finduser!=null)
           {
               finduser.addValueEventListener(new ValueEventListener()
               {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                   {
                       for(DataSnapshot data : dataSnapshot.getChildren())
                       {
                           String user_name = data.child("users").child("teachers").getValue().toString();
                           int eq_pos= user_name.lastIndexOf('=');
                           String teacher_name = user_name.substring(1,eq_pos);

                           if(teacher_name.contentEquals(uname))
                           {
                               teacher_flag=1;
                           }
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });*/

               if(teacher_flag==1)
               {
                   Intent intent = new Intent(MainActivity.this,teacher_activity.class);
                   MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
                  // MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());
                   startActivity(intent);
                   //finish();
               }
               else
               {
                   Intent intent = new Intent(MainActivity.this,student_activity.class);
                   MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
                  // MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());
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
    private void writeNewStudent(final String student_name,final String student_email,String uid)
    {
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

        User user = new User(student_name,student_email);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child("students").child(student_name).child("Email").setValue(student_email);
        mDatabase.child("users").child("students").child(student_name).child("Uid").setValue(uid);

        Intent intent = new Intent(this, student_activity.class);
        MyObjects.getInstance().GetGoogleSignInclient(mGoogleSignInClient);
        //MyObjects.getInstance().GetFireBaseUser(mAuth.getInstance().getCurrentUser());
        //intent.putExtra("Name",mGo
        //intent.putExtra("Serializable",mAuth);
        // int  mauthref = &mAuth;
        //MyObjects ob = new MyObjects(mAuth,mGoogleSignInClient);
        startActivity(intent);
        //signOut();
        //finish();

        // finish();

        //demoref.push().setValue(student_email);
    }
    private void writeNewTeacher(String teacher_name,String teacher_email,String uid)
    {
        User user= new User(teacher_name,teacher_email);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child("teachers").child(teacher_name).child("Email").setValue(teacher_email);
        mDatabase.child("users").child("teachers").child(teacher_name).child("Uid").setValue(uid);
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

        if(currentUser!=null)
        updateUI(currentUser);
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
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            MyObjects.getInstance().GetFireBaseUser(user);
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
        String new_userid = user.getEmail();
        String new_username = user.getDisplayName();
        String uid = user.getUid();

        int at_pos = new_userid.lastIndexOf('@');
        String after_at = new_userid.substring(at_pos,new_userid.length());

        if(after_at.contentEquals("kkwagh.edu.in") || new_userid.contentEquals("adwaitgondhalekar@gmail.com"))
            writeNewTeacher(new_username,new_userid,uid);
        else
            writeNewStudent(new_username,new_userid,uid);
    }
}