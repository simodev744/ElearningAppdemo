package com.learningplatform.repository;

import com.learningplatform.entity.Article;
import com.learningplatform.entity.Comment;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByArticle(Article article);
    
    List<Comment> findByAuthor(User author);
    
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
}