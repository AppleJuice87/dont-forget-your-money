package com.example.dontforgetyourmoney.data.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.*;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.core.app.ApplicationProvider;

import com.example.dontforgetyourmoney.data.DAO.PostDao;
import com.example.dontforgetyourmoney.data.database.AppDatabase;
import com.example.dontforgetyourmoney.data.model.Post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PostRepositoryImplTest {

    private AppDatabase database;
    private PostDao postDao;
    private PostRepositoryImpl postRepository;

    @BeforeEach
    void setUp() {
        // In-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class
        ).allowMainThreadQueries().build(); // 메인 스레드에서 쿼리 허용

        postDao = database.postDao();
        postRepository = new PostRepositoryImpl(postDao);
    }

    @AfterEach
    void tearDown() {
        database.close(); // 데이터베이스 닫기
    }

    @Test
    void insert() {
        Post post = new Post("제목1", "본문1");
        postRepository.insert(post);

        List<Post> posts = postDao.getAllPosts();
        assertEquals(1, posts.size());
        assertEquals("제목1", posts.get(0).getTitle());
    }

    @Test
    void update() {
        Post post = new Post("제목1", "본문1");
        postRepository.insert(post);

        List<Post> posts = postDao.getAllPosts();
        Post insertedPost = posts.get(0);
        insertedPost.setTitle("업데이트된 제목");
        postRepository.update(insertedPost);

        Post updatedPost = postDao.getPostById(insertedPost.getId());
        assertEquals("업데이트된 제목", updatedPost.getTitle());
    }

    @Test
    void delete() {
        Post post = new Post("제목1", "본문1");
        postRepository.insert(post);

        List<Post> posts = postDao.getAllPosts();
        assertEquals(1, posts.size());

        postRepository.delete(posts.get(0));
        posts = postDao.getAllPosts();
        assertEquals(0, posts.size());
    }

    @Test
    void getPostById() {
        Post post = new Post("제목1", "본문1");
        postRepository.insert(post);

        List<Post> posts = postDao.getAllPosts();
        Post insertedPost = posts.get(0);
        Post fetchedPost = postRepository.getPostById(insertedPost.getId());

        assertNotNull(fetchedPost);
        assertEquals("제목1", fetchedPost.getTitle());
    }

    @Test
    void getAllPosts() {
        Post post1 = new Post("제목1", "본문1");
        Post post2 = new Post("제목2", "본문2");
        postRepository.insert(post1);
        postRepository.insert(post2);

        List<Post> posts = postRepository.getAllPosts();
        assertEquals(2, posts.size());
    }

    @Test
    void deleteAllPosts() {
        Post post1 = new Post("제목1", "본문1");
        Post post2 = new Post("제목2", "본문2");
        postRepository.insert(post1);
        postRepository.insert(post2);

        List<Post> posts = postDao.getAllPosts();
        assertEquals(2, posts.size());

        postRepository.deleteAllPosts();
        posts = postDao.getAllPosts();
        assertEquals(0, posts.size());
    }
}