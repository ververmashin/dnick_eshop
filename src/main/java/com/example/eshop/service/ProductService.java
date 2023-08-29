package com.example.eshop.service;

import com.example.eshop.entity.ProductEntity;
import com.example.eshop.repository.CategoryRepository;
import com.example.eshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }


    public ProductEntity findById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(Exception::new);
    }

    public void addProduct(String name, String description, int price, MultipartFile multipartFile, Long category_id) throws Exception {
        ProductEntity product = new ProductEntity(name, description, price, multipartFile.getBytes(), categoryRepository.findById(category_id).orElseThrow(Exception::new));
        productRepository.save(product);
    }


    public void editProduct(Long id, String name, String description, int price, MultipartFile multipartFile, Long category_id) throws Exception {
        ProductEntity product = productRepository.findById(id).orElseThrow(Exception::new);

        if (!multipartFile.isEmpty()) {
            product.setImage(multipartFile.getBytes());
        }

        product.setName(name);
        product.setDescription(description);
        product.setCategory(categoryRepository.findById(category_id).orElseThrow(Exception::new));
        product.setPrice(price);
        productRepository.save(product);

    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    public Page<ProductEntity> filterBy(String filter, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if (filter == null) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findAllByNameContaining(filter, pageable);
        }
    }

    public Page<ProductEntity> filterByCategory(Long id, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAllByCategory(categoryRepository.findById(id).orElseThrow(Exception::new), pageable);
    }

}
