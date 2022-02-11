package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utem.mobile.ecomplaint.model.Complaint;

import java.util.List;

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ViewHolder> {

    private List<String> complaint;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ForumRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.complaint = data;
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_forum_row, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //get data
        //String forum = complaint.get(position);

        // holder.id.setText(forum);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return complaint.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,time;

        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }
        public void setComplaint (Complaint complaint){
            id.setText(complaint.getComplaintID());
            time.setText(complaint.getComplaintDateTime());

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return complaint.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}

