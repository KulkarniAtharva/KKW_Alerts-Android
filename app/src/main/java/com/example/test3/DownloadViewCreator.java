package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DownloadViewCreator extends RecyclerView.Adapter<DownloadViewCreator.ViewHolder>
{
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> usernames = new ArrayList<>();
    ArrayList<String> duedates = new ArrayList<>();
    ArrayList<String> givendates = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> userphotouris = new ArrayList<>();

    public void update(String name,String url,String given_date,String username,String userphotoUri,String des,String due_date)
    {
        title.add(name);
        urls.add(url);
        usernames.add(username);
        duedates.add(due_date);
        givendates.add(given_date);
        description.add(des);
        userphotouris.add(userphotoUri);
        
        //if(getItemCount() == 0)
        //    Toast.makeText(context, "No Results Found", Toast.LENGTH_SHORT).show();
       // else
            notifyDataSetChanged();  // refreshes the recycler view automatically
       // Toast.makeText(context, getItemCount()+"", Toast.LENGTH_SHORT).show();
    }

    public DownloadViewCreator(RecyclerView recyclerView,Context context,ArrayList<String> title,ArrayList<String> urls,ArrayList<String>givendates,ArrayList<String>usernames,ArrayList<String>userphotouris,ArrayList<String>description,ArrayList<String>duedates)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.title = title;
        this.urls = urls;
        this.duedates = duedates;
        this.givendates = givendates;
        this.usernames = usernames;
        this.description = description;
        this.userphotouris = userphotouris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.download_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String givendate = givendates.get(position);

        int given_day = Integer.parseInt(givendate.substring(0,givendate.indexOf('/')));
        int given_month = Integer.parseInt(givendate.substring(givendate.indexOf('/')+1,givendate.lastIndexOf('/')));
        int given_year = Integer.parseInt(givendate.substring(givendate.lastIndexOf('/')+1));

        int today_day =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int today_month =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int today_year = Calendar.getInstance().get(Calendar.YEAR);

        if(given_year == today_year && given_month == today_month)
        {
            if(today_day - given_day == 1)
                givendate = "Yesterday";
            else if(today_day == given_day)
                givendate = "Today";
        }


        // initialize the elements of indiv,items
        holder.filename.setText(title.get(position));
        holder.givendate.setText(givendate);
       // holder.duedate.setText(duedates.get(position));
       // holder.teachername.setText(((usernames.get(position)).toUpperCase().charAt(0))+"");


        Glide.with(context)
                .load(userphotouris.get(position))
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount()       // return the no. of items
    {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView filename,duedate,givendate,teachername;
        CircleImageView circleImageView;

        public ViewHolder(View itemView)        // represents indiv list items
        {
            super(itemView);
            filename = itemView.findViewById(R.id.nameofFile);
            givendate = itemView.findViewById(R.id.givend);
            //duedate = itemView.findViewById(R.id.dued);
            teachername = itemView.findViewById(R.id.initial);
            circleImageView = itemView.findViewById(R.id.user_photo);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = recyclerView.getChildLayoutPosition(view);

                    Intent intent = new Intent(context, download_each.class);
                    intent.putExtra("title",title.get(position));
                    intent.putExtra("description",description.get(position));
                    intent.putExtra("duedate",duedates.get(position));
                    intent.putExtra("givendate",givendates.get(position));
                    intent.putExtra("teachername",usernames.get(position));
                    intent.putExtra("url",urls.get(position));
                    intent.putExtra("position",position);

                    context.startActivity(intent);


                    // denotes that we are going to view something
                    // intent.setData(Uri.parse(urls.get(position)));
                    //intent.setType(Intent.ACTION_VIEW);

                   /* intent.setDataAndType(Uri.parse((urls.get(position))),Intent.ACTION_VIEW);
                    context.startActivity(intent);*/
                }
            });
        }
    }
}

/*

<androidx.cardview.widget.CardView
            android:id="@+id/cardView_circle"
            android:layout_width="55dp"
            android:layout_height="55dp"
            app:cardCornerRadius="60dp"
            app:cardElevation="3dp"
            android:layout_marginLeft="10dp"
            android:layout_marginBottom="10dp"
            app:cardBackgroundColor="@color/deepskyblue">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/initial"
                android:textStyle="bold"
                android:textSize="25dp"
                android:layout_gravity="center_horizontal|center_vertical"
                android:textColor="@color/black"/>

        </androidx.cardview.widget.CardView>


 */