package com.example.dontforgetyourmoney.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Post;
import com.example.dontforgetyourmoney.data.parser.ConditionGen;
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
        holder.conditions.setText(ConditionGen.getConditions(post));
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
        // 날짜를 기준으로 정렬하기
        posts.sort((post1, post2) -> {
            // 1. String에서 -를 모두 제거하고
            String date1 = post1.getDate().replace("-", "");
            String date2 = post2.getDate().replace("-", "");
            
            // 2. 정수로 변환
            int intDate1 = Integer.parseInt(date1);
            int intDate2 = Integer.parseInt(date2);
            
            // 3. 큰 정수가 먼저 오도록 정렬
            return Integer.compare(intDate2, intDate1);
        });

        this.posts = posts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, conditions;

        PostViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            date = itemView.findViewById(R.id.postDate);
            conditions = itemView.findViewById(R.id.postConditions);
        }
    }
}