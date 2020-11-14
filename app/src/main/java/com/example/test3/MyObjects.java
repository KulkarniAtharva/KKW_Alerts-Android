package com.example.test3;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URL;

public class MyObjects
{
  protected GoogleSignInClient mGoogleSignInClient;
  protected FirebaseUser firebaseuser;
  private static final MyObjects ourInstance = new MyObjects();
  protected String year,division,user_email,personphotoUri,uid,username;

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

  protected void getyear(String year)
  {
     this.year = year;
  }

  protected void getdivision(String division)
  {
     this.division = division;
  }

  protected void getUseremail(String user_email)
  {
    this.user_email = user_email;
  }

  protected void getpersonphotoUri(String personphotoUri)
  {
      this.personphotoUri = personphotoUri;
  }

  protected void getUid(String uid)
  {
      this.uid = uid;
  }

  protected void getUsername(String username)
  {
    this.username = username;
  }

}
