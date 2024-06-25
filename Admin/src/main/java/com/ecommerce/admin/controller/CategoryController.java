package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public String categories(Model model){
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        model.addAttribute("size", categoryList.size());
        model.addAttribute("categoryNew", new Category());
        return "categories";
    }

    @PostMapping("/add-category")
    public String addNewCategory(@ModelAttribute("categoryNew") Category category, Model model, RedirectAttributes ra){
        Category category1 = categoryService.findByName(category.getName());
        if(category1 != null || category.getName().isEmpty()){
            ra.addFlashAttribute("errorNameCat", "Category existed or name is empty");
            return "redirect:/categories";
        }
        categoryService.saveCategory(category);
        ra.addFlashAttribute("successAddCat", "Add new category successfully");
        return "redirect:/categories";

    }

    @RequestMapping(value = "/update-category", method = RequestMethod.POST)
    public String updateCategory(@RequestParam("id") Long id, @RequestParam("newCatName") String name, RedirectAttributes ra){
        Category category1 = categoryService.findByName(name);
        if(category1 != null || name.isEmpty()){
            ra.addFlashAttribute("errorNameCat", "Category existed or name is empty");
            return "redirect:/categories";
        }
        ra.addFlashAttribute("successEditCat", "Alter category successfully");
        categoryService.update(id, name);
        return "redirect:/categories";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableCategory(Long id, RedirectAttributes ra){
        ra.addFlashAttribute("success", "Enabled Successfully");
        categoryService.enabledById(id);
        return "redirect:/categories";
    }
    @RequestMapping(value = "/delete-category", method = {RequestMethod.GET, RequestMethod.PUT})
    public String deleteCategory(Long id, RedirectAttributes ra){
        ra.addFlashAttribute("success", "Deleted Successfully");
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
