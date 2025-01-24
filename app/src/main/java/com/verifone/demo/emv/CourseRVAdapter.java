package com.verifone.demo.emv;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.TextView;





import java.util.ArrayList;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {



    // variable for our array list and context

    private ArrayList<test_class> courseModalArrayList;

    private Context context;



    // constructor

    public CourseRVAdapter(ArrayList<test_class> courseModalArrayList, Context context) {

        this.courseModalArrayList = courseModalArrayList;

        this.context = context;

    }



    @NonNull

    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // on below line we are inflating our layout

        // file for our recycler view items.

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_rv_item, parent, false);

        return new ViewHolder(view);

    }



    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // on below line we are setting data

        // to our views of recycler view item.

        test_class modal = courseModalArrayList.get(position);

        holder.courseNameTV.setText(modal.getCourseName());

        holder.courseDescTV.setText(modal.getCourseDescription());

        holder.courseDurationTV.setText(modal.getCourseDuration());

        holder.courseTracksTV.setText(modal.getCourseTracks());

    }



    @Override

    public int getItemCount() {

        // returning the size of our array list

        return courseModalArrayList.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {



        // creating variables for our text views.

        private TextView courseNameTV, courseDescTV, courseDurationTV, courseTracksTV;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            // initializing our text views

            courseNameTV = itemView.findViewById(R.id.idTVCourseName);

            courseDescTV = itemView.findViewById(R.id.idTVCourseDescription);

            courseDurationTV = itemView.findViewById(R.id.idTVCourseDuration);

            courseTracksTV = itemView.findViewById(R.id.idTVCourseTracks);

        }

    }
}