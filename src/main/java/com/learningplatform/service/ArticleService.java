package com.learningplatform.service;

import com.learningplatform.dto.ArticleDto;
import com.learningplatform.dto.CommentDto;
import com.learningplatform.entity.Article;
import com.learningplatform.entity.Comment;
import com.learningplatform.entity.Role;
import com.learningplatform.entity.User;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.mapper.ArticleMapper;
import com.learningplatform.mapper.CommentMapper;
import com.learningplatform.repository.ArticleRepository;
import com.learningplatform.repository.CommentRepository;
import com.learningplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articleMapper.toDtoList(articles);
    }
    
    public ArticleDto getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + id));
        return articleMapper.toDto(article);
    }
    
    public ArticleDto createArticle(ArticleDto articleDto, Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        if (formateur.getRole() != Role.FORMATEUR) {
            throw new UnauthorizedException("Seuls les formateurs peuvent créer des articles");
        }
        
        Article article = articleMapper.toEntity(articleDto);
        article.setAuthor(formateur);
        
        Article savedArticle = articleRepository.save(article);
        return articleMapper.toDto(savedArticle);
    }
    
    public ArticleDto updateArticle(Long id, ArticleDto articleDto, Authentication authentication) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + id));
        
        User user = getUserFromAuthentication(authentication);
        
        if (user.getRole() != Role.ADMIN && !existingArticle.getAuthor().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier cet article");
        }
        
        existingArticle.setTitre(articleDto.getTitre());
        existingArticle.setContenu(articleDto.getContenu());
        existingArticle.setCategorie(articleDto.getCategorie());
        
        Article updatedArticle = articleRepository.save(existingArticle);
        return articleMapper.toDto(updatedArticle);
    }
    
    public void deleteArticle(Long id, Authentication authentication) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + id));
        
        User user = getUserFromAuthentication(authentication);
        
        if (user.getRole() != Role.ADMIN && !article.getAuthor().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à supprimer cet article");
        }
        
        articleRepository.delete(article);
    }
    
    public List<ArticleDto> getArticlesByCategory(String category) {
        List<Article> articles = articleRepository.findByCategorie(category);
        return articleMapper.toDtoList(articles);
    }
    
    public List<String> getAllCategories() {
        return articleRepository.findDistinctCategories();
    }
    
    public CommentDto addComment(Long articleId, CommentDto commentDto, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + articleId));
        
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setArticle(article);
        comment.setAuthor(user);
        
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }
    
    public List<CommentDto> getCommentsByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + articleId));
        
        List<Comment> comments = commentRepository.findByArticleOrderByCreatedAtDesc(article);
        return commentMapper.toDtoList(comments);
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}