package com.sinquinto.serviclick.Category.Infrastructure;

import com.sinquinto.serviclick.Category.Application.CategoryService;
import com.sinquinto.serviclick.Category.Domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category) {
        return new ResponseEntity<>(service.saveCategory(category), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(service.findAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(service.countCategories(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        return new ResponseEntity<>(service.updateCategory(id, category), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCategoryById(id);
    }
}