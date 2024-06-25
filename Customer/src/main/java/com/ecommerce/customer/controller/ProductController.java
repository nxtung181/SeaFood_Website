package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/products")
    public String products(Model model){
        List<Product> products = productService.getAllProduct();
        List<CategoryDto> categories = categoryService.getCategoryAndSize();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "shop-detail";
    }

    @GetMapping("/product-detail/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        Long categoryId = product.getCategory().getId();
        List<Product> relatedProducts = productService.getRelatedProducts(categoryId);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(@PathVariable("id") Long id, Model model){
        List<Product> products = productService.getRelatedProducts(id);
        model.addAttribute("products", products);
        List<CategoryDto> categories = categoryService.getCategoryAndSize();
        model.addAttribute("categories", categories);
        return "products-in-category";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model){
        List<Product> products = productService.filterHighPrice();
        model.addAttribute("products", products);
        List<CategoryDto> categories = categoryService.getCategoryAndSize();
        model.addAttribute("categories", categories);
        return "shop-detail";
    }

    @GetMapping("/low-price")
    public String filterLowPrice(Model model){
        List<Product> products = productService.filterLowPrice();
        model.addAttribute("products", products);
        List<CategoryDto> categories = categoryService.getCategoryAndSize();
        model.addAttribute("categories", categories);
        return "shop-detail";
    }


}
