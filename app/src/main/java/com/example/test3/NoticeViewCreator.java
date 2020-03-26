package com.example.test3;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeViewCreator extends RecyclerView.Adapter<NoticeViewCreator.ViewHolder>
{
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> notices = new ArrayList<>();


    public void update(String name,String date,String text)
    {
        senders.add(name);
        dates.add(date);
        notices.add(text);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public NoticeViewCreator(RecyclerView recyclerView,Context context,ArrayList<String> senders,ArrayList<String> dates,ArrayList<String> notices)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.senders = senders;
        this.dates = dates;
        this.notices = notices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item,parent,false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // initialize the elements of indiv,items
        holder.Date.setText(senders.get(position));
        holder.Sender.setText(dates.get(position));
        holder.Content.setText(notices.get(position));
    }

    @Override
    public int getItemCount()       // return the no. of items
    {
        return senders.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Date;
        TextView Sender;
        TextView Content;
        public ViewHolder(View itemView)        // represents indiv list items
        {
            super(itemView);
            Date = itemView.findViewById(R.id.Date);
            Sender = itemView.findViewById(R.id.Sender);
            Content = itemView.findViewById(R.id.Content);
            /*itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = recyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent();
                    // denotes that we are going to view something
                    // intent.setData(Uri.parse(urls.get(position)));
                    //intent.setType(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse((urls.get(position))),Intent.ACTION_VIEW);
                    context.startActivity(intent);
                }
            });*/
        }
    }
}