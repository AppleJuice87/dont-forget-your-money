package com.example.dontforgetyourmoney.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Post;
import com.example.dontforgetyourmoney.ui.dialog.PostViewDialog;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts = new ArrayList<>();

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.date.setText(post.getDate());
        holder.itemView.setOnClickListener(v -> {
            // 글 목록에서 글을 클릭하면 글 열람 다이얼로그 화면을 표시.
            PostViewDialog dialog = new PostViewDialog(holder.itemView.getContext(), post);
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;

        PostViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            date = itemView.findViewById(R.id.postDate);
        }
    }
}