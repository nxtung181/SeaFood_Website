package com.ecommerce.library.service;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Category findByName(String name);
    List<Category> findByActivatedTrue();
    void saveCategory(Category category);
    void update(Long id, String name);
    Optional<Category> findById(Long id);
    void deleteById(Long id);
    void enabledById(Long id);

    List<CategoryDto> getCategoryAndSize();

}
