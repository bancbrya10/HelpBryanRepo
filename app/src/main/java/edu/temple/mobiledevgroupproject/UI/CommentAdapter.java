package edu.temple.mobiledevgroupproject.UI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.mobiledevgroupproject.Objects.Comment;
import edu.temple.mobiledevgroupproject.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> commentList;
    private static RecyclerViewItemClicked itemClickedListener;

    public CommentAdapter(ArrayList<Comment> commentList, RecyclerViewItemClicked itemClickedListener) {
        this.commentList = commentList;
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.comment_card_layout, viewGroup, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int i) {
        Comment thisComment = commentList.get(viewHolder.getAdapterPosition());


        viewHolder.userTextView.setText(thisComment.getUser().getUserName());
        viewHolder.commentBodyTextView.setText(thisComment.getCommentText());
        String month = String.valueOf(thisComment.getDatePosted().getMonth());
        String day = String.valueOf(thisComment.getDatePosted().getDay());
        String year = String.valueOf(thisComment.getDatePosted().getYear());
        viewHolder.dateTextView.setText(month + "/" + day + "/" + year);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateTextView;
        TextView commentBodyTextView;
        TextView userTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_posted_tv);
            commentBodyTextView = itemView.findViewById(R.id.comment_body_tv);
            userTextView = itemView.findViewById(R.id.user_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickedListener.userItemClick(v, this.getAdapterPosition());
        }
    }
}