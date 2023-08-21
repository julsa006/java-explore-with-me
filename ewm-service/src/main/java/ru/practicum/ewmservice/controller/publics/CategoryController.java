package ru.practicum.ewmservice.controller.publics;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.model.Category;
import ru.practicum.ewmservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public Category getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }

    @GetMapping
    public List<Category> getCategories(@RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }
}
