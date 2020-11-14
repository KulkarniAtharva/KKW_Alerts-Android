package com.example.test3;

import com.google.firebase.database.FirebaseDatabase;

/*
            DISK PERSISTENCE


            Firebase apps automatically handle temporary network interruptions. Cached data is available while offline and
            Firebase resends any writes when network connectivity is restored.

            When you enable disk persistence, your app writes the data locally to the device so your app can maintain state
            while offline, even if the user or operating system restarts the app.

*/

public class Utils
{
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase()
    {
        if (mDatabase == null)
        {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}