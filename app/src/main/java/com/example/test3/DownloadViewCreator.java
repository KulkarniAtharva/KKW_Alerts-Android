package com.example.test3;

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

public class DownloadViewCreator extends RecyclerView.Adapter<DownloadViewCreator.ViewHolder>
{
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    public void update(String name,String url)
    {
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public DownloadViewCreator(RecyclerView recyclerView,Context context,ArrayList<String> items,ArrayList<String> urls)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // initialize the elements of indiv,items
        holder.nameofFile.setText(items.get(position));
    }

    @Override
    public int getItemCount()       // return the no. of items
    {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameofFile;
        public ViewHolder(View itemView)        // represents indiv list items
        {
            super(itemView);
            nameofFile = itemView.findViewById(R.id.nameofFile);
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