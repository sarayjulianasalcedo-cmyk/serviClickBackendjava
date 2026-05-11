package com.sinquinto.serviclick.Category.Application.UseCases;

import com.sinquinto.serviclick.Category.Domain.Category;
import java.util.List;

public interface CategoryCrudUseCase {
    Category saveCategory(Category category);
    Category findCategoryById(Long id);
    List<Category> findAllCategories();
    Category updateCategory(Long id, Category category);
    void deleteCategoryById(Long id);
}