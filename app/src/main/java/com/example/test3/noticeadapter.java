package com.example.test3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class noticeadapter extends RecyclerView.Adapter<noticeadapter.MovieVH>
{
    private static final String TAG = "MovieAdapter";
    List<notice_pojo> movieList;

    public noticeadapter(List<notice_pojo> movieList)
    {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_notice_item, parent, false);
         return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position)
    {
        notice_pojo movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());
        holder.ratingTextView.setText(movie.getRating());
        holder.plotTextView.setText(movie.getPlot());

        boolean isExpanded = movieList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }

    class MovieVH extends RecyclerView.ViewHolder
    {
        private static final String TAG = "MovieVH";

        ConstraintLayout expandableLayout;
        TextView titleTextView, yearTextView, ratingTextView, plotTextView;

        public MovieVH(@NonNull final View itemView)
        {
            super(itemView);

           // titleTextView = itemView.findViewById(R.id.titleTextView);
           // yearTextView = itemView.findViewById(R.id.yearTextView);
          //  ratingTextView = itemView.findViewById(R.id.ratingTextView);
           // plotTextView = itemView.findViewById(R.id.plotTextView);
           // expandableLayout = itemView.findViewById(R.id.expandableLayout);

            titleTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    notice_pojo movie = movieList.get(getAdapterPosition());
                    movie.setExpanded(!movie.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}