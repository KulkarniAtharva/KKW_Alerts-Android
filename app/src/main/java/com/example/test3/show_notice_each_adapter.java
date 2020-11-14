package com.example.test3;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.core.Constants;


import java.util.List;

public class show_notice_each_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    int type;

   // public show_notice_each_adapter(Context context, ,int type)
    {
        this.context = context;
        this.type = type;
        //this.
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View bottomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_notice_each_item,null);

        switch(type)
        {
            /*case Constants.TYPE_BOTTOM_PERSISTENT_SHEET:  return new BottomPersistentHolder(bottomView,context);
            case Constants.TYPE_BOTTOM_MODAL_SHEET:  return new BottomModalHolder(bottomView,context);
            case Constants.TYPE_BOTTOM_SHEET_FULL:  return new BottomFullHolder(bottomView,context);*/
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
       // DataModel flagModel = data.get(Position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
