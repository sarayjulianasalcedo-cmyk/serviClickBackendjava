package com.sinquinto.serviclick.Category.Infrastructure.Repository;

import com.sinquinto.serviclick.Category.Domain.Category;
import com.sinquinto.serviclick.Category.Domain.CategoryRepository;
import com.sinquinto.serviclick.Category.Infrastructure.Mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        return mapper.categoryEntityToCategory(
                jpaRepository.save(mapper.categoryToEntity(category))
        );
    }

    @Override
    public Category findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::categoryEntityToCategory)
                .orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::categoryEntityToCategory)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Long countCategories() {
        return jpaRepository.count();
    }
}