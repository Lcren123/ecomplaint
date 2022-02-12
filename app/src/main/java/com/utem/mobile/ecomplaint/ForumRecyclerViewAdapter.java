package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utem.mobile.ecomplaint.model.Complaint;

import java.util.List;

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ViewHolder> {

    private List<Complaint> complaints;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ForumRecyclerViewAdapter(Context context, List<Complaint> data) {
        this.mInflater = LayoutInflater.from(context);
        this.complaints = data;
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

        if(complaints != null)
            holder.setComplaint(complaints.get(position));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return complaints != null ? 0 : complaints.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,time,title;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            image=itemView.findViewById(R.id.complaintImage);
            itemView.setOnClickListener(this);
        }
        public void setComplaint (Complaint complaint){
            id.setText(Integer.toString(complaint.getComplaintID()));
            title.setText(complaint.getComplaintTitle());
            time.setText(complaint.getComplaintDateTime());
            image.setImageBitmap(complaint.getImageList().get(0).getBitmap());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Complaint getItem(int id) {
        return complaints.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        //this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

