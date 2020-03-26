package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.security.keystore.UserNotAuthenticatedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DownloadViewCreator extends RecyclerView.Adapter<DownloadViewCreator.ViewHolder>
{
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> duedates = new ArrayList<>();
    ArrayList<String> givendates = new ArrayList<>();

    public void update(String name,String url,String due_date,String given_date,String t_name)
    {
        items.add(name);
        urls.add(url);
        senders.add(t_name);
        duedates.add(due_date);
        givendates.add(given_date);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public DownloadViewCreator(RecyclerView recyclerView,Context context,ArrayList<String> items,ArrayList<String> urls,ArrayList<String> duedates,ArrayList<String>givendates,ArrayList<String> senders)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
        this.duedates = duedates;
        this.givendates = givendates;
        this.senders = senders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.assignment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // initialize the elements of indiv,items
        holder.filename.setText(items.get(position));
        holder.givendate.setText(givendates.get(position));
        holder.duedate.setText(duedates.get(position));
        holder.teachername.setText((senders.get(position)));
    }

    @Override
    public int getItemCount()       // return the no. of items
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView filename;
        TextView duedate;
        TextView givendate;
        TextView teachername;
        public ViewHolder(View itemView)        // represents indiv list items
        {
            super(itemView);
            filename = itemView.findViewById(R.id.nameofFile);
            givendate = itemView.findViewById(R.id.givend);
            duedate = itemView.findViewById(R.id.dued);
            teachername = itemView.findViewById(R.id.teachername);
            itemView.setOnClickListener(new View.OnClickListener()
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
            });
        }
    }
}