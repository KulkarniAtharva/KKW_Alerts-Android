package com.example.test3;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyObjects
{
    protected GoogleSignInClient mGoogleSignInClient;
    protected FirebaseUser firebaseuser;
    private static final MyObjects ourInstance = new MyObjects();

    public static MyObjects getInstance()
    {
        return ourInstance;
    }

    private MyObjects()
    {
    }

    protected void GetGoogleSignInclient(GoogleSignInClient mGoogleSignInclient)
    {
        this.mGoogleSignInClient = mGoogleSignInclient;
    }
    protected void SignOut()
    {
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
    }
    protected void GetFireBaseUser(FirebaseUser firebaseUser)

    {
        this.firebaseuser = firebaseUser;
    }
}
