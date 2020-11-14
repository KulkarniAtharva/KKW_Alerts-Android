package com.example.test3;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.core.Constants;

import java.util.ArrayList;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static androidx.recyclerview.widget.DividerItemDecoration.*;

public class NoticeViewCreator extends RecyclerView.Adapter<NoticeViewCreator.ViewHolder> implements View.OnClickListener
{
    RecyclerView recyclerView;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    ActionBar actionBar;
    SearchView searchView;
    View view;
    TextView Date,Sender,Title,Notice,title_onbottomsheet;
    ImageButton close;
    Window window;

    Context context;
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> notices = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    boolean expanded;
    int position;


    public void update(String name,String date,String title,String notice)
    {
        senders.add(name);
        dates.add(date);
        titles.add(title);
        notices.add(notice);
        notifyDataSetChanged();  // refreshes the recycler view automatically
    }

    public NoticeViewCreator(RecyclerView recyclerView, BottomSheetBehavior bottomSheetBehavior, ActionBar actionBar,SearchView searchView,Window window,Context context, ArrayList<String> senders, ArrayList<String> dates, ArrayList<String> titles, ArrayList<String> notices)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.senders = senders;
        this.dates = dates;
        this.titles = titles;
        this.notices = notices;
        this.bottomSheetBehavior = bottomSheetBehavior;
        this.actionBar = actionBar;
        this.searchView = searchView;
        this.window = window;
        //  this.expanded = false;        Expandable recycler view
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   // to create view for recycler view item
    {
        View view = LayoutInflater.from(context).inflate(R.layout.show_notice_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        // initialize the elements of indiv,items
       // holder.Date.setText(dates.get(position));
       // holder.Sender.setText(senders.get(position));
        Title.setText(titles.get(position));


        //holder.Notice.setText(notices.get(position));

       // holder.operation.setTag(position);

       // Toast.makeText(context, position, Toast.LENGTH_SHORT).show();

        /*  Expandable recycler view


        if(isExpanded())
            holder.arrow.setBackgroundResource(R.drawable.arrow_upward);
        else
            holder.arrow.setBackgroundResource(R.drawable.arrow_downward);

        Toast.makeText(context, "Atharva", Toast.LENGTH_SHORT).show();

        boolean isExpanded = isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        */
    }

    /*      Expandable recycler view

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    */

    @Override
    public int getItemCount()       // return the no. of items
    {
        return senders.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       // ConstraintLayout expandableLayout;        Expandable recycler view

        ImageView arrow;                    //Expandable recycler view

        public ViewHolder(final View itemView)        // represents indiv list items
        {
            super(itemView);
            Date = ((Activity)context).findViewById(R.id.date);
            Title = itemView.findViewById(R.id.title);
            Notice = ((Activity)context).findViewById(R.id.notice);
            Sender = ((Activity)context).findViewById(R.id.sender);
            title_onbottomsheet = ((Activity)context).findViewById(R.id.title_onbottomsheet);
            close = ((Activity)context).findViewById(R.id.close);

            view = ((Activity)context).findViewById(R.id.cardBottom);

           // final LinearLayout linearLayout = ((Activity)context).findViewById(R.id.transBg);


           // view = itemView.findViewById(R.id.cardBottom);

           /* bottomSheetBehavior = BottomSheetBehavior.from(view);*/

            arrow = itemView.findViewById(R.id.arrow);

            /*recyclerView = itemView.findViewById(R.id.commonRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
            recyclerView.setAdapter(new show_notice_each_adapter(context, senders, Constants.TYPE_BOTTOM_SHEET_FULL));*/


           /* recyclerView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int itemPosition = recyclerView.indexOfChild(v);
                    Toast.makeText(context, itemPosition+"", Toast.LENGTH_SHORT).show();
                }
            }); */

           // bottomSheetDialog = new BottomSheetDialog((Activity)context);

            Title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    position = getAdapterPosition();



                    View sheetview = ((Activity) context).getLayoutInflater().inflate(R.layout.show_notice,null);

                    close.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });


                   // bottomSheetDialog.setContentView(sheetview);

                    if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        //arrow.setImageResource(R.drawable.arrow_downward);




                       // linearLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        view.setVisibility(View.VISIBLE);
                        // arrow.setImageResource(R.drawable.arrow_upward);

                       // linearLayout.setVisibility(View.VISIBLE);

                        Sender.setText(senders.get(position));
                        Notice.setText(notices.get(position));
                        Date.setText(dates.get(position));
                        title_onbottomsheet.setText(titles.get(position));

                       // actionBar.setHomeAsUpIndicator(R.drawable.close);
                       // actionBar.setTitle(titles.get(position));
                     //   searchView.setVisibility(View.GONE);
                        actionBar.hide();

                        window.setStatusBarColor(Color.parseColor("#08284E"));


                    }bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
                {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState)
                    {
                        switch (newState)
                        {
                            case BottomSheetBehavior.STATE_HIDDEN:

                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
                                actionBar.setTitle("Notices");
                                view.setVisibility(View.GONE);
                                //  searchView.setVisibility(View.VISIBLE);

                                actionBar.show();

                                window.setStatusBarColor(Color.parseColor("#1564C0"));


                                //  imageView.setImageResource(R.drawable.arrow_upward);
                                break;
                            case BottomSheetBehavior.STATE_HALF_EXPANDED:



                                // View view = LayoutInflater.from(context).inflate(R.layout.show_notice,null);

                          /*  TextView sender = view.findViewById(R.id.sender);
                            TextView notice = view.findViewById(R.id.notice);
                            TextView date = view.findViewById(R.id.date);*/






                                //  imageView.setImageResource(R.drawable.arrow_downward);


                                break;

                            case BottomSheetBehavior.STATE_EXPANDED:
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset)
                    {

                    }
                });

                  //   bottomSheetDialog.show();
                }
            });












           /* Expandable recycler view

           arrow = itemView.findViewById(R.id.arrow);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            arrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //notice_pojo movie = movieList.get(getAdapterPosition());

                    setExpanded(!isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });*/


          //  Sender = itemView.findViewById(R.id.Sender);
          //  Content = itemView.findViewById(R.id.Content);
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





       /* public void showFullBottomSheet(String userName)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }*/



    }




}

/*  Expandable recycler view

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="6dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/title"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:padding="16dp"
            android:textAppearance="@style/TextAppearance.AppCompat.Medium"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            android:background="?attr/selectableItemBackground"
            android:textColor="@color/black"/>

        <ImageView
            android:id="@+id/arrow"
            android:layout_width="26dp"
            android:layout_height="26dp"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            android:background="?attr/selectableItemBackground"
            android:layout_marginEnd="13dp" />

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/expandableLayout"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:visibility="visible"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/title"
            android:background="@color/gainsboro">

            <TextView
                android:id="@+id/date"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="8dp"
                android:layout_marginLeft="8dp"
                android:textAppearance="@style/TextAppearance.AppCompat.Small"
                app:layout_constraintStart_toEndOf="@+id/sender"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/notice"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="16dp"
                android:layout_marginRight="16dp"
                android:textAppearance="@style/TextAppearance.AppCompat.Small"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/sender"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:layout_marginLeft="16dp"
                android:textStyle="bold"
                android:textAppearance="@style/TextAppearance.AppCompat.Small"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>
</androidx.cardview.widget.CardView>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/darkblue"/>
</LinearLayout>





 */

/*  show_notice_item

<androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/expandableLayout"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:visibility="visible"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/title"
            android:background="@color/gainsboro">


        </androidx.constraintlayout.widget.ConstraintLayout>


 */