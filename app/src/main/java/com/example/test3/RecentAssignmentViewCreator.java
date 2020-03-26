package com.example.test3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import static com.android.volley.VolleyLog.TAG;

public class RecentAssignmentViewCreator extends RecyclerView.Adapter<RecentAssignmentViewCreator.ViewHolder>
{
    RecyclerView recyclerView;

    Context context;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> duedates = new ArrayList<>();
    ArrayList<String> givendates = new ArrayList<>();
    ArrayList<String> assignmentids = new ArrayList<>();
    ArrayList<String> assignmenturls = new ArrayList<>();



    String teachername;

    public void update(String title ,String duedate,String givendate,String assignmentid,String assignmenturl)
    {
        titles.add(title);
        duedates.add(duedate);
        givendates.add(givendate);
        assignmentids.add(assignmentid);
        assignmenturls.add(assignmenturl);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public RecentAssignmentViewCreator(RecyclerView recyclerView, Context context, ArrayList<String> titles, ArrayList<String> duedates, ArrayList<String> givendates,ArrayList<String> assignmenturls,ArrayList<String>assignmentids)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.titles = titles;
        this.duedates = duedates;
        this.givendates= givendates;
        this.assignmenturls=assignmenturls;
        this.assignmentids=assignmentids;
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
        // initialize the elements of indiv,items
        holder.Duedate.setText(duedates.get(position));
        holder.GivenDate.setText(givendates.get(position));
        holder.Title.setText(titles.get(position));
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
                        final String url = assignmenturls.get(position);
                        final String id = assignmentids.get(position);
                        //final String date = dates.get(position);
                        //final String notice_id = noticesids.get(position);

                        teachername = MyObjects.getInstance().firebaseuser.getDisplayName();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Uploads").child(teachername);

                        databaseReference.addChildEventListener(new ChildEventListener()
                        {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                            {

                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                {
                                    String key = childSnapshot.getKey();
                                    String assignid = childSnapshot.getRef().getParent().getKey();
                                    if(key.contentEquals("Assignment Url"))
                                    {
                                        String assignurl = childSnapshot.getValue(String.class);
                                        if(url.contentEquals(assignurl) && id.contentEquals(assignid))
                                        {

                                            //below is the code for deleting a file from firebase storage using the download url
                                            StorageReference storageReference =
                                                    FirebaseStorage.getInstance().getReference().child("Uploads").child(teachername).child(assignid);

                                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: deleted file successfully");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {

                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Log.d(TAG, "onFailure: File is not delete!");
                                                }
                                            });


                                            childSnapshot.getRef().getParent().removeValue();
                                            //Toast.makeText(context, childSnapshot.getRef().getParent().getKey(), Toast.LENGTH_SHORT).show();
                                            titles.remove(position);
                                            givendates.remove(position);
                                            duedates.remove(position);
                                            assignmenturls.remove(position);
                                            assignmentids.remove(position);
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
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        // If user click no then dialog box is canceled.
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
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView GivenDate,Duedate,Title;
        TextView Sender;

        //TextView Noticeid;
        ImageButton imageButton;

        public ViewHolder(final View itemView)        // represents indiv list items
        {
            super(itemView);
            GivenDate = itemView.findViewById(R.id.givend);
            Sender = itemView.findViewById(R.id.teachername);
            Duedate = itemView.findViewById(R.id.dued);
            Title = itemView.findViewById(R.id.nameofFile);
            //Noticeid = itemView.findViewById(R.id.notice_id);
            imageButton = (ImageButton)itemView.findViewById(R.id.removebtn);
        }
    }
}