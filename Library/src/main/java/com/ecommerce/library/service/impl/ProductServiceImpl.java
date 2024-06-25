package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUpload imageUpload;
    private List<ProductDto> convertToProductDto(List<Product> productList){
        List<ProductDto> dtoList = new ArrayList<>();
        for(Product product : productList){
            ProductDto productDto = setProductDto(product);
            dtoList.add(productDto);
        }
        return dtoList;
    }
    private ProductDto setProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setImage(product.getImage());
        productDto.setCategory(product.getCategory());
        productDto.setActivated(product.is_activated());
        productDto.setDeleted(product.is_deleted());
        return productDto;
    }
    @Override
    public List<ProductDto> findALl() {
        return convertToProductDto(productRepository.findAll());
    }

    @Override
    public void saveProduct(MultipartFile imageP, ProductDto productDto){
        Product product = new Product();
        try{
            if(imageP == null){
                product.setImage(null);
            }else{
                imageUpload.uploadFile(imageP);
                product.setImage(Base64.getEncoder().encodeToString(imageP.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_activated(true);
            product.set_deleted(false);
            productRepository.save(product);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public ProductDto productDtoById(Long id){
        Product product = productRepository.getById(id);
        ProductDto productDto = setProductDto(product);
        return  productDto;
    }

    @Override
    public void update(MultipartFile imageP, ProductDto productDto) {

        try{
            Product product = productRepository.getReferenceById(productDto.getId());
            if (imageUpload.checkExist(imageP)) {
                product.setImage(product.getImage());
            } else {
                imageUpload.uploadFile(imageP);
                product.setImage(Base64.getEncoder().encodeToString(imageP.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            productRepository.save(product);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(false);
        product.set_activated(true);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDto> pageProduct(int pageNo) {
        List<ProductDto> productDtoList= convertToProductDto(productRepository.findAll());
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> productPage = toPage(productDtoList, pageable);
        return productPage;
    }

    private Page toPage(List<ProductDto> list, Pageable pageable){
        int start = (int)pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List subList = start >= end ? new ArrayList<>() : list.subList(start,end);
        return new PageImpl(subList, pageable, list.size());
    }
    /*--CUSTOMER--*>*/
    @Override
    public List<Product> getAllProduct() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> getRelatedProducts(Long categoryId) {
        return productRepository.getRelatedProducts(categoryId);
    }

    @Override
    public List<Product> filterHighPrice() {
        return productRepository.filterHighPrice();
    }

    @Override
    public List<Product> filterLowPrice() {
        return productRepository.filterLowPrice();
    }


}
