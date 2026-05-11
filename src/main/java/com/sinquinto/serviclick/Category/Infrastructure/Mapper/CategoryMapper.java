package com.sinquinto.serviclick.Category.Infrastructure.Mapper;

import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Infrastructure.Entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryEntityToCategory(CategoryEntity entity);
    CategoryEntity categoryToEntity(Category category);
}