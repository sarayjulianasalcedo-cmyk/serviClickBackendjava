package com.sinquinto.serviclick.Category.Infrastructure.Repository;

import com.sinquinto.serviclick.Category.Infrastructure.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}