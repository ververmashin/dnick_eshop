package com.example.eshop.service;

import com.example.eshop.entity.CategoryEntity;
import com.example.eshop.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryEntity> getCategories() {
        return categoryRepository.findAll();
    }


    public List<CategoryEntity> getCategoriesExcept(Long id) {
        return categoryRepository.findAll().stream().filter((category) -> !Objects.equals(category.getId(), id)).collect(Collectors.toList());
    }


    public CategoryEntity findById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(Exception::new);
    }


    public void addCategory(String name, Long parent) throws Exception {
        if (parent == null) {
            categoryRepository.save(new CategoryEntity(name));
        } else {
            categoryRepository.save(new CategoryEntity(name, categoryRepository.findById(parent).orElseThrow(Exception::new)));
        }
    }


    @Transactional
    public void editCategory(Long id, String name, Long parent) throws Exception {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(Exception::new);
        category.setName(name);
        if (parent == null) {
            category.setCategory(null);
        } else {
            category.setCategory(categoryRepository.findById(parent).get());
        }
        categoryRepository.save(category);

    }


    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
