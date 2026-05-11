package com.sinquinto.serviclick.Category.Application;

import com.sinquinto.serviclick.Category.Application.UseCases.CategoryCountUseCase;
import com.sinquinto.serviclick.Category.Application.UseCases.CategoryCrudUseCase;
import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Domain.CategoryRepository;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService implements CategoryCrudUseCase, CategoryCountUseCase {

    private final CategoryRepository repository;

    @Override
    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public Category findCategoryById(Long id) {
        Category category = repository.findById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        return category;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category categoryDB = repository.findById(id);
        if (categoryDB == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryDB.setName(category.getName());
        return repository.save(categoryDB);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category categoryDB = repository.findById(id);
        if (categoryDB == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countCategories() {
        return repository.countCategories();
    }
}