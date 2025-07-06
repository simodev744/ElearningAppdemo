package com.learningplatform.repository;

import com.learningplatform.entity.Article;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    List<Article> findByAuthor(User author);
    
    List<Article> findByCategorie(String categorie);
    
    @Query("SELECT DISTINCT a.categorie FROM Article a")
    List<String> findDistinctCategories();
    
    @Query("SELECT COUNT(a) FROM Article a")
    long countAllArticles();
}