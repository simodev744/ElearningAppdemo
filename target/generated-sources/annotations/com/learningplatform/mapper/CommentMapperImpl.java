package com.learningplatform.mapper;

import com.learningplatform.dto.CommentDto;
import com.learningplatform.entity.Article;
import com.learningplatform.entity.Comment;
import com.learningplatform.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T17:15:49+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setArticleId( commentArticleId( comment ) );
        commentDto.setAuthorId( commentAuthorId( comment ) );
        commentDto.setAuthorNom( commentAuthorNom( comment ) );
        commentDto.setId( comment.getId() );
        commentDto.setContenu( comment.getContenu() );
        commentDto.setCreatedAt( comment.getCreatedAt() );

        return commentDto;
    }

    @Override
    public Comment toEntity(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.article( commentDtoToArticle( commentDto ) );
        comment.author( commentDtoToUser( commentDto ) );
        comment.id( commentDto.getId() );
        comment.contenu( commentDto.getContenu() );
        comment.createdAt( commentDto.getCreatedAt() );

        return comment.build();
    }

    @Override
    public List<CommentDto> toDtoList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toDto( comment ) );
        }

        return list;
    }

    private Long commentArticleId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Article article = comment.getArticle();
        if ( article == null ) {
            return null;
        }
        Long id = article.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long commentAuthorId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        User author = comment.getAuthor();
        if ( author == null ) {
            return null;
        }
        Long id = author.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String commentAuthorNom(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        User author = comment.getAuthor();
        if ( author == null ) {
            return null;
        }
        String nom = author.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected Article commentDtoToArticle(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        Article.ArticleBuilder article = Article.builder();

        article.id( commentDto.getArticleId() );

        return article.build();
    }

    protected User commentDtoToUser(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( commentDto.getAuthorId() );

        return user.build();
    }
}
