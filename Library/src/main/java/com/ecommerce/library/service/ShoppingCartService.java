package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username);
    ShoppingCart updateItem(ProductDto productDto, int quantity, String username);
    ShoppingCart removeItem(ProductDto productDto, String username);
    void deleteCartById(Long id);

}
