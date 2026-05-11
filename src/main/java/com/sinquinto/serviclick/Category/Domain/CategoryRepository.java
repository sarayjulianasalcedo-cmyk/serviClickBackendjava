package com.sinquinto.serviclick.Category.Domain;

import java.util.List;

public interface CategoryRepository {
    Category save(Category category);
    Category findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
    Long countCategories();
}