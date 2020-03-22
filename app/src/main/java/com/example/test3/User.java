package com.example.test3;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User
{

    //public String username;
    public String email;
    public String name;

    public User()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name,String email)
    {
        //this.username = username;
        this.email = email;
        this.name = name;
    }

}