package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findALl();
    void saveProduct(MultipartFile imageP, ProductDto productDto);
    ProductDto productDtoById(Long id);
    void update(MultipartFile imageP, ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);

    Page<ProductDto> pageProduct(int pageNo);

    /*Customer*/
    List<Product> getAllProduct();
    Product getProductById(Long id);
    List<Product> getRelatedProducts(Long categoryId);

    List<Product> filterHighPrice();
    List<Product> filterLowPrice();
}
