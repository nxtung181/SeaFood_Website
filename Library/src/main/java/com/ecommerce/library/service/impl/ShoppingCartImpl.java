package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartImpl implements ShoppingCartService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartItemRepository itemRepository;
    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerRepository.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem cartItem = find(cartItemList, productDto.getId());
        Product product = transfer(productDto);

        double unitPrice = productDto.getCostPrice();

        int itemQuantity = 0;

        if(cartItemList == null){
            cartItemList = new HashSet<>();
        }
        if(cartItem == null){
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(shoppingCart);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(unitPrice);
            cartItemList.add(cartItem);
            itemRepository.save(cartItem);
        }else{
            itemQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(itemQuantity);
            itemRepository.save(cartItem);
        }
        shoppingCart.setCartItem(cartItemList);
        double totalPrice = totalPrice(shoppingCart.getCartItem());
        int totalItem = cartItemList.size();

        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setCustomer(customer);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart updateItem(ProductDto productDto, int quantity, String username) {
        Customer customer = customerRepository.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem cartItem = find(cartItemList, productDto.getId());
        cartItem.setQuantity(quantity);
        itemRepository.save(cartItem);
        shoppingCart.setCartItem(cartItemList);
        double totalPrice = totalPrice(shoppingCart.getCartItem());
        int totalItem = cartItemList.size();

        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setTotalPrice(totalPrice);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeItem(ProductDto productDto, String username) {
        Customer customer = customerRepository.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem item = find(cartItemList, productDto.getId());
        cartItemList.remove(item);
        itemRepository.delete(item);
        shoppingCart.setCartItem(cartItemList);
        double totalPrice = totalPrice(shoppingCart.getCartItem());
        int totalItem = cartItemList.size();

        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setTotalPrice(totalPrice);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getById(id);
        if(!ObjectUtils.isEmpty(shoppingCart) && !ObjectUtils.isEmpty(shoppingCart.getCartItem())){
            itemRepository.deleteAll(shoppingCart.getCartItem());
        }
        shoppingCart.getCartItem().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem find(Set<CartItem> cartItemList, Long id){
        if(cartItemList == null){
            return null;
        }
        CartItem cartItem = null;
        for(CartItem i: cartItemList){
            if(i.getProduct().getId().equals(id)){
                cartItem = i;
                break;
            }
        }
        return cartItem;
    }

    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());
        return product;
    }

    private double totalPrice(Set<CartItem> cartItemList){
        double totalPrice = 0;
        for(CartItem cartItem : cartItemList){
            totalPrice += cartItem.getUnitPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }
}
