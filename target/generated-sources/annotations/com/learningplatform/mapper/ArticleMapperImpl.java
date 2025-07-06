package com.learningplatform.mapper;

import com.learningplatform.dto.ArticleDto;
import com.learningplatform.dto.CommentDto;
import com.learningplatform.entity.Article;
import com.learningplatform.entity.Comment;
import com.learningplatform.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T17:15:48+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ArticleDto toDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleDto articleDto = new ArticleDto();

        articleDto.setAuthorId( articleAuthorId( article ) );
        articleDto.setAuthorNom( articleAuthorNom( article ) );
        articleDto.setId( article.getId() );
        articleDto.setTitre( article.getTitre() );
        articleDto.setContenu( article.getContenu() );
        articleDto.setCategorie( article.getCategorie() );
        articleDto.setComments( commentMapper.toDtoList( article.getComments() ) );
        articleDto.setCreatedAt( article.getCreatedAt() );
        articleDto.setUpdatedAt( article.getUpdatedAt() );

        return articleDto;
    }

    @Override
    public Article toEntity(ArticleDto articleDto) {
        if ( articleDto == null ) {
            return null;
        }

        Article.ArticleBuilder article = Article.builder();

        article.author( articleDtoToUser( articleDto ) );
        article.id( articleDto.getId() );
        article.titre( articleDto.getTitre() );
        article.contenu( articleDto.getContenu() );
        article.categorie( articleDto.getCategorie() );
        article.comments( commentDtoListToCommentList( articleDto.getComments() ) );
        article.createdAt( articleDto.getCreatedAt() );
        article.updatedAt( articleDto.getUpdatedAt() );

        return article.build();
    }

    @Override
    public List<ArticleDto> toDtoList(List<Article> articles) {
        if ( articles == null ) {
            return null;
        }

        List<ArticleDto> list = new ArrayList<ArticleDto>( articles.size() );
        for ( Article article : articles ) {
            list.add( toDto( article ) );
        }

        return list;
    }

    private Long articleAuthorId(Article article) {
        if ( article == null ) {
            return null;
        }
        User author = article.getAuthor();
        if ( author == null ) {
            return null;
        }
        Long id = author.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String articleAuthorNom(Article article) {
        if ( article == null ) {
            return null;
        }
        User author = article.getAuthor();
        if ( author == null ) {
            return null;
        }
        String nom = author.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected User articleDtoToUser(ArticleDto articleDto) {
        if ( articleDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( articleDto.getAuthorId() );

        return user.build();
    }

    protected List<Comment> commentDtoListToCommentList(List<CommentDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Comment> list1 = new ArrayList<Comment>( list.size() );
        for ( CommentDto commentDto : list ) {
            list1.add( commentMapper.toEntity( commentDto ) );
        }

        return list1;
    }
}
