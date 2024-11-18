package com.example.dontforgetyourmoney.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Post;
import com.example.dontforgetyourmoney.data.parser.KUCS_ParserImpl;
import com.example.dontforgetyourmoney.data.repository.PostRepository.PostRepository;
import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import java.util.List;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    PostRepository postRepository;

    @Inject
    KUCS_ParserImpl parser;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private Handler mainHandler = new Handler(Looper.getMainLooper()); //! 메인 스레드 핸들러

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewPosts);
        Button btnRefresh = root.findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(v -> refreshPosts());

        postAdapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);

        refreshPosts();
        return root;
    }

    private void refreshPosts() {
        // 새로운 스레드에서 데이터베이스 작업 수행
        new Thread(() -> {
            // 데이터베이스 인스턴스 가져오기
            // AppDatabase db = AppDatabase.getDatabase(getContext());
            // db.postDao().insert(new Post("제목1", "제목2")); // 메인 스레드에서 직접 호출

            // 게시글 삽입
            //postRepository.insert(new Post("제목1", "123", "본문1", "링크", 2, 10, 4.5));

            //postRepository.insert(new Post("제목1", "123", "본문1", "링크", 2, 10, 4.5));

            //postRepository.deleteAllPosts();
//            postRepository.delete(
//                    postRepository.getPostById(
//                            postRepository.getPostIdByTitle("2024년 대동장학회(대동백화점) 장학생 선발 의뢰")));

            //! 파싱 테스트

            parser.parseAndSavePosts();

//            postRepository.insert(new Post(String.format("랜덤제목 %d", (int) (Math.random() * 100)),
//                    "123", "본문1", "링크", 2, 10, 4.5));
//            for (int i = 0; i < 10; i++) {
//                postRepository.insert(new Post(String.format("제목%d", i+1),
//                        String.format("날짜%d", i+1),
//                        String.format("본문%d", i+1),
//                        String.format("링크%d", i+1),
//                        2,
//                        10,
//                        4.5));
//            }

            // 게시글 가져오기
            List<Post> posts = postRepository.getAllPosts();

            // UI 업데이트는 메인 스레드에서 수행해야 하므로 핸들러 사용
            mainHandler.post(() -> postAdapter.setPosts(posts));
        }).start(); // 스레드 시작
    }
}