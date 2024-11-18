package com.example.dontforgetyourmoney.data.repository.PostRepository;

import com.example.dontforgetyourmoney.data.DAO.PostDao;
import com.example.dontforgetyourmoney.data.model.Post;

import java.util.List;

public class PostRepositoryImpl implements PostRepository {
    private final PostDao postDao;

    public PostRepositoryImpl(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public void insert(Post post) {
        postDao.insert(post);
    }

    @Override
    public void update(Post post) {
        postDao.update(post);
    }

    @Override
    public void delete(Post post) {
        postDao.delete(post);
    }

    @Override
    public Post getPostById(int id) {
        return postDao.getPostById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postDao.getAllPosts();
    }

    @Override
    public void deleteAllPosts() {
        postDao.deleteAllPosts();
    }

    @Override
    public boolean existsByTitle(String title) {
        Post post = postDao.getPostByTitle(title);
        return post != null;
    }

    @Override
    public int getPostIdByTitle(String title) {
        Post post = postDao.getPostByTitle(title);
        return post != null ? post.getId() : -1;
    }
}
