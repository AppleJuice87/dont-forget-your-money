package com.example.dontforgetyourmoney.data.repository.PostRepository;

import com.example.dontforgetyourmoney.data.model.Post;

import java.util.List;

public interface PostRepository {
    
    // 게시글 추가
    void insert(Post post);

    // 게시글 업데이트
    void update(Post post);

    // 게시글 삭제
    void delete(Post post);

    // ID로 게시글 조회
    Post getPostById(int id);

    // 모든 게시글 조회
    List<Post> getAllPosts();

    // 모든 게시글 삭제
    void deleteAllPosts();
}
