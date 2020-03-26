package com.example.test3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecentNoticeViewCreator extends RecyclerView.Adapter<RecentNoticeViewCreator.ViewHolder>
{
    RecyclerView recyclerView;

    Context context;
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> notices = new ArrayList<>();

    //new change
    ArrayList<String>noticesids = new ArrayList<>();
    String teachername;

    public void update(String name,String date,String text,String noticeid)
    {
        senders.add(name);
        dates.add(date);
        notices.add(text);
        noticesids.add(noticeid);//new change
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public RecentNoticeViewCreator(RecyclerView recyclerView, Context context, ArrayList<String> dates, ArrayList<String> notices, ArrayList<String> noticeids)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.dates = dates;
        this.notices = notices;
        this.noticesids= noticeids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_notice_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        // initialize the elements of indiv,items
        holder.Date.setText(senders.get(position));
        holder.Content.setText(notices.get(position));
        //holder.Noticeid.setText(noticesids.get(position));

        holder.imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(context, "clicked at pos "+position, Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, notices.get(position), Toast.LENGTH_SHORT).show();

                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Set the message show for the Alert time
                builder.setMessage("Are you sure you want to delete this notice ?");

                // Set Alert Title
                builder.setTitle("Confirmation !");

                // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will close
                builder.setCancelable(true);

                // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.

                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which)
                                    {
                                        // When the user click yes button
                                        final String notice = notices.get(position);
                                        final String date = dates.get(position);
                                        final String notice_id = noticesids.get(position);

                                        teachername = MyObjects.getInstance().firebaseuser.getDisplayName();
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Notices").child(teachername);

                                        databaseReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                                {
                                                    String key = childSnapshot.getKey();
                                                    String noticeid = childSnapshot.getRef().getParent().getKey();
                                                    if(key.contentEquals("Text"))
                                                    {
                                                        String noticetext = childSnapshot.getValue(String.class);
                                                        if(noticetext.contentEquals(notice) && notice_id.contentEquals(noticeid))
                                                        {
                                                            childSnapshot.getRef().getParent().removeValue();
                                                            dates.remove(position);
                                                            notices.remove(position);
                                                            noticesids.remove(position);
                                                            notifyItemRemoved(position);
                                                            notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });

                // Set the Negative button with No name
                // OnClickListener method is use
                // of DialogInterface interface.
                builder
                        .setNegativeButton(
                                "No",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                        // If user click no
                                        // then dialog box is canceled.
                                        dialog.cancel();
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();


            }
        });
    }
    @Override
    public int getItemCount()       // return the no. of items
    {
        return senders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Date;
        TextView Content;
        //TextView Noticeid;
        ImageButton imageButton;

        public ViewHolder(final View itemView)        // represents indiv list items
        {
            super(itemView);
            Date = itemView.findViewById(R.id.Date);
            Content = itemView.findViewById(R.id.Content);
            //Noticeid = itemView.findViewById(R.id.notice_id);
            imageButton = itemView.findViewById(R.id.deletebtn);
        }
    }
}