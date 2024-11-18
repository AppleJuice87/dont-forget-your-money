package com.example.dontforgetyourmoney.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dontforgetyourmoney.data.model.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM posts WHERE id = :id")
    Post getPostById(int id);

    @Query("SELECT * FROM posts")
    List<Post> getAllPosts();

    @Query("DELETE FROM posts")
    void deleteAllPosts();

    @Query("SELECT * FROM posts WHERE title = :title LIMIT 1")
    Post getPostByTitle(String title);
}