package com.learningplatform.mapper;

import com.learningplatform.dto.CommentDto;
import com.learningplatform.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    
    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.nom", target = "authorNom")
    CommentDto toDto(Comment comment);
    
    @Mapping(source = "articleId", target = "article.id")
    @Mapping(source = "authorId", target = "author.id")
    Comment toEntity(CommentDto commentDto);
    
    List<CommentDto> toDtoList(List<Comment> comments);
}