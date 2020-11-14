package com.example.test3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import static com.android.volley.VolleyLog.TAG;

public class RecentAssignmentViewCreator extends RecyclerView.Adapter<RecentAssignmentViewCreator.ViewHolder>
{
    RecyclerView recyclerView;

    Context context;
    ArrayList<String> years = new ArrayList<>();
    ArrayList<String> divisions = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> duedates = new ArrayList<>();
    ArrayList<String> givendates = new ArrayList<>();
    ArrayList<String> assignmentids = new ArrayList<>();
    ArrayList<String> assignmenturls = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();

    String teachername;
    int position;

    public void update(String year,String division,String title ,String duedate,String givendate,String assignmentid,String assignmenturl,String description)
    {
        years.add(year);
        divisions.add(division);
        titles.add(title);
        duedates.add(duedate);
        givendates.add(givendate);
        assignmentids.add(assignmentid);
        assignmenturls.add(assignmenturl);
        descriptions.add(description);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public RecentAssignmentViewCreator(RecyclerView recyclerView, Context context,ArrayList<String> years,ArrayList<String> divisions, ArrayList<String> titles, ArrayList<String> duedates, ArrayList<String> givendates,ArrayList<String> assignmenturls,ArrayList<String>assignmentids,ArrayList<String>descriptions)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.years = years;
        this.divisions = divisions;
        this.titles = titles;
        this.duedates = duedates;
        this.givendates= givendates;
        this.assignmenturls=assignmenturls;
        this.assignmentids=assignmentids;
        this.descriptions=descriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_assignment_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
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
       // holder.Duedate.setText(duedates.get(position));
        holder.GivenDate.setText(givendate);
        holder.Title.setText(titles.get(position));
        holder.Year_Division.setText(years.get(position)+" "+divisions.get(position));


        //holder.Noticeid.setText(noticesids.get(position));


    }
    @Override
    public int getItemCount()       // return the no. of items
    {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView GivenDate,Duedate,Title,Sender,Year_Division,teacher;

        //TextView Noticeid;
        ImageButton imageButton;

        public ViewHolder(final View itemView)        // represents indiv list items
        {
            super(itemView);

            Year_Division = itemView.findViewById(R.id.year_division);
            GivenDate = itemView.findViewById(R.id.givend);
           // Sender = itemView.findViewById(R.id.teachername);
           // Duedate = itemView.findViewById(R.id.dued);
            Title = itemView.findViewById(R.id.nameofFile);
           // teacher = itemView.findViewById(R.id.initial);
            //Noticeid = itemView.findViewById(R.id.notice_id);
          //  imageButton = (ImageButton)itemView.findViewById(R.id.removebtn);



            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    position = recyclerView.getChildLayoutPosition(view);

                    Intent intent = new Intent(context, recent_assignment_each.class);

                    intent.putExtra("title",titles.get(position));
                    intent.putExtra("description",descriptions.get(position));
                    intent.putExtra("duedate",duedates.get(position));
                    intent.putExtra("givendate",givendates.get(position));
                    intent.putExtra("url",assignmenturls.get(position));
                    intent.putExtra("position", position);
                    intent.putExtra("year",years.get(position));
                    intent.putExtra("division",divisions.get(position));
                    intent.putExtra("assignmentid",assignmentids.get(position));

                    context.startActivity(intent);


                    // denotes that we are going to view something
                    // intent.setData(Uri.parse(urls.get(position)));
                    //intent.setType(Intent.ACTION_VIEW);

                   /* intent.setDataAndType(Uri.parse((urls.get(position))),Intent.ACTION_VIEW);
                    context.startActivity(intent);*/

                }
            });

            //String del = getIntent().getStringExtra("delete");

            /*years.remove(position);
            divisions.remove(position);
            titles.remove(position);
            givendates.remove(position);
            duedates.remove(position);
            assignmenturls.remove(position);
            assignmentids.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();*/
        }
    }
}