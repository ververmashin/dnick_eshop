package com.example.eshop.controller;

import com.example.eshop.service.CategoryService;
import com.example.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/product")
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.getProducts());
        return "product/product";
    }

    @GetMapping("/add/product")
    public String getAddProductPage(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "product/form";
    }

    @GetMapping("/edit/product/{id}")
    public String getEditProductPage(@PathVariable(name = "id") Long id, Model model) throws Exception {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("product", productService.findById(id));
        return "product/form";
    }


    @PostMapping("/add/product")
    public String addProduct(@RequestParam(name = "name") String name,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "price") int price,
                             @RequestParam(name = "image") MultipartFile multipartFile,
                             @RequestParam(name = "category") Long category_id) throws Exception {
        productService.addProduct(name, description, price, multipartFile, category_id);
        return "redirect:/product";
    }

    @PostMapping("/edit/product/{id}")
    public String editProduct(@PathVariable(name = "id") Long id,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "price") int price,
                              @RequestParam(name = "image", required = false) MultipartFile multipartFile,
                              @RequestParam(name = "category") Long category_id) throws Exception {
        productService.editProduct(id, name, description, price, multipartFile, category_id);
        return "redirect:/product";
    }

    @PostMapping("/delete/product/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }

}
