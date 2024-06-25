package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @GetMapping("/products")
    public String products(Model model){
        List<ProductDto> productDtoList = productService.findALl();
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model){
        Page<ProductDto> productPage = productService.pageProduct(pageNo);
        model.addAttribute("size", productPage.getSize());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("products", productPage);
        model.addAttribute("currentPage", pageNo);
        return "products";

    }
    @GetMapping("/add-product")
    public String addProduct(Model model){
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryService.findByActivatedTrue());
        return "add-product";

    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("imageP") MultipartFile imageP, RedirectAttributes ra){
        productService.saveProduct(imageP, productDto);
        ra.addFlashAttribute("success", "Add new product successfully");
        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String getUpdateProduct(@PathVariable("id") Long id, Model model){
        ProductDto productDto = productService.productDtoById(id);
        List<Category> categories = categoryService.findByActivatedTrue();
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto, @RequestParam("imageP") MultipartFile imageP,
                                RedirectAttributes ra){
        productService.update(imageP, productDto);
        ra.addFlashAttribute("success", "Update successfully!");
        return "redirect:/products/0";
    }

    @GetMapping("/enable-product")
    public String enableProduct(@RequestParam("id") Long id, RedirectAttributes ra){
        productService.enableById(id);
        ra.addFlashAttribute("success", "Enabled product");
        return "redirect:/products/0";

    }
    @GetMapping("/delete-product")
    public String deleteProduct(@RequestParam("id") Long id, RedirectAttributes ra){
        productService.deleteById(id);
        ra.addFlashAttribute("success", "Deleted product");
        return "redirect:/products/0";

    }
}
