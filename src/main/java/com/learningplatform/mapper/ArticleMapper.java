package com.learningplatform.mapper;

import com.learningplatform.dto.ArticleDto;
import com.learningplatform.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface ArticleMapper {
    
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.nom", target = "authorNom")
    ArticleDto toDto(Article article);
    
    @Mapping(source = "authorId", target = "author.id")
    Article toEntity(ArticleDto articleDto);
    
    List<ArticleDto> toDtoList(List<Article> articles);
}